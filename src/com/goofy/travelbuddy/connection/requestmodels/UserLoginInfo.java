package com.goofy.travelbuddy.connection.requestmodels;

import com.google.gson.annotations.SerializedName;

public class UserLoginInfo {
	@SerializedName("access_token")
	public String accessToken;
	
	@SerializedName("token_type")
	public String password;
	
	@SerializedName("expires_in")
	public String expiresAfter;
	
	@SerializedName("userName")
	public String username;
	
	@SerializedName(".issued")
	public String taken;
	
	@SerializedName(".expires")
	public String expires;
}
