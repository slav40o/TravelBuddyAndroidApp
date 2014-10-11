package com.goofy.travelbuddy;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.goofy.travelbuddy.connection.ClientManager;

public class SplashScreenActivity extends Activity {
	private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        
        Thread logoTimer = new Thread() {
            public void run(){
                try {
                	// TO DO: Loading initial resources
                	new HttpAsyncTask().execute();
                    int logoTimer = 0;
                    while(logoTimer < 5000){
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                    
                    //TO DO: check if user is logged in and load Login screen or Main screen
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    startActivity(loginIntent);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally{
                    finish();
                }
            }
        };
         
        logoTimer.start();
    }
    
    private class HttpAsyncTask extends AsyncTask<String, Void, NameValuePair> {
        @Override
        protected NameValuePair doInBackground(String... urls) {
        	ClientManager manager = new ClientManager();
        	//NameValuePair status = manager.loginUser("slav@slav.com", "123a123");
        	NameValuePair status = manager.registerUser("slav@slav.com", "123a123", "123a123");
        	return status;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(NameValuePair result) {
            Toast.makeText(getBaseContext(), result.getName(), Toast.LENGTH_LONG).show();
            
       }
    }
}