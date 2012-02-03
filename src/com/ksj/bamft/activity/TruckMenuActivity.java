package com.ksj.bamft.activity;

import java.util.List;

import com.ksj.bamft.R;
import com.ksj.bamft.adapter.TruckScheduleRowAdapter;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class TruckMenuActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Extract info from bundle
		
		Bundle extras = this.getIntent().getExtras();
		String menu = (String) extras.get(Constants.MENU);
		
		//display text..
		setContentView(R.layout.truck_food_menu);
		TextView menuTextTextView = (TextView)findViewById(R.id.menuText);
		
		menuTextTextView.setText(menu);
		
	}
	
}
