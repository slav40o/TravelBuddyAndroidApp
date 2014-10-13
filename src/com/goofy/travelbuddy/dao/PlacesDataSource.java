package com.goofy.travelbuddy.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goofy.models.Location;
import com.goofy.models.Place;

public class PlacesDataSource {
	private SQLiteDatabase database;
	private PlacesSQLiteHelper dbHelper;
	private String[] allColumns = { 
			PlacesSQLiteHelper.COLUMN_ID,
			PlacesSQLiteHelper.COLUMN_TITLE,
			PlacesSQLiteHelper.COLUMN_DESCRIPTION,
			PlacesSQLiteHelper.COLUMN_COUNTRY,
			PlacesSQLiteHelper.COLUMN_LAT,
			PlacesSQLiteHelper.COLUMN_LON,
			PlacesSQLiteHelper.COLUMN_LAST_VISIT
	};

	public PlacesDataSource(Context context){
		dbHelper = new PlacesSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
	    
	public Place createPlace(Place place){
		 ContentValues values = new ContentValues();
	        values.put(PlacesSQLiteHelper.COLUMN_TITLE, place.getTitle());
	        values.put(PlacesSQLiteHelper.COLUMN_DESCRIPTION, place.getDescription());
	        values.put(PlacesSQLiteHelper.COLUMN_COUNTRY, place.getCountry());
	        values.put(PlacesSQLiteHelper.COLUMN_LAT, place.getLatitude());
	        values.put(PlacesSQLiteHelper.COLUMN_LON, place.getLongtitude());
	        values.put(PlacesSQLiteHelper.COLUMN_LAST_VISIT, place.getLastVisited().toString());
	        
	        long insertId = database.insert(PlacesSQLiteHelper.TABLE_PLACES, null,
	                values);
	        Cursor cursor = database.query(PlacesSQLiteHelper.TABLE_PLACES,
	                allColumns, PlacesSQLiteHelper.COLUMN_ID + " = " + insertId, null,
	                null, null, null);
	        cursor.moveToFirst();
	        Place newPlace = cursorToComment(cursor);
	        cursor.close();
		return newPlace;
	}
	 private Place cursorToComment(Cursor cursor) {
		 Location location = new Location(cursor.getDouble(4),cursor.getDouble(5));
		 Place place = new Place(
				 cursor.getInt(0), 
				 cursor.getString(1), 
				 cursor.getString(2), 
				 cursor.getString(3),  
				 location);
	        return place;
	    }
}
