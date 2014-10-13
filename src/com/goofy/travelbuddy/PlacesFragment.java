package com.goofy.travelbuddy;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.goofy.models.Location;
import com.goofy.models.Photo;
import com.goofy.models.Place;
import com.goofy.models.PlaceDetail;
import com.goofy.travelbuddy.dao.PhotosDataSource;
import com.goofy.travelbuddy.dao.PlacesDataSource;

public class PlacesFragment extends Fragment implements
OnItemClickListener{
	
	List<PlaceDetail> placeDetails;
	ListView placesListViews;
	PlacesListViewAdapter placesAdapter;
	private PlacesDataSource datasource;
	private PhotosDataSource photosDatasource;
	Context ctx;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.places_list, container, false);

		this.placeDetails = new ArrayList<PlaceDetail>();
		this.ctx = view.getContext();
		
		// TODO Some fake data - should be replaced with data from the data source
	    // addPlaces();
		addFakePlaces();
		Log.d("FAKE", "Adding fake data" );
		
		placesListViews = (ListView) view.findViewById(R.id.placeslistview);
		placesAdapter = new PlacesListViewAdapter(view.getContext(), R.layout.place_list_item, this.placeDetails);
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
		
		placeDetails.add(new PlaceDetail(0, "Fake", "Some fake place", "JVM Land", new Location(0, 0), null, photos, visitors));
		placeDetails.add(new PlaceDetail(1, "Fake1", "Some fake place1", "No Land", new Location(0, 0), null, photos, visitors));
		placeDetails.add(new PlaceDetail(2, "Cool", "Some fake place2", "LapLand", new Location(0, 0), null, photos, visitors));
		placeDetails.add(new PlaceDetail(3, "Fake3", "Some fake place3", "JVM Land", new Location(0, 0), null, photos, visitors));
		placeDetails.add(new PlaceDetail(4, "Some Where", "Some fake place4", "JVM Land", new Location(0, 0), null, photos, visitors));
		placeDetails.add(new PlaceDetail(5, "Fake5", "Some fake place5", "JVM Land", new Location(0, 0), null, photos, visitors));
		placeDetails.add(new PlaceDetail(6, "Test one", "Some new fake place", "Newerland", new Location(0, 0), null, photos, visitors));
		placeDetails.add(new PlaceDetail(7, "Another one", "Some new fake place", "Newerland", new Location(0, 0), null, photos, visitors));
		placeDetails.add(new PlaceDetail(8, "Fake one", "Some new fake place", "Newerland", new Location(0, 0), null, photos, visitors));
		}
	
	private void addPlaces(){
		datasource = new PlacesDataSource(ctx);
		List<Place> places = datasource.getAllPlaces();
		photosDatasource = new PhotosDataSource(ctx);
		
		int index = 0;
		for (Place place : places) {
			Photo photo =  photosDatasource.getPhotosByPlaceId(place.getId()).get(0);
			// TODO Add visitors to PlaceDetail !!!
			ArrayList<String> visitors = new ArrayList<String>();
			visitors.add("Pesho");
			
			placeDetails.add(new PlaceDetail(index, place.getTitle(), place.getDescription(), place.getCountry(), place.getLocation(), place.getLastVisited(), new ArrayList<byte[]>(photo.getId()), visitors));
			index++;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Toast.makeText(view.getContext(), "Clicked on " + placeDetails.get(position).getTitle(), Toast.LENGTH_LONG).show();
		Intent placeDetailsIntent = new Intent(ctx.getApplicationContext(), PlaceDetailActivity.class);
		placeDetailsIntent.putExtra("PLACEID", placeDetails.get(position).getId());
		startActivity(placeDetailsIntent);
	}
}
