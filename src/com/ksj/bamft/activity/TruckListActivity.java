package com.ksj.bamft.activity;

import java.util.List;

import com.ksj.bamft.R;
import com.ksj.bamft.adapter.TruckRowAdapter;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Truck;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TruckListActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        

        //dynamically load time of day based on previous Activity.
        Bundle timeBundle = this.getIntent().getExtras();
        final String timeOfDay = timeBundle.getString("timeOfDay"); 
        final String dayOfWeek = timeBundle.getString("dayOfWeek"); 
        
        
        //First, we get the food truck data from the API
        final DatabaseHandler db = new DatabaseHandler(this); 
        //TODO: Port database handler into Application object - 
        //see http://stackoverflow.com/questions/3433883/creating-a-service-to-share-database-connection-between-all-activities-in-androi
        
        final List<Truck> truckList = db.getAllTrucks();
        
        
        // Display
        setContentView(R.layout.ab_truck_list);
		// Action Bar Left Icon
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, BamftActivity.createIntent(this), R.drawable.icon));
		actionBar.setTitle("BAMFT!");
        // End Display
        
        //this part is for displaying it in the ListView
        TruckRowAdapter adapter = new TruckRowAdapter(this.getBaseContext(), R.layout.ab_truck_row, truckList);
        setListAdapter(adapter);
        
        // Here is where we do the actual display.
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        //ListView "toast" functionality - for each item being clicked
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        		//Proof of concept...
             	Truck truck = truckList.get(position);
             	
             	/*
             	//Load the schedule (for the intent/next activity)
             	//TODO: This is probably...memory intensive....
             	Schedule schedule = db.getScheduleByTruckAndDayAndTime(truck, dayOfWeek, timeOfDay);
             	int clean_schedule_id = 0;
             	if (schedule != null)
             		clean_schedule_id = schedule.id;
             	*/
             	
        		// Load the activity
        		
        		// Create the intent
        		Intent loadTruckProfileIntent = new Intent(TruckListActivity.this, TruckProfileActivity.class);
        		
        		// create the schedule bundle
        		Bundle truckIdBundle = new Bundle();
        		truckIdBundle.putInt("truckId", truck.getId());
        		truckIdBundle.putString(Constants.REFERRER, "truck");
        		loadTruckProfileIntent.putExtras(truckIdBundle);
        		
        	
        		// Start the activity
        		TruckListActivity.this.startActivity(loadTruckProfileIntent);
        		
        		
        	}
        });
        
        
    }
    
        
}
