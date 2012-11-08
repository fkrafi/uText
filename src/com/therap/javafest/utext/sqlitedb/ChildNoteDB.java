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

/****************************************************************/
/*			DB_TABLE_CHILD_NOTE = "child_note"					*/
/*			CHILD_NOTE_COLUMN_CID = "cid"						*/
/*			CHILD_NOTE_COLUMN_LSID = "lsid"						*/
/*			CHILD_NOTE_COLUMN_TEXT = "text"						*/
/*			CHILD_NOTE_COLUMN_IS_COMPLETE = "is_complete"		*/
/*			CHILD_NOTE_COLUMN_MODIFIED = "modified"				*/
/*			CHILD_NOTE_COLUMN_IS_ACTIVE = "is_active"			*/
/*			CHILD_NOTE_COLUMN_IS_CLOUD = "is_cloud"				*/
/****************************************************************/

public class ChildNoteDB {
	private SQLiteDatabase database;
	private UTextDBHelper helper;

	public ChildNoteDB(Context context) {
		helper = new UTextDBHelper(context);
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
		contentValues.put(UTextDBHelper.CHILD_NOTE_COLUMN_LSID,
				String.valueOf(lsid));
		contentValues.put(UTextDBHelper.CHILD_NOTE_COLUMN_TEXT, text);
		contentValues.put(UTextDBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE,
				String.valueOf(done));
		contentValues
				.put(UTextDBHelper.CHILD_NOTE_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.CHILD_NOTE_COLUMN_IS_ACTIVE, 1);
		contentValues.put(UTextDBHelper.CHILD_NOTE_COLUMN_IS_CLOUD, 0);
		long id = database.insert(UTextDBHelper.DB_TABLE_CHILD_NOTE, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int cid) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues
				.put(UTextDBHelper.CHILD_NOTE_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.CHILD_NOTE_COLUMN_IS_ACTIVE, 0);
		database.update(UTextDBHelper.DB_TABLE_CHILD_NOTE, contentValues,
				UTextDBHelper.CHILD_NOTE_COLUMN_CID + "=?",
				new String[] { String.valueOf(cid) });
	}

	public void update(int mid, String text, int important) {
	}

	public ArrayList<ChildNote> selectByLsid(int lsid) {
		open();
		ArrayList<ChildNote> ret = new ArrayList<ChildNote>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_CHILD_NOTE, null,
				UTextDBHelper.CHILD_NOTE_COLUMN_LSID + "=?",
				new String[] { String.valueOf(lsid) }, null, null, null);

		int iCID = c.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_CID);
		int iLSID = c.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_LSID);
		int iTEXT = c.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_TEXT);
		int iCOMPLETE = c
				.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE);
		int iMODIFIED = c
				.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_MODIFIED);
		int iACTIVE = c
				.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_IS_ACTIVE);
		int iCLOUD = c.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_IS_CLOUD);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ChildNote temp = new ChildNote();
			temp.cid = c.getInt(iCID);
			temp.lsid = c.getInt(iLSID);
			temp.text = c.getString(iTEXT);
			temp.is_complete = c.getInt(iCOMPLETE);
			temp.modified = c.getString(iMODIFIED);
			temp.is_active = c.getInt(iACTIVE);
			temp.is_cloud = c.getInt(iCLOUD);
			ret.add(temp);
		}

		close();
		return ret;
	}

	public ArrayList<ChildNote> selectAll() {
		open();
		ArrayList<ChildNote> ret = new ArrayList<ChildNote>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_CHILD_NOTE, null,
				null, null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ChildNote temp = new ChildNote();
			temp.cid = c.getInt(c
					.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_CID));
			temp.lsid = c.getInt(c
					.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_LSID));
			temp.text = c.getString(c
					.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_TEXT));
			temp.modified = c
					.getString(c
							.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE));
			temp.is_complete = c.getInt(c
					.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_MODIFIED));
			temp.is_active = c.getInt(c
					.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_IS_ACTIVE));
			temp.is_cloud = c.getInt(c
					.getColumnIndex(UTextDBHelper.CHILD_NOTE_COLUMN_IS_CLOUD));
			ret.add(temp);
		}
		close();
		return ret;
	}
}
