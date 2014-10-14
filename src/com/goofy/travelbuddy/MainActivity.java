package com.goofy.travelbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity implements View.OnClickListener{
	private Button topPlacesBtn;
	private Button visitedPlacesBtn;
	private Button personalTravelsBtn;
	private Button addTravelBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen_activity);
		initializeElements();
	}
	
	private void initializeElements() {
		// TODO Auto-generated method stub
		this.addTravelBtn = (Button)findViewById(R.id.btn_add_travel);
		addTravelBtn.setOnClickListener(this);
		this.personalTravelsBtn = (Button)findViewById(R.id.btn_all_travels);
		personalTravelsBtn.setOnClickListener(this);
		this.topPlacesBtn = (Button)findViewById(R.id.btn_top_places);
		topPlacesBtn.setOnClickListener(this);
		this.visitedPlacesBtn = (Button)findViewById(R.id.btn_visited_places);
		visitedPlacesBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == this.topPlacesBtn.getId()) {
			Intent placesIntent = new Intent(this, PlacesActivity.class);
			startActivity(placesIntent);
		}
		else if(v.getId() == this.addTravelBtn.getId()){
			Intent createTripIntent = new Intent(this, CreatTripActivity.class);
			startActivity(createTripIntent);
		}
	}
}
