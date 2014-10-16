package com.goofy.travelbuddy.dao;


import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
		//	TravelsSQLiteHelper.COLUMN_USRNAME,
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
    
    /**
     * Use addOrReplaceTravel(Travel travel) instead
     */
    @Deprecated
    public Travel createTravel(Travel travel){
    	 ContentValues values = new ContentValues();
	 		values.put(TravelsSQLiteHelper.COLUMN_ID, travel.getId());
	        values.put(TravelsSQLiteHelper.COLUMN_TITLE, travel.getTitle());
	        values.put(TravelsSQLiteHelper.COLUMN_DESCRIPTION, travel.getDescription());
	      //  values.put(TravelsSQLiteHelper.COLUMN_USRNAME, travel.getUserName());
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
    
    public void addOrReplaceTravel(Travel travel){
   	 ContentValues values = new ContentValues();
   	 		values.put(TravelsSQLiteHelper.COLUMN_ID, travel.getId());
	        values.put(TravelsSQLiteHelper.COLUMN_TITLE, travel.getTitle());
	        values.put(TravelsSQLiteHelper.COLUMN_DESCRIPTION, travel.getDescription());
	      //  values.put(TravelsSQLiteHelper.COLUMN_USRNAME, travel.getUserName());
	        values.put(TravelsSQLiteHelper.COLUMN_START_DATE, travel.getStartDate().toString());
	        if (travel.getEndDate() != null) {
	        values.put(TravelsSQLiteHelper.COLUMN_END_DATE, travel.getEndDate().toString());				
			}else {
				// TODO else what?
			}
	        values.put(TravelsSQLiteHelper.COLUMN_DISTANCE, travel.getDistance());
	        
	        boolean updated = database.update(TravelsSQLiteHelper.TABLE_TRAVELS, values, TravelsSQLiteHelper.COLUMN_ID  + " = " + travel.getId(), null) > 0;
	        if (!updated) {
	        	database.insert(TravelsSQLiteHelper.TABLE_TRAVELS, null,
		                values);
			}
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
		
		Date start = null; 
		Date end = null;
		try {
			start = GetDate(cursor.getString(4));
			end = GetDate(cursor.getString(5));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Travel travel = new Travel(
				cursor.getInt(0),
				 cursor.getString(1), 
				 cursor.getString(2), 
				 cursor.getString(3), 
				 start, 
				 end, 
				 cursor.getInt(5)
				);
		return travel;
	}
	
	public static DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
	public static Date GetDate(String dateString) throws ParseException{
		Date date = new Date();
		
		if (dateString != null) {
			date = dateFormat.parse(dateString);
		}
		
		return date;
	}
}
