package com.ksj.bamft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;




public class BamftActivity extends Activity {
    /** Called when the activity is first created. */
	
	private static final String LANDMARKS_DUMP_URL = "http://bamftserver.heroku.com/landmarks/full_dump";
	private static final String TRUCKS_DUMP_URL = "http://bamftserver.heroku.com/trucks/full_dump";
	private static final String SCHEDULES_DUMP_URL = "http://bamftserver.heroku.com/schedules/full_dump";
	
	public static final String BAMFT_PREFS_NAME = "BamftPrefsFile";
	public static final String PREFS_CACHE_UPDATED = "cacheUpdated"; // preference for last update of cache. type is Long.
	private static long CACHE_LIFE = 5 * 60 * 1000; // how long the cached SQLite life should be (in millis).

	
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        // Prepares the internal SQLite database, if need be (dictated by CACHE_LIFE).
        prepareData();
        
        /* Testing area 
        final DatabaseHandler db = new DatabaseHandler(this);
        List<Schedule> scheduleList = db.getAllSchedules();
                
        for (Schedule s : scheduleList) {
        	Log.d("Schedule: ", "ID: " + s.getId() + "Truck: " + s.getTruckId() + "Landmark: " + s.getLandmarkId());
        }
        */
        
        SharedPreferences settings = getSharedPreferences(BAMFT_PREFS_NAME, 0);
        
        
        setContentView(R.layout.main);
        
        
        // Home page grid view
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        
        
        //For debugging purposes - showing the life of the cache.
        Time now = new Time();
    	now.setToNow();
    	long cacheBirthday = settings.getLong(PREFS_CACHE_UPDATED, 0);
    	
        Toast.makeText(BamftActivity.this, "Cache should be updated in : " + (cacheBirthday + CACHE_LIFE - now.toMillis(true))/1000 + " seconds.", Toast.LENGTH_SHORT).show();

        
        gridview.setOnItemClickListener(new OnItemClickListener() {
        	//listen for what button is getting pushed...
        	
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        		Bundle timeOfDayBundle = new Bundle();
        		timeOfDayBundle.putString("timeOfDay", "Afternoon"); //default just in case...note that putString automatically overwrites existing values too
        		
        		Intent loadTruckListIntent = new Intent(BamftActivity.this, TruckListActivity.class);
        		
        		switch(position) {
        		case 0:
        			//Load Morning trucks
        			Toast.makeText(BamftActivity.this, "Morning Trucks", Toast.LENGTH_SHORT).show();
        			timeOfDayBundle.putString("timeOfDay", "Morning");
        			loadTruckListIntent.putExtras(timeOfDayBundle);
            		BamftActivity.this.startActivity(loadTruckListIntent);
        			break;
        		case 1:
        			//Load Afternoon trucks
        			Toast.makeText(BamftActivity.this, "Afternoon Trucks", Toast.LENGTH_SHORT).show();
        			timeOfDayBundle.putString("timeOfDay", "Afternoon");
        			loadTruckListIntent.putExtras(timeOfDayBundle);
            		BamftActivity.this.startActivity(loadTruckListIntent);
        			break;
        		case 2:
        			//Load Evening trucks
        			Toast.makeText(BamftActivity.this, "Evening Trucks", Toast.LENGTH_SHORT).show();
        			timeOfDayBundle.putString("timeOfDay", "Evening");
        			loadTruckListIntent.putExtras(timeOfDayBundle);
            		BamftActivity.this.startActivity(loadTruckListIntent);
        			break;
        		case 3:
        			// Force data cache
        			
        			SharedPreferences settings = getSharedPreferences(BAMFT_PREFS_NAME, 0);
        	    	SharedPreferences.Editor editor = settings.edit();
        			editor.putLong(PREFS_CACHE_UPDATED, 0);
        	    	editor.commit();
        			prepareData();
        			
        			Toast.makeText(BamftActivity.this, "Updated Cache...", Toast.LENGTH_SHORT).show();

        			break;
        		}        				
        		
        		/* Moved to individual cases.
        		loadTruckListIntent.putExtras(timeOfDayBundle);
        		BamftActivity.this.startActivity(loadTruckListIntent);
        		*/
        	}
        	
        });
        
        
        
    }
    
    
    /**
     * Prepares the data by querying the bamftserver and updating internal SQL database
     * 
     */
    public void prepareData() {
    	
    	SharedPreferences settings = getSharedPreferences(BAMFT_PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	
    	Time now = new Time();
    	now.setToNow();
    	
    	long cacheBirthday = settings.getLong("cacheUpdated", 0);
    	if ((now.toMillis(true) - cacheBirthday) > CACHE_LIFE) {
    		// now minus the birthday is bigger than the expected age - so we should re-grab from cache
    		
    	
	    	
	    	DatabaseHandler db = new DatabaseHandler(this);
	    	
	    	// First, we pull get the data from the Landmark, Truck, and Schedule tables.
	    	String landmarksDumpData = readServerData(LANDMARKS_DUMP_URL);
	    	String trucksDumpData = readServerData(TRUCKS_DUMP_URL);
	    	String schedulesDumpData = readServerData(SCHEDULES_DUMP_URL);
	    	
	    	// Dump the database
	    	db.recreateTables();
	    	
	    	//Then, we open the JSON
	    	
	    	
	    	//Landmarks
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
	    	
	    	//Trucks
	    	try {
	    		JSONArray trucksArray = new JSONArray(trucksDumpData);
	    		
	    		for (int i = 0; i < trucksArray.length(); i++) {
	    			//Iterate through each entry and save to DB
	    			JSONObject truckObject = trucksArray.getJSONObject(i).getJSONObject("truck");
	    			Truck truck = new Truck(
	    					truckObject.getInt("id"), 
	    					truckObject.getString("name"), 
	    					truckObject.getString("cuisine"), 
	    					truckObject.getString("description"));
	    			
	    			//just for now
	    			truck.setCuisine("Cuiseinetest " + truck.getId());
	    			truck.setDescription("Descriptiontest " + truck.getId());
	    			
	    			db.addTruck(truck);
	    			
	    		}
	    		
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	
	    	//Schedules
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
	    	
	    	
	    	editor.putLong(PREFS_CACHE_UPDATED, now.toMillis(true));
	    	editor.commit();
	    	Toast.makeText(BamftActivity.this, "Cache was updated at " + now.toMillis(true), Toast.LENGTH_SHORT);
	    	Log.d("CACHE", "Cache was updated at " + now.toMillis(true));
	    	
    	} else {
    		
    		Log.d("CACHE", "Cache was not updated.");
    		
    	}
    }
    
    
    /**
     * Reads the server data from specified URL. Essentially a web page http-get function. 
     * @param dumpUrl
     * @return data from the queried URL
     */
    public String readServerData(String dumpUrl) {
    	StringBuilder builder = new StringBuilder();
    	HttpClient client = new DefaultHttpClient();
    	
		
    	HttpGet httpGet = new HttpGet(dumpUrl);
    	try {
			HttpResponse response = client.execute(httpGet); //run the Get
			
			//get the status
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { //HTTP 200 = "OKAY"
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					//basic "while not EOF, keep reading"
					builder.append(line);
				}
			} else {
				Log.e(BamftActivity.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
    }
    
   
        
}
