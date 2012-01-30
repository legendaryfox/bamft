package com.ksj.bamft.activity;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.ksj.bamft.R;
import com.ksj.bamft.adapter.TruckScheduleRowAdapter;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

public class TruckScheduleListActivity extends ListActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Extract info from bundle
		
		Bundle extras = this.getIntent().getExtras();
		Truck truck = (Truck) extras.get(Constants.TRUCK);
		
		// Grab schedule info from database and create ListView
		
		final DatabaseHandler db = new DatabaseHandler(this);
		
		final List<Schedule> scheduleList = db.getSchedulesByTruck(truck);
		
		TruckScheduleRowAdapter adapter = new TruckScheduleRowAdapter(
				this.getBaseContext(), R.layout.truck_schedule_row, scheduleList);	
		
		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	}
}
