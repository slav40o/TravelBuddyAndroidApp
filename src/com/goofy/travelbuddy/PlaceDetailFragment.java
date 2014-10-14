package com.goofy.travelbuddy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.goofy.models.Photo;
import com.goofy.travelbuddy.dao.PhotosDataSource;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceDetailFragment extends Fragment{

	List<Photo> photos;
	String placeTitle;
	ArrayList<String> visitors;
	Context fragmentContext;
	View fragmentView;
	int placeId = 0;
	private PhotosDataSource photosDatasource;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(savedInstanceState != null){
		    placeId = savedInstanceState.getInt("PLACEID", 0);
		    placeTitle = savedInstanceState.getString("PLACE_TITLE");
		    visitors = savedInstanceState.getStringArrayList("VISITORS");
		}
		
		View view = inflater.inflate(R.layout.place_details_fragment,
				container, false);
		this.photos = new ArrayList<Photo>();
		this.fragmentView = view;
		this.fragmentContext = view.getContext();
		
		// TODO Add real photos
		addFakePhotos();
		//photosDatasource = new PhotosDataSource(fragmentContext);
		// photosDatasource.open();
		//photos = photosDatasource.getPhotosByPlaceId(placeId);
		// photosDatasource.close();
		TextView placeDetailsTitle = (TextView) view.findViewById(R.id.placedetailstitle);
		placeDetailsTitle.setText(placeTitle);
		
		
		// The Gallery
		Gallery gallery = (Gallery) view.findViewById(R.id.placedetailsgallery);
		gallery.setAdapter(new GalleryPhotosAdapter(this.fragmentContext,
				this.photos));
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// display the images selected
				ImageView imageView = (ImageView) fragmentView
						.findViewById(R.id.selectedgalleryphoto);
				TextView photoText = (TextView) fragmentView.findViewById(R.id.imagetitletext);
				
				byte[] image = photos.get(position).getImage();
				imageView.setImageBitmap(BitmapFactory.decodeByteArray(image,
						0, image.length));
				photoText.setText(photos.get(position).getName());
			}
		});

		return fragmentView;
	}

	private void addFakePhotos() {
	    this.photos.add( new Photo(0, "Faky", getResourceBitmap(R.drawable.ic_launcher), "0", 0));
	    this.photos.add( new Photo(1, "Faky delete", getResourceBitmap(android.R.drawable.ic_menu_delete), "0", 0));
	    this.photos.add( new Photo(2, "Faky camea", getResourceBitmap(android.R.drawable.ic_menu_camera), "0", 0));
	    this.photos.add( new Photo(3, "Faky call", getResourceBitmap(android.R.drawable.sym_call_missed), "0", 0));
	    this.photos.add( new Photo(4, "Faky star", getResourceBitmap(android.R.drawable.btn_star_big_on), "0", 0));
	}

	private byte[] getResourceBitmap(int drawableId) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
		
		
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    
	    
	    byte[] bitmapdata = stream.toByteArray();
		return bitmapdata;
	}

}
