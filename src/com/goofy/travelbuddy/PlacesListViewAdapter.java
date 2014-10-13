package com.goofy.travelbuddy;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.goofy.models.PlaceDetail;

public class PlacesListViewAdapter extends ArrayAdapter<PlaceDetail> {

	//List<PlaceDetail> places;
	Context context;
	int currentPosition;

	public PlacesListViewAdapter(Context context, int resource,	List<PlaceDetail> places
			) {
		super(context, resource, places);
		this.context = context;
		//this.places = places;
		Log.d("CREARED PlacesListViewAdapter", " " );
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("GETVIEW PlacesListViewAdapter", " " );
		ViewHolderItem holder;
		
		
		if (convertView == null) {
			holder = new ViewHolderItem();

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.place_list_item, parent,
					false);

			holder.placeImage = (ImageView) convertView
					.findViewById(R.id.placeImage);
			holder.placeTitle = (TextView) convertView
					.findViewById(R.id.placeTitle);
			holder.placeCountry = (TextView) convertView
					.findViewById(R.id.placeCountry);

			convertView.setTag(holder);
			this.currentPosition = position;

		} else {
			holder = (ViewHolderItem) convertView.getTag();
			//View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
			//View loader = (ProgressBar) rootView.findViewById(R.id.loadingProgressBar);
			//loader.setVisibility(View.GONE);
		}

		// Gets the first image of them all
		//byte[] byteArray = this.places.get(position).getPhotos().get(0);
		byte[] byteArray = this.getItem(position).getPhotos().get(0);

		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0,
				byteArray.length);
		holder.placeImage.setImageBitmap(bmp);
		//holder.placeTitle.setText(this.places.get(position).getTitle());
		//holder.placeCountry.setText(this.places.get(position).getCountry());
		holder.placeTitle.setText(this.getItem(position).getTitle());
		holder.placeCountry.setText(this.getItem(position).getCountry());
		
		
		return convertView;
	}

	static class ViewHolderItem {
		ImageView placeImage;
		TextView placeTitle;
		TextView placeCountry;
	}

}
