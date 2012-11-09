package com.therap.javafest.utext.sqlitedb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.therap.javafest.utext.lib.MultiMediaNote;
import com.therap.javafest.utext.lib.Note;

public class MultiMediaNoteDB {

	private SQLiteDatabase database;
	private DBHelper helper;

	public MultiMediaNoteDB(Context context) {
		helper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public long insert(String text, int important) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED, curDateTime);
		contentValues.put(DBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT, text);
		contentValues.put(DBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT,
				String.valueOf(important));
		contentValues
				.put(DBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED, curDateTime);
		long id = database.insert(DBHelper.DB_TABLE_MULTIMEDIA_NOTE, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int mid) {
		open();
		database.delete(DBHelper.DB_TABLE_MULTIMEDIA_NOTE,
				DBHelper.MULTIMEDIA_NOTE_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) });
		close();
	}

	public void update(int mid, String text, int important) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED, curDateTime);
		contentValues.put(DBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT, text);
		contentValues.put(DBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT,
				String.valueOf(important));
		contentValues
				.put(DBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED, curDateTime);
		database.update(DBHelper.DB_TABLE_MULTIMEDIA_NOTE, contentValues,
				DBHelper.MULTIMEDIA_NOTE_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) });
		close();
	}

	public ArrayList<Note> selectForList() {
		open();
		Note temp;
		ArrayList<Note> ret = new ArrayList<Note>();
		Cursor c = database.query(DBHelper.DB_TABLE_MULTIMEDIA_NOTE, null,
				null, null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			temp = new Note(
					c.getString(c
							.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_MID)),
					Note.MULTIMEDIA_NOTE,
					c.getString(c
							.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT)),
					c.getString(c
							.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED)),
					c.getInt(c
							.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT)),
					false, false, false, false, 0);
			ret.add(temp);
		}
		close();
		return ret;
	}

	public MultiMediaNote selectByMid(int Mid) {
		open();
		MultiMediaNote ret = new MultiMediaNote();
		Cursor c = database.query(DBHelper.DB_TABLE_MULTIMEDIA_NOTE, null,
				DBHelper.MULTIMEDIA_NOTE_COLUMN_MID + "=?",
				new String[] { String.valueOf(Mid) }, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		ret.mid = c.getInt(c
				.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_MID));
		ret.created = c.getString(c
				.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED));
		ret.modified = c.getString(c
				.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED));
		ret.text = c.getString(c
				.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT));
		ret.is_important = c.getInt(c
				.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT));
		close();
		return ret;
	}

	public ArrayList<MultiMediaNote> selectAll() {
		open();
		ArrayList<MultiMediaNote> ret = new ArrayList<MultiMediaNote>();
		Cursor c = database.query(DBHelper.DB_TABLE_MULTIMEDIA_NOTE, null,
				null, null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			MultiMediaNote temp = new MultiMediaNote();
			temp.mid = c.getInt(c
					.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_MID));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED));
			temp.text = c.getString(c
					.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT));
			temp.is_important = c
					.getInt(c
							.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT));
			ret.add(temp);
		}
		close();
		return ret;
	}
}
