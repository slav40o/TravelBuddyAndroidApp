package com.goofy.travelbuddy.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class VisitorsDataSource {
	private SQLiteDatabase database;
	private VisitorsSQLiteHelper dbHelper;
	private String[] allColumns = { 
			VisitorsSQLiteHelper.COLUMN_ID,
			VisitorsSQLiteHelper.COLUMN_PLACEID,
			VisitorsSQLiteHelper.COLUMN_VISITOR_NAME
	};

	public VisitorsDataSource(Context context) {
		dbHelper = new VisitorsSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public String createVisitor(String visitorName, int placeId) {
		ContentValues values = new ContentValues();
		values.put(VisitorsSQLiteHelper.COLUMN_PLACEID, placeId);
		values.put(VisitorsSQLiteHelper.COLUMN_VISITOR_NAME,visitorName);

		long insertId = database.insert(VisitorsSQLiteHelper.TABLE_VISITORS, null,
				values);
		Cursor cursor = database.query(VisitorsSQLiteHelper.TABLE_VISITORS,
				allColumns, VisitorsSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		String visitorString = cursorToVisitor(cursor);
		cursor.close();
		return visitorString;
	}

	public List<String> getVisitorsByPlaceId( int placeId) {
		List<String> visitorsNames = new ArrayList<String>();
		Cursor cursor = database.query(VisitorsSQLiteHelper.TABLE_VISITORS,
				allColumns, VisitorsSQLiteHelper.COLUMN_PLACEID + " = " + placeId, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String visitorName = cursorToVisitor(cursor);
			visitorsNames.add(visitorName);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return visitorsNames;
	}
	

	private String cursorToVisitor(Cursor cursor) {

		String visiorName = cursor.getString(1);
		return visiorName;
	}
}
