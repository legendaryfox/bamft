package com.ksj.bamft.activity;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class HubwayStationsMapActivity extends MapActivity {

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
}
