package com.goofy.travelbuddy;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.goofy.models.Photo;


public class TravelsActivity extends BaseActivity{

	List<Photo> photos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.travels_activity);
	}
}
