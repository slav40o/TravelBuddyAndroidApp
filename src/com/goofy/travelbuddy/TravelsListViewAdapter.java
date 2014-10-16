package com.goofy.travelbuddy;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goofy.models.TravelDetail;
import com.goofy.travelbuddy.PlacesListViewAdapter.ViewHolderItem;

public class TravelsListViewAdapter extends ArrayAdapter<TravelDetail> {
	
	
	Context context;
	int currentPosition;
	//List<TravelDetail> travels;

	public TravelsListViewAdapter(Context context, int resource,
			List<TravelDetail> travels) {
		super(context, resource, travels);
		this.context = context;
		//this.travels = travels;
		Log.d("CREATED", "TravelsListViewAdapter");
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
ViewHolderItem holder;
		
		Log.d("getVIEW", "TravelsListViewAdapter");
		if (convertView == null) {
			Log.d("getVIEW", "convertView == null");
			holder = new ViewHolderItem();

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.travels_list_item, parent,
					false);

			holder.travelImage = (ImageView) convertView
					.findViewById(R.id.travelImage);
			holder.travelTitle = (TextView) convertView
					.findViewById(R.id.travelTitle);
			holder.travelDistance = (TextView) convertView
					.findViewById(R.id.travelDistance);

			convertView.setTag(holder);
			this.currentPosition = position;

		} else {
			Log.d("getVIEW", "convertView != null");
			holder = (ViewHolderItem) convertView.getTag();
			//View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
			//View loader = (ProgressBar) rootView.findViewById(R.id.loadingProgressBar);
			//loader.setVisibility(View.GONE);
		}
		Log.d("BOUND", "TravelsListViewAdapter");
		// TODO FAKE BITMAP
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		
				holder.travelImage.setImageBitmap(bitmap);
				holder.travelTitle.setText(this.getItem(position).getTitle());
				holder.travelDistance.setText(String.valueOf(this.getItem(position).getDistance())+" m" );
		
				Log.d("DATA BOUND", "TravelsListViewAdapter");		
		return convertView;
	}

	static class ViewHolderItem {
		ImageView travelImage;
		TextView travelTitle;
		TextView travelDistance;
	}
}
