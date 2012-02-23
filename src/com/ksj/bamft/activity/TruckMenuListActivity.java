package com.ksj.bamft.activity;

import java.util.List;

import com.ksj.bamft.R;
import com.ksj.bamft.actionbarhelpers.ActionBarTitleHelper;
import com.ksj.bamft.actionbarhelpers.ProfileTabsHelper;
import com.ksj.bamft.adapter.FoodItemAdapter;
import com.ksj.bamft.adapter.TruckScheduleRowAdapter;
import com.ksj.bamft.adapter.TweetItemAdapter;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.FoodItem;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class TruckMenuListActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// Extract info from bundle
		
		Bundle extras = this.getIntent().getExtras();
		int truck_id = (int) extras.getInt(Constants.TRUCK_ID);
		
		//load food items
		final DatabaseHandler db = new DatabaseHandler(this); 
		Truck truck = db.getTruck(truck_id);
		final List<FoodItem> foodItemList = db.getFoodItemsByTruck(truck);
		
		setContentView(R.layout.ab_food_list);
		ActionBarTitleHelper.setTitleBar(this);
		ProfileTabsHelper.setupProfileTabs(this, truck, "menu");
		
		FoodItemAdapter adapter = new FoodItemAdapter(this, R.layout.food_item_row, foodItemList);
		setListAdapter(adapter);
		
		
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setDivider(null);
		
		ActionBarTitleHelper.setTitleBar(this);
		ProfileTabsHelper.setupProfileTabs(this, truck, "menu");
		
		
		
	}
	
}
