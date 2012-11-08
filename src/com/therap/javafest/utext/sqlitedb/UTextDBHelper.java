package com.therap.javafest.utext.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UTextDBHelper extends SQLiteOpenHelper {

	public static final int DB_VERSION = 3;
	public static final String DB_NAME = "utextdb.db";

	/** multimedia_note(mid, created, modified, text, is_important, is_active) **/
	public static final String DB_TABLE_MULTIMEDIA_NOTE = "multimedia_note";
	public static final String MULTIMEDIA_NOTE_COLUMN_MID = "mid";
	public static final String MULTIMEDIA_NOTE_COLUMN_CREATED = "created";
	public static final String MULTIMEDIA_NOTE_COLUMN_MODIFIED = "modified";
	public static final String MULTIMEDIA_NOTE_COLUMN_TEXT = "text";
	public static final String MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT = "is_important";
	public static final String MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE = "is_active";
	public static final String MULTIMEDIA_NOTE_COLUMN_IS_CLOUD = "is_cloud";

	/** audio_data(aid, mid, created, modified, data, is_active) **/
	public static final String DB_TABLE_AUDIO_DATA = "audio_data";
	public static final String AUDIO_DATA_COLUMN_AID = "aid";
	public static final String AUDIO_DATA_COLUMN_MID = "mid";
	public static final String AUDIO_DATA_COLUMN_CREATED = "created";
	public static final String AUDIO_DATA_COLUMN_MODIFIED = "modified";
	public static final String AUDIO_DATA_COLUMN_DATA = "data";
	public static final String AUDIO_DATA_COLUMN_IS_ACTIVE = "is_active";
	public static final String AUDIO_DATA_COLUMN_IS_CLOUD = "is_cloud";

	/** image_data(iid, mid, created, modified, data, is_active) **/
	public static final String DB_TABLE_IMAGE_DATA = "image_data";
	public static final String IMAGE_DATA_COLUMN_IID = "iid";
	public static final String IMAGE_DATA_COLUMN_MID = "mid";
	public static final String IMAGE_DATA_COLUMN_CREATED = "created";
	public static final String IMAGE_DATA_COLUMN_MODIFIED = "modified";
	public static final String IMAGE_DATA_COLUMN_DATA = "data";
	public static final String IMAGE_DATA_COLUMN_IS_ACTIVE = "is_active";
	public static final String IMAGE_DATA_COLUMN_IS_CLOUD = "is_cloud";

	/** video_data(vid, mid, created, modified, data, is_active) **/
	public static final String DB_TABLE_VIDEO_DATA = "video_data";
	public static final String VIDEO_DATA_COLUMN_VID = "vid";
	public static final String VIDEO_DATA_COLUMN_MID = "mid";
	public static final String VIDEO_DATA_COLUMN_CREATED = "created";
	public static final String VIDEO_DATA_COLUMN_MODIFIED = "modified";
	public static final String VIDEO_DATA_COLUMN_DATA = "data";
	public static final String VIDEO_DATA_COLUMN_IS_ACTIVE = "is_active";
	public static final String VIDEO_DATA_COLUMN_IS_CLOUD = "is_cloud";

	/** location(lid, mid, created, modified, X, Y, place, is_active) **/
	public static final String DB_TABLE_LOCATION_DATA = "location_data";
	public static final String LOCATION_DATA_COLUMN_LID = "lid";
	public static final String LOCATION_DATA_COLUMN_MID = "mid";
	public static final String LOCATION_DATA_COLUMN_CREATED = "created";
	public static final String LOCATION_DATA_COLUMN_MODIFIED = "modified";
	public static final String LOCATION_DATA_COLUMN_X = "x";
	public static final String LOCATION_DATA_COLUMN_Y = "y";
	public static final String LOCATION_DATA_COLUMN_PLACE = "place";
	public static final String LOCATION_DATA_COLUMN_IS_ACTIVE = "is_active";
	public static final String LOCATION_DATA_COLUMN_IS_CLOUD = "is_cloud";

	/** list_note(lsid, title, created, modified, is_active, is_important) **/
	public static final String DB_TABLE_LIST_NOTE = "list_note";
	public static final String LIST_NOTE_COLUMN_LSID = "lsid";
	public static final String LIST_NOTE_COLUMN_TITLE = "title";
	public static final String LIST_NOTE_COLUMN_CREATED = "created";
	public static final String LIST_NOTE_COLUMN_MODIFIED = "modified";
	public static final String LIST_NOTE_COLUMN_IS_IMPORTANT = "is_important";
	public static final String LIST_NOTE_COLUMN_IS_ACTIVE = "is_active";
	public static final String LIST_NOTE_COLUMN_IS_CLOUD = "is_cloud";

	/** child_note(cid, lsid, text, modified, is_complete, is_active) **/
	public static final String DB_TABLE_CHILD_NOTE = "child_note";
	public static final String CHILD_NOTE_COLUMN_CID = "cid";
	public static final String CHILD_NOTE_COLUMN_LSID = "lsid";
	public static final String CHILD_NOTE_COLUMN_TEXT = "text";
	public static final String CHILD_NOTE_COLUMN_MODIFIED = "modified";
	public static final String CHILD_NOTE_COLUMN_IS_COMPLETE = "is_complete";
	public static final String CHILD_NOTE_COLUMN_IS_ACTIVE = "is_active";
	public static final String CHILD_NOTE_COLUMN_IS_CLOUD = "is_cloud";

	/**
	 * reminder(rid, created, modified, rdate, rtime, text, is_important,
	 * is_active)
	 **/
	public static final String DB_TABLE_REMINDER = "reminder";
	public static final String REMINDER_COLUMN_RID = "rid";
	public static final String REMINDER_COLUMN_CREATED = "created";
	public static final String REMINDER_COLUMN_REMINDER_DATE = "rdate";
	public static final String REMINDER_COLUMN_REMINDER_TIME = "rtime";
	public static final String REMINDER_COLUMN_TEXT = "text";
	public static final String REMINDER_COLUMN_MODIFIED = "modified";
	public static final String REMINDER_COLUMN_IS_IMPORTANT = "is_important";
	public static final String REMINDER_COLUMN_IS_ACTIVE = "is_active";
	public static final String REMINDER_COLUMN_IS_CLOUD = "is_cloud";

	public UTextDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DB_TABLE_MULTIMEDIA_NOTE + "("
				+ MULTIMEDIA_NOTE_COLUMN_MID
				+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ MULTIMEDIA_NOTE_COLUMN_CREATED + " TEXT, "
				+ MULTIMEDIA_NOTE_COLUMN_MODIFIED + " TEXT, "
				+ MULTIMEDIA_NOTE_COLUMN_TEXT + " TEXT, "
				+ MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT + " INTEGER(2), "
				+ MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE + " INTEGER(2),"
				+ MULTIMEDIA_NOTE_COLUMN_IS_CLOUD + " INTEGER(2))");

		/** Create Table audio_data **/
		db.execSQL("CREATE TABLE " + DB_TABLE_AUDIO_DATA + "("
				+ AUDIO_DATA_COLUMN_AID
				+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ AUDIO_DATA_COLUMN_MID + " INTEGER, "
				+ AUDIO_DATA_COLUMN_CREATED + " TEXT, "
				+ AUDIO_DATA_COLUMN_MODIFIED + " TEXT, "
				+ AUDIO_DATA_COLUMN_DATA + " TEXT, "
				+ AUDIO_DATA_COLUMN_IS_ACTIVE + " INTEGER(2),"
				+ AUDIO_DATA_COLUMN_IS_CLOUD + " INTEGER(2))");

		/** Create Table image_data **/
		db.execSQL("CREATE TABLE " + DB_TABLE_IMAGE_DATA + "("
				+ IMAGE_DATA_COLUMN_IID
				+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ IMAGE_DATA_COLUMN_MID + " INTEGER, "
				+ IMAGE_DATA_COLUMN_CREATED + " TEXT, "
				+ IMAGE_DATA_COLUMN_MODIFIED + " TEXT, "
				+ IMAGE_DATA_COLUMN_DATA + " blob, "
				+ IMAGE_DATA_COLUMN_IS_ACTIVE + " INTEGER(2),"
				+ IMAGE_DATA_COLUMN_IS_CLOUD + " INTEGER(2))");

		/** Create Table video_data **/
		db.execSQL("CREATE TABLE " + DB_TABLE_VIDEO_DATA + "("
				+ VIDEO_DATA_COLUMN_VID
				+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ VIDEO_DATA_COLUMN_MID + " INTEGER, "
				+ VIDEO_DATA_COLUMN_CREATED + " TEXT, "
				+ VIDEO_DATA_COLUMN_MODIFIED + " TEXT, "
				+ VIDEO_DATA_COLUMN_DATA + " TEXT, "
				+ VIDEO_DATA_COLUMN_IS_ACTIVE + " INTEGER(2),"
				+ VIDEO_DATA_COLUMN_IS_CLOUD + " INTEGER(2))");

		/** Create Table location_data **/
		db.execSQL("CREATE TABLE " + DB_TABLE_LOCATION_DATA + "("
				+ LOCATION_DATA_COLUMN_LID
				+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ LOCATION_DATA_COLUMN_MID + " INTEGER, "
				+ LOCATION_DATA_COLUMN_CREATED + " TEXT, "
				+ LOCATION_DATA_COLUMN_MODIFIED + " TEXT, "
				+ LOCATION_DATA_COLUMN_X + " REAL, " + LOCATION_DATA_COLUMN_Y
				+ " REAL, " + LOCATION_DATA_COLUMN_PLACE + " TEXT, "
				+ LOCATION_DATA_COLUMN_IS_ACTIVE + " INTEGER(2),"
				+ LOCATION_DATA_COLUMN_IS_CLOUD + " INTEGER(2))");

		/** CREATE TABLE list_note **/
		db.execSQL("CREATE TABLE " + DB_TABLE_LIST_NOTE + "("
				+ LIST_NOTE_COLUMN_LSID
				+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ LIST_NOTE_COLUMN_CREATED + " TEXT, "
				+ LIST_NOTE_COLUMN_MODIFIED + " TEXT, "
				+ LIST_NOTE_COLUMN_TITLE + " TEXT, "
				+ LIST_NOTE_COLUMN_IS_IMPORTANT + " INTEGER(2), "
				+ LIST_NOTE_COLUMN_IS_ACTIVE + " INTEGER(2),"
				+ LIST_NOTE_COLUMN_IS_CLOUD + " INTEGER(2))");

		/** CREATE TABLE child_note **/
		db.execSQL("CREATE TABLE " + DB_TABLE_CHILD_NOTE + "("
				+ CHILD_NOTE_COLUMN_CID
				+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ CHILD_NOTE_COLUMN_LSID + " INTEGER, "
				+ CHILD_NOTE_COLUMN_TEXT + " TEXT, "
				+ CHILD_NOTE_COLUMN_MODIFIED + " TEXT, "
				+ CHILD_NOTE_COLUMN_IS_COMPLETE + " INTEGER(2), "
				+ CHILD_NOTE_COLUMN_IS_ACTIVE + " INTEGER(2),"
				+ CHILD_NOTE_COLUMN_IS_CLOUD + " INTEGER(2))");

		/** CREATE TABLE reminder **/
		db.execSQL("CREATE TABLE " + DB_TABLE_REMINDER + "("
				+ REMINDER_COLUMN_RID
				+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ REMINDER_COLUMN_CREATED + " TEXT, "
				+ REMINDER_COLUMN_REMINDER_DATE + " TEXT, "
				+ REMINDER_COLUMN_REMINDER_TIME + " TEXT, "
				+ REMINDER_COLUMN_MODIFIED + " TEXT, " + REMINDER_COLUMN_TEXT
				+ " TEXT, " + REMINDER_COLUMN_IS_IMPORTANT + " INTEGER(2), "
				+ REMINDER_COLUMN_IS_ACTIVE + " INTEGER(2),"
				+ REMINDER_COLUMN_IS_CLOUD + " INTEGER(2))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_MULTIMEDIA_NOTE);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_AUDIO_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_IMAGE_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_VIDEO_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_LOCATION_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_LIST_NOTE);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_CHILD_NOTE);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_REMINDER);
		onCreate(db);

	}

}
