package com.goofy.travelbuddy;

import com.goofy.travelbuddy.connection.UserPreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity{


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


}
