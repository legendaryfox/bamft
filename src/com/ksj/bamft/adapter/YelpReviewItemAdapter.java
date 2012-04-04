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
			setText(rowView, yelpReview);
			setImages(rowView, yelpReview);
		}
		
		return rowView;
	}
	
	/** 
	 * Initializes the text in a Yelp review, including the reviewer's name and an excerpt.
	 * 
	 * @param rowView
	 * @param yelpReview
	 */
	private void setText(View rowView, YelpReview yelpReview) {
		TextView yelpReviewExcerptView = (TextView) rowView.findViewById(R.id.yelpReviewExcerpt);
		TextView yelpUserNameView = (TextView) rowView.findViewById(R.id.yelpUserName);
		
		if (yelpReviewExcerptView != null) {
			yelpReviewExcerptView.setText(yelpReview.getExcerpt());
		}
		
		if (yelpUserNameView != null) {
			yelpUserNameView.setText(yelpReview.getUserName());
		}
	}
	
	/**
	 * Initializes the images in a Yelp review.
	 * 
	 * @param rowView
	 * @param yelpReview
	 */
	private void setImages(View rowView, YelpReview yelpReview) {
		ImageView yelpUserImageView = (ImageView) rowView.findViewById(R.id.yelpUserImage);
		ImageView yelpRatingImageView = (ImageView) rowView.findViewById(R.id.yelpRatingImage);
		
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
	}
}
