package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AddReminderActivity extends GDActivity implements OnClickListener {

	private static final int ACTION_BAR_SAVE = 1;

	Button bDate, bTime, bImportant;

	boolean important = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_add_reminder);

		addActionBarItem(Type.Save, ACTION_BAR_SAVE);

		Init();
	}

	private void Init() {
		bDate = (Button) findViewById(R.id.bDate);
		bTime = (Button) findViewById(R.id.bTime);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("E, dd M yyyy");
		bDate.setText(dateFormat.format(date).toString());
		dateFormat = new SimpleDateFormat("hh:mm:ss a");
		bTime.setText(dateFormat.format(date));
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {

		}
		return super.onHandleActionBarItemClick(item, position);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bImportant:
			if (important) {
				important = false;
				bImportant.setCompoundDrawablesWithIntrinsicBounds(
						getBaseContext().getResources().getDrawable(
								R.drawable.ic_menu_star), null, null, null);
			} else {
				important = true;
				bImportant.setCompoundDrawablesWithIntrinsicBounds(
						getBaseContext().getResources().getDrawable(
								R.drawable.ic_menu_star_yellow), null, null,
						null);
			}
			break;
		}
	}
}
