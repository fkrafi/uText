package com.therap.javafest.backup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ExportDB {

	Context context;
	SQLiteDatabase db;

	public ExportDB(Context context, SQLiteDatabase db) {
		this.context = context;
		this.db = db;
	}

	public void ExportAsXML() {
		DatabaseAssistant da = new DatabaseAssistant(context, db);
		da.exportData();
	}
}
