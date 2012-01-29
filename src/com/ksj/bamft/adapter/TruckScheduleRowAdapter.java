package com.ksj.bamft.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ksj.bamft.R;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;

public class TruckScheduleRowAdapter extends ArrayAdapter<Schedule> {
	DatabaseHandler db = new DatabaseHandler(this.getContext());
	
	private final Context context;
	private final List<Schedule> scheduleList;
	
	public TruckScheduleRowAdapter(Context context, int textViewResourceId, List<Schedule> scheduleList) {
		super(context, textViewResourceId, scheduleList);
		this.context = context;
		this.scheduleList = scheduleList;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.truck_schedule_row, parent, false);		
		}
		
		Schedule schedule = scheduleList.get(position);
		
		if (schedule != null) {
			TextView dayOfWeekText = (TextView) rowView.findViewById(R.id.truckScheduleDayOfWeek);
			TextView timeOfDayText = (TextView) rowView.findViewById(R.id.truckScheduleTimeOfDay);
			TextView landmarkText = (TextView) rowView.findViewById(R.id.truckScheduleLandmark);
			
			if (dayOfWeekText != null) {
				dayOfWeekText.setText(schedule.getDayOfWeek());
			}
			
			if (timeOfDayText != null) {
				timeOfDayText.setText(schedule.getTimeOfDay());
			}
			
			if (landmarkText != null) {
				Landmark landmark = db.getLandmark(schedule.getLandmarkId());
				landmarkText.setText(landmark.getName());
			}
		}
		
		return rowView;
	}
}
