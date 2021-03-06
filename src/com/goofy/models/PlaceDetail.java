package com.goofy.models;

import java.util.ArrayList;
import java.util.Date;
import com.google.gson.annotations.SerializedName;
public class PlaceDetail extends Place{
	@SerializedName("Visitors")
	public ArrayList<String> visitors;
	@SerializedName("Photos")
	public ArrayList<Integer> photos;
	
	public PlaceDetail(int id, String title, String description, String country,
			Location location, Date lastVisited, ArrayList<Integer> photos, ArrayList<String> visitors)
    {
		super(id, title, description, country, location);
        this.setVisitors(visitors);
        this.setPhotos(photos);
    }

	public ArrayList<String> getVisitors() {
		return visitors;
	}

	public void setVisitors(ArrayList<String> visitors) {
		this.visitors = visitors;
	}

	public ArrayList<Integer> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<Integer> photos) {
		this.photos = photos;
	}
}
