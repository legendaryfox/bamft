package com.ksj.bamft.activity;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.ksj.bamft.model.HubwayStation;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

public class BamftMapActivity extends MapActivity {

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        Bundle extras = this.getIntent().getExtras();
        
        String dayOfWeek = extras.getString(Constants.DAY_OF_WEEK);
        String timeOfDay = extras.getString(Constants.TIME_OF_DAY);
        String mapToCreate = extras.getString(Constants.MAP_TYPE);
        
        if (mapToCreate.equals(Constants.MAP_TYPE_TRUCKS))
        	createOpenTrucksMapView(dayOfWeek, timeOfDay);
        
        else if (mapToCreate.equals(Constants.MAP_TYPE_HUBWAY))
        	createHubwayStationsMapView();
	}
	
	
	/** 
	 * Not fully functional yet :(
	 */
	private void createHubwayStationsMapView() {
		setContentView(R.layout.open_trucks_map);
		
		List<HubwayStation> stations = HubwayHelpers.getAvailableStations();
		
		MapView mapView = (MapView) findViewById(R.id.openTrucksMap);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> overlayToDisplay = mapView.getOverlays();
        
        Drawable overlayMarker =
        		this.getResources().getDrawable(R.drawable.androidmarker);
        
        overlayMarker.setBounds(0, 0, overlayMarker.getIntrinsicWidth(), overlayMarker.getIntrinsicHeight());
        
        MapOverlays overlay = new MapOverlays(overlayMarker, this);
        
        for (HubwayStation station : stations) {
        	if (station.isInstalled()) {
        		int lat = MapHelpers.degreesToMicrodegrees(station.getLatitude());
        		int lon = MapHelpers.degreesToMicrodegrees(station.getLongitude());
        		
        		GeoPoint point = new GeoPoint(lat, lon);
        		OverlayItem overlayItem = new OverlayItem(point, station.getName(), "");
        		overlay.addOverlay(overlayItem);
        	}
        }
        
        overlay.populateNow();
        overlayToDisplay.add(overlay);
	}
	
	/**
	 * Create map with overlays for all the currently open 
	 * food trucks.
	 * 
	 * @param dayOfWeek
	 * @param timeOfDay
	 */
	private void createOpenTrucksMapView(String dayOfWeek, String timeOfDay) {
		setContentView(R.layout.open_trucks_map);
        
        MapView mapView = (MapView) findViewById(R.id.openTrucksMap);
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
        overlayMarker.setBounds(0, 0, overlayMarker.getIntrinsicWidth(), overlayMarker.getIntrinsicHeight());
        
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
        
        determineMapZoom(mapView, lowerBound, upperBound, leftBound, rightBound);
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
    	
    	int latitude = (int) (Double.parseDouble(yCoord) * 1e6);
    	int longitude = (int) (Double.parseDouble(xCoord) * 1e6);
    	
    	GeoPoint truckLocation = new GeoPoint(latitude, longitude);
    	
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
        
        GeoPoint centerOfBoston = new GeoPoint(
        		(int) (Constants.BPL_LATITUDE * 1e6),
        		(int) (Constants.BPL_LONGITUDE * 1e6));
        
        mapController.setCenter(centerOfBoston);
	}
}
