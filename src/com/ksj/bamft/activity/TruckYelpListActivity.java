package com.ksj.bamft.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import com.ksj.bamft.R;
import com.ksj.bamft.adapter.FoodItemAdapter;
import com.ksj.bamft.adapter.TweetItemAdapter;
import com.ksj.bamft.adapter.YelpReviewItemAdapter;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.FoodItem;
import com.ksj.bamft.model.Truck;
import com.ksj.bamft.model.Tweet;
import com.ksj.bamft.model.YelpReview;
import com.ksj.bamft.yelp.Yelp;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class TruckYelpListActivity extends ListActivity {

	private final String NO_YELP_HANDLE = "This truck does not have a Yelp handle.";
	private final String NO_REVIEWS = "No reviews to display.";

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// Extract info from bundle
		
		Bundle extras = this.getIntent().getExtras();
		String yelpHandle = (String) extras.get(Constants.YELP_HANDLE);
		
		//TODO: Change from android.R.layout.simple_list_item_1
		YelpReviewItemAdapter adapter = new YelpReviewItemAdapter(this, R.layout.yelp_review_item, getYelpReviewItems(yelpHandle));
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
	
	public ArrayList<YelpReview> getYelpReviewItems(String yelpHandle) {
		
		ArrayList<YelpReview> list = new ArrayList<YelpReview>();
		
		if(yelpHandle.length() < 1) {
			//list.add(new Tweet("", NO_YELP_HANDLE));
			return list;
		}
		
		//String readYelpFeed = readYelpFeed(yelpHandle);
		Yelp yelp = new Yelp();
		String yelpFeed = yelp.getBusinessInfo(yelpHandle);
		try {
			
			JSONObject yelpFeedObject = new JSONObject(yelpFeed);
			JSONArray reviewsArray = yelpFeedObject.getJSONArray("reviews");
			
			
			for (int i = 0; i < reviewsArray.length(); i++) {
				JSONObject reviewObject = reviewsArray.getJSONObject(i);
				JSONObject userObject = reviewObject.getJSONObject("user");
				YelpReview yelpReview = new YelpReview(
						reviewObject.getString("id"), reviewObject.getInt("rating"),
						reviewObject.getString("excerpt"), reviewObject.getString("rating_image_url"), 
						userObject.getString("id"), userObject.getString("name"), userObject.getString("image_url"));
				
				
				list.add(yelpReview);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return list;
		
	}


}