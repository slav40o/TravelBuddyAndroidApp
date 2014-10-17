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
			Log.d("PLACE_DETAIL_ACTIVITY", " traveId=" + traveId);
			isTravel = true;
		}else{
			isTravel = false;
		}
		Log.d("PLACE_DETAIL_ACTIVITY", " = " + placeId);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (isTravel) {
			MenuItem addPhotoMenuItem = menu.findItem(PHOTO_MENU_ITEM);
			if (addPhotoMenuItem == null) {
				addPhotoMenuItem = menu.add(Menu.NONE, PHOTO_MENU_ITEM, 3, "Add Photo");
			}
		}
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {

		case PHOTO_MENU_ITEM:
			Toast.makeText(this, "Clicked: Add photo", Toast.LENGTH_SHORT)
					.show();
			Intent cameraIntent = new Intent(this, CameraActivity.class);
			cameraIntent.putExtra("PLACEID", placeId);
			cameraIntent.putExtra("TRAVELID", traveId);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
