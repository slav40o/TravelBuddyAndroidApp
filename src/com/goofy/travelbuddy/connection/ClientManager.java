package com.goofy.travelbuddy.connection;

import java.io.IOException;
import java.lang.reflect.Type;
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

import com.goofy.models.Location;
import com.goofy.models.Photo;
import com.goofy.models.Place;
import com.goofy.models.Travel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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

		String message = getResponceMessage(getResponseEntity, "LOGIN");
		
		if (status == HttpStatus.SC_OK) {
			try {
				UserPreferenceManager.saveLoginData(message, password, context);
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
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		HttpResponse responce = this.client.Post("api/user/register", bodyParams, null, headers);
		int status = responce.getStatusLine().getStatusCode();
		HttpEntity getResponseEntity = responce.getEntity();
		String message = getResponceMessage(getResponseEntity, "REGISTER");
		
		if (status == HttpStatus.SC_OK) {
			UserPreferenceManager.saveUserData(userName, password, context);
		}
		
		return new BasicNameValuePair(String.valueOf(status), message);
	}

	public void addPlace(Place place){
		Location loc = place.getLocation();
		List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
		bodyParams.add(new BasicNameValuePair("country", place.getCountry()));
		bodyParams.add(new BasicNameValuePair("longtitude", String.valueOf(loc.getLongtitude())));
		bodyParams.add(new BasicNameValuePair("latitude", String.valueOf(loc.getLatitude())));
		bodyParams.add(new BasicNameValuePair("title", place.getTitle()));
		bodyParams.add(new BasicNameValuePair("description", place.getDescription()));
		
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		this.client.Post("api/places", bodyParams, null, headers);
	}
	
	public ArrayList<Place> getTopPlaces(){
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = client.Get("api/places", headers);
		String responceBody = getResponceMessage(responce.getEntity(), "TOP_PLACES");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		Type listType = new TypeToken<ArrayList<Place>>() {}.getType();
		ArrayList<Place> places = gson.fromJson(responceBody, listType);
		return places;
	}
	
	public ArrayList<Travel> getPersonalTrips(){
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = client.Get("api/travels", headers);
		String responceBody = getResponceMessage(responce.getEntity(), "TRIPS");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		Type listType = new TypeToken<ArrayList<Travel>>() {}.getType();
		ArrayList<Travel> trips = gson.fromJson(responceBody, listType);
		return trips;
	}
	
	public NameValuePair addPhoto(Photo photo){
		return null;
	}
	
	public NameValuePair addTrip(String title, String description){
		List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
		bodyParams.add(new BasicNameValuePair("title", title));
		bodyParams.add(new BasicNameValuePair("description", description));
		
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = this.client.Post("api/places", bodyParams, null, headers);
		String message = getResponceMessage(responce.getEntity(), "TRIP_CREATE");
		int status = responce.getStatusLine().getStatusCode();
		return new BasicNameValuePair(String.valueOf(status), message);
	}
	
	private List<NameValuePair> getAuthorisationHeaders(){
		String token = UserPreferenceManager.getToken(this.context);
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Authorisation", "Bearer " + token));
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		return headers;
	}
	
	private String getResponceMessage(HttpEntity responseEntity, String source) {
		String message = "";
		try {
			message = EntityUtils.toString(responseEntity);
			Log.d("RESPONCE_" + source, message);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return message;
	}
}
