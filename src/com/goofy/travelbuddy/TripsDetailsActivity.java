package com.goofy.travelbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class TripsDetailsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trips_details_activity);
		Integer id = getIntent().getIntExtra("ID", 0);
		Toast.makeText(this, id.toString(), Toast.LENGTH_LONG).show();
	}

}
