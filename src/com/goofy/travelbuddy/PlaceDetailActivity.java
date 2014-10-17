package com.goofy.travelbuddy;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class PlaceDetailActivity extends BaseActivity {

	private static final int PHOTO_MENU_ITEM = 1;
	private static final int MAP_MENU_ITEM = 10;
	int placeId;
	int traveId;
	boolean isTravel = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_details_activity);
		placeId =  this.getIntent().getExtras().getInt("PLACEID", 0);
		if (this.getIntent().getExtras().containsKey("TRAVELID")) {
			traveId = this.getIntent().getExtras().getInt("TRAVELID", 0);
			isTravel = true;
		}else{
			isTravel = false;
		}

	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (isTravel) {
			MenuItem addPhotoMenuItem = menu.findItem(PHOTO_MENU_ITEM);
			if (addPhotoMenuItem == null) {
				addPhotoMenuItem = menu.add(Menu.NONE, PHOTO_MENU_ITEM, 3, "Add Photo");
			}
			
			MenuItem addMapMenuItem = menu.findItem(MAP_MENU_ITEM);
			if (addMapMenuItem == null) {
				addMapMenuItem = menu.add(Menu.NONE, MAP_MENU_ITEM, 10, "On Map");
			}
		}
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {
			case PHOTO_MENU_ITEM:
				Intent cameraIntent = new Intent(this, CameraActivity.class);
				cameraIntent.putExtra("PLACEID", placeId);
				cameraIntent.putExtra("TRAVELID", traveId);
				startActivity(cameraIntent);
				return true;
	
			case MAP_MENU_ITEM:
				Intent mapIntent = new Intent(this, MapActivity.class);
				mapIntent.putExtra("PLACEID", placeId);
				startActivity(mapIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

}
