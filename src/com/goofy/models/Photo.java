package com.goofy.models;
import android.util.Base64;
import com.google.gson.annotations.SerializedName;
public class Photo {
	@SerializedName("Id")
	public int id;
	@SerializedName("Name")
	public String name;
	
	public byte[] image;
	@SerializedName("UserId")
	public String userId;
	@SerializedName("PlaceId")
	public int placeID;
	@SerializedName("Image")
    public String photo64;
    
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
		if (image == null || photo64 != null) {
			image = Base64.decode(this.photo64, Base64.DEFAULT);
			return image;
		}
		else{
			return image;
		}
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
