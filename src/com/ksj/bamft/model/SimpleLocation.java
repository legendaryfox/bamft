package com.ksj.bamft.model;

import java.io.Serializable;

public class SimpleLocation implements Serializable {

	private static final long serialVersionUID = -6995662621042014901L;
	private double latitude;
	private double longitude;

	public SimpleLocation() {
	}
	
	public SimpleLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
