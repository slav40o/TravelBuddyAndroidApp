package com.goofy.models;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Place {
	@SerializedName("Id")
	public int id;
	
	@SerializedName("Title")
	public String title;
	
	@SerializedName("Description")
	public String description;
	
	@SerializedName("Country")
	public String country;
	
	@SerializedName("LastVisited")
	public Date lastVisited;
	
	@SerializedName("Longtitude")
	private double longtitude;
	
	@SerializedName("Latitude")
	private double latitude;
	
	public Place(int id, String title, String description, 
			String country, Location location)
    {
        this.setId(id);
        this.setDescription(description);
        this.setCountry(country);
        this.setLastVisited(lastVisited);
        this.setLongtitude(location.getLongtitude());
        this.setLatitude(location.getLatitude());
        this.setTitle(title);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getLastVisited() {
		return lastVisited;
	}

	public void setLastVisited(Date lastVisited) {
		this.lastVisited = lastVisited;
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
	
	public Location getLocation(){
		return new Location(this.getLatitude(), this.getLongtitude());
	}
}
