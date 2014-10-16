package com.goofy.travelbuddy.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.goofy.models.Location;
import com.goofy.models.Place;
import com.goofy.models.Travel;

public class PlacesDataSource {
	private SQLiteDatabase database;
	private PlacesSQLiteHelper dbHelper;
	private String[] allColumns = { PlacesSQLiteHelper.COLUMN_ID,
			PlacesSQLiteHelper.COLUMN_TITLE,
			PlacesSQLiteHelper.COLUMN_DESCRIPTION,
			PlacesSQLiteHelper.COLUMN_COUNTRY, PlacesSQLiteHelper.COLUMN_LAT,
			PlacesSQLiteHelper.COLUMN_LON, PlacesSQLiteHelper.COLUMN_LAST_VISIT };

	public PlacesDataSource(Context context) {
		dbHelper = new PlacesSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Use addOrReplacePlace(Place place) instead
	 * This one doesn't check for existing place id
	 */
	@Deprecated
	public Place createPlace(Place place) {
		ContentValues values = new ContentValues();
		values.put(PlacesSQLiteHelper.COLUMN_ID, place.getId());
		values.put(PlacesSQLiteHelper.COLUMN_TITLE, place.getTitle());
		values.put(PlacesSQLiteHelper.COLUMN_DESCRIPTION,
				place.getDescription());
		values.put(PlacesSQLiteHelper.COLUMN_COUNTRY, place.getCountry());
		values.put(PlacesSQLiteHelper.COLUMN_LAT, place.getLocation()
				.getLatitude());
		values.put(PlacesSQLiteHelper.COLUMN_LON, place.getLocation()
				.getLongtitude());
		values.put(PlacesSQLiteHelper.COLUMN_LAST_VISIT, place.getLastVisited()
				.toString());

		long insertId = database.insert(PlacesSQLiteHelper.TABLE_PLACES, null,
				values);
		Cursor cursor = database.query(PlacesSQLiteHelper.TABLE_PLACES,
				allColumns, PlacesSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Place newPlace = cursorToPlace(cursor);
		cursor.close();
		return newPlace;
	}
	
	 public Place addOrReplacePlace(Place place) {
			ContentValues values = new ContentValues();
			values.put(PlacesSQLiteHelper.COLUMN_ID, place.getId());
			values.put(PlacesSQLiteHelper.COLUMN_TITLE, place.getTitle());
			values.put(PlacesSQLiteHelper.COLUMN_DESCRIPTION,
					place.getDescription());
			values.put(PlacesSQLiteHelper.COLUMN_COUNTRY, place.getCountry());
			values.put(PlacesSQLiteHelper.COLUMN_LAT, place.getLocation()
					.getLatitude());
			values.put(PlacesSQLiteHelper.COLUMN_LON, place.getLocation()
					.getLongtitude());
			values.put(PlacesSQLiteHelper.COLUMN_LAST_VISIT, place.getLastVisited()
					.toString());

		        boolean updated = database.update(PlacesSQLiteHelper.TABLE_PLACES, values, PlacesSQLiteHelper.COLUMN_ID  + " = " + place.getId(), null) > 0;
		        if (!updated) {
		        	Log.d("PLACESSQLITE", "NOT UPDATED");
		        	
		        	database.insert(PlacesSQLiteHelper.TABLE_PLACES, null,
							values);
				}else{
					Log.d("PLACESSQLITE", "UPDATED");
				}
		        Cursor cursor = database.query(PlacesSQLiteHelper.TABLE_PLACES,
						allColumns, PlacesSQLiteHelper.COLUMN_ID + " = " + place.getId(),
						null, null, null, null);
				cursor.moveToFirst();
				Place newPlace = cursorToPlace(cursor);
				cursor.close();
				return newPlace;
	   }

	public Place getPlaceById(int id){
		Cursor cursor = database.query(PlacesSQLiteHelper.TABLE_PLACES,
				allColumns, PlacesSQLiteHelper.COLUMN_ID + " = " + id,
				null, null, null, null);
		cursor.moveToFirst();
		Place foundPlace = cursorToPlace(cursor);
		cursor.close();
		return foundPlace;
	}
	
	public List<Place> getAllPlaces() {
		List<Place> places = new ArrayList<Place>();
		Cursor cursor = database.query(PlacesSQLiteHelper.TABLE_PLACES,
				allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Place place = cursorToPlace(cursor);
			Log.d("PLACESSQLITE", "GET_PLACE");
			
			places.add(place);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return places;
	}

	private Place cursorToPlace(Cursor cursor) {
		Location location = new Location(cursor.getDouble(4),
				cursor.getDouble(5));
		Place place = new Place(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), location);
		return place;
	}
}
