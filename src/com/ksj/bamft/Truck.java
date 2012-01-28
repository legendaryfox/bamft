package com.ksj.bamft;

public class Truck {
	
	private int id;
	private String name;
	private String cuisine;
	private String description;
	
	public Truck() {
		
	}
	
	public Truck(int id, String name, String cuisine, String description) {
		this.id = id;
		this.name = name;
		this.cuisine = cuisine;
		this.description = description;
	}
	
	
	
	
	
	// basic getters/setters
	
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
	
	public String getCuisine() {
		return this.cuisine;
	}
	
	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
