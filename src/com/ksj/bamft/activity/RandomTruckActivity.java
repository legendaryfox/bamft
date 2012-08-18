package com.ksj.bamft.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ksj.bamft.R;
import com.ksj.bamft.actionbarhelpers.ActionBarTitleHelper;
import com.ksj.bamft.actionbarhelpers.ProfileTabsHelper;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.constants.GoogleMapsConstants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.hubway.HubwayHelpers;
import com.ksj.bamft.maps.MapHelpers;
import com.ksj.bamft.maps.SimpleLocationListener;
import com.ksj.bamft.model.HubwayStation;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.SimpleLocation;
import com.ksj.bamft.model.Truck;

public class RandomTruckActivity extends Activity {

	private SimpleLocation userLocation = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActionBarTitleHelper.setTitleBar(this);

		checkLocationProvidersEnabled();
	}

	public void finishCreatingActivity() {
		userLocation = getUserLocation();

		if (userLocation == null) {
			userLocation = new SimpleLocation(Constants.HYNES_LATITUDE, Constants.HYNES_LONGITUDE);
		}

		final double userLatitude = userLocation.getLatitude();
		final double userLongitude = userLocation.getLongitude();

		final String dayOfWeek = this.getIntent().getStringExtra(Constants.DAY_OF_WEEK);
		final String timeOfDay = this.getIntent().getStringExtra(Constants.TIME_OF_DAY);

		Log.d("Success", "yay");

		//Open database connect
		final DatabaseHandler db = new DatabaseHandler(this);

		List<Schedule> schedules = db.getSchedulesByDayAndTime(dayOfWeek, timeOfDay);

		if (schedules == null || schedules.size() < 1) {
			Toast.makeText(this, Constants.ALL_TRUCKS_CLOSED, Toast.LENGTH_SHORT).show();
			return;
		}

		int numSchedules = schedules.size();

		Random rand = new Random();
		int scheduleToGet = rand.nextInt(numSchedules);

		Schedule schedule = schedules.get(scheduleToGet);
		Truck truck = db.getTruck(schedule.getTruckId());
		Landmark landmark = db.getLandmark(schedule.getLandmarkId());

		double truckLatitude = Double.parseDouble(landmark.getYcoord());
		double truckLongitude = Double.parseDouble(landmark.getXcoord());

		double distance = MapHelpers.calculateDistance(
				userLatitude, truckLatitude, userLongitude, truckLongitude);

		//String distanceString = String.format("%.2g", distance) + " " + Constants.MILES;

		String roundedDistance = MapHelpers.roundDistanceToDecimalPlace(1, distance);
		String distanceString = roundedDistance + " " + Constants.MILES;

		// Finally, fill the layout
		setContentView(R.layout.ab_truck_profile);

		TextView truckNameTextView = (TextView)findViewById(R.id.truckNameText);
		TextView landmarkNameTextView = (TextView)findViewById(R.id.landmarkNameText);
		TextView landmarkDistanceTextView = (TextView)findViewById(R.id.landmarkDistanceText);

		TextView truckDescriptionTextView = (TextView)findViewById(R.id.truckDescriptionText);
		TextView truckOpenCloseTextView = (TextView) findViewById(R.id.btn_openclosed);

		truckNameTextView.setText(truck.getName());
		landmarkNameTextView.setText(landmark.getName());
		landmarkDistanceTextView.setText(distanceString);
		truckDescriptionTextView.setText(truck.getDescription());
		truckOpenCloseTextView.setText("Open");


		// Mbta button

		createMbtaButton(
				userLatitude,
				userLongitude,
				Double.parseDouble(landmark.getYcoord()),
				Double.parseDouble(landmark.getXcoord()));

		// Hubway button

		createHubwayButton(
				userLatitude,
				userLongitude,
				Double.parseDouble(landmark.getYcoord()),
				Double.parseDouble(landmark.getXcoord()));

		// Walking directions button

		createWalkingDirectionsButton(
				userLatitude,
				userLongitude,
				Double.parseDouble(landmark.getYcoord()),
				Double.parseDouble(landmark.getXcoord()));

		//ScheduleProfileTabsHelper.SetupProfileTabs(this, truck, "profile", receivedExtras.getString(Constants.REFERRER));
		ActionBarTitleHelper.setTitleBar(this);
		ProfileTabsHelper.referrer = "schedule";
		ProfileTabsHelper.schedule = schedule;
		ProfileTabsHelper.setupProfileTabs(this, truck, "profile");
	}



	/**
	 * Set up MBTA button functionality.
	 */

	private void createMbtaButton(final double userLat, final double userLon,
			final double truckLat, final double truckLon) {

		ImageButton mbtaButton = (ImageButton) findViewById(R.id.truckProfileSubwayButton);
		mbtaButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				//List<MbtaStation> stations = MbtaHelpers.getAllMbtaStations();

				List<SimpleLocation> destinations = new ArrayList<SimpleLocation>(1);
				destinations.add(new SimpleLocation(truckLat, truckLon));

				String mapsQuery = MapHelpers.getMapsQuery(
						new SimpleLocation(userLat, userLon), 
						destinations, 
						GoogleMapsConstants.PUBLIC_TRANSIT,
						GoogleMapsConstants.HTML);

				Intent intent = new Intent(
						android.content.Intent.ACTION_VIEW, Uri.parse(mapsQuery));

				//intent.setClassName(Constants.BROWSER_PACKAGE, Constants.BROWSER_CLASS);
				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

				startActivity(intent);



			}

		});
	}

	/**
	 * Set up Hubway button functionality. 
	 */
	private void createHubwayButton(final double userLat, final double userLon,
			final double truckLat, final double truckLon) {

		ImageButton hubwayButton = (ImageButton) findViewById(R.id.truckProfileHubwayButton);
		hubwayButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				List<HubwayStation> stations = HubwayHelpers.getAvailableStations();

				if (stations == null || stations.size() < 1) {
					Toast.makeText(
							getBaseContext(),
							Constants.HUBWAY_UNAVAILABLE,
							Toast.LENGTH_LONG).show();

					return;
				}

				HubwayStation nearestStationToUser = HubwayHelpers.getNearestStation(stations,
						userLat, userLon);

				SimpleLocation nearestStationToUserLoc = new SimpleLocation(
						nearestStationToUser.getLatitude(), nearestStationToUser.getLongitude());

				List<SimpleLocation> destinations = new ArrayList<SimpleLocation>(2);
				destinations.add(nearestStationToUserLoc);
				destinations.add(new SimpleLocation(truckLat, truckLon));

				String mapsQuery = MapHelpers.getMapsQuery(
						new SimpleLocation(userLat, userLon), 
						destinations, 
						GoogleMapsConstants.BIKING_ROUTE,
						GoogleMapsConstants.HTML);

				Intent intent = new Intent(
						android.content.Intent.ACTION_VIEW, Uri.parse(mapsQuery));

				intent.setClassName(Constants.BROWSER_PACKAGE, Constants.BROWSER_CLASS);
				Toast.makeText(getBaseContext(), "Routing you to the nearest Hubway Bike Station first...", Toast.LENGTH_LONG).show();

				startActivity(intent);
			}
		});
	}

	/**
	 * Set up walking directions button functionality. 
	 */
	private void createWalkingDirectionsButton(final double userLat, final double userLon,
			final double truckLat, final double truckLon) {

		ImageButton hubwayButton = (ImageButton) findViewById(R.id.truckProfileWalkingButton);
		hubwayButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SimpleLocation userLocation = new SimpleLocation(userLat, userLon);
				SimpleLocation truckLocation = new SimpleLocation(truckLat, truckLon);

				List<SimpleLocation> destination = new ArrayList<SimpleLocation>(1);
				destination.add(truckLocation);

				String mapsQuery = MapHelpers.getMapsQuery(userLocation, destination,
						GoogleMapsConstants.WALKING_ROUTE, GoogleMapsConstants.HTML);

				Intent intent = new Intent(
						android.content.Intent.ACTION_VIEW, Uri.parse(mapsQuery));

				intent.setClassName(GoogleMapsConstants.PACKAGE, GoogleMapsConstants.CLASS);
				startActivity(intent);
			}
		});
	}

	/**
	 * Return the user's coordinates in a Location object.
	 * 
	 * @return
	 */
	private SimpleLocation getUserLocation() {

		LocationListener locationListener = new SimpleLocationListener();

		LocationManager locationManager = 
				(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		String bestLocationProvider = MapHelpers.getLocationProvider(this, locationManager,
				Criteria.ACCURACY_FINE);

		// If user has location provider enabled, get user location

		if (bestLocationProvider != null && locationManager.isProviderEnabled(bestLocationProvider)) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER,
					Constants.LOCATION_REFRESH_TIME,
					Constants.LOCATION_REFRESH_DISTANCE,
					locationListener);
		}

		Location userLocation = MapHelpers.getUserLocation(locationManager, bestLocationProvider);
		locationManager.removeUpdates(locationListener);

		if (userLocation == null)
			return null;

		SimpleLocation userSimpleLocation = new SimpleLocation(
				userLocation.getLatitude(), userLocation.getLongitude());

		return userSimpleLocation;
	}

	/** 
	 * 	If user has no location providers enabled, prompt
	 * 	user to turn one on.
	 */
	private void checkLocationProvidersEnabled() {
		LocationManager locationManager = 
				(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		boolean enabled = 
				locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (enabled) {
			finishCreatingActivity();
			return;
		}

		if (!enabled) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);  
			builder.setMessage(Constants.ENABLE_LOCATION_PROVIDERS)  
			.setCancelable(true)  
			.setPositiveButton(
					Constants.ENABLE_LOCATION_PROVIDERS_YES, 
					new DialogInterface.OnClickListener() {  
						public void onClick(DialogInterface dialog, int id) {  
							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
							startActivityForResult(intent, 1); 
						}  
					})  
					.setNegativeButton(
							Constants.ENABLE_LOCATION_PROVIDERS_NO, 
							new DialogInterface.OnClickListener() {  
								public void onClick(DialogInterface dialog, int id) {  
									finishCreatingActivity();
								}  
							}).show();  
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		finishCreatingActivity();
	}
}
