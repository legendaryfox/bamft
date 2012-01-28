package com.ksh.bamft.model;

public class Landmark {
	
	private int id;
	private String name;
	private String xcoord;
	private String ycoord;
	
	// Empty constructor
	public Landmark() {
		
	}
	
	// Constructor
	public Landmark(int id, String name, String xcoord, String ycoord) {
		this.id = id;
		this.name = name;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
	}
	
	// Basic getter/setters
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getXcoord() {
		return this.xcoord;
	}
	
	public void setXcoord(String xcoord) {
		this.xcoord = xcoord;
	}
	
	public String getYcoord() {
		return this.ycoord;
	}
	
	public void setYcoord(String ycoord) {
		this.ycoord = ycoord;
	}

}
