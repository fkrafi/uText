package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends GDActivity implements OnClickListener {

	private static final int ACTION_BAR_SYNC = 1;
	private static final int ACTION_BAR_SEARCH = 2;
	private static final int ACTION_BAR_ADD = 3;

	private ListView lvNotes;
	private Dialog dialogAddNoteOption;

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
		lvNotes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				/*********** ListItemClicked **************/
			}
		});

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
			dialogAddNoteOption = new Dialog(MainActivity.this);
			dialogAddNoteOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogAddNoteOption.setContentView(R.layout.note_type_dialog_items);
			ListView lvAddNoteOptions = (ListView) dialogAddNoteOption
					.findViewById(R.id.lvAddNoteOptions);
			lvAddNoteOptions.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> adapter, View view,
						int pos, long id) {
					dialogAddNoteOption.dismiss();
					switch (pos) {
					case 0:
						startActivity(new Intent(MainActivity.this,
								AddMultiMediaNoteActivity.class));
						break;
					case 1:
						startActivity(new Intent(MainActivity.this,
								AddListNoteActivity.class));
						break;
					case 2:
						startActivity(new Intent(MainActivity.this,
								AddReminderActivity.class));
						break;
					}
				}
			});
			dialogAddNoteOption.show();
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}

	public void onClick(View view) {
	}
}
