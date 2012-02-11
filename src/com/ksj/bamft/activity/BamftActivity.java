package com.ksj.bamft.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ksj.bamft.R;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.FoodItem;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;
import com.ksj.bamft.yelp.Yelp;




public class BamftActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Yelp.testExecute();

		// Prepares the internal SQLite database, if need be (dictated by CACHE_LIFE).
		//prepareData();
		BackgroundPrepareData task = new BackgroundPrepareData();
		task.execute();

		/*
        //BEGIN TEST DATA
        final DatabaseHandler db = new DatabaseHandler(this);
        List<Landmark> landmarkList = db.getAllLandmarks();
        List<Truck> truckList = db.getAllTrucks();
        List<Schedule> scheduleList = db.getAllSchedules();
        List<FoodItem> foodItemList = db.getAllFoodItems();

        for (Landmark l : landmarkList) {
        	Log.d("Landmark", l.toString());
        }
        for (Truck t : truckList) {
        	Log.d("Truck", t.toString());
        }
        for (Schedule s : scheduleList) {
        	Log.d("Schedule", s.toString());
        }
        for (FoodItem f : foodItemList) {
        	Log.d("FoodItem", f.toString());
        }
        //END TEST DATA
		 */








		SharedPreferences settings = getSharedPreferences(Constants.BAMFT_PREFS_NAME, 0);


		setContentView(R.layout.main);


		// Home page grid view
		//GridView gridview = (GridView) findViewById(R.id.gridview);
		//gridview.setAdapter(new ImageAdapter(this));

		// Calculate the formatted day of week and time of day
		Time now = new Time();
		now.setToNow();

		final String dayOfWeek = getDayOfWeek(now);
		final String timeOfDay = getMealOfDay(now);



		//For debugging purposes - showing the life of the cache and today's day.

		long cacheBirthday = settings.getLong(Constants.PREFS_CACHE_UPDATED, 0);
		String testingToastText = "It is currently " + dayOfWeek + " " + timeOfDay + ". Cache should be updated in : " + (cacheBirthday + Constants.CACHE_LIFE - now.toMillis(true))/1000 + " seconds.";
		Toast.makeText(BamftActivity.this, testingToastText, Toast.LENGTH_LONG).show();


	}

	public void menuClickFunction(final View v) {

		Time now = new Time();
		now.setToNow();
		//now.set(0, 0, 20, 7, 2, 2012);

		final String dayOfWeek = getDayOfWeek(now);
		final String timeOfDay = getMealOfDay(now);

		Bundle timeBundle = new Bundle();
		//timeBundle.putString(Constants.DAY_OF_WEEK, dayOfWeek);
		//timeBundle.putString(Constants.TIME_OF_DAY, timeOfDay); //default just in case...note that putString automatically overwrites existing values too
		timeBundle.putString(Constants.DAY_OF_WEEK, "Tuesday");
		timeBundle.putString(Constants.TIME_OF_DAY, "Evening");

		switch(v.getId()) {

		case R.id.menu_item_search_nearby:
			//Load Morning trucks
			//Toast.makeText(BamftActivity.this, "" + dayOfWeek + " Morning trucks", Toast.LENGTH_SHORT).show();
			//timeBundle.putString("timeOfDay", timeOfDay);
			Toast.makeText(BamftActivity.this,  "Nearby open trucks (for " + dayOfWeek + " " + timeOfDay + ")", Toast.LENGTH_SHORT).show();

			Intent loadScheduleListIntent = new Intent(BamftActivity.this, ScheduleListActivity.class);
			loadScheduleListIntent.putExtras(timeBundle);
			BamftActivity.this.startActivity(loadScheduleListIntent);

			break;
		case R.id.menu_item_list_all:
			//Load Afternoon trucks
			Toast.makeText(BamftActivity.this,  "All trucks (open and closed)", Toast.LENGTH_SHORT).show();

			Intent loadTruckListIntent = new Intent(BamftActivity.this, TruckListActivity.class);
			timeBundle.putString("timeOfDay", "Afternoon");
			loadTruckListIntent.putExtras(timeBundle);
			BamftActivity.this.startActivity(loadTruckListIntent);
			break;
		case R.id.menu_item_map_view:
			//Load Evening trucks
			Toast.makeText(BamftActivity.this,  "Not functional yet...", Toast.LENGTH_SHORT).show();



			break;
		case R.id.menu_item_surprise_me:
			// Force data cache

			SharedPreferences settings = getSharedPreferences(Constants.BAMFT_PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putLong(Constants.PREFS_CACHE_UPDATED, 0);
			editor.commit();
			//prepareData();
			
			BackgroundPrepareData task = new BackgroundPrepareData();
			task.execute();
			
			Toast.makeText(BamftActivity.this, "Updated Cache...", Toast.LENGTH_SHORT).show();

			break;
		}        		



	}



	public static String getDayOfWeek(Time now) {


		return Constants.DAYS_OF_WEEK[now.weekDay]; 
	}

	/**
	 * Gets the appropriate name of the meal based upon the hour specified.
	 * @param 
	 * @return "Morning", "Afternoon", "Evening"
	 */
	public static String getMealOfDay(Time now) {



		Time morningStart = new Time(now);
		morningStart.set(0, 0, Constants.MORNING_START_HOUR, now.monthDay, now.month, now.year);

		Time afternoonStart = new Time(now);
		afternoonStart.set(0, 0, Constants.AFTERNOON_START_HOUR, now.monthDay, now.month, now.year);

		Time eveningStart = new Time(now);
		eveningStart.set(0, 0, Constants.EVENING_START_HOUR, now.monthDay, now.month, now.year);

		Time closing = new Time(now);
		closing.set(now.second, now.minute, Constants.CLOSING_HOUR, now.monthDay, now.month, now.year);

		Log.d("MEAL", "Now: " + now.hour + ":" + now.minute + " Morning: " + morningStart.hour + ":" + morningStart.minute + " Afternoon: " + afternoonStart.hour + ":" + afternoonStart.minute + " Evening: " + eveningStart.hour + ":" + eveningStart.minute + " Closing: " + closing.hour + ":" + closing.minute);




		if (now.after(morningStart) && now.before(afternoonStart)) {
			// morning service
			Log.d("MEAL", "MORNING");
			return Constants.MORNING_MEAL_STRING;

		} else if (now.after(afternoonStart) && now.before(eveningStart)) {
			// afternoon service
			Log.d("MEAL", "AFTERNOON");
			return Constants.AFTERNOON_MEAL_STRING;

		} else if (now.after(eveningStart) && now.before(closing)) {
			// evneing service
			Log.d("MEAL", "EVENING");
			return Constants.EVENING_MEAL_STRING;


		} else {
			// closed
			Log.d("MEAL", "CLOSED");
			return Constants.CLOSED_MEAL_STRING;

		}


	}


	/**
	 * Prepares the data by querying the bamftserver and updating internal SQL database
	 * 
	 */
	public void prepareData() {

		SharedPreferences settings = getSharedPreferences(Constants.BAMFT_PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();

		Time now = new Time();
		now.setToNow();

		long cacheBirthday = settings.getLong("cacheUpdated", 0);
		if ((now.toMillis(true) - cacheBirthday) > Constants.CACHE_LIFE) {
			// now minus the birthday is bigger than the expected age - so we should re-grab from cache


			// Connect to DB
			DatabaseHandler db = new DatabaseHandler(this);

			// First, we pull get the data from the Landmark, Truck, and Schedule tables.
			String landmarksDumpData = readServerData(Constants.LANDMARKS_DUMP_URL);
			String trucksDumpData = readServerData(Constants.TRUCKS_DUMP_URL);
			String schedulesDumpData = readServerData(Constants.SCHEDULES_DUMP_URL);
			String foodItemsDumpData = readServerData(Constants.FOOD_ITEMS_DUMP_URL);

			// Dump the database
			//db.recreateAllTables();

			boolean cacheSuccessFlag = true;

			// Landmarks
			if (landmarksDumpData.length() > 0) {
				db.recreateTable("landmarks");

				try {
					JSONArray landmarksArray = new JSONArray(landmarksDumpData);

					for (int i = 0; i < landmarksArray.length(); i++) {
						//Iterate through each entry and save to DB
						JSONObject landmarkObject = landmarksArray.getJSONObject(i).getJSONObject("landmark");
						Landmark landmark = new Landmark(
								landmarkObject.getInt("id"), 
								landmarkObject.getString("name"), 
								landmarkObject.getString("xcoord"), 
								landmarkObject.getString("ycoord"));
						db.addLandmark(landmark);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.d("PrepareData", "Landmarks data updated");
			} else {
				cacheSuccessFlag = false;
			}


			// Trucks
			if (trucksDumpData.length() > 0) {
				db.recreateTable("trucks");   		
				try {
					JSONArray trucksArray = new JSONArray(trucksDumpData);

					for (int i = 0; i < trucksArray.length(); i++) {
						//Iterate through each entry and save to DB
						JSONObject truckObject = trucksArray.getJSONObject(i).getJSONObject("truck");
						Truck truck = new Truck(
								truckObject.getInt("id"), 
								truckObject.getString("name"), 
								truckObject.getString("cuisine"), 
								truckObject.getString("description"),
								truckObject.getString("email"),
								truckObject.getString("menu"),
								truckObject.getString("twitter"),
								truckObject.getString("facebook"),
								truckObject.getString("website"),
								truckObject.getString("yelp"));


						// Fix for null values
						if (truck.getCuisine() == "null" || truck.getCuisine().length() < 1) truck.setCuisine(Constants.EMPTY_FIELD_STRING);
						if (truck.getDescription() == "null" || truck.getDescription().length() < 1) truck.setDescription(Constants.EMPTY_FIELD_STRING);
						if (truck.getEmail() == "null" || truck.getEmail().length() < 1) truck.setEmail(Constants.EMPTY_FIELD_STRING);
						if (truck.getMenu() == "null" || truck.getMenu().length() < 1) truck.setMenu(Constants.EMPTY_FIELD_STRING);
						if (truck.getTwitter() == "null" || truck.getTwitter().length() < 1) truck.setTwitter(Constants.EMPTY_FIELD_STRING);
						if (truck.getFacebook() == "null" || truck.getFacebook().length() < 1) truck.setFacebook(Constants.EMPTY_FIELD_STRING);
						if (truck.getWebsite() == "null" || truck.getWebsite().length() < 1) truck.setWebsite(Constants.EMPTY_FIELD_STRING);
						if (truck.getYelp() == "null" || truck.getYelp().length() < 1) truck.setYelp(Constants.EMPTY_FIELD_STRING);


						db.addTruck(truck);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.d("PrepareData", "Trucks data updated");
			} else {
				cacheSuccessFlag = false;
			}

			// SCHEDULES
			if (schedulesDumpData.length() > 0) {
				db.recreateTable("schedules");
				try {
					JSONArray schedulesArray = new JSONArray(schedulesDumpData);

					for (int i = 0; i < schedulesArray.length(); i++) {
						//Iterate through each entry and save to DB
						JSONObject scheduleObject = schedulesArray.getJSONObject(i).getJSONObject("schedule");
						Schedule schedule = new Schedule(
								scheduleObject.getInt("id"), 
								scheduleObject.getString("day_of_week"), 
								scheduleObject.getString("time_of_day"), 
								scheduleObject.getInt("truck_id"),
								scheduleObject.getInt("landmark_id"));
						db.addSchedule(schedule);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.d("PrepareData", "Schedule data updated");
			} else {
				cacheSuccessFlag = false;
			}

			// FOOD ITEMS
			if (foodItemsDumpData.length() > 0) {
				db.recreateTable("food_items");   		
				try {
					JSONArray foodItemsArray = new JSONArray(foodItemsDumpData);

					for (int i = 0; i < foodItemsArray.length(); i++) {
						//Iterate through each entry and save to DB
						JSONObject foodItemObject = foodItemsArray.getJSONObject(i).getJSONObject("menu_item");
						FoodItem foodItem = new FoodItem(

								foodItemObject.getInt("id"),
								foodItemObject.getString("name"),
								foodItemObject.getString("description"),
								foodItemObject.getString("price"),
								foodItemObject.getInt("truck_id"));

						db.addFoodItem(foodItem);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.d("PrepareData", "FoodItem data updated");
			} else {
				cacheSuccessFlag = false;
			}





			if (cacheSuccessFlag) {
				editor.putLong(Constants.PREFS_CACHE_UPDATED, now.toMillis(true));
				editor.commit();
			}
			//Toast.makeText(BamftActivity.this, "Cache was updated at " + now.toMillis(true), Toast.LENGTH_SHORT);
			//Log.d("CACHE", "Cache was updated at " + now.toMillis(true));

		} else {

			Log.d("CACHE", "Cache was not updated.");

		}
	}

	/**
	 * Reads the server data from specified URL. Essentially a web page http-get function. 
	 * @param dumpUrl
	 * @return data from the queried URL
	 */
	private String readServerData(String dumpUrl) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();


		HttpGet httpGet = new HttpGet(dumpUrl);
		try {
			HttpResponse response = client.execute(httpGet); //run the Get

			//get the status
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { //HTTP 200 = "OKAY"
				Log.d("APIConnection", "I got an HTTP200");
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					//basic "while not EOF, keep reading"
					builder.append(line);
				}
			} else {
				Log.d("APIConnection", "Was not an HTTP200");
				Log.e(BamftActivity.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			Log.e("APIConnection", "ClientProtocol Failed to connect to API.");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("APIConnection", "IO Exception Failed to connect to API.");
			e.printStackTrace();
		}
		//Log.d("APIConnection", "API Value: " + builder.toString());
		return builder.toString();
	}

	/*
	private class GrabJsonData extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... dumpUrls) {

			int count = dumpUrls.length;
			for (int i = 0; i < count; i++) {
				readServerData(dumpUrls[i]);
			}
			
			return null;
		}


		
	}
	*/
	private class BackgroundPrepareData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			prepareData();
			return null;
		}
		
		
	}


}
