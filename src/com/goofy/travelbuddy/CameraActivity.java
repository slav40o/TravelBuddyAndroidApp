package com.goofy.travelbuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends BaseActivity {
	final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	int placeId, travelId;
	boolean isPhotoTaken = false;
	static TextView imageDetails = null;
	public  ImageView showImg = null;
	public  Bitmap imageBit = null;
	Context context;
	Button  photo, savePhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context =  this;
		setContentView(R.layout.camera_activity);
		placeId = getIntent().getIntExtra("PLACEID", -1);
		travelId = getIntent().getIntExtra("TRAVELID", -1);
		imageDetails = (TextView) findViewById(R.id.imageDetails);
		showImg = (ImageView) findViewById(R.id.showImg);
		photo = (Button) findViewById(R.id.btn_take_photo);
		savePhoto = (Button) findViewById(R.id.btn_save_photo);
		
		photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);
			}
		});
		
		savePhoto.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (isPhotoTaken) {
					Toast.makeText(context, "First take picture", Toast.LENGTH_SHORT).show();
				}
				else{
					
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == 0 && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
	        showImg.setImageBitmap(imageBitmap);
	        imageBit = imageBitmap;
	    } else if (resultCode == RESULT_CANCELED) {

			Toast.makeText(this, " Picture was not taken ",
					Toast.LENGTH_SHORT).show();
		} else {

			Toast.makeText(this, " Picture was not taken ",
					Toast.LENGTH_SHORT).show();
		}
	}
}
