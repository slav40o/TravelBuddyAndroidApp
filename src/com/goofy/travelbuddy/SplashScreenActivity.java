package com.goofy.travelbuddy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.goofy.models.Photo;
import com.goofy.models.Place;
import com.goofy.models.Travel;
import com.goofy.travelbuddy.connection.ClientManager;
import com.goofy.travelbuddy.connection.PlacesSharedPreferencesManager;
import com.goofy.travelbuddy.connection.TravelsSharedPreferencesManager;
import com.goofy.travelbuddy.connection.UserPreferenceManager;
import com.goofy.travelbuddy.dao.PhotosDataSource;
import com.goofy.travelbuddy.dao.PhotosSQLite;
import com.goofy.travelbuddy.dao.PlacesDataSource;
import com.goofy.travelbuddy.dao.PlacesSQLiteHelper;
import com.goofy.travelbuddy.dao.PlacesTravelsSQLiteHelper;
import com.goofy.travelbuddy.dao.PlacesTravlesDataSource;
import com.goofy.travelbuddy.dao.TravelsDataSource;
import com.goofy.travelbuddy.dao.TravelsSQLiteHelper;
import com.goofy.travelbuddy.utils.ImageManager;
import com.goofy.travelbuddy.utils.LocationService;
import com.goofy.travelbuddy.utils.PhoneState;
import com.google.android.gms.internal.fl;

