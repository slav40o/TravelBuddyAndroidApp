package com.goofy.travelbuddy.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TravelsSQLiteHelper extends SQLiteOpenHelper{

	public static final String TABLE_TRAVELS = "travels";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "decription";
	public static final String COLUMN_USRNAME = "username";
	public static final String COLUMN_START_DATE= "startdate";  // Text as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
	public static final String COLUMN_END_DATE = "enddate";  // Text as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
	public static final String COLUMN_DISTANCE = "distance";
	private static final String DATABASE_NAME = "travelbuddy.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation SQLite statement
    private static final String DATABASE_CREATE = "create table "
          //  + TABLE_TRAVELS + "(" + COLUMN_ID + " integer primary key autoincrement, " 
           + TABLE_TRAVELS + "(" + COLUMN_ID + " integer primary key, " 
            + COLUMN_TITLE + " text not null, " 
            + COLUMN_DESCRIPTION + " text not null, " 
            + COLUMN_USRNAME + " text not null, " 
            + COLUMN_START_DATE + " text, " 
            + COLUMN_END_DATE + " text, " 
            + COLUMN_DISTANCE + " integer" 
            		+ ");";
	
	
	public TravelsSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVELS);
        onCreate(db);
	}

}
