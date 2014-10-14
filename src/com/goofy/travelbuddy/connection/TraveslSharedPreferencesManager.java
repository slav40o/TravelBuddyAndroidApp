package com.goofy.travelbuddy.connection;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TraveslSharedPreferencesManager {
	public final String TRAVELS = "Travels";
	public final String USER_TRAVELS_FILE = "UserTravelsFile";
	Set<Integer> userTravels;
	
	public void addTravel(Context context, int travelId) throws Exception {
        if (null == userTravels) {
        	userTravels = new HashSet<Integer>();
        }
        userTravels.add(travelId);
        //add favorite place to preference
        SharedPreferences prefs = context.getSharedPreferences(USER_TRAVELS_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString(TRAVELS,  ObjectSerializer.serialize((Serializable) userTravels));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }
	
	public void setTravels(Context context, Set<Integer> travels) throws Exception {
        //save the favorite places set to preference
        SharedPreferences prefs = context.getSharedPreferences(USER_TRAVELS_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString(TRAVELS,  ObjectSerializer.serialize((Serializable) travels));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }
	
	public Set<Integer> getTravels(Context context) throws Exception {
        Set<Integer> userTravels = new HashSet<Integer>();
        
        SharedPreferences prefs = context.getSharedPreferences(USER_TRAVELS_FILE, Context.MODE_PRIVATE);
        try {
        	userTravels = (Set<Integer>) ObjectSerializer.deserialize(prefs.getString(TRAVELS, ObjectSerializer.serialize(new HashSet<Integer>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userTravels;
    }	
}
