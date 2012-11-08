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
	private UTextDBHelper helper;

	public AudioDataDB(Context context) {
		helper = new UTextDBHelper(context);
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
		contentValues.put(UTextDBHelper.AUDIO_DATA_COLUMN_MID,
				String.valueOf(mid));
		contentValues.put(UTextDBHelper.AUDIO_DATA_COLUMN_CREATED, curDateTime);
		contentValues.put(UTextDBHelper.AUDIO_DATA_COLUMN_DATA, audioUri);
		contentValues
				.put(UTextDBHelper.AUDIO_DATA_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.AUDIO_DATA_COLUMN_IS_ACTIVE, "1");
		long id = database.insert(UTextDBHelper.DB_TABLE_AUDIO_DATA, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int mid, int aid) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues
				.put(UTextDBHelper.AUDIO_DATA_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.AUDIO_DATA_COLUMN_IS_ACTIVE, "0");
		database.update(UTextDBHelper.DB_TABLE_AUDIO_DATA, contentValues,
				UTextDBHelper.AUDIO_DATA_COLUMN_MID + "=? AND "
						+ UTextDBHelper.AUDIO_DATA_COLUMN_AID + "=?",
				new String[] { String.valueOf(mid), String.valueOf(aid) });
		close();
	}

	public void update(int mid, String text, int important) {
	}

	public boolean hasAudio(int mid) {
		open();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_AUDIO_DATA, null,
				UTextDBHelper.AUDIO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) }, null, null, null);
		close();
		return (c.getCount() > 0);
	}

	public ArrayList<AudioData> selectByMid(int Mid) {
		open();
		ArrayList<AudioData> ret = new ArrayList<AudioData>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_AUDIO_DATA, null,
				UTextDBHelper.AUDIO_DATA_COLUMN_MID + "=?",
				new String[] { String.valueOf(Mid) }, null, null, null);
		int iid = c.getColumnIndex(UTextDBHelper.AUDIO_DATA_COLUMN_AID);
		int iData = c.getColumnIndex(UTextDBHelper.AUDIO_DATA_COLUMN_DATA);

		if (c != null)
			c.moveToFirst();

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			AudioData temp = new AudioData();
			temp.aid = c.getInt(iid);
			temp.audioUri = Uri.parse(c.getString(iData));
			ret.add(temp);
		}
		close();
		return ret;
	}
}
