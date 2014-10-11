package com.goofy.travelbuddy.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
import android.content.SharedPreferences;
import android.util.Log;

import com.goofy.travelbuddy.connection.requestmodels.UserLoginInfo;
import com.google.gson.Gson;

public class ClientManager {
	private final String Server_Url = "http://travelbuddy-1.apphb.com/";
	private final String User_Info_File = "UserData";
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
				saveToken(responce, password);
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
			try {
				saveUserData(userName, password);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		
		return new BasicNameValuePair(String.valueOf(status), message);
	}
	
	private void saveUserData(String userName, String password){
		SharedPreferences userPref = context.getSharedPreferences(User_Info_File, 0);
		SharedPreferences.Editor editor = userPref.edit();
		editor.putString("username", userName);
		editor.putString("password", password);
		editor.putBoolean("isLogged", false);
		editor.commit();
	}
	
	private void saveToken(HttpResponse responce, String password) throws IllegalStateException, IOException {
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

	private String getResponceMessage(HttpEntity getResponseEntity) {
		String message = "";
		try {
			message = EntityUtils.toString(getResponseEntity);
			Log.d("ERROR_MESSAGE", message);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return message;
	}
}
