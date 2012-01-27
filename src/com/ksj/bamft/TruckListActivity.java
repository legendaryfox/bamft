package com.ksj.bamft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TruckListActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        //dynamically load time of day based on previous Activity.
        String timeOfDay = "Afternoon"; //set this as default for safety
        Bundle timeOfDayBundle = this.getIntent().getExtras();
        timeOfDay = timeOfDayBundle.getString("timeOfDay");
        
        
        
        
        
        //First, we get the food truck data from the API
        //final List<String> truckNameList = readTruckListData(timeOfDay, "name");
        final DatabaseHandler db = new DatabaseHandler(this);
        final List<Schedule> scheduleList = db.getSchedulesByDayAndTime("Thursday", timeOfDay);
        
        //this part is for displaying it in the ListView
        //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, truckNameList));
        setListAdapter(new ArrayAdapter<Schedule>(this, R.layout.list_item, scheduleList));
        
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        //ListView "toast" functionality
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        		int truck_id = scheduleList.get(position).truck_id;
        		Truck truck = db.getTruck(truck_id);
        		//Toast.makeText(getApplicationContext(), truckLocationList.get(position), Toast.LENGTH_SHORT).show(); //show the location
        		Toast.makeText(getApplicationContext(), truck.getName(), Toast.LENGTH_SHORT).show();
        	}
        });
        
        
    }
    
    public List<String> readTruckListData(String timeOfDay, String infoField)
    {
    	/* Options for infoField (string):
    	 * GPS
    	 * Location
    	 * XCoord
    	 * YCoord
    	 * TimeOfDay
    	 * Name
    	 * URL
    	 */
    	
    	if ("GPS".equals(infoField)) {  		
    		return readRawTruckListData(timeOfDay, "GPS");
    		
    	} else if ("Location".equals(infoField)) {  		
    		return readRawTruckListData(timeOfDay, "Location");
    		
    	} else if ("XCoord".equals(infoField)) {
    		return readRawTruckListData(timeOfDay, "XCoord");
    		
    	} else if ("YCoord".equals(infoField)) {    		
    		return readRawTruckListData(timeOfDay, "YCoord");
    		
    	} else if ("TimeOfDay".equals(infoField)) {    		
    		return readRawTruckListData(timeOfDay, "TimeOfDay");
    		
    	/*} else if ("Name".equals(infoField)) {  		
    		List<String> nameList = readRawTruckListData(timeOfDay, "TestFld");
    		ListIterator<String> litr = nameList.listIterator();
    		while(litr.hasNext()) {
    			
    			Object rawTruckName = litr.next();
    			litr.set(sanitizeTruckName((String) rawTruckName));
    		}
    		
    		return nameList;
    	*/
    	} else if ("name".equals(infoField)) {
    		return readRawTruckListData(timeOfDay, "name");
    	
    	} else if ("URL".equals(infoField)) {   		
    		List<String> urlList = readRawTruckListData(timeOfDay, "TestFld");
    		ListIterator<String> litr = urlList.listIterator();
    		while(litr.hasNext()) {
    			
    			Object rawURL = litr.next();
    			litr.set(sanitizeTruckURL((String) rawURL));
    		}
    		
    		return urlList;
    		
    		
    	} else {  		
    		//return garbage?
    		return readRawTruckListData(timeOfDay, infoField);
    		
    	}	
    		
    }
    	
    private List<String> readRawTruckListData(String timeOfDay, String rawField)
    {
    	//for timeOfDay, 0 = morning, 1 = afternoon, 2 = evening
    	
    	//Initialize some variables
        List<String> truckList = new ArrayList<String>();       
        String readGPSData = readAPIData(timeOfDay);

        try {
        	/*
        	 * First we have to dig into the JSON structure - see http://json.bloople.net/ to convert
        	 * http://hubmaps2.cityofboston.gov/ArcGIS/rest/services/Dev_services/food_trucks
        	 /MapServer/1/query?text=%25&outFields=GPS%2CLocation%2CXCoord%2CYCoord%2CDayOfWeek%2CTimeOfDay%2CTestFld%2CShape&f=pjson 
        	 */
        	
        	//JSONArray jsonArray = new JSONObject(readGPSData).getJSONArray("features"); //"features" stores actual food truck entries
        	
        	JSONArray jsonArray = new JSONArray(readGPSData);
        	
        	Log.i(BamftActivity.class.getName(), "Number of entries " + jsonArray.length()); //add a log entry for # of entries
        	
        	for (int i = 0; i < jsonArray.length(); i++) {
        		//Iterate through each entry
        		JSONObject jsonObject = jsonArray.getJSONObject(i);
        		//JSONObject jsonObject = jsonArray.getJSONObject(i).get
        		
        		//This line prints it all out.
        		//String rawInfo = jsonObject.getJSONObject("attributes").getString(rawField); 	
        		String rawInfo = jsonObject.getJSONObject("truck").getString(rawField);
        		Log.i(BamftActivity.class.getName(), "Data: " + rawInfo); //add to log     		
        		truckList.add(rawInfo); //add object to our truckList
    
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return truckList;
    	
    }
    	
    
    
    private List<String> old_readRawTruckListData(String timeOfDay, String rawField)
    {
    	//for timeOfDay, 0 = morning, 1 = afternoon, 2 = evening
    	
    	//Initialize some variables
        List<String> truckList = new ArrayList<String>();       
        String readGPSData = readAPIData(timeOfDay);

        try {
        	/*
        	 * First we have to dig into the JSON structure - see http://json.bloople.net/ to convert
        	 * http://hubmaps2.cityofboston.gov/ArcGIS/rest/services/Dev_services/food_trucks
        	 /MapServer/1/query?text=%25&outFields=GPS%2CLocation%2CXCoord%2CYCoord%2CDayOfWeek%2CTimeOfDay%2CTestFld%2CShape&f=pjson 
        	 */
        	
        	JSONArray jsonArray = new JSONObject(readGPSData).getJSONArray("features"); //"features" stores actual food truck entries
        	
        	Log.i(BamftActivity.class.getName(), "Number of entries " + jsonArray.length()); //add a log entry for # of entries
        	
        	for (int i = 0; i < jsonArray.length(); i++) {
        		//Iterate through each entry
        		JSONObject jsonObject = jsonArray.getJSONObject(i);
        		
        		//This line prints it all out.
        		String rawInfo = jsonObject.getJSONObject("attributes").getString(rawField); 		      		
        		Log.i(BamftActivity.class.getName(), "Data: " + rawInfo); //add to log     		
        		truckList.add(rawInfo); //add object to our truckList
    
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return truckList;
    	
    }
    
    public String sanitizeTruckName(String unsanitizedTruckName)
    {
    	int brIndex = unsanitizedTruckName.indexOf("<br/>");
    	return unsanitizedTruckName.substring(0, brIndex);
    }
    
    public String sanitizeTruckURL(String unsanitizedURL)
    {
    	int openIndex = unsanitizedURL.indexOf("href='");
    	int closeIndex = unsanitizedURL.indexOf("' target");
    	return unsanitizedURL.substring(openIndex + 6, closeIndex);
    }

    
    public String readAPIData(String timeOfDay) {
    	StringBuilder builder = new StringBuilder();
    	HttpClient client = new DefaultHttpClient();
    	
		//HttpGet httpGet = new HttpGet("http://hubmaps2.cityofboston.gov/ArcGIS/rest/services/Dev_services/food_trucks/MapServer/" + timeOfDay + "/query?text=%25&outFields=GPS%2CLocation%2CXCoord%2CYCoord%2CDayOfWeek%2CTimeOfDay%2CTestFld%2CShape&f=pjson");
		
    	HttpGet httpGet = new HttpGet("http://bamftserver.heroku.com/trucks/" + timeOfDay);
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
