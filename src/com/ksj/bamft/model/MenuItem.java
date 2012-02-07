package com.ksj.bamft.model;

public class MenuItem {
	
	private int id;
	private String name;
	private String description;
	private String price;
	private int truck_id;
	
	
	public MenuItem() {

	}

	public MenuItem(int id, String name, String description, String price,
			int truck_id) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.truck_id = truck_id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getTruck_id() {
		return truck_id;
	}
	public void setTruck_id(int truck_id) {
		this.truck_id = truck_id;
	}
	

}
