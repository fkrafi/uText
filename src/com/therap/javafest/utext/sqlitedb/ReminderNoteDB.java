package com.therap.javafest.utext.sqlitedb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.therap.javafest.utext.lib.NoteListItem;
import com.therap.javafest.utext.lib.ReminderNote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/************************************************************/
/*			DB_TABLE_REMINDER = "reminder"					*/
/*			REMINDER_COLUMN_RID = "rid"						*/
/*			REMINDER_COLUMN_CREATED = "created"				*/
/*			REMINDER_COLUMN_REMINDER_DATE = "rdate"			*/
/*			REMINDER_COLUMN_REMINDER_TIME = "rtime"			*/
/*			REMINDER_COLUMN_MODIFIED = "modified"			*/
/*			REMINDER_COLUMN_TEXT = "text" 					*/
/*			REMINDER_COLUMN_IS_IMPORTANT = "is_important"	*/
/*			REMINDER_COLUMN_IS_ACTIVE = "is_active"			*/
/*			REMINDER_COLUMN_IS_CLOUD = "is_cloud"			*/
/************************************************************/

public class ReminderNoteDB {
	private SQLiteDatabase database;
	private UTextDBHelper helper;

	public ReminderNoteDB(Context context) {
		helper = new UTextDBHelper(context);
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public long insert(String rdate, String rtime, String text, int important) {
		open();

		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();

		ContentValues contentValues = new ContentValues();
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_CREATED, curDateTime);
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_REMINDER_DATE, rdate);
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_REMINDER_TIME, rtime);
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_TEXT, text);
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_IS_IMPORTANT,
				String.valueOf(important));
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_IS_ACTIVE, 1);
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_IS_CLOUD, 0);
		long id = database.insert(UTextDBHelper.DB_TABLE_REMINDER, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int rid) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.REMINDER_COLUMN_IS_ACTIVE, 0);
		database.update(UTextDBHelper.DB_TABLE_REMINDER, contentValues,
				UTextDBHelper.REMINDER_COLUMN_RID + "=?",
				new String[] { String.valueOf(rid) });
	}

	public void update(int mid, String text, int important) {
	}

	public ArrayList<NoteListItem> selectForList() {
		open();
		NoteListItem temp;
		ArrayList<NoteListItem> ret = new ArrayList<NoteListItem>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_REMINDER,
				new String[] { UTextDBHelper.REMINDER_COLUMN_RID,
						UTextDBHelper.REMINDER_COLUMN_MODIFIED,
						UTextDBHelper.REMINDER_COLUMN_TEXT,
						UTextDBHelper.REMINDER_COLUMN_IS_ACTIVE }, null, null,
				null, null, null);
		int id = c.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_RID);
		int iModified = c
				.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_MODIFIED);
		int iText = c.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_TEXT);
		int iActive = c.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_IS_ACTIVE);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (c.getString(iActive).equals("1")) {
				temp = new NoteListItem(c.getString(id), NoteListItem.REMINDER,
						c.getString(iText), c.getString(iModified));
				ret.add(temp);
			}
		}
		close();
		return ret;
	}

	public ReminderNote selectByLsid(int rid) {
		open();
		ReminderNote ret = new ReminderNote();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_REMINDER, null,
				UTextDBHelper.REMINDER_COLUMN_RID + "=?",
				new String[] { String.valueOf(rid) }, null, null, null);
		int iRid = c.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_RID);
		int iCreated = c.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_CREATED);
		int iModified = c
				.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_MODIFIED);
		int iRDate = c
				.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_REMINDER_DATE);
		int iRTime = c
				.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_REMINDER_TIME);
		int iText = c.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_TEXT);
		int iImportant = c
				.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_IS_IMPORTANT);
		int iActive = c.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_IS_ACTIVE);
		int iCloud = c.getColumnIndex(UTextDBHelper.REMINDER_COLUMN_IS_CLOUD);

		if (c != null)
			c.moveToFirst();

		ret.rid = c.getInt(iRid);
		ret.created = c.getString(iCreated);
		ret.modified = c.getString(iModified);
		ret.rdate = c.getString(iRDate);
		ret.rtime = c.getString(iRTime);
		ret.text = c.getString(iText);
		ret.is_important = c.getInt(iImportant);
		ret.is_active = c.getInt(iActive);
		ret.is_cloud = c.getInt(iCloud);

		close();
		return ret;
	}
}
