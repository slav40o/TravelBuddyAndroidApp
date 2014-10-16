package com.goofy.travelbuddy;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goofy.travelbuddy.connection.ClientManager;
import com.goofy.travelbuddy.connection.UserPreferenceManager;

public class LoginActivity extends Activity implements View.OnClickListener{
	private Button loginButton;
	private TextView registerLink;
	private EditText passwordField;
	private EditText emailField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		initializeElements();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == this.loginButton.getId()) {
			String username = this.emailField.getText().toString();
			String password = this.passwordField.getText().toString();
			boolean validState = true;
			
			if (username == null || username == "") {
				Toast.makeText(this, "Invalid username", Toast.LENGTH_LONG).show();
				validState = false;
			}
			else if(password == null || password == ""){
				Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show();
				validState = false;
			}
			
			if (validState) {
				new SignUpTask().execute(username, password);
			}
			
		} else if (v.getId() == this.registerLink.getId()) {
			Intent registerIntent = new Intent(this, RegisterActivity.class);
			startActivity(registerIntent);
		}
		
	}

	@Override
	public void onBackPressed() {
		
	}

	private void initializeElements() {
		this.loginButton = (Button)findViewById(R.id.btn_login);
		this.passwordField = (EditText)findViewById(R.id.ed_password_input);
		this.emailField = (EditText)findViewById(R.id.ed_email_input);
		this.registerLink = (TextView)findViewById(R.id.tv_register);
		loginButton.setOnClickListener(this);
		registerLink.setOnClickListener(this);
	}
	
	private class SignUpTask extends AsyncTask<String, Void, NameValuePair> {
		private ProgressDialog dialog;
		
		@Override
        protected NameValuePair doInBackground(String... urls) {
			ClientManager client = new ClientManager(getApplicationContext());
			NameValuePair responce = null;
			try {
				responce = client.loginUser(urls[0], urls[1]);
			} catch (ClientProtocolException e) {
				responce = new BasicNameValuePair("500", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return responce;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setMessage("Logging in...");
			dialog.show();  
		}

		@Override
        protected void onPostExecute(NameValuePair result) {
			int status = Integer.parseInt(result.getName());
			String message = result.getValue();
			dialog.hide();  
			
			if (status == HttpStatus.SC_OK) {
				String name = UserPreferenceManager.getUsername(getApplicationContext());
				Toast.makeText(getApplicationContext(), "Welcome, " + name, Toast.LENGTH_LONG).show();
				Intent registerIntent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(registerIntent);
			}
			else{
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
       }
	}
}
