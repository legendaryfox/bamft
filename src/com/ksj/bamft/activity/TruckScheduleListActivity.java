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
		
		/* get information passed from last activity
		 *	- timeOfDay
		 *	- dayOfWeek
		 *	- truck or truckId
		 */
		
		Bundle extras = this.getIntent().getExtras();
		Truck truck = (Truck) extras.get(Constants.TRUCK);
		String timeOfDay = extras.getString(Constants.TIME_OF_DAY);
		String dayOfWeek = extras.getString(Constants.DAY_OF_WEEK);
		
		final DatabaseHandler db = new DatabaseHandler(this);
		
		final List<Schedule> scheduleList = db.getSchedulesByTruck(truck);
		
		TruckScheduleRowAdapter adapter = new TruckScheduleRowAdapter(
				this.getBaseContext(), R.layout.truck_schedule_row, scheduleList);	
		
		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	}
}
