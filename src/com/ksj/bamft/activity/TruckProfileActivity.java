package com.ksj.bamft.activity;

import com.ksj.bamft.R;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TruckProfileActivity extends Activity {
	
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
	        
	        
	        //TODO: make it calculate real distance...lawl...
	        
	        String distance_string = String.format("%.2g", make_distance) + " mi";
	        
	        truckNameTextView.setText(truck.getName());
	        landmarkNameTextView.setText(landmark_name);
	        landmarkDistanceTextView.setText(distance_string);
	        
	        // Functionality for buttons
	        
	        Button scheduleButton = (Button) findViewById(R.id.truckProfileScheduleButton);
			scheduleButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
	        		// Create the intent
	        		Intent loadTruckScheduleIntent = new Intent(TruckProfileActivity.this, TruckScheduleListActivity.class);
	        		
	        		// create the schedule bundle
	        		Bundle extras = new Bundle();
	        		extras.putString(Constants.DAY_OF_WEEK, dayOfWeek);
	        		extras.putString(Constants.TIME_OF_DAY, timeOfDay);
	        		extras.putSerializable(Constants.TRUCK, truck);
	        		loadTruckScheduleIntent.putExtras(extras);
	        	
	        		// Start the activity
	        		TruckProfileActivity.this.startActivity(loadTruckScheduleIntent);
				}
			});
	 }
}
