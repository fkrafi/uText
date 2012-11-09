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

import com.therap.javafest.utext.lib.VideoData;

public class VideoDataDB {
	private SQLiteDatabase database;
	private DBHelper helper;

	public VideoDataDB(Context context) {
		helper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public long insert(long mid, String videoUri) {
		open();

		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();

		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.VIDEO_DATA_COLUMN_MID, String.valueOf(mid));
		contentValues.put(DBHelper.VIDEO_DATA_COLUMN_CREATED, curDateTime);
		contentValues.put(DBHelper.VIDEO_DATA_COLUMN_MODIFIED, curDateTime);
		contentValues.put(DBHelper.VIDEO_DATA_COLUMN_VIDEO_URI, videoUri);
		long id = database.insert(DBHelper.DB_TABLE_VIDEO_DATA, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int vid) {
		open();
		database.delete(DBHelper.DB_TABLE_VIDEO_DATA,
				DBHelper.VIDEO_DATA_COLUMN_VID + "=?",
				new String[] { String.valueOf(vid) });
		close();
	}

	public void update(int mid, String text, int important) {
	}

	public boolean hasVideo(int mid) {
		open();
		Cursor c = database.query(DBHelper.DB_TABLE_VIDEO_DATA, null,
				DBHelper.VIDEO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) }, null, null, null);
		int size = c.getCount();
		close();
		return (size > 0);
	}

	public ArrayList<VideoData> selectByMid(int mid) {
		open();
		ArrayList<VideoData> ret = new ArrayList<VideoData>();
		Cursor c = database.query(DBHelper.DB_TABLE_VIDEO_DATA, null,
				DBHelper.VIDEO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) }, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			VideoData temp = new VideoData();
			temp.vid = c.getInt(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_VID));
			temp.mid = c.getInt(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_MID));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_MODIFIED));
			temp.videoUri = Uri.parse(c.getString(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_VIDEO_URI)));
			ret.add(temp);
		}
		close();
		return ret;
	}

	public ArrayList<VideoData> selectAll() {
		open();
		ArrayList<VideoData> ret = new ArrayList<VideoData>();
		Cursor c = database.query(DBHelper.DB_TABLE_VIDEO_DATA, null, null,
				null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			VideoData temp = new VideoData();
			temp.vid = c.getInt(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_VID));
			temp.mid = c.getInt(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_MID));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_MODIFIED));
			temp.videoUri = Uri.parse(c.getString(c
					.getColumnIndex(DBHelper.VIDEO_DATA_COLUMN_VIDEO_URI)));
			ret.add(temp);
		}
		close();
		return ret;
	}

	public void deleteAllByMid(int mid) {
		open();
		database.delete(DBHelper.DB_TABLE_VIDEO_DATA,
				DBHelper.VIDEO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) });
		close();
	}
}
