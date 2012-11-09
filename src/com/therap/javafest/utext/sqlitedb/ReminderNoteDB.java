package com.therap.javafest.utext.sqlitedb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.therap.javafest.utext.lib.Note;
import com.therap.javafest.utext.lib.ReminderNote;

public class ReminderNoteDB {
	private SQLiteDatabase database;
	private DBHelper helper;

	public ReminderNoteDB(Context context) {
		helper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public long insert(String rDate, String rTime, String text, int important) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.REMINDER_COLUMN_CREATED, curDateTime);
		contentValues.put(DBHelper.REMINDER_COLUMN_MODIFIED, curDateTime);
		contentValues.put(DBHelper.REMINDER_COLUMN_REMINDER_DATE, rDate);
		contentValues.put(DBHelper.REMINDER_COLUMN_REMINDER_TIME, rTime);
		contentValues.put(DBHelper.REMINDER_COLUMN_TEXT, text);
		contentValues.put(DBHelper.REMINDER_COLUMN_IS_IMPORTANT,
				String.valueOf(important));
		long id = database.insert(DBHelper.DB_TABLE_REMINDER, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int rid) {
		open();
		database.delete(DBHelper.DB_TABLE_REMINDER,
				DBHelper.REMINDER_COLUMN_RID + "=?",
				new String[] { String.valueOf(rid) });
		close();
	}

	public void update(int mid, String text, int important) {
	}

	public ArrayList<Note> selectForList() {
		open();
		Note temp;
		ArrayList<Note> ret = new ArrayList<Note>();
		Cursor c = database.query(DBHelper.DB_TABLE_REMINDER, null, null, null,
				null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			temp = new Note(
					c.getString(c.getColumnIndex(DBHelper.REMINDER_COLUMN_RID)),
					Note.REMINDER,
					c.getString(c.getColumnIndex(DBHelper.REMINDER_COLUMN_TEXT)),
					c.getString(c
							.getColumnIndex(DBHelper.REMINDER_COLUMN_MODIFIED)),
					c.getInt(c
							.getColumnIndex(DBHelper.REMINDER_COLUMN_IS_IMPORTANT)),
					false, false, false, false, 0);
			ret.add(temp);
		}
		close();
		return ret;
	}

	public ReminderNote selectByLsid(int rid) {
		open();
		ReminderNote ret = new ReminderNote();
		Cursor c = database.query(DBHelper.DB_TABLE_REMINDER, null,
				DBHelper.REMINDER_COLUMN_RID + "=?",
				new String[] { String.valueOf(rid) }, null, null, null);
		if (c != null)
			c.moveToFirst();
		ret.rid = c.getInt(c.getColumnIndex(DBHelper.REMINDER_COLUMN_RID));
		ret.created = c.getString(c
				.getColumnIndex(DBHelper.REMINDER_COLUMN_CREATED));
		ret.modified = c.getString(c
				.getColumnIndex(DBHelper.REMINDER_COLUMN_MODIFIED));
		ret.rdate = c.getString(c
				.getColumnIndex(DBHelper.REMINDER_COLUMN_REMINDER_DATE));
		ret.rtime = c.getString(c
				.getColumnIndex(DBHelper.REMINDER_COLUMN_REMINDER_TIME));
		ret.text = c.getString(c.getColumnIndex(DBHelper.REMINDER_COLUMN_TEXT));
		ret.is_important = c.getInt(c
				.getColumnIndex(DBHelper.REMINDER_COLUMN_IS_IMPORTANT));
		close();
		return ret;
	}

	public ArrayList<ReminderNote> selectAll() {
		open();
		ArrayList<ReminderNote> ret = new ArrayList<ReminderNote>();
		Cursor c = database.query(DBHelper.DB_TABLE_REMINDER, null, null, null,
				null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ReminderNote temp = new ReminderNote();
			temp.rid = c.getInt(c.getColumnIndex(DBHelper.REMINDER_COLUMN_RID));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.REMINDER_COLUMN_CREATED));
			temp.rdate = c.getString(c
					.getColumnIndex(DBHelper.REMINDER_COLUMN_REMINDER_DATE));
			temp.text = c.getString(c
					.getColumnIndex(DBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT));
			temp.rtime = c.getString(c
					.getColumnIndex(DBHelper.REMINDER_COLUMN_REMINDER_TIME));
			temp.text = c.getString(c
					.getColumnIndex(DBHelper.REMINDER_COLUMN_TEXT));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.REMINDER_COLUMN_MODIFIED));
			temp.is_important = c.getInt(c
					.getColumnIndex(DBHelper.REMINDER_COLUMN_IS_IMPORTANT));
			ret.add(temp);
		}
		close();
		return ret;
	}

	public void update(int rid, String rDate, String rTime, String text,
			int important) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.REMINDER_COLUMN_MODIFIED, curDateTime);
		contentValues.put(DBHelper.REMINDER_COLUMN_REMINDER_DATE, rDate);
		contentValues.put(DBHelper.REMINDER_COLUMN_REMINDER_TIME, rTime);
		contentValues.put(DBHelper.REMINDER_COLUMN_TEXT, text);
		contentValues.put(DBHelper.REMINDER_COLUMN_IS_IMPORTANT,
				String.valueOf(important));
		database.update(DBHelper.DB_TABLE_REMINDER, contentValues,
				DBHelper.REMINDER_COLUMN_RID + "=?",
				new String[] { String.valueOf(rid) });
		close();
	}
}
