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
	
	private final String DIALOG_YELP_TITLE = "Yelp";
	private final String DIALOG_ACCESSING_YELP = "Accessing Yelp reviews...";

	private static String yelpHandle;
	private static ArrayList<YelpReview> yelpReviewItems;
	
	private static ProgressDialog dialog;
	private static Handler handler;
	private static Thread downloadYelpThread;
	private YelpRunnable yelpRunnable = new YelpRunnable();

	/*
	 * NOTE: There's a lot of weird voodoo here because of the way we need to pass adapters, but how they don't work within a static class.
	 */

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Extract info from bundles

		Bundle extras = this.getIntent().getExtras();
		yelpHandle = (String) extras.get(Constants.YELP_HANDLE);

		handler = new Handler();
		yelpRunnable.setListView(getListView());

		// Check to see if we already have the information
		if (yelpReviewItems != null) {
			YelpReviewItemAdapter adapter = new YelpReviewItemAdapter(TruckYelpListActivity.this.getBaseContext(), R.layout.yelp_review_item, yelpReviewItems);
			ListView lv = getListView();
			lv.setAdapter(adapter);
		}

		// Check if the thread is already running
		downloadYelpThread = (Thread) getLastNonConfigurationInstance();
		if (downloadYelpThread != null && downloadYelpThread.isAlive()) {
			dialog = ProgressDialog.show(this, DIALOG_YELP_TITLE, DIALOG_ACCESSING_YELP);
		}

		downloadYelpItems();

	}

	public void downloadYelpItems() {
		// Begin the long process, start the dialog box
		dialog = ProgressDialog.show(this, DIALOG_YELP_TITLE, DIALOG_ACCESSING_YELP);
		
		downloadYelpThread = new DownloadYelpThread(yelpRunnable);
		downloadYelpThread.start();

	}




	static private class DownloadYelpThread extends Thread {

		private final YelpRunnable myYelpRunnable;

		/*
		 * VOODOO: We need to do this because of the way Adapters work within the whole Static/Nonstatic framework.
		 * This is why we need to repass the above "normal" yelpRunnable into this *final* yelpRunnable.
		 */
		public DownloadYelpThread(YelpRunnable yelpRunnable) {
			this.myYelpRunnable = yelpRunnable;
		}

		@Override
		public void run() {
			yelpReviewItems = getYelpReviewItems(yelpHandle); // <-- this is the longest part.
			handler.post(myYelpRunnable); // <-- once we finish the above long part, we can run the runnable part.

		}
	}

	private class YelpRunnable implements Runnable {

		private ListView listView;

		public void setListView(ListView listView) {
			this.listView = listView;
		}

		public void run() {
			YelpReviewItemAdapter adapter = new YelpReviewItemAdapter(TruckYelpListActivity.this.getBaseContext(), R.layout.yelp_review_item, yelpReviewItems);
			listView.setAdapter(adapter);
			
			// Finished loading, dismiss the dialog
			dialog.dismiss();
		}
	}

	static public ArrayList<YelpReview> getYelpReviewItems(String yelpHandle) {

		ArrayList<YelpReview> list = new ArrayList<YelpReview>();

		if(yelpHandle.length() < 1) {
			return list;
		}

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
			//TODO: print something to show no yelp entries exist.
			e.printStackTrace();
		}


		return list;

	}
	// Save the thread
	@Override
	public Object onRetainNonConfigurationInstance() {
		return downloadYelpThread;
	}


}