package com.goofy.travelbuddy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.goofy.models.TravelDetail;
import com.goofy.travelbuddy.connection.ClientManager;

public class TripsDetailsActivity extends BaseActivity implements View.OnClickListener{
	private TravelDetail travel;
	private Context context;
	private TextView title;
	private TextView description;
	private DialogFragment mDialog;
	private Button addBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trips_details_activity);
		this.title = (TextView)findViewById(R.id.tv_place_title);
		this.description = (TextView)findViewById(R.id.tv_trip_description);
		this.addBtn = (Button)findViewById(R.id.btn_add_place);
		addBtn.setOnClickListener(this);
		Integer id = getIntent().getIntExtra("ID", 0);
		this.context = this;
		new GetTripDetailTask().execute(id);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == this.addBtn.getId()) {
			mDialog = AlertDialogFragment.newInstance();
			mDialog.show(getFragmentManager(), "Alert");
		}
	}
	
	public void addPlace(boolean exists){
		if (exists) {
			// TO DO list available places;
		}
		else{
			// TO DO create place;
		}
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
	
	private static class AlertDialogFragment extends DialogFragment {

		public static AlertDialogFragment newInstance() {
			return new AlertDialogFragment();
		}

		// Build AlertDialog using AlertDialog.Builder 
		// So nothing in common with JavaScript ? 
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setMessage("Add a place to your trip from ?")
					
					// User cannot dismiss dialog by hitting back button
					.setCancelable(false)
					
					// Set up No Button
					.setNegativeButton("New",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									((TripsDetailsActivity) getActivity())
											.addPlace(false);
								}
							})
							
					// Set up Yes Button
					.setPositiveButton("Existing",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog, int id) {
									((TripsDetailsActivity) getActivity())
											.addPlace(true);
								}
							}).create();
		}
	}
}

