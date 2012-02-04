package com.ksj.bamft.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ksj.bamft.R;
import com.ksj.bamft.adapter.ScheduleRowAdapter;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

public class ScheduleListActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // Create a listener to get the user's location

        LocationListener locationListener = new LocationListener() {

    		public void onLocationChanged(Location location) {
    			double latitude = location.getLatitude();
    			double longitude = location.getLongitude();
    			
    			Log.d("LocationListener", latitude + " " + longitude);
    		}

    		public void onProviderDisabled(String provider) {
    			
    		}

    		public void onProviderEnabled(String provider) {
    			
    		}

    		public void onStatusChanged(String provider, int status, Bundle extras) {
    			
    		}
    	};
    	
    	// Create a location manager
    	
    	LocationManager locationManager = 
    			(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	
    	// Get enabled location provider with the best accuracy
    	
    	String bestLocationProvider;
    	
    	Criteria criteria = new Criteria();
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);
    	
    	bestLocationProvider = locationManager.getBestProvider(criteria, true);
        
        // Register location listener with location manager
        
        locationManager.requestLocationUpdates(
        		LocationManager.NETWORK_PROVIDER,
        		Constants.LOCATION_REFRESH_TIME,
        		Constants.LOCATION_REFRESH_DISTANCE,
        		locationListener);
        
        // Get user location
        
        final Location userLocation = locationManager.getLastKnownLocation(bestLocationProvider);
        
        // Stop listening for user location
        
        locationManager.removeUpdates(locationListener);
        
        Log.d("UserLocation", userLocation.getLatitude() + " " + userLocation.getLongitude());

        
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
        		Bundle extras = new Bundle();
        		extras.putSerializable(Constants.SCHEDULE, scheduleList.get(position));
        		extras.putDouble(Constants.USER_LATITUDE, userLocation.getLatitude());
        		extras.putDouble(Constants.USER_LONGITUDE, userLocation.getLongitude());
        		loadScheduleProfileIntent.putExtras(extras);
        	
        		// Start the activity
        		ScheduleListActivity.this.startActivity(loadScheduleProfileIntent);
        		
        		
        	}
        });
        
        
    }
    
        
}
