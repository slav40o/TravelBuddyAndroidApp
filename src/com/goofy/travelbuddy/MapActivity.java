package com.goofy.travelbuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends BaseActivity{
	
	private GoogleMap mMap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment);
		Intent intent = getIntent();
		String title = intent.getStringExtra("TITLE");
		double longtitude = intent.getDoubleExtra("LONGTITUDE", 0);
		double latitude = intent.getDoubleExtra("LATITUDE", 0);
		
		// The GoogleMap instance underlying the GoogleMapFragment defined in main.xml 
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		if (mMap != null) {

			// Set the map position
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
					longtitude), 0));

			// Add a marker on the Place,
			mMap.addMarker(new MarkerOptions().position(
					new LatLng(latitude, longtitude)).title(title));
			
		}
	}
}
