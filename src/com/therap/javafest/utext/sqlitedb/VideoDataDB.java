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
	private UTextDBHelper helper;

	public VideoDataDB(Context context) {
		helper = new UTextDBHelper(context);
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
		contentValues.put(UTextDBHelper.VIDEO_DATA_COLUMN_MID,
				String.valueOf(mid));
		contentValues.put(UTextDBHelper.VIDEO_DATA_COLUMN_CREATED, curDateTime);
		contentValues.put(UTextDBHelper.VIDEO_DATA_COLUMN_DATA, videoUri);
		contentValues
				.put(UTextDBHelper.VIDEO_DATA_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.VIDEO_DATA_COLUMN_IS_ACTIVE, "1");
		long id = database.insert(UTextDBHelper.DB_TABLE_VIDEO_DATA, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int mid, int vid) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues
				.put(UTextDBHelper.VIDEO_DATA_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.VIDEO_DATA_COLUMN_IS_ACTIVE, "0");
		database.update(UTextDBHelper.DB_TABLE_VIDEO_DATA, contentValues,
				UTextDBHelper.VIDEO_DATA_COLUMN_MID + "=? AND "
						+ UTextDBHelper.VIDEO_DATA_COLUMN_VID + "=?",
				new String[] { String.valueOf(mid), String.valueOf(vid) });
		close();
	}

	public void update(int mid, String text, int important) {
	}

	public boolean hasVideo(int mid) {
		open();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_VIDEO_DATA, null,
				UTextDBHelper.VIDEO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) }, null, null, null);
		close();
		return (c.getCount() > 0);
	}

	public ArrayList<VideoData> selectByMid(int Mid) {
		open();
		ArrayList<VideoData> ret = new ArrayList<VideoData>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_VIDEO_DATA, null,
				UTextDBHelper.VIDEO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(Mid) }, null, null, null);
		int vid = c.getColumnIndex(UTextDBHelper.VIDEO_DATA_COLUMN_VID);
		int iData = c.getColumnIndex(UTextDBHelper.VIDEO_DATA_COLUMN_DATA);

		if (c != null)
			c.moveToFirst();

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			VideoData temp = new VideoData();
			temp.vid = c.getInt(vid);
			temp.videoUri = Uri.parse(c.getString(iData));
			ret.add(temp);
		}
		close();
		return ret;
	}
}
