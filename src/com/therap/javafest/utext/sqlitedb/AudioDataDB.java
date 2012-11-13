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

import com.therap.javafest.utext.lib.AudioData;

public class AudioDataDB {
	private SQLiteDatabase database;
	private DBHelper helper;

	public AudioDataDB(Context context) {
		helper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public long insert(long mid, String audioUri) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.AUDIO_DATA_COLUMN_MID, String.valueOf(mid));
		contentValues.put(DBHelper.AUDIO_DATA_COLUMN_CREATED, curDateTime);
		contentValues.put(DBHelper.AUDIO_DATA_COLUMN_AUDIO_URI, audioUri);
		contentValues.put(DBHelper.AUDIO_DATA_COLUMN_MODIFIED, curDateTime);
		long id = database.insert(DBHelper.DB_TABLE_AUDIO_DATA, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int aid) {
		open();
		database.delete(DBHelper.DB_TABLE_AUDIO_DATA,
				DBHelper.AUDIO_DATA_COLUMN_AID + "=?",
				new String[] { String.valueOf(aid) });
		close();
	}

	public boolean hasAudio(int mid) {
		open();
		Cursor c = database.query(DBHelper.DB_TABLE_AUDIO_DATA, null,
				DBHelper.AUDIO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) }, null, null, null);
		int size = c.getCount();
		close();
		return (size > 0);
	}

	public ArrayList<AudioData> selectByMid(int mid) {
		open();
		ArrayList<AudioData> ret = new ArrayList<AudioData>();
		Cursor c = database.query(DBHelper.DB_TABLE_AUDIO_DATA, null,
				DBHelper.AUDIO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) }, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			AudioData temp = new AudioData();
			temp.aid = c.getInt(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_AID));
			temp.mid = c.getInt(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_MID));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_MODIFIED));
			temp.audioUri = Uri.parse(c.getString(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_AUDIO_URI)));
			ret.add(temp);
		}
		close();
		return ret;
	}

	public ArrayList<AudioData> selectAll() {
		open();
		ArrayList<AudioData> ret = new ArrayList<AudioData>();
		Cursor c = database.query(DBHelper.DB_TABLE_AUDIO_DATA, null, null,
				null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			AudioData temp = new AudioData();
			temp.aid = c.getInt(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_AID));
			temp.mid = c.getInt(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_MID));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_MODIFIED));
			temp.audioUri = Uri.parse(c.getString(c
					.getColumnIndex(DBHelper.AUDIO_DATA_COLUMN_AUDIO_URI)));
			ret.add(temp);
		}
		close();
		return ret;
	}

	public void deleteAllByMid(int mid) {
		open();
		database.delete(DBHelper.DB_TABLE_AUDIO_DATA,
				DBHelper.AUDIO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) });
		close();
	}
}
