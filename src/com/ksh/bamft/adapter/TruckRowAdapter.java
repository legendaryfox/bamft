package com.ksh.bamft.adapter;

import java.util.List;

import com.ksh.bamft.model.Truck;
import com.ksj.bamft.R;
import com.ksj.bamft.R.id;
import com.ksj.bamft.R.layout;
import com.ksj.bamft.database.DatabaseHandler;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
			rowView = inflater.inflate(R.layout.truck_row, parent, false);		
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
		}
		
		return rowView;
		
	}
		
}
