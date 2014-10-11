package com.goofy.models;

public class Photo {
	private int id;
	private String name;
	private byte[] image;
	private String userId;
	private int placeID;
    
	public Photo(int id, String name, byte[] image, String userId, int placeId){
		this.id = id;
		this.setName(name);
		this.setImage(image);
		this.setUserId(userId);
		this.setPlaceID(placeId);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPlaceID() {
		return placeID;
	}

	public void setPlaceID(int placeID) {
		this.placeID = placeID;
	}
}
