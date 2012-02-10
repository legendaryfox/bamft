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
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class TruckYelpListActivity extends ListActivity {

	private final String NO_YELP_HANDLE = "This truck does not have a Yelp handle.";
	private final String NO_REVIEWS = "No reviews to display.";

	private static String downloadYelpFeed;
	private static String yelpHandle;
	private static ProgressDialog dialog;
	
	
	private static ArrayList<YelpReview> yelpReviewItems;
	private static Handler handler;
	private static Thread downloadYelpThread;
	private YelpRunnable myYelpRunnable = new YelpRunnable();

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Extract info from bundle

		Bundle extras = this.getIntent().getExtras();
		yelpHandle = (String) extras.get(Constants.YELP_HANDLE);
		
		
		
		//BEGIN THREAD
		// Create a handler to update the UI
		handler = new Handler();
		//myYelpRunnable.setListView(R.id.yelpReviewList);
		myYelpRunnable.setListView(getListView());
		Log.d("THREAD", "lv is currently set to " + myYelpRunnable.getListView() + " but it should be " + getListView());
		/*
		// get the latest imageView after restart of the application
		imageView = (ImageView) findViewById(R.id.imageView1);
		// Did we already download the image?
		if (downloadBitmap != null) {
			imageView.setImageBitmap(downloadBitmap);
		}
		*/
		// Check if the thread is already running
		downloadYelpThread = (Thread) getLastNonConfigurationInstance();
		if (downloadYelpThread != null && downloadYelpThread.isAlive()) {
			dialog = ProgressDialog.show(this, "Yelp", "Accessing Yelp reviews....");
		}
		
		downloadYelpItems();
		/*
		YelpReviewItemAdapter adapter = new YelpReviewItemAdapter(this, R.layout.yelp_review_item, getYelpReviewItems(yelpHandle));
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		*/
		/*
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
		 */

	}
	
	public void downloadYelpItems() {
		dialog = ProgressDialog.show(this, "Yelp", "Accessing Yelp reviews....");
		downloadYelpThread = new DownloadYelpThread(myYelpRunnable);
		Log.d("THREAD", "my yelp runnable lv is now " + myYelpRunnable.listView);
		downloadYelpThread.start();
	}
	

	// Save the thread
	@Override
	public Object onRetainNonConfigurationInstance() {
		return downloadYelpThread;
	}


	static public class DownloadYelpThread extends Thread {
		
		private final YelpRunnable yelpRunnable;
		
		
		public DownloadYelpThread(YelpRunnable yelpRunnable) {
			this.yelpRunnable = yelpRunnable;
			//this.start();
		}
		
		@Override
		public void run() {
			Yelp yelp = new Yelp();
			/*
			try {
				//new Thread().sleep(5000);
				Log.d("THREAD", "I am ACTUALLYW AITING");
			} catch (InterruptedException e) {
				Log.d("THREAD", "I am waiting EXCEPTION");
				e.printStackTrace();
			}
			*/
			Log.d("THREAD", "I am done waiting.");
			//downloadYelpFeed = yelp.getBusinessInfo(yelpHandle);
			yelpReviewItems = getYelpReviewItems(yelpHandle);
			handler.post(yelpRunnable);
			Log.d("THREAD", "I should've posted runnable to handler by now.");

		}
	}

	private class YelpRunnable implements Runnable {
		
		public ListView listView;
		//public ArrayList<YelpReview> yelpReviewItems;
		
		public void setListView(ListView listView) {
			//this.listView = (ListView) TruckYelpListActivity.findViewById(id);
			this.listView = listView;
		}
		
		
		public ListView getListView() {
			return this.listView;
		}
		
		public void run() {
			//yelpReviewItems = getYelpReviewItems
			Log.d("THREAD", "I am in runnable. About to define adapter");
			YelpReviewItemAdapter adapter = new YelpReviewItemAdapter(TruckYelpListActivity.this.getBaseContext(), R.layout.yelp_review_item, yelpReviewItems);
			Log.d("THREAD", "Adapter defined. About to set. Adapter is " + adapter.toString() + "and list view is " + listView + " and yelpReview is " + yelpReviewItems);
			
			listView.setAdapter(adapter);
			Log.d("THREAD", "Adapter set.");
			//setListAdapter(adapter);

			//ListView listView = getListView();
			//lv.setTextFilterEnabled(true);
			dialog.dismiss();
		}
	}







	static public ArrayList<YelpReview> getYelpReviewItems(String yelpHandle) {

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