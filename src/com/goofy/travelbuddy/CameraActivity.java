package com.goofy.travelbuddy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.goofy.models.Photo;
import com.goofy.models.PhotoDetails;
import com.goofy.models.PlaceDetail;
import com.goofy.models.TravelDetail;
import com.goofy.travelbuddy.connection.ClientManager;
import com.goofy.travelbuddy.connection.UserPreferenceManager;
import com.goofy.travelbuddy.dao.PhotosDataSource;
import com.goofy.travelbuddy.utils.ImageManager;

public class CameraActivity extends BaseActivity {
	final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	int placeId, travelId;
	boolean isPhotoTaken = false;
	public ImageView showImg = null;
	public Bitmap imageBit = null;
	Context context;
	Button  photoBtn, savePhotoBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context =  this;
		setContentView(R.layout.camera_activity);
		placeId = getIntent().getIntExtra("PLACEID", -1);
		travelId = getIntent().getIntExtra("TRAVELID", -1);
		showImg = (ImageView) findViewById(R.id.showImg);
		photoBtn = (Button) findViewById(R.id.btn_take_photo);
		savePhotoBtn = (Button) findViewById(R.id.btn_save_photo);
		
		photoBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);
			}
		});
		
		savePhotoBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (isPhotoTaken) {
					Toast.makeText(context, "First take picture", Toast.LENGTH_SHORT).show();
				}
				else{
					PhotoDetails details = new PhotoDetails();
					details.photo = imageBit;
					details.placeId = placeId;
					details.travelId = travelId;
					details.name = String.valueOf(new Date());
					new SavePhotoTask().execute(details);
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
	    } else {

			Toast.makeText(this, " Picture was not taken ",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private class SavePhotoTask extends AsyncTask<PhotoDetails, Void, NameValuePair> {
		private ProgressDialog dialog;
		private String placeName;
		
		@Override
		protected NameValuePair doInBackground(PhotoDetails... args) {
			PhotoDetails details = args[0];
			ClientManager manager = new ClientManager(context);
			PlaceDetail place = manager.getPlaceDetail(details.placeId);
			placeName = place.title;
			TravelDetail travel = manager.getTravelDetail(details.travelId);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			details.photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			NameValuePair responce = null;
			
			// TO DO: Save to Database
			try {
				Photo photo = new Photo(0, details.name, byteArray, "", details.placeId);
				String user = UserPreferenceManager.getUsername(context);
				ImageManager localManager = new ImageManager(user, context);
				ArrayList<Photo> photos = new ArrayList<Photo>();
				photos.add(photo);
				List<Photo> dbPhotos = localManager.savePhotos(photos, travel.title, place.title);
				PhotosDataSource photoSource = new PhotosDataSource(context);
				photoSource.open();
				for (Photo dbPhoto : dbPhotos) {
					photoSource.addOrReplacePhoto(dbPhoto);
				}
				
				photoSource.close();
				responce = manager.addPhoto(photo);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return responce;
		}

		@Override
		protected void onPostExecute(NameValuePair result) {
			super.onPostExecute(result);
			int status = Integer.parseInt(result.getName());
			String message = result.getValue();
			dialog.hide(); 
			if (status == HttpStatus.SC_OK) {
				Toast.makeText(getApplicationContext(), "Photo added!", Toast.LENGTH_LONG).show();
				Intent detailIntent = new Intent(getApplicationContext(), MainActivity.class);
				detailIntent.putExtra("TRAVELID", travelId);
				detailIntent.putExtra("PLACEID", placeId);
				detailIntent.putExtra("PLACE_TITLE", placeName);
				ArrayList<String> visitors = new ArrayList<String>();
				visitors.add("PESHO");
				detailIntent.putStringArrayListExtra("VISITORS", visitors);
				startActivity(detailIntent);
			}
			else{
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CameraActivity.this);
			dialog.setMessage("Saving picture...");
			dialog.show();
		}
		
	}
}
