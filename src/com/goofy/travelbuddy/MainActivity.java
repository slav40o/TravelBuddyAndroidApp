package com.goofy.travelbuddy;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.goofy.travelbuddy.connection.UserPreferenceManager;

public class MainActivity extends Activity implements View.OnClickListener{
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_logout) {
			UserPreferenceManager.logoutUser(this);
			Intent loginIntent = new Intent(this, LoginActivity.class);
			Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
			startActivity(loginIntent);
		}
		
		return true;
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
