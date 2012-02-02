package com.ksj.bamft.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TruckTwitterActivity extends ListActivity {
	

	public void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getFeedItems()));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	public ArrayList<String> getFeedItems() {
		
		ArrayList<String> list = new ArrayList<String>();
		
		String readTwitterFeed = readTwitterFeed();
		try {
			JSONArray jsonArray = new JSONArray(readTwitterFeed);
			//JSONArray jsonArray = new JSONObject(readTwitterFeed).getJSONArray("features");
			
			
			
			Log.i(TruckTwitterActivity.class.getName(),
					"Number of entries " + jsonArray.length());
			
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Log.i(TruckTwitterActivity.class.getName(), jsonObject.getString("text"));
				//Log.i(ParseJSON.class.getName(), jsonObject.getJSONObject("attributes").getString("TestFld"));
				list.add(jsonObject.getString("text"));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}

	public String readTwitterFeed() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://twitter.com/statuses/user_timeline/vogella.json");
		//HttpGet httpGet = new HttpGet("http://hubmaps2.cityofboston.gov/ArcGIS/rest/services/Dev_services/food_trucks/MapServer/1/query?text=%25&outFields=GPS%2CLocation%2CXCoord%2CYCoord%2CDayOfWeek%2CTimeOfDay%2CTestFld%2CShape&f=pjson");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(TruckTwitterActivity.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			Log.i("LOLZER", "ClientProtocl");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("LOLZER", "IOException");
			e.printStackTrace();
		}
		return builder.toString();
	}

}
