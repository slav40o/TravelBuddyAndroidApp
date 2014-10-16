package com.goofy.travelbuddy;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.goofy.models.Place;
import com.goofy.travelbuddy.connection.ClientManager;
import com.goofy.travelbuddy.utils.LocationGetter;

public class CreatePlaceActivity extends BaseActivity implements View.OnClickListener{
	private Button createBtn;
	private Button cancelBtn;
	private EditText nameInput;
	private EditText descriptionInput;
	private EditText countryInput;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_trip_activity);
		this.createBtn = (Button)findViewById(R.id.btn_create_place);
		createBtn.setOnClickListener(this);
		this.cancelBtn = (Button)findViewById(R.id.btn_cancel_create_place);
		cancelBtn.setOnClickListener(this);
		this.descriptionInput = (EditText)findViewById(R.id.et_place_description);
		this.nameInput = (EditText)findViewById(R.id.et_place_title_input);
		this.countryInput = (EditText)findViewById(R.id.et_country_input);
		this.context = this;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == this.createBtn.getId()) {
			String description = this.descriptionInput.getText().toString();
			String name = this.nameInput.getText().toString();
			String country = this.countryInput.getText().toString();
			
			boolean validState = true;
			
			if (description == null || description == "") {
				Toast.makeText(getBaseContext(), "Plese enter a description", Toast.LENGTH_LONG).show();
				validState = false;
			}
			else if(name == null || name == ""){
				Toast.makeText(getBaseContext(), "Plese enter a name", Toast.LENGTH_LONG).show();
				validState = false;
			}
			else if(country == null || country == ""){
				Toast.makeText(getBaseContext(), "Plese enter a country", Toast.LENGTH_LONG).show();
				validState = false;
			}
			
			if (validState) {
				new CreatePlaceTask().execute(description, name);
			}
		}
		else if(v.getId() == this.cancelBtn.getId()){
			Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
		}
	}
	
	private class CreatePlaceTask extends AsyncTask<String, Void, NameValuePair> {
		private ProgressDialog dialog;
		
		@Override
		protected NameValuePair doInBackground(String... data) {
			String name = data[1];
			String country = data[2];
			String description = data[0];
			Location loc = LocationGetter.getBestLocation(context);
			com.goofy.models.Location modelLoc =
					new com.goofy.models.Location(loc.getLatitude(), loc.getLongitude());
			Place place = new Place(0, name, description, country, modelLoc);
			ClientManager manager = new ClientManager(context);
    		NameValuePair responce = null;
			try {
				responce = manager.addPlace(place);
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
				Toast.makeText(getBaseContext(), "Place created", Toast.LENGTH_LONG).show();
				Intent travelDetailIntent = new Intent(context, TripsDetailsActivity.class);
				int id  = getIntent().getIntExtra("ID", 0);
				travelDetailIntent.putExtra("ID", id);
				startActivity(travelDetailIntent);
			}
			else{
				Toast.makeText(getBaseContext(), result.getValue(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CreatePlaceActivity.this);
			dialog.setMessage("Adding the place...");
			dialog.show();  
		}
	}
}
