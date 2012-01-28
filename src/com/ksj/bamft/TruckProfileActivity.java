package com.ksj.bamft;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TruckProfileActivity extends Activity {
	
	 public void onCreate(Bundle savedInstanceState) {
		 
	        super.onCreate(savedInstanceState);
	        
	        
	        // start doing stuff?
	        
	        //Open database connect
	        final DatabaseHandler db = new DatabaseHandler(this);
	        
	        Bundle scheduleIdBundle = this.getIntent().getExtras();
	        int scheduleId = scheduleIdBundle.getInt("scheduleId");
	        
	        
	        //Grab the relevant data
	        Schedule schedule = db.getSchedule(scheduleId);
	        Truck truck = db.getTruck(schedule.getTruckId());
	        Landmark landmark = db.getLandmark(schedule.getLandmarkId());
	        
	        // Finally, fill the layout
	        setContentView(R.layout.truck_profile);
	        
	        TextView truckNameTextView = (TextView)findViewById(R.id.truckNameText);
	        TextView landmarkNameTextView = (TextView)findViewById(R.id.landmarkNameText);
	        TextView landmarkDistanceTextView = (TextView)findViewById(R.id.landmarkDistanceText);
	        
	        //TODO: make it calculate real distance...lawl...
	        float make_x = new Float(landmark.getXcoord());
	        float make_y = new Float(landmark.getYcoord());
	        float make_distance = make_x / make_y;
	        String distance_string = String.format("%.2g", make_distance) + " mi";
	        
	        truckNameTextView.setText(truck.getName());
	        landmarkNameTextView.setText(landmark.getName());
	        landmarkDistanceTextView.setText(distance_string);
	      
	        
	        
	 }

}