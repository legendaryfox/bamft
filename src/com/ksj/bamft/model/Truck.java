package com.ksj.bamft.model;

import java.io.Serializable;

public class Truck implements Serializable {
	
	private static final long serialVersionUID = 3368751234089509108L;
	private int id;
	private String name;
	private String cuisine;
	private String description;
	
	private String email;
	private String menu; // TODO: we probably don't use this anymore...
	private String twitter;
	private String facebook;
	private String website;
	private String yelp;
	
	public Truck() {
		
	}
	
	public Truck(int id, String name, String cuisine, String description) {
		this.id = id;
		this.name = name;
		this.cuisine = cuisine;
		this.description = description;
	}
	
	public Truck(int id, String name, String cuisine, String description, String email, String menu, String twitter, String facebook, String website, String yelp) {
		this.id = id;
		this.name = name;
		this.cuisine = cuisine;
		this.description = description;
		
		this.email = email;
		this.menu = menu;
		this.twitter = twitter;
		this.facebook = facebook;
		this.website = website;
		this.yelp = yelp;
	}
	
	
	// basic getters/setters
	
	@Override
	public String toString() {
		return "Truck [id=" + id + ", name=" + name + ", cuisine=" + cuisine
				+ ", description=" + description + ", email=" + email
				+ ", menu=" + menu + ", twitter=" + twitter + ", facebook="
				+ facebook + ", website=" + website + ", yelp=" + yelp + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getYelp() {
		return yelp;
	}

	public void setYelp(String yelp) {
		this.yelp = yelp;
	}

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
