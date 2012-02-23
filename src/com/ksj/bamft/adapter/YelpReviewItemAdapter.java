package com.ksj.bamft.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksj.bamft.R;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Tweet;
import com.ksj.bamft.model.YelpReview;

public class YelpReviewItemAdapter extends ArrayAdapter<YelpReview> {
	
	private final Context context;
	private final List<YelpReview> yelpReviewList;
	
	public YelpReviewItemAdapter(Context context, int textViewResourceId, List<YelpReview> yelpReviewList) {
		super(context, textViewResourceId, yelpReviewList);
		this.context = context;
		this.yelpReviewList = yelpReviewList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.ab_yelp_review_item, parent, false);		
		}
		
		YelpReview yelpReview = yelpReviewList.get(position);
		
		if (yelpReview != null) {
			
			
			ImageView yelpUserImageView = (ImageView) rowView.findViewById(R.id.yelpUserImage);
			ImageView yelpRatingImageView = (ImageView) rowView.findViewById(R.id.yelpRatingImage);
			TextView yelpReviewExcerptView = (TextView) rowView.findViewById(R.id.yelpReviewExcerpt);
			TextView yelpUserNameView = (TextView) rowView.findViewById(R.id.yelpUserName);
			
			try {
				if (yelpUserImageView != null) {
					Bitmap yelpUserImageBitmap = BitmapFactory.decodeStream((InputStream)new URL(yelpReview.getUserImageUrl()).getContent());
					yelpUserImageView.setImageBitmap(yelpUserImageBitmap);
				}
				
				if (yelpRatingImageView != null) {
					Bitmap yelpRatingImageBitmap = BitmapFactory.decodeStream((InputStream)new URL(yelpReview.getRatingImageUrl()).getContent());
					yelpRatingImageView.setImageBitmap(yelpRatingImageBitmap);
				}
				
			} catch (MalformedURLException e) {
				  e.printStackTrace();
			} catch (IOException e) {
				  e.printStackTrace();
			}
			
			if (yelpReviewExcerptView != null) {
				yelpReviewExcerptView.setText(yelpReview.getExcerpt());
			}
			
			if (yelpUserNameView != null) {
				yelpUserNameView.setText(yelpReview.getUserName());
			}
			
			
		}
		
		return rowView;
	}
}
