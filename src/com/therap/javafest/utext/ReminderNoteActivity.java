package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem.Type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ReminderNoteActivity extends GDActivity implements OnClickListener {

	final static int ACTION_BAR_SAVE = 0;

	private Button bDate, bTime;
	private EditText etNote;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_reminder_note);

		addActionBarItem(Type.Save, ACTION_BAR_SAVE);

		init();
	}

	private void init() {

		bDate = (Button) findViewById(R.id.bDate);
		bDate.setOnClickListener(this);
		bTime = (Button) findViewById(R.id.bTime);
		bTime.setOnClickListener(this);

		etNote = (EditText) findViewById(R.id.etNote);

		Date now = new Date();

		DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
		bDate.setText(dateFormat.format(now));

		dateFormat = new SimpleDateFormat("h:mm a");
		bTime.setText(dateFormat.format(now));
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bDate:
			break;
		case R.id.bTime:
			break;
		}
	}
}
