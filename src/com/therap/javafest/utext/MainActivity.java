package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends GDActivity implements OnClickListener {

	private static final int ACTION_BAR_INFO = 0;
	private static final int ACTION_BAR_SETTINGS = 1;
	private static final int ACTION_BAR_SEARCH = 2;

	private Button bTextNote, bImageNote, bVideoNote, bReminder, bListNote,
			bAudioNote;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_main);

		addActionBarItem(Type.Info, ACTION_BAR_INFO);
		addActionBarItem(Type.Settings, ACTION_BAR_SETTINGS);
		addActionBarItem(Type.Search, ACTION_BAR_SEARCH);

		Init();
	}

	private void Init() {
		bTextNote = (Button) findViewById(R.id.bTextNote);
		bTextNote.setOnClickListener(this);
		bImageNote = (Button) findViewById(R.id.bImageNote);
		bImageNote.setOnClickListener(this);
		bVideoNote = (Button) findViewById(R.id.bVideoNote);
		bVideoNote.setOnClickListener(this);
		bReminder = (Button) findViewById(R.id.bReminder);
		bReminder.setOnClickListener(this);
		bListNote = (Button) findViewById(R.id.bListNote);
		bListNote.setOnClickListener(this);
		bAudioNote = (Button) findViewById(R.id.bAudioNote);
		bAudioNote.setOnClickListener(this);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case ACTION_BAR_SEARCH:
			startActivity(new Intent(this, SearchActivity.class));
		}
		return super.onHandleActionBarItemClick(item, position);
	}

	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.bTextNote:
			startActivity(new Intent(this, TextNoteActivity.class));
			break;
		case R.id.bImageNote:
			startActivity(new Intent(this, ImageNoteActivity.class));
			break;
		case R.id.bVideoNote:
			startActivity(new Intent(this, VideoNoteActivity.class));
			break;
		case R.id.bAudioNote:
			startActivity(new Intent(this, AudioNoteActivity.class));
			break;
		case R.id.bListNote:
			startActivity(new Intent(this, ListNoteActivity.class));
			break;
		case R.id.bReminder:
			startActivity(new Intent(this, ReminderNoteActivity.class));
			break;
		}

	}
}