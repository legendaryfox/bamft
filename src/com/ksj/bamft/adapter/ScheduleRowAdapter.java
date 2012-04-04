package com.ksj.bamft.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ksj.bamft.R;
import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.constants.GoogleMapsConstants;
import com.ksj.bamft.database.DatabaseHandler;
import com.ksj.bamft.hubway.HubwayHelpers;
import com.ksj.bamft.maps.MapHelpers;
import com.ksj.bamft.model.HubwayStation;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.SimpleLocation;
import com.ksj.bamft.model.Truck;

public class ScheduleRowAdapter extends ArrayAdapter<Schedule> {

	DatabaseHandler db = new DatabaseHandler(this.getContext());

	private final Context context;
	private final List<Schedule> scheduleList;
	private final HashMap<Schedule, Double> scheduleToDistanceMap;
	private final double userLatitude;
	private final double userLongitude;

	public ScheduleRowAdapter(Context context, int textViewResourceId,
			List<Schedule> scheduleList,
			HashMap<Schedule, Double> scheduleToDistanceMap, double userLatitude, double userLongitude) {
		
		super(context, textViewResourceId, scheduleList);
		this.context = context;
		this.scheduleList = scheduleList;
		this.scheduleToDistanceMap = scheduleToDistanceMap;
		this.userLatitude = userLatitude;
		this.userLongitude = userLongitude;
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
			final Landmark landmark = db.getLandmark(schedule.getLandmarkId());
			
			setText(rowView, schedule);
			initHubwayButton(rowView, landmark);
			initMbtaButton(rowView, landmark);
			initWalkButton(rowView, landmark);
		}

		return rowView;
	}
	
	/**
	 * Initializes all the TextViews: truck name, cuisine, and distance.
	 * 
	 * @param rowView
	 * @param schedule
	 */
	private void setText(View rowView, Schedule schedule) {
		TextView truckNameText = (TextView) rowView.findViewById(R.id.truckNameText);
		TextView truckCuisineText = (TextView) rowView.findViewById(R.id.truckCuisineText);
		TextView truckDistanceText = (TextView) rowView.findViewById(R.id.landmarkDistanceText);

		Truck truck = db.getTruck(schedule.getTruckId());
		
		if (truckNameText != null) {
			truckNameText.setText(truck.getName());
		}

		if (truckCuisineText != null) {
			truckCuisineText.setText(truck.getCuisine());
		}

		if (truckDistanceText != null) {
			double distance = scheduleToDistanceMap.get(schedule);

			// If distance is NaN or Double.MAX_VALUE, set distance to "N/A"
			
			if (Double.isNaN(distance) || distance == Double.MAX_VALUE) 
				truckDistanceText.setText("N/A");

			else {
				truckDistanceText.setText(
					MapHelpers.roundDistanceToDecimalPlace(1, distance) + " " +
							Constants.MILES);
			}
		}
	}
	
	/**
	 * Initializes the MBTA button, which directs the user to the truck
	 * via public transportation.
	 * 
	 * @param rowView
	 * @param landmark
	 */
	private void initMbtaButton(View rowView, final Landmark landmark) {
		ImageButton subwayButton = (ImageButton) rowView.findViewById(R.id.truckProfileSubwayButton);
		
		subwayButton.setFocusable(false);
		subwayButton.setFocusableInTouchMode(false);
		subwayButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				List<SimpleLocation> destinations = new ArrayList<SimpleLocation>(1);
				destinations.add(new SimpleLocation(Double.parseDouble(landmark.getYcoord()), Double.parseDouble(landmark.getXcoord())));

				String mapsQuery = MapHelpers.getMapsQuery(
						new SimpleLocation(userLatitude, userLongitude), 
						destinations, 
						GoogleMapsConstants.PUBLIC_TRANSIT,
						GoogleMapsConstants.HTML);

				Intent intent = new Intent(
						android.content.Intent.ACTION_VIEW, Uri.parse(mapsQuery));

				intent.setClassName(GoogleMapsConstants.PACKAGE, GoogleMapsConstants.CLASS);

				context.startActivity(intent);	
			}
		});
	}
	
	/**
	 * Initialize the walk button, which opens Google Maps with the fastest
	 * walking route to the truck from the user's location. 
	 * 
	 * @param rowView
	 * @param landmark
	 */
	private void initWalkButton(View rowView, final Landmark landmark) {
		ImageButton walkButton = (ImageButton) rowView.findViewById(R.id.truckProfileWalkingButton);
		
		walkButton.setFocusable(false);
		walkButton.setFocusableInTouchMode(false);
		walkButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SimpleLocation userLocation = new SimpleLocation(userLatitude, userLongitude);
				SimpleLocation truckLocation = new SimpleLocation(Double.parseDouble(landmark.getYcoord()), Double.parseDouble(landmark.getXcoord()));

				List<SimpleLocation> destination = new ArrayList<SimpleLocation>(1);
				destination.add(truckLocation);

				String mapsQuery = MapHelpers.getMapsQuery(userLocation, destination,
						GoogleMapsConstants.WALKING_ROUTE, GoogleMapsConstants.HTML);

				Intent intent = new Intent(
						android.content.Intent.ACTION_VIEW, Uri.parse(mapsQuery));

				intent.setClassName(GoogleMapsConstants.PACKAGE, GoogleMapsConstants.CLASS);
				

				context.startActivity(intent);
			}
		});
	}
	
	/**
	 * Initialize the Hubway button, which opens Google Maps with directions 
	 * to the truck via the closest Hubway station.
	 * 
	 * @param rowView
	 * @param landmark
	 */
	private void initHubwayButton(View rowView, final Landmark landmark) {
		ImageButton bikeButton = (ImageButton) rowView.findViewById(R.id.truckProfileHubwayButton);
		
		bikeButton.setFocusable(false);
		bikeButton.setFocusableInTouchMode(false);
		bikeButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				List<HubwayStation> stations = HubwayHelpers.getAvailableStations();
				HubwayStation nearestStationToUser = HubwayHelpers.getNearestStation(stations,
						userLatitude, userLongitude);
				
				if (nearestStationToUser == null) {
					Toast.makeText(context, Constants.HUBWAY_UNAVAILABLE, Toast.LENGTH_LONG).show();
					return;
				}

				SimpleLocation nearestStationToUserLoc = new SimpleLocation(
						nearestStationToUser.getLatitude(), nearestStationToUser.getLongitude());

				List<SimpleLocation> destinations = new ArrayList<SimpleLocation>(2);
				destinations.add(nearestStationToUserLoc);
				destinations.add(new SimpleLocation(Double.parseDouble(landmark.getYcoord()), Double.parseDouble(landmark.getXcoord())));

				String mapsQuery = MapHelpers.getMapsQuery(
						new SimpleLocation(userLatitude, userLongitude), 
						destinations, 
						GoogleMapsConstants.BIKING_ROUTE,
						GoogleMapsConstants.HTML);

				Intent intent = new Intent(
						android.content.Intent.ACTION_VIEW, Uri.parse(mapsQuery));

				intent.setClassName(Constants.BROWSER_PACKAGE, Constants.BROWSER_CLASS);
				
				Toast.makeText(context, Constants.ROUTING_NEAREST_HUBWAY, Toast.LENGTH_LONG).show();
				
				context.startActivity(intent);
			}
		});
	}
}
