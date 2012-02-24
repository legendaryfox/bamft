package com.ksj.bamft.activity;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
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
import com.ksj.bamft.actionbarhelpers.ActionBarTitleHelper;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.hubway.HubwayHelpers;
import com.ksj.bamft.maps.HubwayBalloonItemizedOverlay;
import com.ksj.bamft.maps.HubwayOverlayItem;
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
		setContentView(R.layout.ab_map_activity);
		ActionBarTitleHelper.setTitleBar(this);
		Button hubwayStationsButton = (Button) findViewById(R.id.hubwayStationsButton);
		hubwayStationsButton.setBackgroundResource(R.drawable.tab_hover);
		
		
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
        
        Drawable overlayMarker = this.getResources().getDrawable(R.drawable.ic_bike);
        
        overlayMarker.setBounds(0, 0,
        		overlayMarker.getIntrinsicWidth(), overlayMarker.getIntrinsicHeight());
        
       // MapOverlays overlay = new MapOverlays(overlayMarker, this);
        HubwayBalloonItemizedOverlay<OverlayItem> overlay = 
        		new HubwayBalloonItemizedOverlay<OverlayItem>(overlayMarker, mapView);
        
        // commented out geocoder since it's slow
        // if we can speed it up, we should definitely bring it back
        //Geocoder geocoder = new Geocoder(this);
        
        for (HubwayStation station : stations) {
        	//TODO: uncomment in release mode
        	if (station.isInstalled()) {
        		double latitude = station.getLatitude();
        		double longitude = station.getLongitude();
        		
        		GeoPoint point = MapHelpers.getGeoPoint(latitude, longitude);
        		
        		//OverlayItem overlayItem = new OverlayItem(point, station.getName(), "");
        		//overlay.addOverlay(overlayItem);
        		
        		/*String addressString = "";
        		
        		try {
        			Address address = null;
					List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
					
					if (addressList != null && addressList.size() > 0)
						address = addressList.get(0);
					
					if (address != null)
						addressString = address.getAddressLine(0);
					
				} catch (IOException e) {
					e.printStackTrace();
				}*/
        		
        		// Num bikes
        		
        		int numBikes = station.getNumBikes();
        		String numBikesStr = Constants.HUBWAY_NUM_BIKES + numBikes;
        		
        		// Num empty docks
        		
        		int numEmptyDocks = station.getNumEmptyDocks();
        		String numEmptyDocksStr = Constants.HUBWAY_NUM_EMPTY_DOCKS + numEmptyDocks;
        		
        		// Station locked info
        		
        		boolean stationLocked = station.isLocked();
        		
        		String lockedInfo = "";
        		
        		if (stationLocked)
        			lockedInfo = Constants.HUBWAY_STATION_LOCKED;
        		
        		HubwayOverlayItem overlayItem = new HubwayOverlayItem(
        				point,
        				station.getName(), 
        				numBikesStr,
        				numEmptyDocksStr, lockedInfo, "", "");
        		
        		overlay.addOverlay(overlayItem);
        	}
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
		setContentView(R.layout.ab_map_activity);
		ActionBarTitleHelper.setTitleBar(this);
		Button openTrucksButton = (Button) findViewById(R.id.openTrucksButton);
		openTrucksButton.setBackgroundResource(R.drawable.tab_hover);
        
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
        
        Drawable overlayMarker = this.getResources().getDrawable(R.drawable.truck_overlay);
        overlayMarker.setBounds(0, 0, 
        		overlayMarker.getIntrinsicWidth(), overlayMarker.getIntrinsicHeight());
        
        // Initialize bounds for the map. These will be used to determine
        // how far out we should zoom in order to fit all the markers.
        
        // Create list of overlays while keeping track of boundaries
        
        HubwayBalloonItemizedOverlay<OverlayItem> overlay = 
        		new HubwayBalloonItemizedOverlay<OverlayItem>(overlayMarker, mapView);
        
        Geocoder geocoder = new Geocoder(this);
        
        for (Schedule schedule : schedules) {
        	Landmark landmark = db.getLandmark(schedule.getLandmarkId());
        	
        	Double latitude = Double.parseDouble(landmark.getYcoord());
        	Double longitude = Double.parseDouble(landmark.getXcoord());
        	
        	GeoPoint point = MapHelpers.getGeoPoint(latitude, longitude);
        	
        	String addressString = "";
    		
    		try {
    			Address address = null;
				List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
				
				if (addressList != null && addressList.size() > 0)
					address = addressList.get(0);
				
				if (address != null)
					addressString = address.getAddressLine(0);
				
			} catch (IOException e) {
				e.printStackTrace();
			}        	
    		
        	Truck truck = db.getTruck(schedule.getTruckId());
        	
        	if (truck != null) {
	    		HubwayOverlayItem overlayItem = new HubwayOverlayItem(
	    				point, truck.getName(), truck.getCuisine(), addressString,  
	    				truck.getWebsite(), "", "");
	        	
	        	if (overlay != null)
	        		overlay.addOverlay(overlayItem);
        	}
        }
        
        overlay.populateNow();
        overlaysList.add(overlay);
        
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
