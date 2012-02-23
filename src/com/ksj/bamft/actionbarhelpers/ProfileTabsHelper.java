package com.ksj.bamft.actionbarhelpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ksj.bamft.R;
import com.ksj.bamft.activity.BamftActivity;
import com.ksj.bamft.activity.ScheduleProfileActivity;
import com.ksj.bamft.activity.TruckMenuListActivity;
import com.ksj.bamft.activity.TruckScheduleListActivity;
import com.ksj.bamft.activity.TruckTwitterListActivity;
import com.ksj.bamft.activity.TruckYelpListActivity;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.model.Truck;

public class ProfileTabsHelper {
	
	//public static String referrer;

	public static final void SetupProfileTabs(final Context context, final Truck truck, String highlightTab, final String referrer) {

		Time now = new Time();
		now.setToNow();

		final String dayOfWeek = BamftActivity.getDayOfWeek(now);
		final String timeOfDay = BamftActivity.getMealOfDay(now);

		

		/*
		if (referrer == null) {
			referrer = "truck";
		}
		*/

		// BEGIN "TAB" BUTTONS
		// Profile button

		// For this profile button, we first need to find out if we came from ScheduleList or TruckList
		
		Button profileButton = (Button) ((Activity) context).findViewById(R.id.truckProfileProfileButton);
		profileButton.setOnClickListener(new View.OnClickListener() {

			
			
			public void onClick(View v) {
				
				
				if (referrer == "truck") {
					
					
				} else {
					
				}
					
			
				
				// Create the intent
				Intent loadTruckScheduleIntent = new Intent(context, TruckScheduleListActivity.class);

				// Create extras bundle
				Bundle extras = new Bundle();
				extras.putString(Constants.DAY_OF_WEEK, dayOfWeek);
				extras.putString(Constants.TIME_OF_DAY, timeOfDay);
				extras.putSerializable(Constants.TRUCK, truck);
				extras.putString(Constants.REFERRER, referrer);
				loadTruckScheduleIntent.putExtras(extras);

				// Start the activity
				context.startActivity(loadTruckScheduleIntent);
				
			}
		});


		// Schedule button

		Button scheduleButton = (Button) ((Activity) context).findViewById(R.id.truckProfileScheduleButton);
		scheduleButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Create the intent
				Intent loadTruckScheduleIntent = new Intent(context, TruckScheduleListActivity.class);

				// Create extras bundle
				Bundle extras = new Bundle();
				extras.putString(Constants.DAY_OF_WEEK, dayOfWeek);
				extras.putString(Constants.TIME_OF_DAY, timeOfDay);
				extras.putSerializable(Constants.TRUCK, truck);
				extras.putString(Constants.REFERRER, referrer);
				loadTruckScheduleIntent.putExtras(extras);

				// Start the activity
				context.startActivity(loadTruckScheduleIntent);
			}
		});

		// Menu button
		Button menuButton = (Button) ((Activity) context).findViewById(R.id.truckProfileMenuButton);
		menuButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//Create intent
				Intent loadTruckMenuIntent = new Intent(context, TruckMenuListActivity.class);

				// Create extras bundle
				Bundle extras = new Bundle();
				extras.putString(Constants.REFERRER, referrer);
				extras.putInt(Constants.TRUCK_ID, truck.getId()); // we'll pass the truck ID and let TruckMenuListActivity handle it.
				//TODO: probably should change this to TRUCK instead of truck_id for consistency's sake.
				loadTruckMenuIntent.putExtras(extras);

				// Start the activity
				context.startActivity(loadTruckMenuIntent);

			}

		});

		// Twitter button

		Button twitterButton = (Button) ((Activity) context).findViewById(R.id.truckProfileTwitterButton);
		twitterButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//Create intent
				Intent loadTruckTwitterIntent = new Intent(context, TruckTwitterListActivity.class);

				// Create extras bundle
				Bundle extras = new Bundle();
				extras.putString(Constants.TWITTER_HANDLE, truck.getTwitter());
				extras.putSerializable(Constants.TRUCK, truck);
				extras.putString(Constants.REFERRER, referrer);
				loadTruckTwitterIntent.putExtras(extras);

				// Start the activity
				context.startActivity(loadTruckTwitterIntent);

			}

		});

		// Yelp Button
		Button yelpButton = (Button) ((Activity) context).findViewById(R.id.truckProfileYelpButton);
		yelpButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Create intent
				Intent loadTruckYelpIntent = new Intent(context, TruckYelpListActivity.class);

				// Create extras bundle
				Bundle extras = new Bundle();
				extras.putString(Constants.YELP_HANDLE, truck.getYelp());
				extras.putSerializable(Constants.TRUCK, truck);
				extras.putString(Constants.REFERRER, referrer);
				loadTruckYelpIntent.putExtras(extras);

				// Star the activity
				context.startActivity(loadTruckYelpIntent);


			}
		});



		//Highlight the appropriate tab?
		if ("profile" == highlightTab) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileProfileButton);
			profileButton.setBackgroundResource(R.drawable.tab_hover);
		} else if ("schedule" == highlightTab) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileScheduleButton);
			scheduleButton.setBackgroundResource(R.drawable.tab_hover);
		} else if ("menu" == highlightTab) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileMenuButton);
			menuButton.setBackgroundResource(R.drawable.tab_hover);
		} else if ("twitter" == highlightTab) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileTwitterButton);
			twitterButton.setBackgroundResource(R.drawable.tab_hover);
		} else if ("reviews" == highlightTab) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileYelpButton);
			yelpButton.setBackgroundResource(R.drawable.tab_hover);
		} else {
			// do nothing..
		}






		// END "TAB" BUTTONS


	}

}
