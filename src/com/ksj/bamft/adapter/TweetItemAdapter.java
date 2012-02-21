package com.ksj.bamft.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ksj.bamft.R;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Tweet;

public class TweetItemAdapter extends ArrayAdapter<Tweet> {
	
	private final Context context;
	private final List<Tweet> tweetList;
	
	public TweetItemAdapter(Context context, int textViewResourceId, List<Tweet> tweetList) {
		super(context, textViewResourceId, tweetList);
		this.context = context;
		this.tweetList = tweetList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.ab_tweet_item, parent, false);		
		}
		
		Tweet tweet = tweetList.get(position);
		
		
		if (tweet != null) {
			
			TextView tweetDateTextView = (TextView) rowView.findViewById(R.id.tweetDate);
			TextView tweetContentTextView = (TextView) rowView.findViewById(R.id.tweetContent);
			
			Log.d("TWEET", "Tweet was: " + tweet.getContent());
	
			if (tweetDateTextView != null) {
				tweetDateTextView.setText(tweet.getDate());
			}
			
			if (tweetContentTextView != null) {
				tweetContentTextView.setText(tweet.getContent());
			}
		
		}
		
		return rowView;
	}
}
