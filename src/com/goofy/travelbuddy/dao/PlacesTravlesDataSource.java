package com.goofy.travelbuddy.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goofy.models.Place;
import com.goofy.models.Travel;
import com.goofy.models.TravelDetail;

public class PlacesTravlesDataSource {
	private SQLiteDatabase database;
	private PlacesTravelsSQLiteHelper dbHelper;
	private String[] allColumns = { 
			PlacesTravelsSQLiteHelper.COLUMN_ID,
			PlacesTravelsSQLiteHelper.COLUMN_PLACEID, 
			PlacesTravelsSQLiteHelper.COLUMN_TRAVELID,
			PlacesTravelsSQLiteHelper.COLUMN_PHOTOID,
			};
	
	public PlacesTravlesDataSource(Context context) {
		dbHelper = new PlacesTravelsSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * PlaceTravel Id column is AUTOINCREMENTED
	 * returns placeTravelId
	*/ 
	public int createTravelDetailByIds(int placeId, int travelId) {
		ContentValues values = new ContentValues();
		values.put(PlacesTravelsSQLiteHelper.COLUMN_PLACEID, placeId);
		values.put(PlacesTravelsSQLiteHelper.COLUMN_TRAVELID, travelId);
	

		long insertId = database.insert(PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS, null,
				values);
		Cursor cursor = database.query(PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS,
				allColumns, PlacesTravelsSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		int placeTravelId = cursorToPlaceTravel(cursor);
		cursor.close();
		return placeTravelId;
	}
	
	/**
	 PlaceTravel Id column is AUTOINCREMENTED
	 returns placeTravelId
	*/ 
	public int createTravelDetailByIds(int placeId, int travelId, int photoId) {
		ContentValues values = new ContentValues();
		values.put(PlacesTravelsSQLiteHelper.COLUMN_PLACEID, placeId);
		values.put(PlacesTravelsSQLiteHelper.COLUMN_TRAVELID, travelId);
		values.put(PlacesTravelsSQLiteHelper.COLUMN_PHOTOID, photoId);

		long insertId = database.insert(PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS, null,
				values);
		Cursor cursor = database.query(PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS,
				allColumns, PlacesTravelsSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		int placeTravelId = cursorToTravel(cursor);
		cursor.close();
		return placeTravelId;
	}
	
	/**
	 PlaceTravel Id column is AUTOINCREMENTED
	 returns placeTravelId
	*/ 
	public int createTravelDetailByObjects(Place place, Travel travel) {
		ContentValues values = new ContentValues();
		values.put(PlacesTravelsSQLiteHelper.COLUMN_PLACEID, place.getId());
		values.put(PlacesTravelsSQLiteHelper.COLUMN_TRAVELID, travel.getId());

		long insertId = database.insert(PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS, null,
				values);
		Cursor cursor = database.query(PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS,
				allColumns, PlacesTravelsSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		int placeTravelId = cursorToTravel(cursor);
		cursor.close();
		return placeTravelId;
	}

	public Set<Integer> getPlacesIdsByTravelId(int travelId) {
		//List<Integer> placeIds = new ArrayList<Integer>();
		Set<Integer> placeIds = new HashSet<Integer>();
		Cursor cursor = database.query(PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS,
				allColumns, PlacesTravelsSQLiteHelper.COLUMN_TRAVELID + " = " + travelId, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int placeId = cursorToPlace(cursor);
			placeIds.add(placeId);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return placeIds;
	}
	
	public List<Integer> getPhotoIdsByTravelId(int travelId) {
		List<Integer> photoIds = new ArrayList<Integer>();
		Cursor cursor = database.query(PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS,
				allColumns, PlacesTravelsSQLiteHelper.COLUMN_TRAVELID + " = " + travelId, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int photoId = cursorToPhoto(cursor);
			photoIds.add(photoId);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return photoIds;
	}
	
	
	public List<Integer> getTravelIdsByPlaceId(int placeId) {
		List<Integer> travelIds = new ArrayList<Integer>();
		Cursor cursor = database.query(PlacesTravelsSQLiteHelper.TABLE_PLACES_TRAVELS,
				allColumns,  PlacesTravelsSQLiteHelper.COLUMN_PLACEID + " = " + placeId, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int travelId = cursorToTravel(cursor);
			travelIds.add(travelId);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return travelIds;
	}
	
	
	

	private int cursorToPlaceTravel(Cursor cursor) {
		int place = cursor.getInt(0);
		return place;
	}
	
	private int cursorToPlace(Cursor cursor) {
		int place = cursor.getInt(1);
		return place;
	}

	private int cursorToTravel(Cursor cursor) {
		int travel = cursor.getInt(2);
		return travel;
	}
	
	private int cursorToPhoto(Cursor cursor) {
		int travel = cursor.getInt(3);
		return travel;
	}
}
