package com.ksj.bamft.model;

public class Factlet {
	private int id;
	private String title;
	private String content;
	private int truckId;
	
	public Factlet() {
		super();
	}
	
	public Factlet(int id, String title, String content, int truckId) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.truckId = truckId;
	}
	

	@Override
	public String toString() {
		return "Factlet [id=" + id + ", title=" + title + ", content="
				+ content + ", truckId=" + truckId + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTruckId() {
		return truckId;
	}

	public void setTruckId(int truckId) {
		this.truckId = truckId;
	}
}
