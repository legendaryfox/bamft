package com.ksj.bamft.model;

public class Schedule {
	
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
