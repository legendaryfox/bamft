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
import com.ksj.bamft.activity.ScheduleListActivity;
import com.ksj.bamft.activity.ScheduleProfileActivity;
import com.ksj.bamft.activity.TruckListActivity;
import com.ksj.bamft.activity.TruckMenuListActivity;
import com.ksj.bamft.activity.TruckProfileActivity;
import com.ksj.bamft.activity.TruckScheduleListActivity;
import com.ksj.bamft.activity.TruckTwitterListActivity;
import com.ksj.bamft.activity.TruckYelpListActivity;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;



public class ProfileTabsHelper {

	public static String referrer;
	public static Schedule schedule;
	public static double userLatitude;
	public static double userLongitude;

	public static final void setupProfileTabs(final Context context, final Truck truck, String highlightTab) {




		//this.referrer = referrer;

		Time now = new Time();
		now.setToNow();

		final String dayOfWeek = BamftActivity.getDayOfWeek(now);
		final String timeOfDay = BamftActivity.getMealOfDay(now);




		// BEGIN "TAB" BUTTONS
		// Profile button

		// For this profile button, we first need to find out if we came from ScheduleList or TruckList

		Button profileButton = (Button) ((Activity) context).findViewById(R.id.truckProfileProfileButton);
		if(profileButton != null) {
			profileButton.setOnClickListener(new View.OnClickListener() {



				public void onClick(View v) {

					Log.d("REFERRER", "referrer is " + referrer);

					if (referrer == "truck" || schedule == null) {
						// from truck view

						Intent loadTruckProfileIntent = new Intent(context, TruckProfileActivity.class);

						// create the schedule bundle
						Bundle truckIdBundle = new Bundle();
						truckIdBundle.putInt("truckId", truck.getId());
						truckIdBundle.putString(Constants.REFERRER, "truck");
						loadTruckProfileIntent.putExtras(truckIdBundle);


						// Start the activity
						context.startActivity(loadTruckProfileIntent);

					} else {
						// from schedule view
						//Proof of concept...
						final DatabaseHandler db = new DatabaseHandler(context);
						int landmark_id = schedule.getLandmarkId();
						Landmark landmark = db.getLandmark(landmark_id);


						// Create the intent
						Intent loadScheduleProfileIntent = new Intent(context, ScheduleProfileActivity.class);

						// create the schedule bundle
						Bundle extras = new Bundle();
						extras.putSerializable(Constants.SCHEDULE, schedule);
						extras.putDouble(Constants.USER_LATITUDE, userLatitude);
						extras.putDouble(Constants.USER_LONGITUDE, userLongitude);
						extras.putString(Constants.REFERRER, "schedule");
						loadScheduleProfileIntent.putExtras(extras);

						// Start the activity
						context.startActivity(loadScheduleProfileIntent);

					}


					/*
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
					 */
				}
			});
		}

		// Schedule button

		Button scheduleButton = (Button) ((Activity) context).findViewById(R.id.truckProfileScheduleButton);
		if(scheduleButton != null) {
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
					if ("schedule" == referrer) {
						extras.putSerializable(Constants.SCHEDULE, schedule);
						extras.putDouble(Constants.USER_LATITUDE, userLatitude);
						extras.putDouble(Constants.USER_LONGITUDE, userLongitude);
					}
					loadTruckScheduleIntent.putExtras(extras);

					// Start the activity
					context.startActivity(loadTruckScheduleIntent);
				}
			});
		}

		// Menu button
		Button menuButton = (Button) ((Activity) context).findViewById(R.id.truckProfileMenuButton);
		if (menuButton != null) {
			menuButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					//Create intent
					Intent loadTruckMenuIntent = new Intent(context, TruckMenuListActivity.class);

					// Create extras bundle
					Bundle extras = new Bundle();
					extras.putString(Constants.REFERRER, referrer);
					extras.putInt(Constants.TRUCK_ID, truck.getId()); // we'll pass the truck ID and let TruckMenuListActivity handle it.
					//TODO: probably should change this to TRUCK instead of truck_id for consistency's sake.
					if ("schedule" == referrer) {
						extras.putSerializable(Constants.SCHEDULE, schedule);
						extras.putDouble(Constants.USER_LATITUDE, userLatitude);
						extras.putDouble(Constants.USER_LONGITUDE, userLongitude);
					}
					loadTruckMenuIntent.putExtras(extras);

					// Start the activity
					context.startActivity(loadTruckMenuIntent);

				}

			});
		}

		// Twitter button

		Button twitterButton = (Button) ((Activity) context).findViewById(R.id.truckProfileTwitterButton);
		if (twitterButton != null) {
			twitterButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					//Create intent
					Intent loadTruckTwitterIntent = new Intent(context, TruckTwitterListActivity.class);

					// Create extras bundle
					Bundle extras = new Bundle();
					extras.putString(Constants.TWITTER_HANDLE, truck.getTwitter());
					extras.putSerializable(Constants.TRUCK, truck);
					extras.putString(Constants.REFERRER, referrer);
					if ("schedule" == referrer) {
						extras.putSerializable(Constants.SCHEDULE, schedule);
						extras.putDouble(Constants.USER_LATITUDE, userLatitude);
						extras.putDouble(Constants.USER_LONGITUDE, userLongitude);
					}
					loadTruckTwitterIntent.putExtras(extras);

					// Start the activity
					context.startActivity(loadTruckTwitterIntent);

				}

			});
		}

		// Yelp Button
		Button yelpButton = (Button) ((Activity) context).findViewById(R.id.truckProfileYelpButton);
		if (yelpButton != null) {
			yelpButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// Create intent
					Intent loadTruckYelpIntent = new Intent(context, TruckYelpListActivity.class);

					// Create extras bundle
					Bundle extras = new Bundle();
					extras.putString(Constants.YELP_HANDLE, truck.getYelp());
					extras.putSerializable(Constants.TRUCK, truck);
					extras.putString(Constants.REFERRER, referrer);
					if ("schedule" == referrer) {
						extras.putSerializable(Constants.SCHEDULE, schedule);
						extras.putDouble(Constants.USER_LATITUDE, userLatitude);
						extras.putDouble(Constants.USER_LONGITUDE, userLongitude);
					}
					loadTruckYelpIntent.putExtras(extras);

					// Star the activity
					context.startActivity(loadTruckYelpIntent);


				}
			});
		}



		//Highlight the appropriate tab?
		if ("profile" == highlightTab && profileButton != null) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileProfileButton);
			profileButton.setBackgroundResource(R.drawable.tab_hover);
		} else if ("schedule" == highlightTab && scheduleButton != null) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileScheduleButton);
			scheduleButton.setBackgroundResource(R.drawable.tab_hover);
		} else if ("menu" == highlightTab && menuButton != null) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileMenuButton);
			menuButton.setBackgroundResource(R.drawable.tab_hover);
		} else if ("twitter" == highlightTab && twitterButton != null) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileTwitterButton);
			twitterButton.setBackgroundResource(R.drawable.tab_hover);
		} else if ("reviews" == highlightTab && yelpButton != null) {
			//Button highlightButton = (Button) ((Activity) context).findViewById(R.id.truckProfileYelpButton);
			yelpButton.setBackgroundResource(R.drawable.tab_hover);
		} else {
			// do nothing..
		}






		// END "TAB" BUTTONS


	}

}
