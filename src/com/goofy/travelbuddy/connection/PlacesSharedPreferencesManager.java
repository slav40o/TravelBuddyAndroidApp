package com.goofy.travelbuddy.connection;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PlacesSharedPreferencesManager {
	public static final String TOPPLACES = "TopPlaces";
	public static final String FAVORITE_PLACES = "FavoritePlaces";
	public static final String PLACES_FILE = "PlacesFile";
	
	static Set<Integer> favoritePlaces;
	static Set<Integer> topPlaces;
	
	@Deprecated
	public static void addFavoritePlace(Context context, int placeId) throws Exception {
        if (null == favoritePlaces) {
            favoritePlaces = new HashSet<Integer>();
        }
        favoritePlaces.add(placeId);
        //add favorite place to preference
        SharedPreferences prefs = context.getSharedPreferences(PLACES_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString(FAVORITE_PLACES,  ObjectSerializer.serialize((Serializable) favoritePlaces));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }
	
	public static void setFavoritePlaces(Context context, Set<Integer> places) throws Exception {
        //save the favorite places set to preference
        SharedPreferences prefs = context.getSharedPreferences(PLACES_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString(FAVORITE_PLACES,  ObjectSerializer.serialize((Serializable) places));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }
	
	public static Set<Integer> getFavoritePlaces(Context context) throws Exception {
        Set<Integer> favPlaces = new HashSet<Integer>();
        
        SharedPreferences prefs = context.getSharedPreferences(PLACES_FILE, Context.MODE_PRIVATE);
        try {
        	favPlaces = (Set<Integer>) ObjectSerializer.deserialize(prefs.getString(FAVORITE_PLACES, ObjectSerializer.serialize(new HashSet<Integer>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favPlaces;
    }
	
	@Deprecated
	public static void addTopPlace(Context context, int placeId) throws Exception {
        if (null == topPlaces) {
        	topPlaces = new HashSet<Integer>();
        }
        topPlaces.add(placeId);

        //adds top place to preference
        SharedPreferences prefs = context.getSharedPreferences(PLACES_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString(TOPPLACES,  ObjectSerializer.serialize((Serializable) topPlaces));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }
	
	public static void setTopPlaces(Context context, Set<Integer> places) throws Exception {
       
        //save the places set to preference
        SharedPreferences prefs = context.getSharedPreferences(PLACES_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString(TOPPLACES,  ObjectSerializer.serialize((Serializable) places));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }
	
	public static Set<Integer> getTopPlaces(Context context) throws Exception {
		
        Set<Integer> topPlaces = new HashSet<Integer>();
        
        SharedPreferences prefs = context.getSharedPreferences(PLACES_FILE, Context.MODE_PRIVATE);

        try {
        	topPlaces = (Set<Integer>) ObjectSerializer.deserialize(prefs.getString(TOPPLACES, ObjectSerializer.serialize(new HashSet<Integer>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return topPlaces;
    }
	
}
