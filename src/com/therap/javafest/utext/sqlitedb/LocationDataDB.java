package com.therap.javafest.utext.sqlitedb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.therap.javafest.utext.lib.LocationData;

public class LocationDataDB {
	private SQLiteDatabase database;
	private DBHelper helper;

	public LocationDataDB(Context context) {
		helper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public long insert(long lsid, int ntype, double longitude, double latitude,
			String place) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_NID,
				String.valueOf(lsid));
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_NTYPE,
				String.valueOf(ntype));
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_CREATED, curDateTime);
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_MODIFIED, curDateTime);
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_LONGITUDE,
				String.valueOf(longitude));
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_LATITUDE,
				String.valueOf(latitude));
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_PLACE, place);
		long id = database.insert(DBHelper.DB_TABLE_LOCATION_DATA, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int nid, int ntype) {
		open();
		database.delete(DBHelper.DB_TABLE_LOCATION_DATA,
				DBHelper.LOCATION_DATA_COLUMN_NID + "=? AND "
						+ DBHelper.LOCATION_DATA_COLUMN_NTYPE + "=?",
				new String[] { String.valueOf(nid), String.valueOf(ntype) });
		close();
	}

	public void update(int lid, double longitude, double latitude, String place) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_MODIFIED, curDateTime);
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_LONGITUDE,
				String.valueOf(longitude));
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_LATITUDE,
				String.valueOf(latitude));
		contentValues.put(DBHelper.LOCATION_DATA_COLUMN_PLACE, place);
		database.update(DBHelper.DB_TABLE_LOCATION_DATA, contentValues,
				DBHelper.LOCATION_DATA_COLUMN_LID + "=?",
				new String[] { String.valueOf(lid) });
		close();
	}

	public LocationData selectByNoteId(int nid, int ntype) {
		open();
		LocationData ret = null;
		Cursor c = database.query(DBHelper.DB_TABLE_LOCATION_DATA, null,
				DBHelper.LOCATION_DATA_COLUMN_NID + "=? AND "
						+ DBHelper.LOCATION_DATA_COLUMN_NTYPE + "=?",
				new String[] { String.valueOf(nid), String.valueOf(ntype) },
				null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		if (c.getCount() > 0) {
			ret = new LocationData();
			ret.lid = c.getInt(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_LID));
			ret.nid = c.getInt(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_NID));
			ret.ntype = c.getInt(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_NTYPE));
			ret.created = c.getString(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_CREATED));
			ret.modified = c.getString(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_MODIFIED));
			ret.longitude = c.getDouble(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_LONGITUDE));
			ret.latitude = c.getDouble(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_LATITUDE));
			ret.place = c.getString(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_PLACE));
		}
		close();
		return ret;
	}

	public boolean hasLocation(int nid, int ntype) {
		open();

		Cursor c = database.query(DBHelper.DB_TABLE_LOCATION_DATA, null,
				DBHelper.LOCATION_DATA_COLUMN_NID + "=? AND "
						+ DBHelper.LOCATION_DATA_COLUMN_NTYPE + "=?",
				new String[] { String.valueOf(nid), String.valueOf(ntype) },
				null, null, null);
		int size = c.getCount();
		close();
		return (size > 0);
	}

	public ArrayList<LocationData> selectAll() {
		open();
		ArrayList<LocationData> ret = new ArrayList<LocationData>();
		Cursor c = database.query(DBHelper.DB_TABLE_LOCATION_DATA, null, null,
				null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			LocationData temp = new LocationData();
			temp.lid = c.getInt(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_LID));
			temp.nid = c.getInt(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_NID));
			temp.ntype = c.getInt(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_NTYPE));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_MODIFIED));
			temp.longitude = c.getDouble(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_LONGITUDE));
			temp.latitude = c.getDouble(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_LATITUDE));
			temp.place = c.getString(c
					.getColumnIndex(DBHelper.LOCATION_DATA_COLUMN_PLACE));
			ret.add(temp);
		}
		close();
		return ret;

	}
}
