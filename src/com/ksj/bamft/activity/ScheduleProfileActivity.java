package com.ksj.bamft.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.ksj.bamft.R;
import com.ksj.bamft.actionbarhelpers.ActionBarTitleHelper;
import com.ksj.bamft.actionbarhelpers.ProfileTabsHelper;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.constants.GoogleMapsConstants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.hubway.HubwayHelpers;
import com.ksj.bamft.maps.MapHelpers;
import com.ksj.bamft.mbta.MbtaHelpers;
import com.ksj.bamft.model.HubwayStation;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.MbtaStation;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.SimpleLocation;
import com.ksj.bamft.model.Truck;

public class ScheduleProfileActivity extends MapActivity {
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	        
	    // Grab stuff passed from last activity 
        Bundle receivedExtras = this.getIntent().getExtras();
        Schedule schedule = (Schedule) receivedExtras.getSerializable(Constants.SCHEDULE);
        final double userLatitude = receivedExtras.getDouble(Constants.USER_LATITUDE);
        final double userLongitude = receivedExtras.getDouble(Constants.USER_LONGITUDE);
        
        //Open database connect
        final DatabaseHandler db = new DatabaseHandler(this);
        
        
        
        //Grab the relevant data
        final Truck truck = db.getTruck(schedule.getTruckId());
        Landmark landmark = db.getLandmark(schedule.getLandmarkId());
        
        double truckLatitude = Double.parseDouble(landmark.getYcoord());
        double truckLongitude = Double.parseDouble(landmark.getXcoord());
        
        double distance = MapHelpers.calculateDistance(userLatitude, truckLatitude, 
        		userLongitude, truckLongitude);
        
        String roundedDistance = MapHelpers.roundDistanceToDecimalPlace(1, distance);
        String distanceString = roundedDistance + " " + Constants.MILES;
        
        // Finally, fill the layout
        setContentView(R.layout.ab_truck_profile);
        
        TextView truckNameTextView = (TextView)findViewById(R.id.truckNameText);
        TextView landmarkNameTextView = (TextView)findViewById(R.id.landmarkNameText);
        TextView landmarkDistanceTextView = (TextView)findViewById(R.id.landmarkDistanceText);
        TextView truckDescriptionTextView = (TextView)findViewById(R.id.truckDescriptionText);
        
        //TODO: make it calculate real distance...lawl...
        //float make_x = new Float(landmark.getXcoord());
        //float make_y = new Float(landmark.getYcoord());
        //float make_distance = make_x / make_y;
        
        truckNameTextView.setText(truck.getName());
        landmarkNameTextView.setText(landmark.getName());
        landmarkDistanceTextView.setText(distanceString);
        truckDescriptionTextView.setText(truck.getDescription());
        
        truckDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());
        
        
        /*
        //set scrolling for description
        truckDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());
        
        
        // Map view
        
        MapView mapView = (MapView) findViewById(R.id.truckProfileMap);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> overlayToDisplay = mapView.getOverlays();
        Drawable overlayMarker =
        		this.getResources().getDrawable(R.drawable.androidmarker);
        MapOverlays overlay = new MapOverlays(overlayMarker, this);
        
        int truckLat = MapHelpers.degreesToMicrodegrees(Double.parseDouble(landmark.getYcoord()));
        int truckLon = MapHelpers.degreesToMicrodegrees(Double.parseDouble(landmark.getXcoord()));
        
        GeoPoint truckLocation = new GeoPoint(truckLat, truckLon);
        OverlayItem overlayItem = new OverlayItem(truckLocation, "Hola, Mundo!", "I'm in Mexico City!");
        
        overlay.addOverlay(overlayItem);
        overlay.populateNow();
        overlayToDisplay.add(overlay);
        
        MapController mapController = mapView.getController();
        mapController.setCenter(truckLocation);
        mapController.setZoom(17);
        */
        

        Time now = new Time();
    	now.setToNow();
        
        final String dayOfWeek = BamftActivity.getDayOfWeek(now);
        final String timeOfDay = BamftActivity.getMealOfDay(now);
        
        // MBTA Stats
        List<MbtaStation> mbtaStationList = MbtaHelpers.getAllMbtaStations(getBaseContext());
        MbtaStation nearestUserMbtaStation = MbtaHelpers.getNearestMbtaStation(mbtaStationList, userLatitude, userLongitude);
        MbtaStation nearestTruckMbtaStation = MbtaHelpers.getNearestMbtaStation(mbtaStationList, Double.parseDouble(landmark.getYcoord()), Double.parseDouble(landmark.getXcoord()));
        Log.d("Nearest MBTA", "The nearest MBTA is " + nearestUserMbtaStation.getStopName() + " to " + nearestTruckMbtaStation.getStopName());
        
        
        /*
        // Google Maps button -- temporary, only here for testing intents to Maps!
        
        Button mapsButton = (Button) findViewById(R.id.truckProfileMapsButton);
        mapsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("geo:0,0?q=" + userLatitude + "," + userLongitude + "(you are here)"));
		        
		        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		        
		        startActivity(intent);
			}
		});
        */
        
        
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
        ProfileTabsHelper.userLatitude = userLatitude;
        ProfileTabsHelper.userLongitude = userLongitude;
        ProfileTabsHelper.setupProfileTabs(this, truck, "profile");
        
        
        /*
        // BEGIN "TAB" BUTTONS
        // Schedule button
        
        Button scheduleButton = (Button) findViewById(R.id.truckProfileScheduleButton);
		scheduleButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
        		// Create the intent
        		Intent loadTruckScheduleIntent = new Intent(ScheduleProfileActivity.this, TruckScheduleListActivity.class);
        		
        		// Create extras bundle
        		Bundle extras = new Bundle();
        		extras.putString(Constants.DAY_OF_WEEK, dayOfWeek);
        		extras.putString(Constants.TIME_OF_DAY, timeOfDay);
        		extras.putSerializable(Constants.TRUCK, truck);
        		loadTruckScheduleIntent.putExtras(extras);
        	
        		// Start the activity
        		ScheduleProfileActivity.this.startActivity(loadTruckScheduleIntent);
			}
		});
		
		// Menu button
		Button menuButton = (Button) findViewById(R.id.truckProfileMenuButton);
		menuButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Create intent
				Intent loadTruckMenuIntent = new Intent(ScheduleProfileActivity.this, TruckMenuListActivity.class);
				
				// Create extras bundle
				Bundle extras = new Bundle();
				extras.putInt(Constants.TRUCK_ID, truck.getId()); // we'll pass the truck ID and let TruckMenuListActivity handle it.
				loadTruckMenuIntent.putExtras(extras);
				
				// Start the activity
				ScheduleProfileActivity.this.startActivity(loadTruckMenuIntent);
				
			}
			
		});
		
		// Twitter button
		
		Button twitterButton = (Button) findViewById(R.id.truckProfileTwitterButton);
		twitterButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Create intent
				Intent loadTruckTwitterIntent = new Intent(ScheduleProfileActivity.this, TruckTwitterListActivity.class);
				
				// Create extras bundle
				Bundle extras = new Bundle();
				extras.putString(Constants.TWITTER_HANDLE, truck.getTwitter());
				loadTruckTwitterIntent.putExtras(extras);
				
				// Start the activity
				ScheduleProfileActivity.this.startActivity(loadTruckTwitterIntent);
				
			}
			
		});
		
		// Yelp Button
        Button yelpButton = (Button) findViewById(R.id.truckProfileYelpButton);
        yelpButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Create intent
				Intent loadTruckYelpIntent = new Intent(ScheduleProfileActivity.this, TruckYelpListActivity.class);
				
				// Create extras bundle
				Bundle extras = new Bundle();
				extras.putString(Constants.YELP_HANDLE, truck.getYelp());
				loadTruckYelpIntent.putExtras(extras);
				
				// Star the activity
				ScheduleProfileActivity.this.startActivity(loadTruckYelpIntent);

				
			}
		});
        
        
        // END "TAB" BUTTONS
        */
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
}

