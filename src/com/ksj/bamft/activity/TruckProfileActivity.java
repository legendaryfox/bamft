package com.ksj.bamft.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.ksj.bamft.R;
import com.ksj.bamft.actionbarhelpers.ActionBarTitleHelper;
import com.ksj.bamft.actionbarhelpers.ProfileTabsHelper;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.constants.GoogleMapsConstants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.hubway.HubwayHelpers;
import com.ksj.bamft.maps.MapHelpers;
import com.ksj.bamft.maps.MapOverlays;
import com.ksj.bamft.maps.SimpleLocationListener;
import com.ksj.bamft.model.HubwayStation;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.SimpleLocation;
import com.ksj.bamft.model.Truck;

public class TruckProfileActivity extends MapActivity {
	private SimpleLocation userLocation = null;

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// start doing stuff?

		// First, set the layout
		setContentView(R.layout.ab_truck_profile);

		// Open database connect
		final DatabaseHandler db = new DatabaseHandler(this);

		userLocation = getUserLocation();

		if (userLocation == null) {
			userLocation = new SimpleLocation(Constants.HYNES_LATITUDE,
					Constants.HYNES_LONGITUDE);
		}

		final double userLatitude = userLocation.getLatitude();
		final double userLongitude = userLocation.getLongitude();

		Time now = new Time();
		now.setToNow();

		final String dayOfWeek = BamftActivity.getDayOfWeek(now);
		final String timeOfDay = BamftActivity.getMealOfDay(now);

		Bundle truckIdBundle = this.getIntent().getExtras();
		int truckId = truckIdBundle.getInt("truckId");

		// Grab the relevant data

		final Truck truck = db.getTruck(truckId);
		String landmark_name;
		float make_x;
		float make_y;
		float make_distance;
		Schedule schedule = db.getScheduleByTruckAndDayAndTime(truck,
				dayOfWeek, timeOfDay); // db.getSchedule(scheduleId);

		String distance_string;
		TextView truckOpenCloseTextView = (TextView) findViewById(R.id.btn_openclosed);
		Button mapsButton = (Button) findViewById(R.id.btn_phone);
		if (schedule != null) {
			final Landmark landmark = db.getLandmark(schedule.getLandmarkId());
			landmark_name = landmark.getName();
			make_x = new Float(landmark.getXcoord());
			make_y = new Float(landmark.getYcoord());
			make_distance = make_x / make_y;

			// temporarily set to 0 until we get correct data
			// from API

			/*
			 * make_x = 0; make_y = 0; make_distance = 0;
			 */

			// distance_string = String.format("%.2g", make_distance) + " mi";
			// ...this isn't a real distance...
			distance_string = "";

			truckOpenCloseTextView.setText("Open");

			// MBTA button

			createMbtaButton(userLatitude, userLongitude,
					Double.parseDouble(landmark.getYcoord()),
					Double.parseDouble(landmark.getXcoord()));


			// Hubway button

			createHubwayButton(userLatitude, userLongitude,
					Double.parseDouble(landmark.getYcoord()),
					Double.parseDouble(landmark.getXcoord()));

			// Walking directions button

			createWalkingDirectionsButton(userLatitude, userLongitude,
					Double.parseDouble(landmark.getYcoord()),
					Double.parseDouble(landmark.getXcoord()));
			
			
			
			
			mapsButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
							Uri.parse("geo:0,0?q=" + landmark.getYcoord() + "," + landmark.getXcoord() + "(" + truck.getName() + ")"));

					intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

