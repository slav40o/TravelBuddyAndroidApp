package com.goofy.travelbuddy.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpResponse;

import com.goofy.travelbuddy.connection.requestmodels.UserLoginInfo;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferenceManager {
	private static final String User_Info_File = "";
	
	public static void saveUserData(String userName, String password, Context context){
		SharedPreferences userPref = context.getSharedPreferences(User_Info_File, 0);
		SharedPreferences.Editor editor = userPref.edit();
		editor.putString("username", userName);
		editor.putString("password", password);
		editor.putBoolean("isLogged", false);
		editor.commit();
	}
	
	public static void saveLoginData(HttpResponse responce, String password, Context context) 
			throws IllegalStateException, IOException{
		Gson gson = new Gson();
		InputStream responceContent = responce.getEntity().getContent();
        Reader reader = new InputStreamReader(responceContent);
		UserLoginInfo userInfo = gson.fromJson(reader, UserLoginInfo.class);
		SharedPreferences userPref = context.getSharedPreferences(User_Info_File, 0);
		SharedPreferences.Editor editor = userPref.edit();
		editor.putString("username", userInfo.username);
		editor.putString("password", password);
		editor.putString("token", userInfo.accessToken);
		editor.putString("expiration", userInfo.expires);
		editor.putBoolean("isLogged", true);
		editor.commit();
	}
	
	public static boolean checkForLogin(Context context){
		SharedPreferences userPref = context.getSharedPreferences(User_Info_File, 0);
		if (userPref.contains("token")) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean checkForRegistration(Context context){
		SharedPreferences userPref = context.getSharedPreferences(User_Info_File, 0);
		boolean user = userPref.contains("username");
		boolean pass = userPref.contains("password");
		boolean isRegistered  =  user && pass;
		return isRegistered;
	}
	
	public static String getToken(Context context){
		SharedPreferences userPref = context.getSharedPreferences(User_Info_File, 0);
		String token = userPref.getString("token", "");
		return token;
	}
	
	public static String getUsername(Context context){
		SharedPreferences userPref = context.getSharedPreferences(User_Info_File, 0);
		String token = userPref.getString("username", "");
		return token;
	}
	
	public static String getPassword(Context context){
		SharedPreferences userPref = context.getSharedPreferences(User_Info_File, 0);
		String token = userPref.getString("password", "");
		return token;
	}
}
