package com.goofy.travelbuddy.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
PlaceTravel Id column is AUTOINCREMENTED
*/ 
public class PlacesTravelsSQLiteHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_PLACES_TRAVELS = "places_travels";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PLACEID = "placeid";
	public static final String COLUMN_TRAVELID = "travelid";
	public static final String COLUMN_PHOTOID = "photoid"; // each photo can be of only one place 
	private static final String DATABASE_NAME = "travelbuddy.db";
	private static final int DATABASE_VERSION = 1;
	
	  // Database creation SQLite statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PLACES_TRAVELS + "(" + COLUMN_ID + " integer primary key autoincrement, " 
            + COLUMN_PLACEID + " integer not null, " 
            + COLUMN_TRAVELID + " integer not null, " 
            + COLUMN_PHOTOID + " integer not null" 
            		+ ");";
	
	public PlacesTravelsSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES_TRAVELS);
        onCreate(db);
	}
}
