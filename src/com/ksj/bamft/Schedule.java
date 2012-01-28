package com.ksj.bamft;

public class Schedule {
	
	private int id;
	private String day_of_week;
	private String time_of_day;
	private int truck_id;
	private int landmark_id;
	
	public Schedule() {
		
	}
	
	public Schedule(int id, String day_of_week, String time_of_day, int truck_id, int landmark_id) {
		this.id = id;
		this.day_of_week = day_of_week;
		this.time_of_day = time_of_day;
		this.truck_id = truck_id;
		this.landmark_id = landmark_id;
	}
	
	//basic setter/getters
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDayOfWeek() {
		return this.day_of_week;
	}
	
	public void setDayOfWeek(String day_of_week) {
		this.day_of_week = day_of_week;
	}
	
	public String getTimeOfDay() {
		return this.time_of_day;
	}
	
	public void setTimeOfDay(String time_of_day) {
		this.time_of_day = time_of_day;
	}
	
	
	public int getTruckId() {
		return this.truck_id;
	}
	
	public void setTruckId(int truck_id) {
		this.truck_id = truck_id;
	}
	
	public int getLandmarkId() {
		return this.landmark_id;
	}
	
	public void setLandmarkId(int landmark_id) {
		this.landmark_id = landmark_id;
	}
	
	
	

}
