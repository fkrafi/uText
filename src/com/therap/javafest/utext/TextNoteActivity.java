package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem.Type;
import android.os.Bundle;

public class TextNoteActivity extends GDActivity {

	final static int ACTION_BAR_SAVE = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_text_note);

		addActionBarItem(Type.Save, ACTION_BAR_SAVE);
	}

}
