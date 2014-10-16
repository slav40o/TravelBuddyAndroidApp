package com.goofy.travelbuddy.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.goofy.models.Photo;
import com.goofy.travelbuddy.dao.PhotosDataSource;
public class ImageManager {
	private File appFolder;
	//private PhotosDataSource data;
	Context ctx;
	
	public ImageManager(String user, Context context) throws IOException{
		this.appFolder =  
				new File(Environment.getExternalStorageDirectory() + "/TravelBuddy/" + user);
		ctx = context;
		//data = new PhotosDataSource(context);
		if (!this.appFolder.exists()) {
			appFolder.mkdirs();
		}
	}
	
	private String savePhoto(Photo photo, String travelName, String placeName)
			throws IOException{
		File dir = this.getAlbumStorageDir(travelName, placeName);
		return this.writeFile(dir.getCanonicalPath() + "/" + photo.name, photo.getImage());
	}
	
	public List<Photo> savePhotos(List<Photo> photos, String travelName, String placeName)
			throws IOException{

		List<Photo> saved = new ArrayList<Photo>();
		for (Photo photo : photos) {
			String path = this.savePhoto(photo, travelName, placeName);
			Photo photoInfo = new Photo(photo.getId(), path.toString(), null, photo.getUserId(), photo.getPlaceID());
			saved.add(photoInfo);
			Log.d("PHOTO_SAVED", photoInfo.name);
		}
		return saved;
		
	}
	
	public void deletePhoto(String photoPath){
		
	}
	
	private String writeFile (String fileName, byte[] photo) throws IOException{
		if (PhoneState.isExternalStorageWritable()) {
			FileOutputStream out = new FileOutputStream(fileName + ".jpg");
			out.write(photo);
			out.close();
			return fileName + ".jpg";
		}
		
		return "";
	}
	
	private File getAlbumStorageDir(String travelName, String placeName) throws IOException {
		File file = new File(this.appFolder.getCanonicalPath() + "/" + travelName + "/" + placeName);
		boolean success = file.mkdirs();
		if (success) {
		    return file;
		}
		else{
			Log.d("PHOTO_SAVE", "getAlbumStorageDir IOEXCEPTION");
		   //throw new IOException();
			return file;
		}
	}
}
