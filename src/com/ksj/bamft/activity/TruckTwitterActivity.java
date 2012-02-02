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

import com.ksj.bamft.constants.Constants;

public class TruckTwitterActivity extends ListActivity {
	

	public void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		
		// Extract info from bundle
		
		Bundle extras = this.getIntent().getExtras();
		String twitterHandle = (String) extras.get(Constants.TWITTER);
		
		//TODO: Change from android.R.layout.simple_list_item_1
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getFeedItems(twitterHandle)));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	public ArrayList<String> getFeedItems(String twitterHandle) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		if(twitterHandle.isEmpty()) {
			list.add("This truck does not have a Twitter handle.");
			return list;
		}
		
		String readTwitterFeed = readTwitterFeed(twitterHandle);
		
		
		try {
			JSONArray jsonArray = new JSONArray(readTwitterFeed);
			//JSONArray jsonArray = new JSONObject(readTwitterFeed).getJSONArray("features");
			
			
		
			
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				//Log.i(TruckTwitterActivity.class.getName(), jsonObject.getString("text"));
				//Log.i(ParseJSON.class.getName(), jsonObject.getJSONObject("attributes").getString("TestFld"));
				list.add(jsonObject.getString("text"));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (list.size() == 0) {
			list.add("No feed items to display");
		}		
		return list;
		
	}

	public String readTwitterFeed(String twitterHandle) {
		
		String twitterUrl = "http://twitter.com/statuses/user_timeline/" + twitterHandle + ".json";
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(twitterUrl);
		
		
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
			Log.i(TruckTwitterActivity.class.toString(), "ClientProtocl");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i(TruckTwitterActivity.class.toString(), "IOException");
			e.printStackTrace();
		}
		return builder.toString();
	}

}
