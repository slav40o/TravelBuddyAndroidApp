package com.goofy.travelbuddy.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.os.Environment;

import com.goofy.models.Photo;
public class ImageManager {
	private File appFolder;
	
	public ImageManager(String user) throws IOException{
		this.appFolder =  
				new File(Environment.getExternalStorageDirectory() + "/TravelBuddy/" + user);
		appFolder.mkdirs();
		if (!this.appFolder.exists()) {
			throw new IOException();
		}
	}
	
	public boolean  savePhoto(Photo photo, String travelName, String placeName)
			throws IOException{
		File dir = this.getAlbumStorageDir(travelName, placeName);
		return this.writeFile(dir.getCanonicalPath() + "/" + photo.name, photo.getImage());
	}
	
	public void savePhotos(List<Photo> photos, String travelName, String placeName)
			throws IOException{
		for (Photo photo : photos) {
			this.savePhoto(photo, travelName, placeName);
		}
	}
	
	public void deletePhoto(String photoPath){
		
	}
	
	private boolean writeFile (String fileName, byte[] photo) throws IOException{
		if (PhoneState.isExternalStorageWritable()) {
			FileOutputStream out = new FileOutputStream(fileName + ".jpg");
			out.write(photo);
			out.close();
			return true;
		}
		
		return false;
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
