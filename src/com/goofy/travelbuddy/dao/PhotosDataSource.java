package com.goofy.travelbuddy.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goofy.models.Photo;

public class PhotosDataSource {
	private SQLiteDatabase database;
	private PhotosSQLiteHelper dbHelper;
	private String[] allColumns = { PhotosSQLiteHelper.COLUMN_ID,
			PhotosSQLiteHelper.COLUMN_NAME, PhotosSQLiteHelper.COLUMN_IMAGE,
			PhotosSQLiteHelper.COLUMN_USERID,
			PhotosSQLiteHelper.COLUMN_PLACEID, };

	public PhotosDataSource(Context context) {
		dbHelper = new PhotosSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Photo createPhoto(Photo photo) {
		ContentValues values = new ContentValues();
		values.put(PhotosSQLiteHelper.COLUMN_NAME, photo.getName());
		values.put(PhotosSQLiteHelper.COLUMN_IMAGE, photo.getImage());
		values.put(PhotosSQLiteHelper.COLUMN_USERID, photo.getUserId());
		values.put(PhotosSQLiteHelper.COLUMN_PLACEID, photo.getPlaceID());

		long insertId = database.insert(PhotosSQLiteHelper.TABLE_PHOTOS, null,
				values);
		Cursor cursor = database.query(PhotosSQLiteHelper.TABLE_PHOTOS,
				allColumns, PhotosSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Photo newPhoto = cursorToPhotos(cursor);
		cursor.close();
		return newPhoto;
	}

	public List<Photo> getAllPhotos() {
		List<Photo> photos = new ArrayList<Photo>();
		Cursor cursor = database.query(PhotosSQLiteHelper.TABLE_PHOTOS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Photo photo = cursorToPhotos(cursor);
			photos.add(photo);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return photos;
	}
	
	public List<Photo> getPhotosByPlaceId( int placeId) {
		List<Photo> photos = new ArrayList<Photo>();
		Cursor cursor = database.query(PhotosSQLiteHelper.TABLE_PHOTOS,
				allColumns, PhotosSQLiteHelper.COLUMN_PLACEID + " = " + placeId, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Photo photo = cursorToPhotos(cursor);
			photos.add(photo);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return photos;
	}
	
	public Photo getPhotoById(int id){
		Cursor cursor = database.query(PhotosSQLiteHelper.TABLE_PHOTOS,
				allColumns, PhotosSQLiteHelper.COLUMN_ID + " = " + id,
				null, null, null, null);
		cursor.moveToFirst();
		Photo foundPhoto = cursorToPhotos(cursor);
		cursor.close();
		return foundPhoto;
	}

	private Photo cursorToPhotos(Cursor cursor) {
		Photo place = new Photo(cursor.getInt(0), cursor.getString(1),
				cursor.getBlob(2), cursor.getString(3), cursor.getInt(4));
		return place;
	}
}
