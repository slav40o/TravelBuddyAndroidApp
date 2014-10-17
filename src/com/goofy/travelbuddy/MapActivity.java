package com.goofy.travelbuddy;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.goofy.models.Place;
import com.goofy.travelbuddy.connection.ClientManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends BaseActivity{
	
	private GoogleMap mMap;
	private int placeId;
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment);
		Intent intent = getIntent();
		context = this;
		
		placeId = intent.getIntExtra("PLACEID", -1);
		
		// The GoogleMap instance underlying the GoogleMapFragment defined in main.xml 
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		new LoadMapInfo().execute(placeId);
		
	}
	
	private class LoadMapInfo extends AsyncTask<Integer, Void, String>{
		private Place place;
		
		@Override
		protected String doInBackground(Integer... params) {
			ClientManager manager = new ClientManager(context);
			place = manager.getPlaceDetail(params[0]);
			return place.title;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mMap != null) {

				// Set the map position
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatitude(),
						place.getLongtitude()), 0));

				// Add a marker on the Place,
				mMap.addMarker(new MarkerOptions().position(
						new LatLng(place.getLatitude(), place.getLongtitude())).title(result));
				
			}
		}
	}
}
