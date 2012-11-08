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
import com.therap.javafest.utext.lib.NoteListItem;

/****************************************************************/
/*			DB_TABLE_LIST_NOTE = "list_note"					*/
/*			LIST_NOTE_COLUMN_LSID = "lsid"						*/
/*			LIST_NOTE_COLUMN_CREATED = "created"				*/
/*			LIST_NOTE_COLUMN_TITLE = "title"					*/
/*			LIST_NOTE_COLUMN_MODIFIED = "modified"				*/
/*			LIST_NOTE_COLUMN_IS_IMPORTANT = "is_important"		*/
/*			LIST_NOTE_COLUMN_IS_ACTIVE = "is_active"			*/
/*			LIST_NOTE_COLUMN_IS_CLOUD = "is_cloud"				*/
/****************************************************************/

public class ListNoteDB {
	private SQLiteDatabase database;
	private UTextDBHelper helper;

	public ListNoteDB(Context context) {
		helper = new UTextDBHelper(context);
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
		contentValues.put(UTextDBHelper.LIST_NOTE_COLUMN_CREATED, curDateTime);
		contentValues.put(UTextDBHelper.LIST_NOTE_COLUMN_TITLE, title);
		contentValues.put(UTextDBHelper.LIST_NOTE_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT,
				String.valueOf(important));
		contentValues.put(UTextDBHelper.LIST_NOTE_COLUMN_IS_ACTIVE, 1);
		contentValues.put(UTextDBHelper.LIST_NOTE_COLUMN_IS_CLOUD, 0);
		long id = database.insert(UTextDBHelper.DB_TABLE_LIST_NOTE, null,
				contentValues);
		close();
		return id;
	}

	public void delete(int lsid) {
		open();
		Date date = new Date();
		String curDateTime = (new Timestamp(date.getTime())).toString();
		ContentValues contentValues = new ContentValues();
		contentValues.put(UTextDBHelper.LIST_NOTE_COLUMN_MODIFIED, curDateTime);
		contentValues.put(UTextDBHelper.LIST_NOTE_COLUMN_IS_ACTIVE, "0");
		database.update(UTextDBHelper.DB_TABLE_LIST_NOTE, contentValues,
				UTextDBHelper.LIST_NOTE_COLUMN_LSID + "=?",
				new String[] { String.valueOf(lsid) });
	}

	public void update(int mid, String text, int important) {
	}

	public ArrayList<NoteListItem> selectForList() {
		open();
		NoteListItem temp;
		ArrayList<NoteListItem> ret = new ArrayList<NoteListItem>();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_LIST_NOTE,
				new String[] { UTextDBHelper.LIST_NOTE_COLUMN_LSID,
						UTextDBHelper.LIST_NOTE_COLUMN_TITLE,
						UTextDBHelper.LIST_NOTE_COLUMN_MODIFIED,
						UTextDBHelper.LIST_NOTE_COLUMN_IS_ACTIVE }, null, null,
				null, null, null);
		int id = c.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_LSID);
		int iModified = c
				.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_MODIFIED);
		int iText = c.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_TITLE);
		int iActive = c
				.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_IS_ACTIVE);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (c.getString(iActive).equals("1")) {
				temp = new NoteListItem(c.getString(id),
						NoteListItem.LIST_NOTE, c.getString(iText),
						c.getString(iModified));
				ret.add(temp);
			}
		}
		close();
		return ret;
	}

	public ListNote selectByLsid(int lsid) {
		open();
		ListNote ret = new ListNote();
		Cursor c = database.query(UTextDBHelper.DB_TABLE_LIST_NOTE, null,
				UTextDBHelper.LIST_NOTE_COLUMN_LSID + "=?",
				new String[] { String.valueOf(lsid) }, null, null, null);

		int iLSID = c.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_LSID);
		int iCREATED = c.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_CREATED);
		int iTITLE = c.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_TITLE);
		int iMODIFIED = c
				.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_MODIFIED);
		int iIS_IMPORTANT = c
				.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT);
		int iIS_ACTIVE = c
				.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_IS_ACTIVE);
		int iIS_CLOUD = c
				.getColumnIndex(UTextDBHelper.LIST_NOTE_COLUMN_IS_CLOUD);

		if (c != null) {
			c.moveToFirst();
		}

		ret.lsid = c.getInt(iLSID);
		ret.created = c.getString(iCREATED);
		ret.title = c.getString(iTITLE);
		ret.modified = c.getString(iMODIFIED);
		ret.is_important = c.getInt(iIS_IMPORTANT);
		ret.is_active = c.getInt(iIS_ACTIVE);
		ret.is_cloud = c.getInt(iIS_CLOUD);

		close();
		return ret;
	}
}
