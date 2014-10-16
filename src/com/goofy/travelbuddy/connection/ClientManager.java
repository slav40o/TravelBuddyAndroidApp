package com.goofy.travelbuddy.connection;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.goofy.models.Location;
import com.goofy.models.Photo;
import com.goofy.models.Place;
import com.goofy.models.PlaceDetail;
import com.goofy.models.Travel;
import com.goofy.models.TravelDetail;
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
	
	public NameValuePair loginUser(String userName, String password) throws ClientProtocolException, IOException{
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

	public NameValuePair registerUser(String userName, String password, String confirmPassword) throws ClientProtocolException, IOException{
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

	public NameValuePair addPlace(Place place) throws ClientProtocolException, IOException{
		Location loc = place.getLocation();
		List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
		bodyParams.add(new BasicNameValuePair("country", place.getCountry()));
		bodyParams.add(new BasicNameValuePair("longtitude", String.valueOf(loc.getLongtitude())));
		bodyParams.add(new BasicNameValuePair("latitude", String.valueOf(loc.getLatitude())));
		bodyParams.add(new BasicNameValuePair("title", place.getTitle()));
		bodyParams.add(new BasicNameValuePair("description", place.getDescription()));
		
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = this.client.Post("api/places", bodyParams, null, headers);
		int status = responce.getStatusLine().getStatusCode();
		HttpEntity getResponseEntity = responce.getEntity();
		String message = getResponceMessage(getResponseEntity, "ADD_PLACE");
		
		return new BasicNameValuePair(String.valueOf(status), message);
	}
	
	public ArrayList<Photo> getPlacePhotos(int placeId){
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
		urlParams.add(new BasicNameValuePair("placeId", String.valueOf(placeId)));
		HttpResponse responce = client.Get("api/photos", headers, urlParams);
		String responceBody = getResponceMessage(responce.getEntity(), "PLACE_PHOTOS");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		Type listType = new TypeToken<ArrayList<Photo>>() {}.getType();
		ArrayList<Photo> photos = gson.fromJson(responceBody, listType);
		return photos;
	}
	
	public ArrayList<Place> getTopPlaces(){
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = client.Get("api/places", headers, null);
		String responceBody = getResponceMessage(responce.getEntity(), "TOP_PLACES");
		int status = responce.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			Type listType = new TypeToken<ArrayList<Place>>() {}.getType();
			ArrayList<Place> places = gson.fromJson(responceBody, listType);
			return places;
		}
		else{
			throw new NoSuchElementException();
		}
	}
	
	public ArrayList<Place> getFavouritePlaces(){
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = client.Get("api/user/favourites", headers, null);
		String responceBody = getResponceMessage(responce.getEntity(), "TOP_PLACES");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		Type listType = new TypeToken<ArrayList<Place>>() {}.getType();
		ArrayList<Place> places = gson.fromJson(responceBody, listType);
		return places;
	}
	
	// FOR TEST
	public ArrayList<Travel> getPersonalTrips(){
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = client.Get("api/travels", headers, null);
		String responceBody = getResponceMessage(responce.getEntity(), "TRIPS");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		Type listType = new TypeToken<ArrayList<Travel>>() {}.getType();
		ArrayList<Travel> trips = gson.fromJson(responceBody, listType);
		return trips;
	}
	
	// FOR TEST
	public NameValuePair addPlaceToFavourites(int placeId) throws ClientProtocolException, IOException{
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
		urlParams.add(new BasicNameValuePair("placeId", String.valueOf(placeId)));
		HttpResponse responce = this.client.Post("api/user/favourites", null, null, headers);
		String message = getResponceMessage(responce.getEntity(), "PLACE_TO_FAVOURITES");
		int status = responce.getStatusLine().getStatusCode();
		return new BasicNameValuePair(String.valueOf(status), message);
	}
	
	public NameValuePair addPhoto(Photo photo) throws ClientProtocolException, IOException{
		List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
		String image = Base64.encodeToString(photo.getImage(), Base64.DEFAULT);
		bodyParams.add(new BasicNameValuePair("name", photo.getName()));
		bodyParams.add(new BasicNameValuePair("image", image));
		bodyParams.add(new BasicNameValuePair("placeId", String.valueOf(photo.getPlaceID())));
		bodyParams.add(new BasicNameValuePair("userId", photo.getUserId()));
		
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = this.client.Post("api/photos", bodyParams, null, headers);
		int status = responce.getStatusLine().getStatusCode();
		HttpEntity getResponseEntity = responce.getEntity();
		String message = getResponceMessage(getResponseEntity, "ADD_PHOTO");
		
		return new BasicNameValuePair(String.valueOf(status), message);
	}
	
	public PlaceDetail getPlaceDetail(int id){
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = client.Get("api/places/" + id, headers, null);
		String responceBody = getResponceMessage(responce.getEntity(), "GET_PLACE_DETAIL");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		PlaceDetail details = gson.fromJson(responceBody, PlaceDetail.class);
		return details;
	}
	
	public TravelDetail getTravelDetail(int id){
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = client.Get("api/travels/" + id, headers, null);
		String responceBody = getResponceMessage(responce.getEntity(), "TRIPS");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		TravelDetail details = gson.fromJson(responceBody, TravelDetail.class);
		return details;
	}
	
	public NameValuePair addTrip(String title, String description) throws ClientProtocolException, IOException{
		List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
		bodyParams.add(new BasicNameValuePair("Title", title));
		bodyParams.add(new BasicNameValuePair("Description", description));
		
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		HttpResponse responce = this.client.Post("api/travels", bodyParams, null, headers);
		String message = getResponceMessage(responce.getEntity(), "TRIP_CREATE");
		int status = responce.getStatusLine().getStatusCode();
		return new BasicNameValuePair(String.valueOf(status), message);
	}
	
	private List<NameValuePair> getAuthorisationHeaders(){
		String token = UserPreferenceManager.getToken(this.context);
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Authorization", "Bearer " + token));
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

	public void updateLocation(double latitude, double longitude) {
		List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
		bodyParams.add(new BasicNameValuePair("longtitude", String.valueOf(longitude)));
		bodyParams.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
		
		List<NameValuePair> headers = this.getAuthorisationHeaders();
		try {
			this.client.Post("api/user/location", bodyParams, null, headers);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
