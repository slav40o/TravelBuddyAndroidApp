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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.goofy.models.Location;
import com.goofy.models.Photo;
import com.goofy.models.Place;
import com.goofy.models.PlaceDetail;
import com.goofy.travelbuddy.connection.PlacesSharedPreferencesManager;
import com.goofy.travelbuddy.dao.PhotosDataSource;
import com.goofy.travelbuddy.dao.PlacesDataSource;
import com.goofy.travelbuddy.dao.PlacesTravelsSQLiteHelper;
import com.goofy.travelbuddy.dao.PlacesTravlesDataSource;
import com.goofy.travelbuddy.dao.VisitorsDataSource;

public class PlacesFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener{
	
	List<PlaceDetail> placeDetails;
	ListView placesListViews;
	PlacesListViewAdapter placesAdapter;
	private PlacesDataSource placesDatasource;
	private PhotosDataSource photosDatasource;
	private VisitorsDataSource visitorsDataSource;
	private PlacesTravlesDataSource ptDataSource;
	Context ctx;
	boolean isTravel = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int travelId = 0;
		String travelTitle;
		
		if(savedInstanceState != null){
			Log.d("PLACES_FRGAMENT", " savedInstanceState not null ");
		} else {
			Log.d("PLACES_FRGAMENT", " savedInstanceState IS NULL ");
		}
			if (getActivity().getIntent().getExtras() != null) {
				isTravel = getActivity().getIntent().getExtras().getBoolean("ISTRAVEL");
			    travelId = getActivity().getIntent().getExtras().getInt("TRAVELID", 0);
			    travelTitle = getActivity().getIntent().getExtras().getString("TRAVEL_TITLE");
			}
			
		    
		View view = inflater.inflate(R.layout.places_fragment, container, false);

		this.placeDetails = new ArrayList<PlaceDetail>();
		this.ctx = view.getContext();
		
		// TODO Some fake data - should be replaced with data from the data source
		if (isTravel) {
			placesByTravelId(travelId);
		}else {
			//allPlaces();
			 addFakePlaces();
		}
		
		Log.d("FAKE", "Adding fake data" );
		
		placesListViews = (ListView) view.findViewById(R.id.placeslistview);
		placesAdapter = new PlacesListViewAdapter(view.getContext(), R.layout.places_list_item, this.placeDetails);
		//placesAdapter = new PlacesListViewAdapter(view.getContext(), R.layout.place_list_item);
		
		placesListViews.setAdapter(placesAdapter);
		
		placesListViews.setOnItemClickListener(this);
		placesListViews.setOnItemLongClickListener(this);
		return view;
	}
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	       
	    }
	
	private void addFakePlaces() {
		/*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	 
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    byte[] bitmapdata = stream.toByteArray();
		
		ArrayList<byte[]> photos = new ArrayList<byte[]>();
		photos.add(bitmapdata);*/
		
		ArrayList<Integer> photoIds = new ArrayList<Integer>();
		photoIds.add(0);
		
		
		ArrayList<String> visitors = new ArrayList<String>();
		visitors.add("Pesho");
		visitors.add("Gosho");
		visitors.add("Joro");
		
		placeDetails.add(new PlaceDetail(0, "Fake", "Some fake place", "JVM Land", new Location(0, 0), null, photoIds, visitors));
		placeDetails.add(new PlaceDetail(1, "Fake1", "Some fake place1", "No Land", new Location(0, 0), null, photoIds, visitors));
		placeDetails.add(new PlaceDetail(2, "Cool", "Some fake place2", "LapLand", new Location(0, 0), null, photoIds, visitors));
		placeDetails.add(new PlaceDetail(3, "Fake3", "Some fake place3", "JVM Land", new Location(0, 0), null, photoIds, visitors));
		placeDetails.add(new PlaceDetail(4, "Some Where", "Some fake place4", "JVM Land", new Location(0, 0), null, photoIds, visitors));
		placeDetails.add(new PlaceDetail(5, "Fake5", "Some fake place5", "JVM Land", new Location(0, 0), null, photoIds, visitors));
		placeDetails.add(new PlaceDetail(6, "Test one", "Some new fake place", "Newerland", new Location(0, 0), null, photoIds, visitors));
		placeDetails.add(new PlaceDetail(7, "Another one", "Some new fake place", "Newerland", new Location(0, 0), null, photoIds, visitors));
		placeDetails.add(new PlaceDetail(8, "Fake one", "Some new fake place", "Newerland", new Location(0, 0), null, photoIds, visitors));
		}
	
	private void allPlaces(){
		placesDatasource = new PlacesDataSource(ctx);
		placesDatasource.open();
		List<Place> places = placesDatasource.getAllPlaces();
		placesDatasource.close();
		
		photosDatasource = new PhotosDataSource(ctx);
		photosDatasource.open();
		visitorsDataSource = new VisitorsDataSource(ctx);
		visitorsDataSource.open();
		
		int index = 0;
		for (Place place : places) {
			ArrayList<Integer> photoIds = (ArrayList<Integer>) photosDatasource.getPhotoIdsByPlaceId(place.getId());
			ArrayList<String> visitors = (ArrayList<String>) visitorsDataSource.getVisitorsByPlaceId(place.getId());
			placeDetails.add(new PlaceDetail(index, place.getTitle(), place.getDescription(), place.getCountry(), place.getLocation(), place.getLastVisited(), photoIds, visitors));
			index++;
		}
		photosDatasource.close();
		visitorsDataSource.close();
	}
	
	private void placesByTravelId(int travelId){
		placesDatasource = new PlacesDataSource(ctx);
		placesDatasource.open();
		ptDataSource = new PlacesTravlesDataSource(ctx);
		ptDataSource.open();
		photosDatasource = new PhotosDataSource(ctx);
		photosDatasource.open();
		visitorsDataSource = new VisitorsDataSource(ctx);
		visitorsDataSource.open();
		
		List<Integer> placesIds = ptDataSource.getPhotoIdsByTravelId(travelId);
		int index = 0;
		for (Integer placeId : placesIds) {
			Place place = placesDatasource.getPlaceById(placeId);
			ArrayList<Integer> photoIds = (ArrayList<Integer>) photosDatasource.getPhotoIdsByPlaceId(placeId);
			ArrayList<String> visitors = (ArrayList<String>) visitorsDataSource.getVisitorsByPlaceId(placeId);
			placeDetails.add(new PlaceDetail(index, place.getTitle(), place.getDescription(), place.getCountry(), place.getLocation(), place.getLastVisited(), photoIds, visitors));
			index++;
		}
		placesDatasource.close();
		ptDataSource.close();
		photosDatasource.close();
		visitorsDataSource.close();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(view.getContext(), "Clicked on " + placeDetails.get(position).getTitle(), Toast.LENGTH_LONG).show();
		Intent placeDetailsIntent = new Intent(ctx.getApplicationContext(), PlaceDetailActivity.class);
		placeDetailsIntent.putExtra("PLACEID", placeDetails.get(position).getId());
		placeDetailsIntent.putExtra("PLACE_TITLE", placeDetails.get(position).getTitle());
		placeDetailsIntent.putStringArrayListExtra("VISITORS", placeDetails.get(position).getVisitors());
		startActivity(placeDetailsIntent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		 // TODO Add to favorites
		if (isTravel) {
			Toast.makeText(view.getContext(),"Adding "+placeDetails.get(position).getTitle() + " to favorites", Toast.LENGTH_LONG).show();
		}else {
			Toast.makeText(view.getContext(),"Adding "+placeDetails.get(position).getTitle() + " to favorite places", Toast.LENGTH_LONG).show();
		}
		return true;
	}

}
