/**
 * BamftActivity.java
 * 
 * Activity that is loaded upon app startup.
 */

package com.ksj.bamft.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ksj.bamft.R;
import com.ksj.bamft.actionbarhelpers.ActionBarTitleHelper;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.mbta.MbtaHelpers;
import com.ksj.bamft.model.Factlet;
import com.ksj.bamft.model.FoodItem;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.MbtaStation;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

public class BamftActivity extends Activity {

	private List<Factlet> factlets = null;
	boolean currentlyUpdating = false; 
	boolean safeToUpdate = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// BEGIN DISPLAY
		// setContentView(R.layout.main);

		setContentView(R.layout.ab_home);

		// Action Bar Left Icon
		/*		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.icon));
		actionBar.setTitle("BAMFT!");
		 */
		ActionBarTitleHelper.setTitleBar(this);

		DatabaseHandler db = new DatabaseHandler(this);

		setupFactlets(db);
		// END DISPLAY

		//Prompt user for internets if necessary
		checkInternetConnection();
		BackgroundPrepareData task = new BackgroundPrepareData();
		task.execute();
		Log.d("ASYNC", "should start now...");
	}

	private void finishCreatingActivity() {
		//Yelp.testExecute();

		// Prepares the internal SQLite database, if need be (dictated by CACHE_LIFE).
		//prepareData();

		final DatabaseHandler db = new DatabaseHandler(this);

		//BEGIN TEST DATA
		// TEST - MBTA Stuff
		List<MbtaStation> mbtaStationList = MbtaHelpers.getAllMbtaStations(this.getBaseContext());
		for (MbtaStation m : mbtaStationList) {
			//Log.d("MBTA Station", m.toString());
		}

		// TEST - Database Stuff

		List<Landmark> landmarkList = db.getAllLandmarks();
		List<Truck> truckList = db.getAllTrucks();
		List<Schedule> scheduleList = db.getAllSchedules();
		List<FoodItem> foodItemList = db.getAllFoodItems();
		List<Factlet> factletList = db.getAllFactlets();

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
		for (Factlet a: factletList) {
			Log.d("Factlet", a.toString());
		}
		//END TEST DATA


		// Home page grid view
		//GridView gridview = (GridView) findViewById(R.id.gridview);
		//gridview.setAdapter(new ImageAdapter(this));

		// Calculate the formatted day of week and time of day
		Time now = new Time();
		now.setToNow();

		final String dayOfWeek = getDayOfWeek(now);
		final String timeOfDay = getMealOfDay(now);

		//For debugging purposes - showing the life of the cache and today's day.
		SharedPreferences settings = getSharedPreferences(Constants.BAMFT_PREFS_NAME, 0);

		long cacheBirthday = settings.getLong(Constants.PREFS_CACHE_UPDATED, 0);
		String testingToastText = "It is currently " + dayOfWeek + " " + timeOfDay + ". Cache should be updated in : " + (cacheBirthday + Constants.CACHE_LIFE - now.toMillis(true))/1000 + " seconds.";
		Toast.makeText(BamftActivity.this, testingToastText, Toast.LENGTH_LONG).show();
	}


	/*
	// Action Bar Home Icon leads to Home
	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, BamftActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}
	 */
	public void menuClickFunction(final View v) {

		Time now = new Time();
		//now.setToNow();
		now.set(0, 0, 12, 24, 2, 2012);

		final String dayOfWeek = getDayOfWeek(now);
		final String timeOfDay = getMealOfDay(now);

		Bundle timeBundle = new Bundle();
		timeBundle.putString(Constants.DAY_OF_WEEK, dayOfWeek);
		timeBundle.putString(Constants.TIME_OF_DAY, timeOfDay); //default just in case...note that putString automatically overwrites existing values too
		//timeBundle.putString(Constants.DAY_OF_WEEK, "Tuesday");
		//timeBundle.putString(Constants.TIME_OF_DAY, "Evening");

		switch(v.getId()) {

		case R.id.menu_item_search_nearby:
			//Load Morning trucks
			//Toast.makeText(BamftActivity.this, "" + dayOfWeek + " Morning trucks", Toast.LENGTH_SHORT).show();
			//timeBundle.putString("timeOfDay", timeOfDay);
			if(!currentlyUpdating) {
				safeToUpdate = false;
				Toast.makeText(BamftActivity.this,  "Nearby open trucks (for " + dayOfWeek + " " + timeOfDay + ")", Toast.LENGTH_SHORT).show();

				Intent loadScheduleListIntent = new Intent(BamftActivity.this, ScheduleListActivity.class);
				loadScheduleListIntent.putExtras(timeBundle);
				BamftActivity.this.startActivity(loadScheduleListIntent);
			} else {
				Toast.makeText(BamftActivity.this, Constants.CURRENTLY_UPDATING_STRING, Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.menu_item_list_all:
			//Load Afternoon trucks

			if(!currentlyUpdating) {
				safeToUpdate = false;
				Toast.makeText(BamftActivity.this,  "All trucks (open and closed)", Toast.LENGTH_SHORT).show();

				Intent loadTruckListIntent = new Intent(BamftActivity.this, TruckListActivity.class);
				timeBundle.putString("timeOfDay", "Afternoon");
				loadTruckListIntent.putExtras(timeBundle);
				BamftActivity.this.startActivity(loadTruckListIntent);
			} else {
				Toast.makeText(BamftActivity.this, Constants.CURRENTLY_UPDATING_STRING, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.menu_item_map_view:
			//Load Evening trucks
			if(!currentlyUpdating) {
				safeToUpdate = false;
				Toast.makeText(BamftActivity.this,  "All open trucks", Toast.LENGTH_SHORT).show();

				Intent openTrucksMapIntent = new Intent(this, BamftMapActivity.class);
				openTrucksMapIntent.putExtras(timeBundle);
				openTrucksMapIntent.putExtra(Constants.MAP_TYPE, Constants.MAP_TYPE_TRUCKS);
				startActivity(openTrucksMapIntent);
			} else {
				Toast.makeText(BamftActivity.this, Constants.CURRENTLY_UPDATING_STRING, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.menu_item_surprise_me:

			if(!currentlyUpdating) {
				safeToUpdate = false;
				Intent randomTruckIntent = new Intent(this, RandomTruckActivity.class);
				randomTruckIntent.putExtra(Constants.DAY_OF_WEEK, dayOfWeek);
				randomTruckIntent.putExtra(Constants.TIME_OF_DAY, timeOfDay);
				startActivity(randomTruckIntent);

				// Force data cache

				/*SharedPreferences settings = getSharedPreferences(Constants.BAMFT_PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putLong(Constants.PREFS_CACHE_UPDATED, 0);
			editor.commit();
			//prepareData();

			BackgroundPrepareData task = new BackgroundPrepareData();
			task.execute();

			Toast.makeText(BamftActivity.this, "Updated Cache...", Toast.LENGTH_SHORT).show();*/
			} else {
				Toast.makeText(BamftActivity.this, Constants.CURRENTLY_UPDATING_STRING, Toast.LENGTH_SHORT).show();
			}
			break;
		}        		



	}



	public static String getDayOfWeek(Time now) {
		/*
		if (true) {
			return "Monday";
		}
		 */
		return Constants.DAYS_OF_WEEK[now.weekDay]; 
	}

	/**
	 * Gets the appropriate name of the meal based upon the hour specified.
	 * @param 
	 * @return "Morning", "Afternoon", "Evening"
	 */
	public static String getMealOfDay(Time now) {
		/*
		if (false) {
			return "Afternoon";
		}

		 */
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

		Log.d("BACKGROUNDER", "I am in preparedata");
		SharedPreferences settings = getSharedPreferences(Constants.BAMFT_PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();

		Time now = new Time();
		now.setToNow();

		long cacheBirthday = settings.getLong("cacheUpdated", 0);
		//if ((now.toMillis(true) - cacheBirthday) > Constants.CACHE_LIFE) {
			if (true) {
			// now minus the birthdays sis bigger than the expected age - so we should re-grab from cache




			// First, we pull get the data from the Landmark, Truck, and Schedule tables.
			String landmarksDumpData = readServerData(Constants.LANDMARKS_DUMP_URL);
			String trucksDumpData = readServerData(Constants.TRUCKS_DUMP_URL);
			String schedulesDumpData = readServerData(Constants.SCHEDULES_DUMP_URL);
			String foodItemsDumpData = readServerData(Constants.FOOD_ITEMS_DUMP_URL);
			String factletsDumpData = readServerData(Constants.FACTLETS_DUMP_URL);

			// Dump the database
			//db.recreateAllTables();


			// BEGIN FREEZE
			currentlyUpdating = true;
			boolean cacheSuccessFlag = false;
			if (safeToUpdate) {
				// Connect to DB
				DatabaseHandler db = new DatabaseHandler(this);
				cacheSuccessFlag = true;

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

				// FACTLETS
				if (factletsDumpData.length() > 0) {
					db.recreateTable("factlets");   		
					try {
						JSONArray factletsArray = new JSONArray(factletsDumpData);

						for (int i = 0; i < factletsArray.length(); i++) {
							//Iterate through each entry and save to DB
							JSONObject factletObject = factletsArray.getJSONObject(i).getJSONObject("factlet");

							// In case truckId is null, which is usually the case.
							int truckId;
							if (factletObject.isNull("truck_id")) {
								truckId = 0;
							} else {
								truckId = factletObject.getInt("truck_id");
							}
							Factlet factlet = new Factlet(

									factletObject.getInt("id"),
									factletObject.getString("title"),
									factletObject.getString("content"),
									truckId);

							db.addFactlet(factlet);

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					Log.d("PrepareData", "Factlets data updated");
				} else {
					cacheSuccessFlag = false;
				}
			} else {
				// got blocked..
				Log.d("PrepareData", "GOT BLOCKED.");
			}
			safeToUpdate = true;
			currentlyUpdating = false;
			// END FREEZE - reset


			// DONE - update prefs

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
			Log.d("BACKGROUNDER", "I am in doInBackground");
			prepareData();
			return null;
		}


	}

	private void checkInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(
				Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		boolean connected = 
				networkInfo != null &&
				networkInfo.isAvailable() &&
				networkInfo.isConnectedOrConnecting();

		if (!connected) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);  
			builder.setMessage(Constants.ENABLE_INTERNET)  
			.setCancelable(true)  
			.setPositiveButton(
					Constants.ENABLE_INTERNET_YES, 
					new DialogInterface.OnClickListener() {  
						public void onClick(DialogInterface dialog, int id) {  
							Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);  
							startActivityForResult(intent, 1); 
						}  
					})  
					.setNegativeButton(
							Constants.ENABLE_INTERNET_NO, 
							new DialogInterface.OnClickListener() {  
								public void onClick(DialogInterface dialog, int id) {  
									finishCreatingActivity();
								}  
							}).show();  
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		finishCreatingActivity();
	}

	public void onTriviaClick(View v) {
		if (factlets == null || factlets.size() < 1) 
			return;

		Random rand = new Random();
		int factletToGet = rand.nextInt(factlets.size());

		Factlet factlet = factlets.get(factletToGet);

		// Get one of many random responses

		int responseToGet = rand.nextInt(Constants.TRIVIA_RESPONSES.length);
		String response = Constants.TRIVIA_RESPONSES[responseToGet];

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();  
		alertDialog.setMessage(factlet.getContent());
		alertDialog.setButton(response, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	private void setupFactlets(DatabaseHandler db) {
		factlets = db.getAllFactlets();

		Log.d("factlets", "finished");
	}
}
