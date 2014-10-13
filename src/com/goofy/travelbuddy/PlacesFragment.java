package com.goofy.travelbuddy;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.goofy.models.Location;
import com.goofy.models.PlaceDetail;
import com.goofy.travelbuddy.dao.PlacesDataSource;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class PlacesFragment extends Fragment implements
OnItemClickListener{
	
	List<PlaceDetail> places;
	ListView placesListViews;
	PlacesListViewAdapter placesAdapter;
	private PlacesDataSource datasource;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.places_list, container, false);

		this.places = new ArrayList<PlaceDetail>();
		
		// TODO Some fake data - should be replaced with data from the data source
		addFakePlaces();
		Log.d("FAKE", "Adding fake data" );
		
		placesListViews = (ListView) view.findViewById(R.id.placeslistview);
		placesAdapter = new PlacesListViewAdapter(view.getContext(), R.layout.place_list_item, this.places);
		//placesAdapter = new PlacesListViewAdapter(view.getContext(), R.layout.place_list_item);
		
		placesListViews.setAdapter(placesAdapter);
		placesListViews.setOnItemClickListener(this);
		
		return view;
	}
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	       
	    }
	
	private void addFakePlaces() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	 
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    byte[] bitmapdata = stream.toByteArray();
		
		ArrayList<byte[]> photos = new ArrayList<byte[]>();
		photos.add(bitmapdata);
		ArrayList<String> visitors = new ArrayList<String>();
		visitors.add("Pesho");
		
		places.add(new PlaceDetail(0, "Fake", "Some fake place", "JVM Land", new Location(0, 0), null, photos, visitors));
		places.add(new PlaceDetail(0, "Fake1", "Some fake place1", "No Land", new Location(0, 0), null, photos, visitors));
		places.add(new PlaceDetail(0, "Cool", "Some fake place2", "LapLand", new Location(0, 0), null, photos, visitors));
		places.add(new PlaceDetail(0, "Fake3", "Some fake place3", "JVM Land", new Location(0, 0), null, photos, visitors));
		places.add(new PlaceDetail(0, "Some Where", "Some fake place4", "JVM Land", new Location(0, 0), null, photos, visitors));
		places.add(new PlaceDetail(0, "Fake5", "Some fake place5", "JVM Land", new Location(0, 0), null, photos, visitors));
		places.add(new PlaceDetail(0, "Test one", "Some new fake place", "Newerland", new Location(0, 0), null, photos, visitors));
		places.add(new PlaceDetail(0, "Another one", "Some new fake place", "Newerland", new Location(0, 0), null, photos, visitors));
		places.add(new PlaceDetail(0, "Fake one", "Some new fake place", "Newerland", new Location(0, 0), null, photos, visitors));
		}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Toast.makeText(view.getContext(), "Clicked on " + places.get(position).getTitle(), Toast.LENGTH_LONG).show();
	}
}
