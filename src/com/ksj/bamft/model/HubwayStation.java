package com.ksj.bamft.model;

public class HubwayStation {
	
	private String name;
	private double latitude;
	private double longitude;
	private boolean installed;
	private boolean locked;
	private int numBikes;
	private int numEmptyDocks;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public boolean isInstalled() {
		return installed;
	}
	
	public void setInstalled(boolean installed) {
		this.installed = installed;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public int getNumBikes() {
		return numBikes;
	}
	
	public void setNumBikes(int numBikes) {
		this.numBikes = numBikes;
	}
	
	public int getNumEmptyDocks() {
		return numEmptyDocks;
	}
	
	public void setNumEmptyDocks(int numEmptyDocks) {
		this.numEmptyDocks = numEmptyDocks;
	}
}
