package com.goofy.models;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Travel {
	@SerializedName("Id")
	public int id;
	
	@SerializedName("Title")
	public String title;
	
	@SerializedName("Description")
	public String description;
	
	@SerializedName("UserName")
	public String userName;
	
	@SerializedName("StartDate")
	public Date startDate;
	
	@SerializedName("EndDate")
	public Date endDate;
	
	@SerializedName("Distance")
	public int distance;
	
	public Travel(int id, String title, String description, String userName, 
			Date start, Date end, int distance){
		this.setId(id);
        this.setDescription(description);
        this.setUserName(userName);
        this.setStartDate(start);
        this.setEndDate(end);
        this.setDistance(distance);
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}
