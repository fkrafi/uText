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

/****************************************************************/
/*			DB_TABLE_IMAGE_DATA = "image_data";					*/
/*			IMAGE_DATA_COLUMN_IID = "iid";						*/
/*			IMAGE_DATA_COLUMN_MID = "mid";						*/
/*			IMAGE_DATA_COLUMN_CREATED = "created";				*/
/*			IMAGE_DATA_COLUMN_MODIFIED = "modified";			*/
/*			IMAGE_DATA_COLUMN_DATA = "data";					*/
/*			IMAGE_DATA_COLUMN_IS_ACTIVE = "is_active";			*/
/*			IMAGE_DATA_COLUMN_IS_CLOUD = "is_cloud";			*/
/****************************************************************/

public class ImageDataDB {
	private SQLiteDatabase database;
	private UTextDBHelper helper;

	public ImageDataDB(Context context) {
		helper = new UTextDBHelper(context);
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
		contentValues.put(UTextDBHelper.IMAGE_DATA_COLUMN_MID,
				String.valueOf(mid));
		contentValues.put(UTextDBHelper.IMAGE_DATA_COLUMN_CREATED, curDateTime);
		contentValues.put(UTextDBHelper.IMAGE_DATA_COLUMN_DATA, imageUri);
		contentValues
				.put(UTextDBHelper.IMAGE_DATA_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.IMAGE_DATA_COLUMN_IS_ACTIVE, "1");
		long id = database.insert(UTextDBHelper.DB_TABLE_IMAGE_DATA, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int mid, int iid) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues
				.put(UTextDBHelper.IMAGE_DATA_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.IMAGE_DATA_COLUMN_IS_ACTIVE, "0");
		database.update(UTextDBHelper.DB_TABLE_IMAGE_DATA, contentValues,
				UTextDBHelper.IMAGE_DATA_COLUMN_MID + "=? AND "
						+ UTextDBHelper.IMAGE_DATA_COLUMN_IID + "=?",
				new String[] { String.valueOf(mid), String.valueOf(iid) });
		close();
	}

	public void update(int mid, String text, int important) {
	}

	public boolean hasImage(int mid) {
		open();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_IMAGE_DATA, null,
				UTextDBHelper.IMAGE_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) }, null, null, null);
		int size = c.getCount();
		close();
		return (size > 0);
	}

	public ArrayList<ImageData> selectByMid(int Mid) {
		open();
		ArrayList<ImageData> ret = new ArrayList<ImageData>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_IMAGE_DATA, null,
				UTextDBHelper.IMAGE_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(Mid) }, null, null, null);
		int iid = c.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_IID);
		int iData = c.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_DATA);
		int iActive = c
				.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_IS_ACTIVE);
		if (c != null)
			c.moveToFirst();

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (c.getInt(iActive) == 1) {
				ImageData temp = new ImageData();
				temp.iid = c.getInt(iid);
				temp.bitmapUri = Uri.parse(c.getString(iData));
				ret.add(temp);
			}
		}
		close();
		return ret;
	}

	public ArrayList<ImageData> selectAll() {
		open();
		ArrayList<ImageData> ret = new ArrayList<ImageData>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_IMAGE_DATA, null,
				null, null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ImageData temp = new ImageData();
			temp.iid = c.getInt(c
					.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_IID));
			temp.mid = c.getInt(c
					.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_MID));
			temp.created = c.getString(c
					.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_MODIFIED));
			temp.bitmapUri = Uri.parse(c.getString(c
					.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_DATA)));
			temp.is_active = c.getInt(c
					.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_IS_ACTIVE));
			temp.is_cloud = c.getInt(c
					.getColumnIndex(UTextDBHelper.IMAGE_DATA_COLUMN_IS_CLOUD));
			ret.add(temp);
		}
		close();
		return ret;
	}
}
