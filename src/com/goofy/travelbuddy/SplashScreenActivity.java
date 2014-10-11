package com.goofy.travelbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.goofy.travelbuddy.connection.RestApiClient;

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
    
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        	RestApiClient client = new RestApiClient("http://travelbuddy-1.apphb.com/");
        	String getResp = client.Get("/api/photos", null);
            return getResp;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            
       }
    }
}
