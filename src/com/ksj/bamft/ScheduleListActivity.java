package com.ksj.bamft;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ScheduleListActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        //dynamically load time of day based on previous Activity.
        String timeOfDay = "Afternoon"; //set this as default for safety
        String dayOfWeek = "Thursday"; //set this as a defualt for safety
        Bundle timeBundle = this.getIntent().getExtras();
        timeOfDay = timeBundle.getString("timeOfDay");
        dayOfWeek = timeBundle.getString("dayOfWeek");
        
        //First, we get the food truck data from the API
        final DatabaseHandler db = new DatabaseHandler(this); 
        //TODO: Port database handler into Application object - 
        //see http://stackoverflow.com/questions/3433883/creating-a-service-to-share-database-connection-between-all-activities-in-androi
        
        final List<Schedule> scheduleList = db.getSchedulesByDayAndTime(dayOfWeek, timeOfDay);
        
        //this part is for displaying it in the ListView
        //note that we still use R.layout.truck_row
        ScheduleRowAdapter adapter = new ScheduleRowAdapter(this.getBaseContext(), R.layout.truck_row, scheduleList);
        setListAdapter(adapter);
        
        // Here is where we do the actual display.
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        //ListView "toast" functionality - for each item being clicked
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        		//Proof of concept...
        		int truck_id = scheduleList.get(position).getTruckId();
        		Truck truck = db.getTruck(truck_id);
        		
        		int landmark_id = scheduleList.get(position).getLandmarkId();
        		Landmark landmark = db.getLandmark(landmark_id);
        		
        		String location_string = landmark.getName() + " at (" + landmark.getXcoord() + ", " + landmark.getYcoord() + ")";
        		Toast.makeText(getApplicationContext(), location_string, Toast.LENGTH_SHORT).show();
        		
        		// Load the activity
        		
        		// Create the intent
        		Intent loadScheduleProfileIntent = new Intent(ScheduleListActivity.this, ScheduleProfileActivity.class);
        		
        		// create the schedule bundle
        		Bundle scheduleIdBundle = new Bundle();
        		scheduleIdBundle.putInt("scheduleId", scheduleList.get(position).getId());
        		loadScheduleProfileIntent.putExtras(scheduleIdBundle);
        	
        		// Start the activity
        		ScheduleListActivity.this.startActivity(loadScheduleProfileIntent);
        		
        		
        	}
        });
        
        
    }
    
        
}
