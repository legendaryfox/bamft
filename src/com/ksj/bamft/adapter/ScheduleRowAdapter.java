package com.ksj.bamft.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

			//Just for example - we will probably get different values later.
			TextView truckNameText = (TextView) rowView.findViewById(R.id.truckNameText);
			TextView truckCuisineText = (TextView) rowView.findViewById(R.id.truckCuisineText);
			TextView truckDistanceText = (TextView) rowView.findViewById(R.id.landmarkDistanceText);

			Truck truck = db.getTruck(schedule.getTruckId());
			final Landmark landmark = db.getLandmark(schedule.getLandmarkId());

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
			ImageButton bikeButton = (ImageButton) rowView.findViewById(R.id.truckProfileHubwayButton);
			ImageButton walkButton = (ImageButton) rowView.findViewById(R.id.truckProfileWalkingButton);
			ImageButton subwayButton = (ImageButton) rowView.findViewById(R.id.truckProfileSubwayButton);
			
			bikeButton.setFocusable(false);
			bikeButton.setFocusableInTouchMode(false);
			bikeButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {

					List<HubwayStation> stations = HubwayHelpers.getAvailableStations();

					if (stations == null || stations.size() < 1) {
						Toast.makeText(
								context,
								Constants.HUBWAY_UNAVAILABLE,
								Toast.LENGTH_LONG).show();

						return;
					}

					HubwayStation nearestStationToUser = HubwayHelpers.getNearestStation(stations,
							userLatitude, userLongitude);

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
					context.startActivity(intent);
				}
			});
			
			
			
			
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
					Toast.makeText(context, "Routing you to the nearest Hubway Bike Station first...", Toast.LENGTH_LONG).show();

					context.startActivity(intent);
				}
			});
			
			
			
			subwayButton.setFocusable(false);
			subwayButton.setFocusableInTouchMode(false);
			subwayButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {

					//List<MbtaStation> stations = MbtaHelpers.getAllMbtaStations();

					List<SimpleLocation> destinations = new ArrayList<SimpleLocation>(1);
					destinations.add(new SimpleLocation(Double.parseDouble(landmark.getYcoord()), Double.parseDouble(landmark.getXcoord())));

					String mapsQuery = MapHelpers.getMapsQuery(
							new SimpleLocation(userLatitude, userLongitude), 
							destinations, 
							GoogleMapsConstants.PUBLIC_TRANSIT,
							GoogleMapsConstants.HTML);

					Intent intent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri.parse(mapsQuery));

					//intent.setClassName(Constants.BROWSER_PACKAGE, Constants.BROWSER_CLASS);
					intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

					
					context.startActivity(intent);	

				}

			});
			
			
			
			//END UNFOCUS

			

		}





		return rowView;
	}
	
	
	
	
}
