package com.ksj.bamft.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ksj.bamft.R;
import com.ksj.bamft.activity.ScheduleListActivity;
import com.ksj.bamft.activity.ScheduleProfileActivity;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.maps.MapHelpers;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

public class ScheduleRowAdapter extends ArrayAdapter<Schedule> {

	DatabaseHandler db = new DatabaseHandler(this.getContext());

	private final Context context;
	private final List<Schedule> scheduleList;
	private final HashMap<Schedule, Double> scheduleToDistanceMap;

	public ScheduleRowAdapter(Context context, int textViewResourceId,
			List<Schedule> scheduleList,
			HashMap<Schedule, Double> scheduleToDistanceMap) {
		super(context, textViewResourceId, scheduleList);
		this.context = context;
		this.scheduleList = scheduleList;
		this.scheduleToDistanceMap = scheduleToDistanceMap;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.ab_truck_row, parent, false);		
		}
		Schedule schedule = scheduleList.get(position);
		if (schedule != null) {

			//Just for example - we will probably get different values later.
			TextView truckNameText = (TextView) rowView.findViewById(R.id.truckNameText);
			TextView truckCuisineText = (TextView) rowView.findViewById(R.id.truckCuisineText);
			TextView truckDistanceText = (TextView) rowView.findViewById(R.id.landmarkDistanceText);

			Truck truck = db.getTruck(schedule.getTruckId());
			Landmark landmark = db.getLandmark(schedule.getLandmarkId());

			if (truckNameText != null) {
				truckNameText.setText(truck.getName());
			}

			if (truckCuisineText != null) {
				truckCuisineText.setText(truck.getCuisine());
			}

			if (truckDistanceText != null) {
				double distance = scheduleToDistanceMap.get(schedule);

				// TODO: if distance is NaN or Double.MAX_VALUE,
				// show "N/A" for distance

				truckDistanceText.setText(
						MapHelpers.roundDistanceToDecimalPlace(1, distance) + " " +
								Constants.MILES);
			}
			
			
			//UNFOCUS THE IMAGE BUTTONS
			ImageButton bikeButton = (ImageButton) rowView.findViewById(R.id.btn_bike);
			ImageButton walkButton = (ImageButton) rowView.findViewById(R.id.btn_walk);
			ImageButton subwayButton = (ImageButton) rowView.findViewById(R.id.btn_subway);
			
			bikeButton.setFocusable(false);
			bikeButton.setFocusableInTouchMode(false);
			
			walkButton.setFocusable(false);
			walkButton.setFocusableInTouchMode(false);
			
			subwayButton.setFocusable(false);
			subwayButton.setFocusableInTouchMode(false);
			
			
			
			//END UNFOCUS

			

		}





		return rowView;
	}
}