					startActivity(intent);
				}
			});

		}

		else {
			landmark_name = "";
			make_x = 0;
			make_y = 0;
			make_distance = 0;

			createHubwayEmptyButton();
			createWalkingEmptyButton();
			createMbtaEmptyButton();

			truckOpenCloseTextView.setText("Closed");

			distance_string = "";
			
			mapsButton.setText("Truck is Closed");

			// Set the buttons to make toasts...


		}

		TextView truckNameTextView = (TextView) findViewById(R.id.truckNameText);
		TextView landmarkNameTextView = (TextView) findViewById(R.id.landmarkNameText);
		TextView landmarkDistanceTextView = (TextView) findViewById(R.id.landmarkDistanceText);
		TextView truckDescriptionTextView = (TextView) findViewById(R.id.truckDescriptionText);

		// TODO: make it calculate real distance...lawl...

		// String distance_string = String.format("%.2g", make_distance) +
		// " mi";

		truckNameTextView.setText(truck.getName());
		landmarkNameTextView.setText(landmark_name);
		landmarkDistanceTextView.setText(distance_string);
		truckDescriptionTextView.setText(truck.getDescription());
		truckDescriptionTextView
		.setMovementMethod(new ScrollingMovementMethod());

		ActionBarTitleHelper.setTitleBar(this);
		ProfileTabsHelper.referrer = "truck";
		ProfileTabsHelper.setupProfileTabs(this, truck, "profile");
		
		
		if (schedule != null){
			
		}

	}


	private void createHubwayEmptyButton() {
		ImageButton hubwayButton = (ImageButton) findViewById(R.id.truckProfileHubwayButton);
		hubwayButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Toast.makeText(arg0.getContext(), "Truck is closed...", Toast.LENGTH_SHORT).show();
				return ;
			}
		});
	}

	private void createWalkingEmptyButton() {
		ImageButton hubwayButton = (ImageButton) findViewById(R.id.truckProfileWalkingButton);
		hubwayButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Toast.makeText(arg0.getContext(), "Truck is closed...", Toast.LENGTH_SHORT).show();
				return ;
			}
		});
	}

	private void createMbtaEmptyButton() {
		ImageButton mbtaButton = (ImageButton) findViewById(R.id.truckProfileSubwayButton);
		mbtaButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Toast.makeText(arg0.getContext(), "Truck is closed...", Toast.LENGTH_SHORT).show();
				return ;
			}
		});
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

				List<HubwayStation> stations = HubwayHelpers
						.getAvailableStations();

				if (stations == null || stations.size() < 1) {
					Toast.makeText(getBaseContext(),
							Constants.HUBWAY_UNAVAILABLE, Toast.LENGTH_LONG)
							.show();

					return;
				}

				HubwayStation nearestStationToUser = HubwayHelpers
						.getNearestStation(stations, userLat, userLon);

				SimpleLocation nearestStationToUserLoc = new SimpleLocation(
						nearestStationToUser.getLatitude(),
						nearestStationToUser.getLongitude());

				List<SimpleLocation> destinations = new ArrayList<SimpleLocation>(
						2);
				destinations.add(nearestStationToUserLoc);
				destinations.add(new SimpleLocation(truckLat, truckLon));

				String mapsQuery = MapHelpers.getMapsQuery(new SimpleLocation(
						userLat, userLon), destinations,
						GoogleMapsConstants.BIKING_ROUTE,
						GoogleMapsConstants.HTML);

				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(mapsQuery));

				
				intent.setClassName(Constants.BROWSER_PACKAGE,
						Constants.BROWSER_CLASS);
				Toast.makeText(getBaseContext(), "Routing you to the nearest Hubway Bike Station first...", Toast.LENGTH_LONG).show();

				startActivity(intent);
			}
		});
	}

	/**
	 * Set up walking directions button functionality.
	 */
	private void createWalkingDirectionsButton(final double userLat,
			final double userLon, final double truckLat, final double truckLon) {

		ImageButton hubwayButton = (ImageButton) findViewById(R.id.truckProfileWalkingButton);
		hubwayButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SimpleLocation userLocation = new SimpleLocation(userLat,
						userLon);
				SimpleLocation truckLocation = new SimpleLocation(truckLat,
						truckLon);

				List<SimpleLocation> destination = new ArrayList<SimpleLocation>(
						1);
				destination.add(truckLocation);

				String mapsQuery = MapHelpers.getMapsQuery(userLocation,
						destination, GoogleMapsConstants.WALKING_ROUTE,
						GoogleMapsConstants.HTML);

				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(mapsQuery));

				intent.setClassName(GoogleMapsConstants.PACKAGE,
						GoogleMapsConstants.CLASS);
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

		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		String bestLocationProvider = MapHelpers.getLocationProvider(this,
				locationManager, Criteria.ACCURACY_FINE);

		// If user has location provider enabled, get user location

		if (bestLocationProvider != null
				&& locationManager.isProviderEnabled(bestLocationProvider)) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER,
					Constants.LOCATION_REFRESH_TIME,
					Constants.LOCATION_REFRESH_DISTANCE, locationListener);
		}

		Location userLocation = MapHelpers.getUserLocation(locationManager,
				bestLocationProvider);
		locationManager.removeUpdates(locationListener);

		if (userLocation == null)
			return null;

		SimpleLocation userSimpleLocation = new SimpleLocation(
				userLocation.getLatitude(), userLocation.getLongitude());

		return userSimpleLocation;
	}

}
