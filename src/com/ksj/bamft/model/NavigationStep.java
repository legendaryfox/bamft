package com.ksj.bamft.model;

import java.io.Serializable;

public class NavigationStep implements Serializable {
	
	private static final long serialVersionUID = -2366762892958686145L;
	private String address = "";
	private String directions = "";
	private String description = "";
	private SimpleLocation location = new SimpleLocation();
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getDirections() {
		return directions;
	}
	
	public void setDirections(String directions) {
		this.directions = directions;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public SimpleLocation getLocation() {
		return location;
	}
	
	public void setLocation(SimpleLocation location) {
		this.location = location;
	}
}
