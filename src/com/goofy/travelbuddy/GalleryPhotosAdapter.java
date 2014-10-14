package com.goofy.travelbuddy;

import java.util.List;

import com.goofy.models.Photo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryPhotosAdapter extends BaseAdapter{
	Context context;
	private List<Photo> photos;
	 private int itemBackground;
	
	public GalleryPhotosAdapter(Context context, List<Photo> photos){
		this.photos = photos;
		this.context = context;
		 // sets a grey background; wraps around the images
		 TypedArray a = context.obtainStyledAttributes(R.styleable.MyGallery);
		 itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
		 a.recycle();}
	
	@Override
	public int getCount() {
		return this.photos.size();
	}

	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		byte[] image = photos.get(position).getImage();
		 imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
		 imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
		 imageView.setBackgroundResource(itemBackground);
		 //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		 return imageView;
	}
}
