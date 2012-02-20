package com.ksj.bamft.model;

public class MbtaStation {
	
	@Override
	public String toString() {
		return "MbtaStation [platformKey=" + platformKey + ", platformName="
				+ platformName + ", stationName=" + stationName
				+ ", lineColor=" + lineColor + ", platformOrder="
				+ platformOrder + ", startOfLine=" + startOfLine
				+ ", endOfLine=" + endOfLine + ", branch=" + branch
				+ ", direction=" + direction + ", stopId=" + stopId
				+ ", stopName=" + stopName + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}

	private String platformKey;
	private String platformName;
	private String stationName;
	private String lineColor;
	private int platformOrder;
	private Boolean startOfLine;
	private Boolean endOfLine;
	private String branch;
	private String direction;
	private String stopId;
	private String stopName;
	private double latitude;
	private double longitude;
	
	public MbtaStation() {
		super();
	}

	public MbtaStation(String platformKey, String platformName,
			String stationName, String lineColor, int platformOrder,
			Boolean startOfLine, Boolean endOfLine, String branch,
			String direction, String stopId, String stopName, double latitude,
			double longitude) {
		super();
		this.platformKey = platformKey;
		this.platformName = platformName;
		this.stationName = stationName;
		this.lineColor = lineColor;
		this.platformOrder = platformOrder;
		this.startOfLine = startOfLine;
		this.endOfLine = endOfLine;
		this.branch = branch;
		this.direction = direction;
		this.stopId = stopId;
		this.stopName = stopName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public MbtaStation(String platformKey, String lineColor, String stopName,
			double latitude, double longitude) {
		super();
		this.platformKey = platformKey;
		this.lineColor = lineColor;
		this.stopName = stopName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getPlatformKey() {
		return platformKey;
	}

	public void setPlatformKey(String platformKey) {
		this.platformKey = platformKey;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}

	public int getPlatformOrder() {
		return platformOrder;
	}

	public void setPlatformOrder(int platformOrder) {
		this.platformOrder = platformOrder;
	}

	public Boolean getStartOfLine() {
		return startOfLine;
	}

	public void setStartOfLine(Boolean startOfLine) {
		this.startOfLine = startOfLine;
	}

	public Boolean getEndOfLine() {
		return endOfLine;
	}

	public void setEndOfLine(Boolean endOfLine) {
		this.endOfLine = endOfLine;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	
	
	
	
	

}
