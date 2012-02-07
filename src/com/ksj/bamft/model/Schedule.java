package com.ksj.bamft.model;

import java.io.Serializable;

public class Schedule implements Serializable {
	
	

	private static final long serialVersionUID = -8758085257141140831L;
	private int id;
	private int truckId;
	private int landmarkId;
	private String dayOfWeek;
	private String timeOfDay;
	
	public Schedule() {
		
	}
	
	public Schedule(int id, String dayOfWeek, String timeOfDay, int truckId, int landmarkId) {
		this.id = id;
		this.dayOfWeek = dayOfWeek;
		this.timeOfDay = timeOfDay;
		this.truckId = truckId;
		this.landmarkId = landmarkId;
	}
	
	@Override
	public String toString() {
		return "Schedule [id=" + id + ", truckId=" + truckId + ", landmarkId="
				+ landmarkId + ", dayOfWeek=" + dayOfWeek + ", timeOfDay="
				+ timeOfDay + "]";
	}
	
	//basic setter/getters
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDayOfWeek() {
		return this.dayOfWeek;
	}
	
	public void setDayOfWeek(String day_of_week) {
		this.dayOfWeek = day_of_week;
	}
	
	public String getTimeOfDay() {
		return this.timeOfDay;
	}
	
	public void setTimeOfDay(String time_of_day) {
		this.timeOfDay = time_of_day;
	}
	
	
	public int getTruckId() {
		return this.truckId;
	}
	
	public void setTruckId(int truck_id) {
		this.truckId = truck_id;
	}
	
	public int getLandmarkId() {
		return this.landmarkId;
	}
	
	public void setLandmarkId(int landmark_id) {
		this.landmarkId = landmark_id;
	}
}
