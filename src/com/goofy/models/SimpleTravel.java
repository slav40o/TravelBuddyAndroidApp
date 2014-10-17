package com.goofy.models;

import com.google.gson.annotations.SerializedName;

public class SimpleTravel {
	@SerializedName("Id")
	public int id;
	
	@SerializedName("Title")
	public String title;
	
	@SerializedName("Description")
	public String description;
}
