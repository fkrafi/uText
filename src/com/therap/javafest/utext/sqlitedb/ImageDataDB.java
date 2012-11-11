package com.therap.javafest.utext.sqlitedb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.therap.javafest.utext.lib.ImageData;

public class ImageDataDB {
	private SQLiteDatabase database;
	private DBHelper helper;

	public ImageDataDB(Context context) {
		helper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public long insert(long mid, String imageUri) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.IMAGE_DATA_COLUMN_MID, String.valueOf(mid));
		contentValues.put(DBHelper.IMAGE_DATA_COLUMN_CREATED, curDateTime);
		contentValues.put(DBHelper.IMAGE_DATA_COLUMN_IMAGE_URI, imageUri);
		contentValues.put(DBHelper.IMAGE_DATA_COLUMN_MODIFIED, curDateTime);
		long id = database.insert(DBHelper.DB_TABLE_IMAGE_DATA, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int iid) {
		open();
		database.delete(DBHelper.DB_TABLE_IMAGE_DATA,
				DBHelper.IMAGE_DATA_COLUMN_IID + "=?",
				new String[] { String.valueOf(iid) });
		close();
	}

	public boolean hasImage(int mid) {
		open();
		Cursor c = database.query(DBHelper.DB_TABLE_IMAGE_DATA, null,
				DBHelper.IMAGE_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) }, null, null, null);
		int size = c.getCount();
		close();
		return (size > 0);
	}

	public ArrayList<ImageData> selectByMid(int mid) {
		open();
		ArrayList<ImageData> ret = new ArrayList<ImageData>();
		Cursor c = database.query(DBHelper.DB_TABLE_IMAGE_DATA, null,
				DBHelper.IMAGE_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) }, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ImageData temp = new ImageData();
			temp.iid = c.getInt(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_IID));
			temp.mid = c.getInt(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_MID));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_MODIFIED));
			temp.bitmapUri = Uri.parse(c.getString(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_IMAGE_URI)));
			ret.add(temp);
		}
		close();
		return ret;
	}

	public ArrayList<ImageData> selectAll() {
		open();
		ArrayList<ImageData> ret = new ArrayList<ImageData>();
		Cursor c = database.query(DBHelper.DB_TABLE_IMAGE_DATA, null, null,
				null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ImageData temp = new ImageData();
			temp.iid = c.getInt(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_IID));
			temp.mid = c.getInt(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_MID));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_MODIFIED));
			temp.bitmapUri = Uri.parse(c.getString(c
					.getColumnIndex(DBHelper.IMAGE_DATA_COLUMN_IMAGE_URI)));
			ret.add(temp);
		}
		close();
		return ret;
	}

	public void deleteAllByMid(int mid) {
		open();
		database.delete(DBHelper.DB_TABLE_IMAGE_DATA,
				DBHelper.IMAGE_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) });
		close();
	}
}
