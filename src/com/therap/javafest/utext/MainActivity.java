package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class MainActivity extends GDActivity implements OnClickListener {

	private static final int ACTION_BAR_SYNC = 1;
	private static final int ACTION_BAR_SEARCH = 2;
	private static final int ACTION_BAR_ADD = 3;

	private ListView lvNotes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_main);

		addActionBarItem(Type.Share, ACTION_BAR_SYNC);
		addActionBarItem(Type.Search, ACTION_BAR_SEARCH);
		addActionBarItem(Type.Add, ACTION_BAR_ADD);

		Init();
	}

	private void Init() {
		lvNotes = (ListView) findViewById(R.id.lvNotes);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_SYNC:
			startActivity(new Intent(this, SignInActivity.class));
			break;
		case ACTION_BAR_SEARCH:
			startActivity(new Intent(this, SearchActivity.class));
			break;
		case ACTION_BAR_ADD:
			startActivity(new Intent(this, AddReminderActivity.class));
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}

	public void onClick(View view) {
		switch (view.getId()) {

		}

	}
}
