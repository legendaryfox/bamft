package com.ksj.bamft.activity;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.ksj.bamft.R;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.hubway.HubwayHelpers;
import com.ksj.bamft.maps.MapHelpers;
import com.ksj.bamft.maps.MapOverlays;
import com.ksj.bamft.maps.RouteOverlay;
import com.ksj.bamft.model.HubwayStation;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.NavigationStep;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

public class BamftMapActivity extends MapActivity {

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 *	When user presses back button, go back to the main menu
	 *	(instead of from the hubway map to the truck map, to the
	 *	hubway map, back to the truck map, etc.) 
	 */
	@Override
	public void onBackPressed() {
		Intent bamftActivityIntent = new Intent(this, BamftActivity.class);
		
		startActivity(bamftActivityIntent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        Bundle extras = this.getIntent().getExtras();
        String mapToCreate = extras.getString(Constants.MAP_TYPE);

        if (mapToCreate.equals(Constants.MAP_TYPE_TRUCKS)) {
        	String dayOfWeek = extras.getString(Constants.DAY_OF_WEEK);
            String timeOfDay = extras.getString(Constants.TIME_OF_DAY);

        	createOpenTrucksMapView(dayOfWeek, timeOfDay);
        	setOpenTrucksButtonFunctionality(dayOfWeek, timeOfDay);
        	setHubwayButtonFunctionality(dayOfWeek, timeOfDay);
        }
        
        else if (mapToCreate.equals(Constants.MAP_TYPE_HUBWAY)) {
        	String dayOfWeek = extras.getString(Constants.DAY_OF_WEEK);
            String timeOfDay = extras.getString(Constants.TIME_OF_DAY);

        	createHubwayStationsMapView();
        	setOpenTrucksButtonFunctionality(dayOfWeek, timeOfDay);
        	setHubwayButtonFunctionality(dayOfWeek, timeOfDay);
        }
	}
	
	/**
	 * Create functionality for the trucks button.
	 */
	private void setOpenTrucksButtonFunctionality(final String dayOfWeek, final String timeOfDay) {
		Button trucksButton = (Button) findViewById(R.id.openTrucksButton);
        trucksButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent truckMapIntent = new Intent(
						BamftMapActivity.this, BamftMapActivity.class);
				truckMapIntent.putExtra(Constants.DAY_OF_WEEK, dayOfWeek);
				truckMapIntent.putExtra(Constants.TIME_OF_DAY, timeOfDay);
				truckMapIntent.putExtra(Constants.MAP_TYPE, Constants.MAP_TYPE_TRUCKS);
				startActivity(truckMapIntent);
			}
		});
	}
	
	/**
	 * Create functionality for the Hubway stations button.
	 */
	private void setHubwayButtonFunctionality(final String dayOfWeek, final String timeOfDay) {
		Button hubwayStationsButton = (Button) findViewById(R.id.hubwayStationsButton);
        hubwayStationsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent hubwayStationsMapIntent = new Intent(
						BamftMapActivity.this, BamftMapActivity.class);
				hubwayStationsMapIntent.putExtra(Constants.DAY_OF_WEEK, dayOfWeek);
				hubwayStationsMapIntent.putExtra(Constants.TIME_OF_DAY, timeOfDay);
				hubwayStationsMapIntent.putExtra(Constants.MAP_TYPE, Constants.MAP_TYPE_HUBWAY);
				startActivity(hubwayStationsMapIntent);
			}
		});
	}
	
	
	/** 
	 * Create the MapView to display all the Hubway stations.
	 */
	//TODO: clean this messy method up!
	private void createHubwayStationsMapView() {
		setContentView(R.layout.map_activity);
		
		List<HubwayStation> stations = HubwayHelpers.getAvailableStations();
		
		if (stations == null || stations.size() < 1) {
			Toast.makeText(
					getBaseContext(),
					Constants.HUBWAY_UNAVAILABLE,
					Toast.LENGTH_LONG).show();
			
			return;
		}
		
		MapView mapView = (MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> overlayToDisplay = mapView.getOverlays();
        
        Drawable overlayMarker = this.getResources().getDrawable(R.drawable.androidmarker);
        
        overlayMarker.setBounds(0, 0,
        		overlayMarker.getIntrinsicWidth(), overlayMarker.getIntrinsicHeight());
        
        MapOverlays overlay = new MapOverlays(overlayMarker, this);
        
        for (HubwayStation station : stations) {
        	//TODO: uncomment in release mode
        	//if (station.isInstalled()) {
        		GeoPoint point = MapHelpers.getGeoPoint(
        				station.getLatitude(), station.getLongitude());
        		
        		OverlayItem overlayItem = new OverlayItem(point, station.getName(), "");
        		overlay.addOverlay(overlayItem);
        	//}
        }
        
        overlay.populateNow();
        overlayToDisplay.add(overlay);
        
        GeoPoint hynes = MapHelpers.getGeoPoint(
        		Constants.HYNES_LATITUDE, Constants.HYNES_LONGITUDE);
        
        MapController mapController = mapView.getController();
        mapController.setCenter(hynes);
        mapController.setZoom(Constants.HUBWAY_STATIONS_MAP_ZOOM);
	}
	
	/**
	 * Create map with overlays for all the currently open 
	 * food trucks.
	 * 
	 * @param dayOfWeek
	 * @param timeOfDay
	 */
	private void createOpenTrucksMapView(String dayOfWeek, String timeOfDay) {
		setContentView(R.layout.map_activity);
        
        MapView mapView = (MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> overlays = mapView.getOverlays();
        showOpenTruckOverlays(mapView, overlays, dayOfWeek, timeOfDay);
	}

	
	/**
	 * Display a list of overlay items for Google Maps, one for each
	 * open truck.
	 * 
	 * @param overlaysList
	 * @param dayOfWeek
	 * @param timeOfDay
	 * @return
	 */
	private void showOpenTruckOverlays(MapView mapView,
			List<Overlay> overlaysList, String dayOfWeek, String timeOfDay) {
		
        // Get a list of open trucks
        
        DatabaseHandler db = new DatabaseHandler(this);
        List<Schedule> schedules = db.getSchedulesByDayAndTime(dayOfWeek, timeOfDay);
        
        if (schedules == null || schedules.size() < 1) {
        	Toast.makeText(this, Constants.ALL_TRUCKS_CLOSED, Toast.LENGTH_SHORT).show();
        	return;
        }
        
        // Define marker to use on overlay map
        
        Drawable overlayMarker = this.getResources().getDrawable(R.drawable.androidmarker);
        overlayMarker.setBounds(0, 0, 
        		overlayMarker.getIntrinsicWidth(), overlayMarker.getIntrinsicHeight());
        
        // Initialize bounds for the map. These will be used to determine
        // how far out we should zoom in order to fit all the markers.
        
        int upperBound = Integer.MIN_VALUE;
        int lowerBound = Integer.MAX_VALUE;
        int leftBound = Integer.MAX_VALUE;
        int rightBound = Integer.MIN_VALUE;
        
        // Create list of overlays while keeping track of boundaries
        
        MapOverlays overlays = new MapOverlays(overlayMarker, this);
        
        for (Schedule schedule : schedules) {
        	OverlayItem overlay = getOverlayItemFromSchedule(schedule, db);
        	
        	if (overlay != null)
        		overlays.addOverlay(overlay);
        	
        	int latitude = overlay.getPoint().getLatitudeE6();
        	int longitude = overlay.getPoint().getLongitudeE6();
        	
        	if (latitude < lowerBound)
        		lowerBound = latitude;
        	
        	if (latitude > upperBound)
        		upperBound = latitude;	
        	
        	if (longitude < leftBound)
        		leftBound = longitude;
        	
        	if (longitude > rightBound)
        		rightBound = longitude;
        }
        
        overlays.populateNow();
        overlaysList.add(overlays);
        
        MapController mapController = mapView.getController();
        GeoPoint hynes = MapHelpers.getGeoPoint(
        		Constants.HYNES_LATITUDE, Constants.HYNES_LONGITUDE);
        mapController.setCenter(hynes);
        mapController.setZoom(Constants.OPEN_TRUCKS_MAP_ZOOM);
	}
	
	
	/** 
	 * Given a schedule, return an OverlayItem for the truck at that given
	 * time and location.
	 * 
	 * @param schedule
	 * @param db
	 * @return
	 */
	private OverlayItem getOverlayItemFromSchedule(Schedule schedule, DatabaseHandler db) {
		
		// Get truck name
    	
    	Truck truck = db.getTruck(schedule.getTruckId());
    	String truckName = truck.getName();
    	
    	// Get truck location
    	
    	Landmark landmark = db.getLandmark(schedule.getLandmarkId());

    	String xCoord = landmark.getXcoord();
    	String yCoord = landmark.getYcoord();
    	
    	if (xCoord == null || xCoord.length() < 1 || yCoord == null || yCoord.length() < 1)
    		return null;
    	
    	GeoPoint truckLocation = MapHelpers.getGeoPoint(
    			Double.parseDouble(yCoord), Double.parseDouble(xCoord));
    	
    	return new OverlayItem(truckLocation, truckName, "herro");
	}
	
	/**
	 * Set appropriate zoom level and map center point such that all
	 * markers are displayed.
	 * 
	 * @param mapView
	 * @param lowerBound
	 * @param upperBound
	 * @param leftBound
	 * @param rightBound
	 */
	private void determineMapZoom(MapView mapView, int lowerBound, int upperBound,
			int leftBound, int rightBound) {
		
        MapController mapController = mapView.getController();
        mapController.zoomToSpan(rightBound - leftBound, upperBound - lowerBound);
        
        GeoPoint centerOfBoston = MapHelpers.getGeoPoint(
        		Constants.BPL_LATITUDE, Constants.BPL_LONGITUDE);
        
        mapController.setCenter(centerOfBoston);
	}
}
