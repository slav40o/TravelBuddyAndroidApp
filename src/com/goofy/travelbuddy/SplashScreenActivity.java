package com.goofy.travelbuddy;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;

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
//    		Intent loginIntent = new Intent(context, LoginActivity.class);
//            startActivity(loginIntent);
    		new StartupTask().execute();
    	} else{
    		Toast.makeText(context, "No registration found.", Toast.LENGTH_LONG).show();
			Intent registerIntent = new Intent(context, RegisterActivity.class);
            startActivity(registerIntent);
    	}
        //TO DO: check if user is logged in and load Login screen or Main screen
                    

    }
    
    private class StartupTask extends AsyncTask<String, Void, NameValuePair> {
    	
        @Override
        protected NameValuePair doInBackground(String... urls) {
        	ClientManager manager = new ClientManager(context);
        	String name = UserPreferenceManager.getUsername(context);
    		String pass = UserPreferenceManager.getPassword(context);
    		NameValuePair responce = manager.loginUser(name, pass);
    		int status = Integer.parseInt(responce.getName());
    		
    		if (status == HttpStatus.SC_OK) {
    			Intent mainIntent = new Intent(context, PlacesActivity.class);
                startActivity(mainIntent);
			}
    		else{
//    			Toast.makeText(context, responce.getValue(), Toast.LENGTH_LONG).show();
    			Intent loginIntent = new Intent(context, LoginActivity.class);
                startActivity(loginIntent);
    		}
        	
        	return responce;
        }
        @Override
		protected void onPreExecute() {
			
		}

        @Override
        protected void onPostExecute(NameValuePair result) {
            Toast.makeText(getBaseContext(), result.getName(), Toast.LENGTH_LONG).show();
       }
    }
}