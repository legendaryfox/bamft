package com.ksj.bamft.activity;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.ksj.bamft.R;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.MapOverlays;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

public class TruckProfileActivity extends MapActivity {
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 
        super.onCreate(savedInstanceState);
        
        // start doing stuff?
        
        //Open database connect
        final DatabaseHandler db = new DatabaseHandler(this);
        
        
        Time now = new Time();
    	now.setToNow();
    	
    	
        final String dayOfWeek = BamftActivity.getDayOfWeek(now);
        final String timeOfDay = BamftActivity.getMealOfDay(now);

        Bundle truckIdBundle = this.getIntent().getExtras();
        int truckId = truckIdBundle.getInt("truckId");
        
        
        //Grab the relevant data
        
        final Truck truck = db.getTruck(truckId);      
        String landmark_name;
        float make_x;
        float make_y;
        float make_distance;
        Schedule schedule = db.getScheduleByTruckAndDayAndTime(truck, dayOfWeek, timeOfDay); //db.getSchedule(scheduleId);
        
        if (schedule != null) { 
        	Landmark landmark = db.getLandmark(schedule.getLandmarkId());
        	landmark_name = landmark.getName();
        	make_x = new Float(landmark.getXcoord());
	        make_y = new Float(landmark.getYcoord());
	        make_distance = make_x / make_y;
        }
        
        else {
        	landmark_name = "";
        	make_x = 0;
        	make_y = 0;
        	make_distance = 0;
        	
        }
        
        // Finally, fill the layout
        setContentView(R.layout.truck_profile);
        
        TextView truckNameTextView = (TextView)findViewById(R.id.truckNameText);
        TextView landmarkNameTextView = (TextView)findViewById(R.id.landmarkNameText);
        TextView landmarkDistanceTextView = (TextView)findViewById(R.id.landmarkDistanceText);
        TextView truckDescriptionTextView = (TextView)findViewById(R.id.truckDescriptionText);
        
        
        //TODO: make it calculate real distance...lawl...
        
        String distance_string = String.format("%.2g", make_distance) + " mi";
        
        truckNameTextView.setText(truck.getName());
        landmarkNameTextView.setText(landmark_name);
        landmarkDistanceTextView.setText(distance_string);
        truckDescriptionTextView.setText(truck.getDescription());
        
        // Map view
        
        MapView mapView = (MapView) findViewById(R.id.truckProfileMap);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> overlayToDisplay = mapView.getOverlays();
        Drawable overlayMarker =
        		this.getResources().getDrawable(R.drawable.androidmarker);
        MapOverlays overlay = new MapOverlays(overlayMarker);
        
        GeoPoint truckLocation = new GeoPoint(19240000,-99120000);
        OverlayItem overlayItem = new OverlayItem(truckLocation, "Hola, Mundo!", "I'm in Mexico City!");
        
        overlay.addOverlay(overlayItem);
        overlayToDisplay.add(overlay);
        
        MapController mapController = mapView.getController();
        mapController.setCenter(truckLocation);
        
        // Google Maps button -- temporary, only here for testing intents to !
        
        Button mapsButton = (Button) findViewById(R.id.truckProfileMapsButton);
        mapsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
		        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
		        		Uri.parse("geo:0,0?q=37.423156,-122.084917 (" + truck.getName() + ")"));
		        
		        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		        
		        startActivity(intent);
			}
		});
        
        // Schedule button
        
        Button scheduleButton = (Button) findViewById(R.id.truckProfileScheduleButton);
		scheduleButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
        		// Create the intent
        		Intent loadTruckScheduleIntent = new Intent(TruckProfileActivity.this, TruckScheduleListActivity.class);
        		
        		// Create extras bundle
        		Bundle extras = new Bundle();
        		extras.putString(Constants.DAY_OF_WEEK, dayOfWeek);
        		extras.putString(Constants.TIME_OF_DAY, timeOfDay);
        		extras.putSerializable(Constants.TRUCK, truck);
        		loadTruckScheduleIntent.putExtras(extras);
        	
        		// Start the activity
        		TruckProfileActivity.this.startActivity(loadTruckScheduleIntent);
			}
		});
		
		// Twitter button
		
		Button twitterButton = (Button) findViewById(R.id.truckProfileTwitterButton);
		twitterButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Create intent
				Intent loadTruckTwitterIntent = new Intent(TruckProfileActivity.this, TruckTwitterActivity.class);
				
				// Create extras bundle
				
				// Start the activity
				TruckProfileActivity.this.startActivity(loadTruckTwitterIntent);
				
			}
			
		});
	 }
}
