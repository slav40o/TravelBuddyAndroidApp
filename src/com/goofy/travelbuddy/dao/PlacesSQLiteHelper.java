package com.goofy.travelbuddy.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlacesSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_PLACES = "places";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "decription";
	public static final String COLUMN_COUNTRY = "country";
	public static final String COLUMN_LAT= "lat"; // real
	public static final String COLUMN_LON = "lon"; // real
	public static final String COLUMN_LAST_VISIT = "lastvisited"; // Text as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
	private static final String DATABASE_NAME = "travelbuddy.db";
	private static final int DATABASE_VERSION = 1;

	  // Database creation SQLite statement
    public final String DATABASE_CREATE = "create table "
            + TABLE_PLACES + "(" + COLUMN_ID + " integer primary key, " 
            + COLUMN_TITLE + " text not null, " 
            + COLUMN_DESCRIPTION + " text not null, " 
            + COLUMN_COUNTRY + " text not null, " 
            + COLUMN_LAT + " real not null, " 
            + COLUMN_LON + " real not null, " 
            + COLUMN_LAST_VISIT + " text" 
            		+ ");";
	
	public PlacesSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	@Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }

}
