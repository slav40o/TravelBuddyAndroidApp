package com.goofy.travelbuddy.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PhotosSQLite  extends SQLiteOpenHelper {
	
	public static final String TABLE_PHOTOS = "photostable";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_IMAGE = "image";
	public static final String COLUMN_USERID = "userid";
	public static final String COLUMN_PLACEID= "placeid"; // real
	private static final String DATABASE_NAME = "travelbuddy.db";
	private static final int DATABASE_VERSION = 1;
	
	 public final String DATABASE_CREATE = "create table "
	            + TABLE_PHOTOS + "(" + COLUMN_ID + " integer primary key, " 
	            + COLUMN_NAME + " text not null, " 
	            + COLUMN_IMAGE + " text, " 
	            + COLUMN_USERID + " text not null, " 
	            + COLUMN_PLACEID + " integer not null " 
	            		+ ");";
	public PhotosSQLite (Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        onCreate(db);		
	}
}
