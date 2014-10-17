package com.goofy.models;

import java.util.ArrayList;
import java.util.Date;
import com.google.gson.annotations.SerializedName;
public class TravelDetail extends Travel{
	
	@SerializedName("Photos")
	public ArrayList<Photo> photos;
	@SerializedName("Places")
	public ArrayList<Place> places;
	
	public TravelDetail(int id, String title, String description, String userName, 
			Date start, Date end, int distance, ArrayList<Photo> photos, ArrayList<Place> places)
    {
		super(id, title, description, userName, start, end, distance);
        this.setPhotos(photos);
        this.setPlaces(places);
    }
	

	public ArrayList<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}

	public ArrayList<Place> getPlaces() {
		return places;
	}

	public void setPlaces(ArrayList<Place> places) {
		this.places = places;
	}
}
