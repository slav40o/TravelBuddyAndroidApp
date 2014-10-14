package com.goofy.travelbuddy.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VisitorsSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_VISITORS = "visitors";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PLACEID= "placeid";
	public static final String COLUMN_VISITOR_NAME= "visitorname";
	private static final String DATABASE_NAME = "travelbuddy.db";
	private static final int DATABASE_VERSION = 1;

	  // Database creation SQLite statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_VISITORS + "(" + COLUMN_ID + " integer primary key autoincrement, " 
            + COLUMN_PLACEID + " integer not null" 
            + COLUMN_VISITOR_NAME + " text not null" 
            		+ ");";
	
	public VisitorsSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITORS);
        onCreate(db);
    }
}
