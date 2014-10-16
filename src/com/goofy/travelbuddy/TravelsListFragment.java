package com.goofy.travelbuddy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.goofy.models.Travel;
import com.goofy.models.TravelDetail;
import com.goofy.travelbuddy.dao.TravelsDataSource;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TravelsListFragment extends Fragment  implements OnItemClickListener {
	
	
	List<TravelDetail> travelsDetails;
	ListView travelsListView;
	TravelsListViewAdapter travelsAdapter;
	Context context;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.travels_listview_fragment,
				container, false);	
			context =  rootView.getContext();
	
			this.travelsDetails = new ArrayList<TravelDetail>();
			
			addTravels();
			
			travelsListView = (ListView) rootView.findViewById(R.id.travelslistview);
			
			travelsAdapter = new TravelsListViewAdapter(context, R.layout.travels_list_item , travelsDetails);
			
			Log.d("AAAAA", this.travelsDetails.get(0).getTitle());
			
			travelsListView.setAdapter(travelsAdapter);
			travelsListView.setOnItemClickListener(this);
		
		
		return rootView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TextView travelTitle;
		TextView travelDescription;
		TextView travelStartDate;
		TextView travelEndDate;
		TextView travelDistance;
		
		travelTitle = (TextView) getActivity().findViewById(R.id.travel_detail_Title);
		travelTitle.setText( travelsDetails.get(position).getTitle());
		
		travelDescription = (TextView) getActivity().findViewById(R.id.travel_detail_Desctiption);
		travelDescription.setText("Description: " + travelsDetails.get(position).getDescription());
		
		travelDistance = (TextView) getActivity().findViewById(R.id.travel_detail_Distance);
		travelDistance.setText("Distance: " + travelsDetails.get(position).getDistance()+" m");
		
		travelStartDate = (TextView) getActivity().findViewById(R.id.travel_detail_Start);
		travelStartDate.setText("Started: " + travelsDetails.get(position).getStartDate().toString());
		
		travelEndDate = (TextView) getActivity().findViewById(R.id.travel_detail_End);
		if (travelsDetails.get(position).getEndDate() != null) {
			travelEndDate.setText("Ended: " + travelsDetails.get(position).getEndDate().toString());
		}else {
			travelEndDate.setText("Ended: not yet");
		}
	}
	
	private void addFakeTravels(){
		
		try {
			this.travelsDetails.add(new TravelDetail(0, "Long jorney", "Going to nowhere", "Pesho", GetDate("January 2, 2014"), GetDate("January 2, 2015"), 10000, null, null));
			this.travelsDetails.add(new TravelDetail(1, "New jorney", "Going home", "Pesho", GetDate("February 2, 2014"), GetDate("January 2, 2015"), 5000, null, null));
			this.travelsDetails.add(new TravelDetail(2, "Some travel", "Getting to Gosho", "Pesho", GetDate("March 2, 2014"), GetDate("March 3, 2014"), 100, null, null));
			this.travelsDetails.add(new TravelDetail(3, "Fake travel", "Fake Description", "Pesho", GetDate("January 2, 2014"), GetDate("January 2, 2015"), 12, null, null));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addTravels(){
		// read from Travels Shared Prefs
		//  - get TravelIds
		// read from TravelsDataSource
		// - for each TravelId get Travel
		TravelsDataSource travelsDataSource = new TravelsDataSource(context);
		travelsDataSource.open();
	    List<Travel> travelsInfo = travelsDataSource.getAllTravels();
	    
		for (Travel travel : travelsInfo) {
			this.travelsDetails.add(new TravelDetail(travel.getId(), travel.getTitle(), travel.getDescription(),  "Pesho", travel.getStartDate(), travel.getEndDate(), travel.getDistance(), null, null));
		}
		travelsDataSource.close();
	}
	
	public static DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
	public static Date GetDate(String dateString) throws ParseException{
		Date date = new Date();
		
		if (dateString != null) {
			date = dateFormat.parse(dateString);
		}
		
		return date;
	}
}