public class SplashScreenActivity extends Activity {
	private Context context;
	private boolean hammering = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        Intent intent = new Intent(SplashScreenActivity.this, LocationService.class); 	 	
        Log.d("SERVICE", "called"); 	 	
        startService(intent); 
//        if (PhoneState.isNetworkAvailable(this)) {
//        	Toast.makeText(context, "No internet connection.", Toast.LENGTH_LONG).show();
//        	Intent loginIntent = new Intent(context, LoginActivity.class);
//            startActivity(loginIntent);
//		} else 
        if(UserPreferenceManager.checkForLogin(context)){
    		new StartupTask().execute();
    	}
    	else if(UserPreferenceManager.checkForRegistration(context)){
    		Intent loginIntent = new Intent(context, LoginActivity.class);
            startActivity(loginIntent);
    	} else{
    		Toast.makeText(context, "No registration found.", Toast.LENGTH_LONG).show();
    		//hammering = true;
    		hammerDbs(context);
			Intent registerIntent = new Intent(context, RegisterActivity.class);
            startActivity(registerIntent);
    	}
    }
    
    private void hammerDbs(Context context){
    	Log.d("HAMMER", "STARTED");
    	SQLiteDatabase flash;
    	PlacesSQLiteHelper placesHammer = new PlacesSQLiteHelper(context);
		flash = placesHammer.getWritableDatabase();
		flash.execSQL("DROP TABLE IF EXISTS " + PlacesSQLiteHelper.TABLE_PLACES);
		flash.execSQL(placesHammer.DATABASE_CREATE);
		flash = placesHammer.getWritableDatabase();
		Log.d("PLACESDB_FLASH", placesHammer.DATABASE_CREATE);
		
    	PhotosSQLite photosHammer = new PhotosSQLite(context);
		flash = photosHammer.getWritableDatabase();
		flash.execSQL("DROP TABLE IF EXISTS " + PhotosSQLite.TABLE_PHOTOS);
		flash.execSQL(photosHammer.DATABASE_CREATE);
		flash = photosHammer.getWritableDatabase();
		Log.d("PHOTODB_FLASH", photosHammer.DATABASE_CREATE);
		
		TravelsSQLiteHelper travelsHammer = new TravelsSQLiteHelper(context);
		flash = travelsHammer.getWritableDatabase();
		flash.execSQL("DROP TABLE IF EXISTS " + TravelsSQLiteHelper.TABLE_TRAVELS);
		flash.execSQL(travelsHammer.DATABASE_CREATE); 
		flash = travelsHammer.getWritableDatabase();
		Log.d("TRAVELS_FLASH", travelsHammer.DATABASE_CREATE);
		
		PlacesTravelsSQLiteHelper placesTravelsHammer = new PlacesTravelsSQLiteHelper(context);
		flash = placesTravelsHammer.getWritableDatabase();
		flash.execSQL("DROP TABLE IF EXISTS " + PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS);
		flash.execSQL(placesTravelsHammer.DATABASE_CREATE);
		flash = placesTravelsHammer.getWritableDatabase();
		Log.d("PLACES_TRAVELS_FLASH", placesTravelsHammer.DATABASE_CREATE);
		
		Log.d("HAMMER", "DONE");
		
    }
    
    private class StartupTask extends AsyncTask<String, Void, NameValuePair> {
    	
        @Override
        protected NameValuePair doInBackground(String... urls) {
        	ClientManager manager = new ClientManager(context);
        	String name = UserPreferenceManager.getUsername(context);
    		String pass = UserPreferenceManager.getPassword(context);
    		
    		// FETCHING TOP PLACES -------------------------------
    		PlacesDataSource dataScource = new PlacesDataSource(context);
    		dataScource.open();
    		/*
    		SQLiteDatabase flash;
    		if (hammering) {
        		PhotosSQLite asdasd = new PhotosSQLite(context);
        		flash = asdasd.getWritableDatabase();
        		flash.execSQL(asdasd.DATABASE_CREATE);
        		Log.d("PHOTODB_FLASH", asdasd.DATABASE_CREATE);
			}
    		*/
    		PhotosDataSource data = new PhotosDataSource(context);
			data.open();
    		List<Place> top = manager.getTopPlaces();
    		ImageManager imageManager = null;
    		try {
				imageManager = new ImageManager(name, context);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
    		
    		Set<Integer> topPlacesIds = new HashSet<Integer>();
    		for (Place place : top) {
    			Place newPlace = dataScource.addOrReplacePlace(place);
    			Log.d("PLACE_IMPORT", newPlace.title + " imported!"+ " id "+ newPlace.getId());
    			topPlacesIds.add(place.getId());
    			List<Photo> topPhotos = manager.getPlacePhotos(place.getId());
    			//Log.d("TOPPHOTOS", "id " +topPhotos.get(0).getId() + " placeId "+topPhotos.get(0).getPlaceID()+" name " + topPhotos.get(0).getName());  			
    			try {
    				// Photo info here
					List<Photo> saved = imageManager.savePhotos(topPhotos, "TopPhotos", newPlace.getTitle());
					for (Photo photo : saved) {
						// TODO May need fixes
						photo.setPlaceID(place.getId());
						Log.d("PHOTO_to_SQL", "id " +photo.getId() + " placeId "+photo.getPlaceID());
						Photo asd = data.addOrReplacePhoto(photo);
					}
					Log.d("PHOTOS_SAVE_OK", "asdasdasdsadasd");
				} catch (IOException e) {
					Log.d("PHOTOS_EXCEPTION", "asdasdasdsadasd");
					e.printStackTrace();
				}
			}
    		data.close();
    		dataScource.close();
    		try {
				PlacesSharedPreferencesManager.setTopPlaces(context, topPlacesIds);
				Log.d("PHOTO_SHARED_PREFS", "ADDED: "+ topPlacesIds.toString());
			} catch (Exception e1) {
				Log.d("PHOTO_SHARED_PREFS", "FAILED");
				e1.printStackTrace();
			}
    		// FETCHING TOP PLACES ------ END ------------------
    		
    		// FETCHING PERSONAL TRAVELS
    		/*
    		if (hammering) {
    			TravelsSQLiteHelper hammer = new TravelsSQLiteHelper(context);
        		flash = hammer.getWritableDatabase();
        		flash.execSQL(hammer.DATABASE_CREATE);
        		Log.d("TRAVELS_FLASH", hammer.DATABASE_CREATE);
        		PlacesTravelsSQLiteHelper h2 = new PlacesTravelsSQLiteHelper(context);
        		flash = h2.getWritableDatabase();
        		flash.execSQL(h2.DATABASE_CREATE);
        		Log.d("PLACES_TRAVELS_FLASH", hammer.DATABASE_CREATE);
			}
    		*/
    		
    		
    		
    		TravelsDataSource travelsDataSource = new TravelsDataSource(context);
    		travelsDataSource.open();

    		List<Travel> personaTravels = manager.getPersonalTrips();
    		Set<Integer> personaTravelsIds = new HashSet<Integer>();
    	    for (Travel travel : personaTravels) {
    	    	//Log.d("TRAVELS_DATA", travel.endDate.toString());
    	    	Log.d("TRAVELS_DATA", "id: "+travel.getId()+ " title: "+ travel.getTitle()+" ");
				travelsDataSource.addOrReplaceTravel(travel);
				personaTravelsIds.add(travel.getId());
			}
    	    travelsDataSource.close();
    	    
    	    PlacesTravlesDataSource places_travels_ds = new PlacesTravlesDataSource(context);
    		places_travels_ds.open();
    		
    		for (Integer travelId : personaTravelsIds) {
    			ArrayList<Place> matchedPlaces = new ArrayList<Place>();
    			matchedPlaces = manager.getPlacesByTrip(travelId);
    			for (Place place : matchedPlaces) {
    				places_travels_ds.createTravelDetailByIds(place.getId(), travelId);
				}
    			Log.d("PLACES_TRAVELS_DATA", "travel id: "+travelId+ " places: "+ matchedPlaces.toString());
			}
    		
    	    places_travels_ds.close();
    	    
    	    try {
				TravelsSharedPreferencesManager.setTravels(context, personaTravelsIds);
				Log.d("TRAVELS_SHARED_PREFS", "ADDED: "+ personaTravelsIds.toString());
			} catch (Exception e1) {
				Log.d("TRAVELS_SHARED_PREFS", "FAILED");
				e1.printStackTrace();
			}
    	 // FETCHING PERSONAL TRAVELS ------ END ------------------
    		
    		NameValuePair responce = null;
			try {
				responce = manager.loginUser(name, pass);
			} catch (ClientProtocolException e) {
				responce = new BasicNameValuePair("500", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	return responce;
        }

        @Override
        protected void onPostExecute(NameValuePair responce) {
    		int status = Integer.parseInt(responce.getName());
    		
    		if (status == HttpStatus.SC_OK) {
    			//TO DO Load needed resources from server
    			Intent mainIntent = new Intent(context, MainActivity.class);
                startActivity(mainIntent);
                Toast.makeText(getBaseContext(), "Autologin", Toast.LENGTH_SHORT).show();
			}
    		else{
    			Toast.makeText(getBaseContext(), "Unable to login", Toast.LENGTH_SHORT).show();
    			Intent loginIntent = new Intent(context, LoginActivity.class);
                startActivity(loginIntent);
    		}
       }
    }
}