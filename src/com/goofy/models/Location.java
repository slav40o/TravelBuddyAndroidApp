package com.goofy.models;

public class Location {
	private double longtitude;
	private double latitude;
	
	public Location(double lat, double lon){
		this.setLatitude(lat);
		this.setLongtitude(lon);
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
