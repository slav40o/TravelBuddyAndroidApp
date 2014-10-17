package com.goofy.travelbuddy;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import android.widget.TextView;
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
	boolean isFavourites = false;
	int travelId = 0;
	String travelTitle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.placeDetails = new ArrayList<PlaceDetail>();

			if (getActivity().getIntent().getExtras() != null) {
				if (getActivity().getIntent().getExtras().containsKey("FAVOURITES")) {
					isFavourites = true;
				}else {
					isTravel = true; //getActivity().getIntent().getExtras().getBoolean("ISTRAVEL");
				    travelId = getActivity().getIntent().getExtras().getInt("TRAVELID", 0);
				    travelTitle = getActivity().getIntent().getExtras().getString("TRAVEL_TITLE");
				    Log.d("PLACES_FRAGMENT", "EXTRA: travelId="+ travelId + " title="+travelTitle );
				}
			}
			
		    
		View view = inflater.inflate(R.layout.places_fragment, container, false);

		this.ctx = view.getContext();
		
		TextView placesListTitle = (TextView) view.findViewById(R.id.placeslisttitle);
		if (isTravel) {
			placesListTitle.setText(travelTitle);
			placesByTravelId(travelId);
		}else if (isFavourites) {
			addFavouritePlaces();
			placesListTitle.setText("My Favorite Places");
		}else {
			allPlaces();
		}
		
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
	
	private ArrayList<String> getFakeVisitors(){
		ArrayList<String> visitors = new ArrayList<String>();
		visitors.add("Pesho");
		visitors.add("Gosho");
		visitors.add("Joro");
		return visitors;
	}
	 
	private void addFavouritePlaces() {
		placesDatasource = new PlacesDataSource(ctx);
		placesDatasource.open();
		photosDatasource = new PhotosDataSource(ctx);
		photosDatasource.open();
		Set<Integer> placesIds = new HashSet<Integer>();
		try {
			placesIds = PlacesSharedPreferencesManager.getFavoritePlaces(ctx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		visitorsDataSource = new VisitorsDataSource(ctx);
		visitorsDataSource.open();*/
		Log.d("FETCHING_FAVOURITE_PLCES", " places Ids: " + placesIds.toString() );
		for (Integer placeId : placesIds) {
			Place place = placesDatasource.getPlaceById(placeId);
			ArrayList<Integer> photoIds = (ArrayList<Integer>) photosDatasource.getPhotoIdsByPlaceId(placeId);
		// 	ArrayList<String> visitors = (ArrayList<String>) visitorsDataSource.getVisitorsByPlaceId(placeId);
			placeDetails.add(new PlaceDetail(place.getId(), place.getTitle(), place.getDescription(), place.getCountry(), place.getLocation(), place.getLastVisited(), photoIds, getFakeVisitors()));
		}
		//visitorsDataSource.close();
		placesDatasource.close();
		photosDatasource.close();
	}
	
	private void allPlaces(){
		placesDatasource = new PlacesDataSource(ctx);
		placesDatasource.open();
		List<Place> places = placesDatasource.getAllPlaces();
		placesDatasource.close();
		
		photosDatasource = new PhotosDataSource(ctx);
		photosDatasource.open();
		/*
		visitorsDataSource = new VisitorsDataSource(ctx);
		visitorsDataSource.open();*/	
		int index = 0;
		for (Place place : places) {
			ArrayList<Integer> photoIds = new ArrayList<Integer>();
			//photoIds.add(0);
			photoIds = (ArrayList<Integer>) photosDatasource.getPhotoIdsByPlaceId(place.getId());
			Log.d("PHOTOIDs",photoIds.toString()+" for placeId " + place.getId() );
			//ArrayList<String> visitors = (ArrayList<String>) visitorsDataSource.getVisitorsByPlaceId(place.getId());
			placeDetails.add(new PlaceDetail(place.getId(), place.getTitle(), place.getDescription(), place.getCountry(), place.getLocation(), place.getLastVisited(), photoIds, getFakeVisitors()));
			index++;
		}
		Log.d("PLACES_COUNT", String.valueOf(index));
		photosDatasource.close();
		//visitorsDataSource.close();
	}
	
	private void placesByTravelId(int travelId){
		placesDatasource = new PlacesDataSource(ctx);
		placesDatasource.open();
		ptDataSource = new PlacesTravlesDataSource(ctx);
		ptDataSource.open();
		photosDatasource = new PhotosDataSource(ctx);
		photosDatasource.open();
	//	visitorsDataSource = new VisitorsDataSource(ctx);
	//	visitorsDataSource.open();		
		Set<Integer> placesIds = ptDataSource.getPlacesIdsByTravelId(travelId);
		Log.d("FETCHING_TRAVLES_PLCES", "travelId: " + travelId+ " places Ids: " + placesIds.toString() );
		int index = 0;
		for (Integer placeId : placesIds) {
			Place place = placesDatasource.getPlaceById(placeId);
			ArrayList<Integer> photoIds = (ArrayList<Integer>) photosDatasource.getPhotoIdsByPlaceId(placeId);
		//	ArrayList<String> visitors = (ArrayList<String>) visitorsDataSource.getVisitorsByPlaceId(placeId);
			placeDetails.add(new PlaceDetail(place.getId(), place.getTitle(), place.getDescription(), place.getCountry(), place.getLocation(), place.getLastVisited(), photoIds, getFakeVisitors()));
			index++;
		}
		placesDatasource.close();
		ptDataSource.close();
		photosDatasource.close();
		//visitorsDataSource.close();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//Toast.makeText(view.getContext(), "Clicked on " + placeDetails.get(position).getTitle()+ " id:"+placeDetails.get(position).getId(), Toast.LENGTH_LONG).show();
		Intent placeDetailsIntent = new Intent(ctx.getApplicationContext(), PlaceDetailActivity.class);
		placeDetailsIntent.putExtra("PLACEID", placeDetails.get(position).getId());
		placeDetailsIntent.putExtra("PLACE_TITLE", placeDetails.get(position).getTitle());
		placeDetailsIntent.putStringArrayListExtra("VISITORS", placeDetails.get(position).getVisitors());
		if (isTravel) {
			placeDetailsIntent.putExtra("TRAVELID", travelId);
			placeDetailsIntent.putExtra("TRAVEL_TITLE", travelTitle);
			Log.d("PLACESLISTFRAGMENT", "EXTRA: TRAVLEID: " + travelId + " TRAVEL_TITLE: " + travelTitle );
			
		}
		startActivity(placeDetailsIntent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		 // TODO Add to favorites or something else
		if (isTravel) {
	//		Toast.makeText(view.getContext(),"Adding "+placeDetails.get(position).getTitle() + " to favorites", Toast.LENGTH_LONG).show();
		}else {
	//		Toast.makeText(view.getContext(),"Adding "+placeDetails.get(position).getTitle() + " to favorite places", Toast.LENGTH_LONG).show();
		}
		return true;
	}

}
