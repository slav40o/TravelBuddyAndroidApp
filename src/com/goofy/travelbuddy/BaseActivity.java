package com.goofy.travelbuddy;

import com.goofy.travelbuddy.connection.UserPreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity{
	private DialogFragment mDialog;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_logout) {
			mDialog = AlertDialogFragment.newInstance();
			mDialog.show(getFragmentManager(), "Alert");
		}
		else if (item.getItemId() == R.id.action_main_menu) {
			Intent mainIntent = new Intent(this, MainActivity.class);
			startActivity(mainIntent);
		}
		return true;
	}
	
	private void logout(boolean toLogout){
		if (toLogout) {
			UserPreferenceManager.logoutUser(this);
			Intent loginIntent = new Intent(this, LoginActivity.class);
			Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
			startActivity(loginIntent);
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
					.setMessage("Do you really want to logout?")
					
					// User cannot dismiss dialog by hitting back button
					.setCancelable(false)
					
					// Set up No Button
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									((BaseActivity) getActivity())
											.logout(false);
								}
							})
							
					// Set up Yes Button
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog, int id) {
									((BaseActivity) getActivity())
											.logout(true);
								}
							}).create();
		}
	}
}
