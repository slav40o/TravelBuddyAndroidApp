package com.goofy.models;

import java.util.Date;

public class Place {
	private int id;
	private String title;
	private String description;
	private String country;
	private Location location;
	private Date lastVisited;
	
	public Place(int id, String title, String description, 
			String country, Location location)
    {
        this.setId(id);
        this.setDescription(description);
        this.setCountry(country);
        this.setLastVisited(lastVisited);
        this.setLocation(location);
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getLastVisited() {
		return lastVisited;
	}

	public void setLastVisited(Date lastVisited) {
		this.lastVisited = lastVisited;
	}
}
