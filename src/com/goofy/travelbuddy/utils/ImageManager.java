package com.goofy.travelbuddy.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.os.Environment;

import com.goofy.models.Photo;
import com.goofy.travelbuddy.dao.PhotosDataSource;
public class ImageManager {
	private File appFolder;
	private PhotosDataSource data;
	
	public ImageManager(String user, Context context) throws IOException{
		this.appFolder =  
				new File(Environment.getExternalStorageDirectory() + "/TravelBuddy/" + user);
		data = new PhotosDataSource(context);
		if (!this.appFolder.exists()) {
			appFolder.mkdirs();
		}
	}
	
	private String savePhoto(Photo photo, String travelName, String placeName)
			throws IOException{
		File dir = this.getAlbumStorageDir(travelName, placeName);
		return this.writeFile(dir.getCanonicalPath() + "/" + photo.name, photo.getImage());
	}
	
	public void savePhotos(List<Photo> photos, String travelName, String placeName)
			throws IOException{
		this.data.open();
		for (Photo photo : photos) {
			String path = this.savePhoto(photo, travelName, placeName);
			Photo photoInfo = new Photo(photo.getId(), path, null, photo.getUserId(), photo.getPlaceID());
			data.addOrReplacePhoto(photoInfo);
		}
		
		data.close();
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
		   throw new IOException();
		}
	}
}
