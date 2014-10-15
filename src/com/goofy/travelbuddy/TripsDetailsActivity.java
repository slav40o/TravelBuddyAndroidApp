package com.goofy.travelbuddy;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.goofy.models.TravelDetail;
import com.goofy.travelbuddy.connection.ClientManager;

public class TripsDetailsActivity extends BaseActivity{
	private TravelDetail travel;
	private Context context;
	private TextView title;
	private TextView description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trips_details_activity);
		this.title = (TextView)findViewById(R.id.tv_trip_title);
		this.description = (TextView)findViewById(R.id.tv_trip_description);
		Integer id = getIntent().getIntExtra("ID", 0);
		this.context = this;
		new GetTripDetailTask().execute(id);
	}
	
	private class GetTripDetailTask extends AsyncTask<Integer, Void, TravelDetail> {
		private ProgressDialog dialog;
		
		@Override
		protected TravelDetail doInBackground(Integer... data) {
			ClientManager client = new ClientManager(getApplicationContext());
			TravelDetail result = client.getTravelDetail(data[0]);
			return result;
		}

		@Override
		protected void onPostExecute(TravelDetail result) {
			dialog.hide(); 
			travel = result;
			title.setText(result.title);
			description.setText(result.description);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TripsDetailsActivity.this);
			dialog.setMessage("Loading trip info...");
			dialog.show(); 
		}
	}
}

