package com.therap.javafest.utext.sqlitedb;

/************************************************************************/
/*			DB_TABLE_MULTIMEDIA_NOTE = "multimedia_note"				*/
/*			MULTIMEDIA_NOTE_COLUMN_MID = "mid"							*/
/*			MULTIMEDIA_NOTE_COLUMN_CREATED = "created"					*/
/*			MULTIMEDIA_NOTE_COLUMN_MODIFIED = "modified"				*/
/*			MULTIMEDIA_NOTE_COLUMN_TEXT = "text"						*/
/*			MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT = "is_important"		*/
/*			MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE = "is_active"				*/
/*			MULTIMEDIA_NOTE_COLUMN_IS_CLOUD = "is_cloud"				*/
/************************************************************************/

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
	private UTextDBHelper helper;

	public MultiMediaNoteDB(Context context) {
		helper = new UTextDBHelper(context);
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
		contentValues.put(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED,
				curDateTime);
		contentValues.put(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT, text);
		contentValues.put(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT,
				String.valueOf(important));
		contentValues.put(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED,
				curDateTime);
		contentValues.put(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE, "1");
		contentValues.put(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_CLOUD, "0");
		long id = database.insert(UTextDBHelper.DB_TABLE_MULTIMEDIA_NOTE, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int mid) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED,
				curDateTime);
		contentValues.put(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE, "0");
		database.update(UTextDBHelper.DB_TABLE_MULTIMEDIA_NOTE, contentValues,
				UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MID + "=?",
				new String[] { String.valueOf(mid) });
		close();
	}

	public void update(int mid, String text, int important) {
	}

	public ArrayList<Note> selectForList() {
		open();
		Note temp;
		ArrayList<Note> ret = new ArrayList<Note>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_MULTIMEDIA_NOTE,
				new String[] { UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MID,
						UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED,
						UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT,
						UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE }, null,
				null, null, null, null);
		int id = c.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MID);
		int iModified = c
				.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED);
		int iText = c.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT);
		int iActive = c
				.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (c.getString(iActive).equals("1")) {
				temp = new Note(c.getString(id), Note.MULTIMEDIA_NOTE,
						c.getString(iText), c.getString(iModified));
				ret.add(temp);
			}
		}
		close();
		return ret;
	}

	public MultiMediaNote selectByMid(int Mid) {
		open();
		MultiMediaNote ret = new MultiMediaNote();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_MULTIMEDIA_NOTE, null,
				UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MID + "=?",
				new String[] { String.valueOf(Mid) }, null, null, null);
		int iMid = c.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MID);
		int iCreated = c
				.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED);
		int iModified = c
				.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED);
		int iText = c.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT);
		int iImportant = c
				.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT);
		int iActive = c
				.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE);
		int iCloud = c
				.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_CLOUD);

		if (c != null)
			c.moveToFirst();

		ret.mid = c.getInt(iMid);
		ret.created = c.getString(iCreated);
		ret.modified = c.getString(iModified);
		ret.text = c.getString(iText);
		ret.is_important = c.getInt(iImportant);
		ret.is_active = c.getInt(iActive);
		ret.is_cloud = c.getInt(iCloud);

		close();
		return ret;
	}

	public ArrayList<MultiMediaNote> selectAll() {
		open();
		ArrayList<MultiMediaNote> ret = new ArrayList<MultiMediaNote>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_MULTIMEDIA_NOTE, null,
				null, null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			MultiMediaNote temp = new MultiMediaNote();
			temp.mid = c.getInt(c
					.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MID));
			temp.created = c
					.getString(c
							.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED));
			temp.modified = c
					.getString(c
							.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED));
			temp.text = c.getString(c
					.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT));
			temp.is_important = c
					.getInt(c
							.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT));
			temp.is_active = c
					.getInt(c
							.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE));
			temp.is_cloud = c
					.getInt(c
							.getColumnIndex(UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_CLOUD));
			ret.add(temp);
		}
		close();
		return ret;
	}
}
