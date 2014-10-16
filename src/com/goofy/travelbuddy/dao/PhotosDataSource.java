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
	private PhotosSQLite dbHelper;
	private String[] allColumns = { PhotosSQLite.COLUMN_ID,
			PhotosSQLite.COLUMN_NAME, PhotosSQLite.COLUMN_IMAGE,
			PhotosSQLite.COLUMN_USERID,
			PhotosSQLite.COLUMN_PLACEID, };
	private String[] photosIdsOnly = { 
			PhotosSQLite.COLUMN_ID,
			PhotosSQLite.COLUMN_PLACEID };
	
	public PhotosDataSource(Context context) {
		dbHelper = new PhotosSQLite(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Use addOrReplacePhoto(Photo photo)
	 */
	@Deprecated
	public Photo createPhoto(Photo photo) {
		ContentValues values = new ContentValues();
		values.put(PhotosSQLite.COLUMN_ID, photo.getId());
		values.put(PhotosSQLite.COLUMN_NAME, photo.getName());
		values.put(PhotosSQLite.COLUMN_IMAGE, photo.getImage());
		values.put(PhotosSQLite.COLUMN_USERID, photo.getUserId());
		values.put(PhotosSQLite.COLUMN_PLACEID, photo.getPlaceID());

		long insertId = database.insert(PhotosSQLite.TABLE_PHOTOS, null,
				values);
		Cursor cursor = database.query(PhotosSQLite.TABLE_PHOTOS,
				allColumns, PhotosSQLite.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Photo newPhoto = cursorToPhotos(cursor);
		cursor.close();
		return newPhoto;
	}
	
	public Photo addOrReplacePhoto(Photo photo ){
		ContentValues values = new ContentValues();
		values.put(PhotosSQLite.COLUMN_ID, photo.getId());
		values.put(PhotosSQLite.COLUMN_NAME, photo.getName());
		values.put(PhotosSQLite.COLUMN_IMAGE, " ");
		values.put(PhotosSQLite.COLUMN_USERID, photo.getUserId());
		values.put(PhotosSQLite.COLUMN_PLACEID, photo.getPlaceID());

		boolean updated = database.update(PhotosSQLite.TABLE_PHOTOS, values, PhotosSQLite.COLUMN_ID + " = " + photo.getId(), null) > 0;
		if (!updated) {
			database.insert(PhotosSQLite.TABLE_PHOTOS, null,
					values);
		}
		Cursor cursor = database.query(PhotosSQLite.TABLE_PHOTOS,
				allColumns, PhotosSQLite.COLUMN_ID + " = " + photo.getId(),
				null, null, null, null);
		cursor.moveToFirst();
		Photo newPhoto = cursorToPhotos(cursor);
		cursor.close();
		return newPhoto;
	}

	public List<Photo> getAllPhotos() {
		List<Photo> photos = new ArrayList<Photo>();
		Cursor cursor = database.query(PhotosSQLite.TABLE_PHOTOS,
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
		Cursor cursor = database.query(PhotosSQLite.TABLE_PHOTOS,
				allColumns, PhotosSQLite.COLUMN_PLACEID + " = " + placeId, null, null, null, null);

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
	
	public List<Integer> getPhotoIdsByPlaceId( int placeId) {
		List<Integer> photoIds = new ArrayList<Integer>();
		Cursor cursor = database.query(PhotosSQLite.TABLE_PHOTOS,
				allColumns, PhotosSQLite.COLUMN_PLACEID + " = " + placeId, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Photo photo = cursorToPhotos(cursor);
			photoIds.add(photo.getId());
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return photoIds;
	}
	
	public Photo getPhotoById(int id){
		Cursor cursor = database.query(PhotosSQLite.TABLE_PHOTOS,
				allColumns, PhotosSQLite.COLUMN_ID + " = " + id,
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
