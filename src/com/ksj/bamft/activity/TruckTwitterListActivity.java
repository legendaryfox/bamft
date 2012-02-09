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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ksj.bamft.R;
import com.ksj.bamft.adapter.TweetItemAdapter;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.model.Tweet;

public class TruckTwitterListActivity extends ListActivity {
	
	private final String NO_TWITTER_HANDLE = "This truck does not have a Twitter handle.";
	private final String NO_TWEETS = "No tweets to display.";

	public void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		
		// Extract info from bundle
		
		Bundle extras = this.getIntent().getExtras();
		String twitterHandle = (String) extras.get(Constants.TWITTER_HANDLE);
		
		//TODO: Change from android.R.layout.simple_list_item_1
		TweetItemAdapter adapter = new TweetItemAdapter(this, R.layout.tweet_item, getFeedItems(twitterHandle));
		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		/*
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
		*/
		
	}
	
	public ArrayList<Tweet> getFeedItems(String twitterHandle) {
		
		ArrayList<Tweet> list = new ArrayList<Tweet>();
		
		if(twitterHandle.length() < 1) {
			list.add(new Tweet("", NO_TWITTER_HANDLE));
			return list;
		}
		
		String readTwitterFeed = readTwitterFeed(twitterHandle);
		try {
			JSONArray jsonArray = new JSONArray(readTwitterFeed);
			
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String time = jsonObject.getString("created_at"); //TODO: format this to look pretty
				String content = jsonObject.getString("text");

				list.add(new Tweet(time, content) );
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (list.size() == 0) {
			list.add(new Tweet("", NO_TWEETS));
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
				Log.e(TruckTwitterListActivity.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			Log.i(TruckTwitterListActivity.class.toString(), "ClientProtocl");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i(TruckTwitterListActivity.class.toString(), "IOException");
			e.printStackTrace();
		}
		return builder.toString();
	}

}
