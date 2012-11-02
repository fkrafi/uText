package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.os.Bundle;

public class ViewReminderActivity extends GDActivity {

	private static final int ACTION_BAR_DELETE = 1;
	private static final int ACTION_BAR_EDIT = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_reminder);

		addActionBarItem(Type.Trashcan, ACTION_BAR_DELETE);
		addActionBarItem(Type.Edit, ACTION_BAR_EDIT);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {

		}
		return super.onHandleActionBarItemClick(item, position);
	}
}
