package com.therap.javafest.utext.sqlitedb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.therap.javafest.utext.lib.ChildNote;

public class ChildNoteDB {
	private SQLiteDatabase database;
	private DBHelper helper;

	public ChildNoteDB(Context context) {
		helper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public long insert(long lsid, String text, int done) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues
				.put(DBHelper.CHILD_NOTE_COLUMN_LSID, String.valueOf(lsid));
		contentValues.put(DBHelper.CHILD_NOTE_COLUMN_TEXT, text);
		contentValues.put(DBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE,
				String.valueOf(done));
		contentValues.put(DBHelper.CHILD_NOTE_COLUMN_MODIFIED, curDateTime);
		long id = database.insert(DBHelper.DB_TABLE_CHILD_NOTE, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int cid) {
		open();
		database.delete(DBHelper.DB_TABLE_CHILD_NOTE,
				DBHelper.CHILD_NOTE_COLUMN_CID + "=?",
				new String[] { String.valueOf(cid) });
		close();
	}

	public void update(int cid, String text, int complete) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.LIST_NOTE_COLUMN_MODIFIED, curDateTime);
		contentValues.put(DBHelper.CHILD_NOTE_COLUMN_TEXT, text);
		contentValues.put(DBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE, complete);
		database.update(DBHelper.DB_TABLE_CHILD_NOTE, contentValues,
				DBHelper.CHILD_NOTE_COLUMN_CID + "=?",
				new String[] { String.valueOf(cid) });
		close();
	}

	public ArrayList<ChildNote> selectByLsid(int lsid) {
		open();
		ArrayList<ChildNote> ret = new ArrayList<ChildNote>();
		Cursor c = database.query(DBHelper.DB_TABLE_CHILD_NOTE, null,
				DBHelper.CHILD_NOTE_COLUMN_LSID + "=?",
				new String[] { String.valueOf(lsid) }, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ChildNote temp = new ChildNote();
			temp.cid = c.getInt(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_CID));
			temp.lsid = c.getInt(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_LSID));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_MODIFIED));
			temp.text = c.getString(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_TEXT));
			temp.is_complete = c.getInt(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE));
			ret.add(temp);
		}
		close();
		return ret;
	}

	public ArrayList<ChildNote> selectAll() {
		open();
		ArrayList<ChildNote> ret = new ArrayList<ChildNote>();
		Cursor c = database.query(DBHelper.DB_TABLE_CHILD_NOTE, null, null,
				null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ChildNote temp = new ChildNote();
			temp.cid = c.getInt(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_CID));
			temp.lsid = c.getInt(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_LSID));
			temp.modified = c.getString(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_MODIFIED));
			temp.text = c.getString(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_TEXT));
			temp.is_complete = c.getInt(c
					.getColumnIndex(DBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE));
			ret.add(temp);
		}
		close();
		return ret;
	}

	public void deleteAllChildByLsid(int lsid) {
		open();
		database.delete(DBHelper.DB_TABLE_CHILD_NOTE,
				DBHelper.CHILD_NOTE_COLUMN_LSID + "=?",
				new String[] { String.valueOf(lsid) });
		close();
	}
}
