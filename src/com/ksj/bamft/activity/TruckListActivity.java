package com.ksj.bamft.activity;

import java.util.List;

import com.ksj.bamft.R;
import com.ksj.bamft.actionbarhelpers.ActionBarTitleHelper;
import com.ksj.bamft.adapter.TruckRowAdapter;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Truck;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TruckListActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //First, we get the food truck data from the API
        final DatabaseHandler db = new DatabaseHandler(this); 
        final List<Truck> truckList = db.getAllTrucks();
        //TODO: Port database handler into Application object - 
        //see http://stackoverflow.com/questions/3433883/creating-a-service-to-share-database-connection-between-all-activities-in-androi
        
        // Display
        setContentView(R.layout.ab_truck_list);
        ActionBarTitleHelper.setTitleBar(this);
        
        //this part is for displaying it in the ListView
        TruckRowAdapter adapter = new TruckRowAdapter(this.getBaseContext(), R.layout.ab_truck_row, truckList);
        setListAdapter(adapter);
        
        // Here is where we do the actual display.
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        //ListView "toast" functionality - for each item being clicked
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        		Truck truck = truckList.get(position);
             	
        		// Create the intent
        		Intent loadTruckProfileIntent = new Intent(TruckListActivity.this, TruckProfileActivity.class);
        		
        		// Create the extras bundle
        		Bundle truckIdBundle = new Bundle();
        		truckIdBundle.putInt(Constants.TRUCK_ID, truck.getId());
        		truckIdBundle.putString(Constants.REFERRER, Constants.TRUCK);
        		loadTruckProfileIntent.putExtras(truckIdBundle);
        		
        		// Start the activity
        		TruckListActivity.this.startActivity(loadTruckProfileIntent);
        	}
        });
    }
}
