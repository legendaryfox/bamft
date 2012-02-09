package com.ksj.bamft.model;

public class YelpReview {
	private String id;
	private int rating;
	private String excerpt;
	private String ratingImageUrl;
	private String userImageUrl;
	private String userId;
	private String userName;
	
	
	
	
	public YelpReview() {
		super();
	}
	
	
	
	
	public YelpReview(String id, int rating, String excerpt,
			String ratingImageUrl, String userId,
			String userName, String userImageUrl) {
		super();
		this.id = id;
		this.rating = rating;
		this.excerpt = excerpt;
		this.ratingImageUrl = ratingImageUrl;
		
		this.userId = userId;
		this.userName = userName;
		this.userImageUrl = userImageUrl;
	}




	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getExcerpt() {
		return excerpt;
	}
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}
	public String getRatingImageUrl() {
		return ratingImageUrl;
	}
	public void setRatingImageUrl(String ratingImageUrl) {
		this.ratingImageUrl = ratingImageUrl;
	}
	public String getUserImageUrl() {
		return userImageUrl;
	}
	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	

}
