package com.ksj.bamft.adapter;

import java.util.List;

import com.ksj.bamft.R;
import com.ksj.bamft.model.FoodItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FoodItemAdapter extends ArrayAdapter<FoodItem> {

	private final Context context;
	private final List<FoodItem> foodItemList;
	
	public FoodItemAdapter(Context context, int textViewResourceId, List<FoodItem> foodItemList) {
		super(context, textViewResourceId, foodItemList);
		this.context = context;
		this.foodItemList = foodItemList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.food_item_row, parent, false);		
		}
		
		FoodItem foodItem = foodItemList.get(position);
		
		if (foodItem != null) {
			setText(rowView, foodItem);
		}
		
		return rowView;
	}
	

	/**
	 * Initializes the text for each menu item, including the name, description, and price.
	 * 
	 * @param rowView
	 * @param foodItem
	 */
	private void setText(View rowView, FoodItem foodItem) {
		TextView foodItemNameTextView = (TextView) rowView.findViewById(R.id.foodItemNameText);
		TextView foodItemDescriptionTextView = (TextView) rowView.findViewById(R.id.foodItemDescriptionText);
		TextView foodItemPriceTextView = (TextView) rowView.findViewById(R.id.foodItemPriceText);
		
		if (foodItemNameTextView != null) {
			foodItemNameTextView.setText(foodItem.getName());
		}
		
		if (foodItemDescriptionTextView != null) {
			foodItemDescriptionTextView.setText(foodItem.getDescription());
		}

		if (foodItemPriceTextView != null) {
			foodItemPriceTextView.setText(foodItem.getPrice());
		}
	}
}
