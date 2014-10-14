package com.goofy.travelbuddy;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.goofy.travelbuddy.connection.ClientManager;
import com.goofy.travelbuddy.connection.UserPreferenceManager;

public class SplashScreenActivity extends Activity {
	private Context context;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        
    	if(UserPreferenceManager.checkForLogin(context)){
    		new StartupTask().execute();
    	}
    	else if(UserPreferenceManager.checkForRegistration(context)){
    		Intent loginIntent = new Intent(context, LoginActivity.class);
            startActivity(loginIntent);
    	} else{
    		Toast.makeText(context, "No registration found.", Toast.LENGTH_LONG).show();
			Intent registerIntent = new Intent(context, RegisterActivity.class);
            startActivity(registerIntent);
    	}
    }
    
    private class StartupTask extends AsyncTask<String, Void, NameValuePair> {
    	
        @Override
        protected NameValuePair doInBackground(String... urls) {
        	ClientManager manager = new ClientManager(context);
        	String name = UserPreferenceManager.getUsername(context);
    		String pass = UserPreferenceManager.getPassword(context);
    		NameValuePair responce = null;
			try {
				responce = manager.loginUser(name, pass);
			} catch (ClientProtocolException e) {
				responce = new BasicNameValuePair("500", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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