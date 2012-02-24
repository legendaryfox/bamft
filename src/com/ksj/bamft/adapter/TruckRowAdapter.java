package com.ksj.bamft.adapter;

import java.util.List;

import com.ksj.bamft.R;
import com.ksj.bamft.R.id;
import com.ksj.bamft.R.layout;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Truck;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class TruckRowAdapter extends ArrayAdapter<Truck> {
	
	DatabaseHandler db = new DatabaseHandler(this.getContext());
	
	private final Context context;
	private final List<Truck> truckList;
	
	public TruckRowAdapter(Context context, int textViewResourceId, List<Truck> truckList) {
		super(context, textViewResourceId, truckList);
		this.context = context;
		this.truckList = truckList;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.ab_truck_row, parent, false);		
		}
		Truck truck = truckList.get(position);
		if (truck != null) {
			
			//Just for example - we will probably get different values later.
			TextView truckNameText = (TextView) rowView.findViewById(R.id.truckNameText);
			TextView truckCuisineText = (TextView) rowView.findViewById(R.id.truckCuisineText);
			
			Time now = new Time();
	    	now.setToNow();
			
			
			if (truckNameText != null) {
				truckNameText.setText(truck.getName());
			}
			if (truckCuisineText != null) {
				truckCuisineText.setText(truck.getCuisine());
			}	
			
			
			//UNFOCUS THE IMAGE BUTTONS
			ImageButton bikeButton = (ImageButton) rowView.findViewById(R.id.truckProfileHubwayButton);
			ImageButton walkButton = (ImageButton) rowView.findViewById(R.id.truckProfileWalkingButton);
			ImageButton subwayButton = (ImageButton) rowView.findViewById(R.id.truckProfileSubwayButton);
			
			bikeButton.setFocusable(false);
			bikeButton.setFocusableInTouchMode(false);
			bikeButton.setVisibility(bikeButton.GONE);
			
			walkButton.setFocusable(false);
			walkButton.setFocusableInTouchMode(false);
			walkButton.setVisibility(walkButton.GONE);
			
			subwayButton.setFocusable(false);
			subwayButton.setFocusableInTouchMode(false);
			subwayButton.setVisibility(subwayButton.GONE);
			
			
			
			//END UNFOCUS
		}
		
		return rowView;
		
	}
		
}
