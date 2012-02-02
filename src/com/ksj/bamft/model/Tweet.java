package com.ksj.bamft.model;

public class Tweet {

	private String date;
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Tweet() {
		
	}
	
	public Tweet(String date, String content) {
		this.date = date;
		this.content = content;
				
	}


	@Override
	public String toString() {
		return "Tweet [date=" + date + ", content=" + content + "]";
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
}
