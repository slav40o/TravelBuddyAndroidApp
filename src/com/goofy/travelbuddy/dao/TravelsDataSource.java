package com.goofy.travelbuddy.dao;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goofy.models.Travel;

public class TravelsDataSource {
	private SQLiteDatabase database;
	private TravelsSQLiteHelper dbHelper;
	private String[] allColumns = { 
			TravelsSQLiteHelper.COLUMN_ID,
			TravelsSQLiteHelper.COLUMN_TITLE,
			TravelsSQLiteHelper.COLUMN_DESCRIPTION,
			TravelsSQLiteHelper.COLUMN_USRNAME,
			TravelsSQLiteHelper.COLUMN_START_DATE,
			TravelsSQLiteHelper.COLUMN_END_DATE,
			TravelsSQLiteHelper.COLUMN_DISTANCE
	};
	
	public TravelsDataSource(Context context){
		dbHelper = new TravelsSQLiteHelper(context);
	}
	public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    
    public Travel createTravel(Travel travel){
    	 ContentValues values = new ContentValues();
	        values.put(TravelsSQLiteHelper.COLUMN_TITLE, travel.getTitle());
	        values.put(TravelsSQLiteHelper.COLUMN_DESCRIPTION, travel.getDescription());
	        values.put(TravelsSQLiteHelper.COLUMN_USRNAME, travel.getUserName());
	        values.put(TravelsSQLiteHelper.COLUMN_START_DATE, travel.getStartDate().toString());
	        values.put(TravelsSQLiteHelper.COLUMN_END_DATE, travel.getEndDate().toString());
	        values.put(TravelsSQLiteHelper.COLUMN_DISTANCE, travel.getDistance());
	        
	        long insertId = database.insert(TravelsSQLiteHelper.TABLE_TRAVELS, null,
	                values);
	        Cursor cursor = database.query(TravelsSQLiteHelper.TABLE_TRAVELS,
	                allColumns, TravelsSQLiteHelper.COLUMN_ID + " = " + insertId, null,
	                null, null, null);
	        cursor.moveToFirst();
	        Travel newTravel= cursorToTravel(cursor);
	        cursor.close();
	        return newTravel;
    }
    
    public List<Travel> getAllTravels() {
		List<Travel> travels = new ArrayList<Travel>();
		Cursor cursor = database.query(TravelsSQLiteHelper.TABLE_TRAVELS,
				allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Travel travel = cursorToTravel(cursor);
			travels.add(travel);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return travels;
	}
    
    public Travel getTravelById(int id){
    	 Cursor cursor = database.query(TravelsSQLiteHelper.TABLE_TRAVELS,
	                allColumns, TravelsSQLiteHelper.COLUMN_ID + " = " + id, null,
	                null, null, null);
	        cursor.moveToFirst();
	        Travel foundTravel= cursorToTravel(cursor);
	        cursor.close();
	        return foundTravel;
    }
    
	private Travel cursorToTravel(Cursor cursor) {
		Travel travel = new Travel(
				cursor.getInt(0),
				 cursor.getString(1), 
				 cursor.getString(2), 
				 cursor.getString(3), 
				 Date.valueOf(cursor.getString(4)), 
				 Date.valueOf(cursor.getString(5)), 
				 cursor.getInt(5)
				);
		return travel;
	}
}
