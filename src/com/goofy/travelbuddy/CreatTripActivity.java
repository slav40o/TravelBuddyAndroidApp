package com.goofy.travelbuddy;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.goofy.travelbuddy.connection.ClientManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreatTripActivity extends Activity implements View.OnClickListener{
	private Button createBtn;
	private Button cancelBtn;
	private EditText nameInput;
	private EditText descriptionInput;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_trip_activity);
		this.createBtn = (Button)findViewById(R.id.btn_create_trip);
		createBtn.setOnClickListener(this);
		this.cancelBtn = (Button)findViewById(R.id.btn_cancel_create_trip);
		cancelBtn.setOnClickListener(this);
		this.descriptionInput = (EditText)findViewById(R.id.et_trip_description);
		this.nameInput = (EditText)findViewById(R.id.et_trip_name);
		this.context = this;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == this.createBtn.getId()) {
			String description = this.descriptionInput.getText().toString();
			String name = this.nameInput.getText().toString();
			boolean validState = true;
			
			if (description == null || description == "") {
				Toast.makeText(getBaseContext(), "Plese enter a description", Toast.LENGTH_LONG).show();
				validState = false;
			}
			else if(name == null || name == ""){
				Toast.makeText(getBaseContext(), "Plese enter a name", Toast.LENGTH_LONG).show();
				validState = false;
			}
			
			if (validState) {
				new CreateTripTask().execute(description, name);
			}
		}
		else if(v.getId() == this.cancelBtn.getId()){
			Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
		}
	}

	private class CreateTripTask extends AsyncTask<String, Void, NameValuePair> {
		private ProgressDialog dialog;
		
		@Override
		protected NameValuePair doInBackground(String... data) {
			String name = data[1];
			String description = data[0];
			ClientManager manager = new ClientManager(context);
    		NameValuePair responce = null;
			try {
				responce = manager.addTrip(name, description);
			} catch (ClientProtocolException e) {
				responce = new BasicNameValuePair("500", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	return responce;
		}

		@Override
		protected void onPostExecute(NameValuePair result) {
			super.onPostExecute(result);
			dialog.hide();  
			int status = Integer.parseInt(result.getName());	
			if (status == HttpStatus.SC_OK) {
				Toast.makeText(getBaseContext(), "Trip created", Toast.LENGTH_LONG).show();
				
				Intent travelDetailIntent = new Intent(context, TripsDetailsActivity.class);
				int id = extractId(result.getValue());
				travelDetailIntent.putExtra("travelId", id);
				startActivity(travelDetailIntent);
			}
			else{
				Toast.makeText(getBaseContext(), result.getValue(), Toast.LENGTH_LONG).show();
			}
		}
		
		private int extractId(String message){
			int startIdx = message.indexOf(":") + 1;
			int endIdx = message.indexOf(",");
			int id = Integer.parseInt(message.substring(startIdx, endIdx));
			return id;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CreatTripActivity.this);
			dialog.setMessage("Preparing your trip in...");
			dialog.show();  
		}
		
	}
}
