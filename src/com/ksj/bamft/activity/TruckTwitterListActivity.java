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
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.ksj.bamft.R;
import com.ksj.bamft.actionbarhelpers.ActionBarTitleHelper;
import com.ksj.bamft.actionbarhelpers.ProfileTabsHelper;
import com.ksj.bamft.adapter.TweetItemAdapter;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.model.Truck;
import com.ksj.bamft.model.Tweet;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class TruckTwitterListActivity extends ListActivity {

	private final static String NO_TWITTER_HANDLE = "This truck does not have a Twitter handle.";
	private final static String NO_TWEETS = "No tweets to display.";

	private final String DIALOG_TWITTER_TITLE = "Twitter";
	private final String DIALOG_ACCESSING_TWITTER = "Accessing Tweets...";

	private static String twitterHandle;
	private static ArrayList<Tweet> tweetItems;

	private static ProgressDialog dialog;
	private static Handler handler;
	private static Thread downloadTwitterThread;
	private TwitterRunnable twitterRunnable = new TwitterRunnable();

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Extract info from bundle

		Bundle extras = this.getIntent().getExtras();
		twitterHandle = (String) extras.get(Constants.TWITTER_HANDLE);
		Truck truck = (Truck) extras.get(Constants.TRUCK);

		handler = new Handler();
		setContentView(R.layout.ab_twitter_list);

		
		//extras.getString(Constants.REFERRER)
		ActionBarTitleHelper.setTitleBar(this);
		ProfileTabsHelper.setupProfileTabs(this, truck, "twitter");

		twitterRunnable.setListView(getListView());

		TextView twitterHandleTextView = (TextView) findViewById(R.id.twitterHandle);
		if(twitterHandleTextView != null) {
			twitterHandleTextView.setText(twitterHandle);
		}


		if (tweetItems != null) {
			//setContentView(R.layout.ab_twitter_list);

			TweetItemAdapter adapter = new TweetItemAdapter(this, R.layout.ab_tweet_item, tweetItems);
			ListView lv = getListView();
			lv.setAdapter(adapter);
			lv.setDivider(null);




			/*
			// Action Bar Left Icon
			final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
			actionBar.setHomeAction(new IntentAction(this, BamftActivity.createIntent(this), R.drawable.icon));
			actionBar.setTitle("BAMFT!");
			 */
			/*
			TextView twitterHandleTextView = (TextView) getListView().findViewById(R.id.twitterHandle);
			twitterHandleTextView.setText(twitterHandle);
			 */

		}


		// Check if the thread is already running
		downloadTwitterThread = (Thread) getLastNonConfigurationInstance();
		if (downloadTwitterThread != null && downloadTwitterThread.isAlive()) {
			dialog = ProgressDialog.show(this, DIALOG_TWITTER_TITLE, DIALOG_ACCESSING_TWITTER);
		}

		downloadTwitterItems();



	}

	public void downloadTwitterItems() {
		// Begin the long process, start the dialog box
		dialog = ProgressDialog.show(this, DIALOG_TWITTER_TITLE, DIALOG_ACCESSING_TWITTER);

		downloadTwitterThread = new DownloadTwitterThread(twitterRunnable);
		downloadTwitterThread.start();

	}

	static private class DownloadTwitterThread extends Thread {

		private final TwitterRunnable myTwitterRunnable;

		/*
		 * VOODOO: We need to do this because of the way Adapters work within the whole Static/Nonstatic framework.
		 * This is why we need to repass the above "normal" twitterRunnable into this *final* twitterRunnable.
		 */
		public DownloadTwitterThread(TwitterRunnable twitterRunnable) {
			this.myTwitterRunnable = twitterRunnable;
		}

		@Override
		public void run() {
			tweetItems = getFeedItems(twitterHandle); // <-- this is the longest part.
			handler.post(myTwitterRunnable); // <-- once we finish the above long part, we can run the runnable part.

		}
	}

	private class TwitterRunnable implements Runnable {

		private ListView listView;

		public void setListView(ListView listView) {
			this.listView = listView;
		}

		public void run() {
			//setContentView(R.layout.ab_twitter_list);

			TweetItemAdapter adapter = new TweetItemAdapter(TruckTwitterListActivity.this.getBaseContext(), R.layout.ab_tweet_item, tweetItems);
			listView.setAdapter(adapter);
			listView.setDivider(null);




			/*
			// Action Bar Left Icon
			final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
			actionBar.setHomeAction(new IntentAction(this, BamftActivity.createIntent(this), R.drawable.icon));
			actionBar.setTitle("BAMFT!");
			 */
			/*
			TextView twitterHandleTextView = (TextView) getListView().findViewById(R.id.twitterHandle);
			twitterHandleTextView.setText(twitterHandle);
			 */

			// Finished loading, dismiss the dialog
			dialog.dismiss();
		}
	}




	static public ArrayList<Tweet> getFeedItems(String twitterHandle) {

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

	static public String readTwitterFeed(String twitterHandle) {

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
