package com.therap.javafest.utext.sqlitedb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.therap.javafest.utext.lib.ListNote;
import com.therap.javafest.utext.lib.Note;

public class ListNoteDB {
	private SQLiteDatabase database;
	private DBHelper helper;

	public ListNoteDB(Context context) {
		helper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public long insert(String title, int important) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.LIST_NOTE_COLUMN_CREATED, curDateTime);
		contentValues.put(DBHelper.LIST_NOTE_COLUMN_TITLE, title);
		contentValues.put(DBHelper.LIST_NOTE_COLUMN_MODIFIED, curDateTime);
		contentValues.put(DBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT,
				String.valueOf(important));
		long id = database.insert(DBHelper.DB_TABLE_LIST_NOTE, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int lsid) {
		open();
		database.delete(DBHelper.DB_TABLE_LIST_NOTE,
				DBHelper.LIST_NOTE_COLUMN_LSID + "=?",
				new String[] { String.valueOf(lsid) });
		close();
	}

	public void update(int lsid, String title, int important) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.LIST_NOTE_COLUMN_TITLE, title);
		contentValues.put(DBHelper.LIST_NOTE_COLUMN_MODIFIED, curDateTime);
		contentValues.put(DBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT,
				String.valueOf(important));
		database.update(DBHelper.DB_TABLE_LIST_NOTE, contentValues,
				DBHelper.LIST_NOTE_COLUMN_LSID + "=?",
				new String[] { String.valueOf(lsid) });
		close();
	}

	public ArrayList<Note> selectForList() {
		open();
		Note temp;
		ArrayList<Note> ret = new ArrayList<Note>();
		Cursor c = database.query(DBHelper.DB_TABLE_LIST_NOTE, null, null,
				null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			temp = new Note(
					c.getString(c
							.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_LSID)),
					Note.LIST_NOTE,
					c.getString(c
							.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_TITLE)),
					c.getString(c
							.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_MODIFIED)),
					c.getInt(c
							.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT)),
					false, false, false, false, 0);
			ret.add(temp);
		}
		close();
		return ret;
	}

	public ListNote selectByLsid(int lsid) {
		open();
		ListNote ret = new ListNote();
		Cursor c = database.query(DBHelper.DB_TABLE_LIST_NOTE, null,
				DBHelper.LIST_NOTE_COLUMN_LSID + "=?",
				new String[] { String.valueOf(lsid) }, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		ret.lsid = c.getInt(c.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_LSID));
		ret.created = c.getString(c
				.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_CREATED));
		ret.title = c.getString(c
				.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_TITLE));
		ret.modified = c.getString(c
				.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_MODIFIED));
		ret.is_important = c.getInt(c
				.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT));
		close();
		return ret;
	}

	public ArrayList<ListNote> selectAll() {
		open();
		ArrayList<ListNote> ret = new ArrayList<ListNote>();
		Cursor c = database.query(DBHelper.DB_TABLE_LIST_NOTE, null, null,
				null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ListNote temp = new ListNote();
			temp.lsid = c.getInt(c
					.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_LSID));
			temp.title = c.getString(c
					.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_TITLE));
			temp.created = c.getString(c
					.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_CREATED));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_MODIFIED));
			temp.is_important = c.getInt(c
					.getColumnIndex(DBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT));
			ret.add(temp);
		}
		close();
		return ret;
	}
}
