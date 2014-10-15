package com.goofy.travelbuddy;

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

		// The GoogleMap instance underlying the GoogleMapFragment defined in main.xml 
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		if (mMap != null) {

			// Set the map position
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29,
					-88), 0));

			// Add a marker on Washington, DC, USA
			mMap.addMarker(new MarkerOptions().position(
					new LatLng(38.8895, -77.0352)).title(
					getString(R.string.pernik)));
			
			// Add a marker on Mexico City, Mexico
			mMap.addMarker(new MarkerOptions().position(
					new LatLng(19.13, -99.4)).title(
					getString(R.string.telerik)));
		}
	}
}
