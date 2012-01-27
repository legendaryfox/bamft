package com.ksj.bamft;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TruckRowAdapter extends ArrayAdapter<Schedule> {
	
	DatabaseHandler db = new DatabaseHandler(this.getContext());
	
	private final Context context;
	private final List<Schedule> scheduleList;
	
	public TruckRowAdapter(Context context, int textViewResourceId, List<Schedule> scheduleList) {
		super(context, textViewResourceId, scheduleList);
		this.context = context;
		this.scheduleList = scheduleList;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.truck_row, parent, false);		
		}
		Schedule schedule = scheduleList.get(position);
		if (schedule != null) {
			
			//Just for example - we will probably get different values later.
			TextView truckNameText = (TextView) rowView.findViewById(R.id.truckNameText);
			TextView truckCuisineText = (TextView) rowView.findViewById(R.id.truckCuisineText);
			
			Truck truck = db.getTruck(schedule.truck_id);
			
			if (truckNameText != null) {
				truckNameText.setText(truck.getName());
			}
			if (truckCuisineText != null) {
				truckCuisineText.setText(truck.getCuisine());
			}	
		}
		
		return rowView;
		
	}
		
}
