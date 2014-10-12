package com.goofy.travelbuddy.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

public class ClientManager {
	private final String Server_Url = "http://travelbuddy-1.apphb.com/";
	private RestApiClient client;
	private Context context;
	
	public ClientManager(Context context){
		this.client = new RestApiClient(Server_Url);
		this.context = context;
	}
	
	public NameValuePair loginUser(String userName, String password){
		List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
		bodyParams.add(new BasicNameValuePair("username", userName));
		bodyParams.add(new BasicNameValuePair("password", password));
		bodyParams.add(new BasicNameValuePair("grant_type", "password"));
		
		HttpResponse responce = this.client.Post("api/user/login", bodyParams, null, null);
		int status = responce.getStatusLine().getStatusCode();
		HttpEntity getResponseEntity = responce.getEntity();

		String message = getResponceMessage(getResponseEntity);
		
		if (status == HttpStatus.SC_OK) {
			try {
				UserPreferenceManager.saveLoginData(responce, password, context);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return new BasicNameValuePair(String.valueOf(status), message);
	}

	public NameValuePair registerUser(String userName, String password, String confirmPassword){
		List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
		bodyParams.add(new BasicNameValuePair("email", userName));
		bodyParams.add(new BasicNameValuePair("password", password));
		bodyParams.add(new BasicNameValuePair("confirmpassword", confirmPassword));
		
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		bodyParams.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		HttpResponse responce = this.client.Post("api/user/register", bodyParams, null, headers);
		int status = responce.getStatusLine().getStatusCode();
		HttpEntity getResponseEntity = responce.getEntity();
		String message = getResponceMessage(getResponseEntity);
		
		if (status == HttpStatus.SC_OK) {
			UserPreferenceManager.saveUserData(userName, password, context);
		}
		
		return new BasicNameValuePair(String.valueOf(status), message);
	}

	public void addPlace(){
		
	}
	
	public void getPlaces(){
		
	}
	
	public void getTravels(){
		
	}
	
	public void addPhoto(){
		
	}
	
	private String getResponceMessage(HttpEntity responseEntity) {
		String message = "";
		try {
			message = EntityUtils.toString(responseEntity);
			Log.d("ERROR_MESSAGE", message);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return message;
	}
}
