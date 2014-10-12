package com.goofy.travelbuddy;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;

import com.goofy.travelbuddy.connection.ClientManager;
import com.goofy.travelbuddy.connection.UserPreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements View.OnClickListener{
	private Button registerButton;
	private EditText confirmedPasswordField;
	private EditText passwordField;
	private EditText emailField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		initializeElements();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == this.registerButton.getId()) {
			String username = this.emailField.getText().toString();
			String password = this.passwordField.getText().toString();
			String confirmPassword = this.confirmedPasswordField.getText().toString();
			boolean validState = true;
			
			if (username == null || username == "") {
				Toast.makeText(this, "Invalid username", Toast.LENGTH_LONG).show();
				validState = false;
			}
			else if(password == null || password == ""){
				Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show();
				validState = false;
			}
			else if(!confirmPassword.equals(password)){
				Toast.makeText(this, "Password not confirmed", Toast.LENGTH_LONG).show();
				validState = false;
			}
			
			if (validState) {
				 new RegisterTask().execute(username, password);
			}
			
		}
		
	}
	
	private void initializeElements() {
		this.registerButton = (Button)findViewById(R.id.btn_register);
		this.passwordField = (EditText)findViewById(R.id.et_register_password);
		this.emailField = (EditText)findViewById(R.id.et_register_email);
		this.confirmedPasswordField = (EditText)findViewById(R.id.et_confirm_pass);
		registerButton.setOnClickListener(this);
		//loginLink.setOnClickListener(this);
	}
	
	private class RegisterTask extends AsyncTask<String, Void, NameValuePair> {
		@Override
        protected NameValuePair doInBackground(String... urls) {
			ClientManager client = new ClientManager(getApplicationContext());
			NameValuePair responce = client.registerUser(urls[0], urls[1], urls[1]);
			return responce;
		}
		
		@Override
        protected void onPostExecute(NameValuePair result) {
			int status = Integer.parseInt(result.getName());
			String message = result.getValue();
			
			if (status == HttpStatus.SC_OK) {
//				Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_LONG).show();
//				ClientManager client = new ClientManager(getApplicationContext());
//				String username = UserPreferenceManager.getUsername(getApplicationContext());
//				String password = UserPreferenceManager.getPassword(getApplicationContext());
//				NameValuePair responce = client.loginUser(username, password);
//				int logStatus = Integer.parseInt(responce.getName());
//				
//				if (logStatus == HttpStatus.SC_OK) {
//					Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//					startActivity(mainIntent);
//				}
//				else{
					Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
	                startActivity(loginIntent);
//				}
			}
			else{
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
       }
	}
}
