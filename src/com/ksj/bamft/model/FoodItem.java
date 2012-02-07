package com.ksj.bamft.model;

public class FoodItem {
	
	private int id;
	private String name;
	private String description;
	private String price;
	private int truckId;
	
	
	public FoodItem() {

	}

	public FoodItem(int id, String name, String description, String price,
			int truckId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.truckId = truckId;
	}
	
	@Override
	public String toString() {
		return "FoodItem [id=" + id + ", name=" + name + ", description="
				+ description + ", price=" + price + ", truckId=" + truckId
				+ "]";
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
	public int getTruckId() {
		return truckId;
	}
	public void setTruckId(int truckId) {
		this.truckId = truckId;
	}
	

}
