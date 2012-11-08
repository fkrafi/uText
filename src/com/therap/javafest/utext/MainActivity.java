package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.io.File;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.therap.javafest.utext.lib.NoteListItem;
import com.therap.javafest.utext.lib.NoteRetriever;

public class MainActivity extends GDActivity {

	private static final int ACTION_BAR_SYNC = 1;
	private static final int ACTION_BAR_SEARCH = 2;
	private static final int ACTION_BAR_ADD = 3;

	private ListView lvNotes;
	private Dialog dialogAddNoteOption;

	private NoteListViewItemAdapter adapter;
	private ArrayList<NoteListItem> allNotes;

	private ArrayList<NoteListItem> notes;
	private NoteRetriever noteRetriever;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_main);

		addActionBarItem(Type.Share, ACTION_BAR_SYNC);
		addActionBarItem(Type.Search, ACTION_BAR_SEARCH);
		addActionBarItem(Type.Add, ACTION_BAR_ADD);

		Init();
		createFolders();
	}

	private void Init() {
		lvNotes = (ListView) findViewById(R.id.lvNotes);

		noteRetriever = new NoteRetriever(MainActivity.this);

		notes = new ArrayList<NoteListItem>();
		allNotes = new ArrayList<NoteListItem>();

		allNotes.addAll(noteRetriever.getAll());
		notes = allNotes;

		adapter = new NoteListViewItemAdapter(this, notes);
		lvNotes.setAdapter(adapter);

		lvNotes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				Intent intent = null;
				NoteListItem item = notes.get(pos);
				if (item.getType() == NoteListItem.MULTIMEDIA_NOTE) {
					intent = new Intent(MainActivity.this,
							ViewMultiMediaNoteActivity.class);
					intent.putExtra("mid", item.getId());
				} else if (item.getType() == NoteListItem.LIST_NOTE) {
					intent = new Intent(MainActivity.this,
							ViewListNoteActivity.class);
					intent.putExtra("lsid", item.getId());
				} else if (item.getType() == NoteListItem.REMINDER) {
					intent = new Intent(MainActivity.this,
							ViewReminderActivity.class);
					intent.putExtra("rid", item.getId());
				}
				try {
					startActivity(intent);
					finish();
				} catch (Exception exp) {
					Toast.makeText(MainActivity.this, exp.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	private void createFolders() {
		String root = Environment.getExternalStorageDirectory()
				+ File.separator;
		File folder = new File(root + "uText");
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
		}

		folder = new File(root + "uText" + File.separator + "images");
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
		}

		folder = new File(root + "uText" + File.separator + "temp");
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
		}

		folder = new File(root + "uText" + File.separator + "export");
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_SYNC:
			startActivity(new Intent(this, SignInActivity.class));
			finish();
			break;
		case ACTION_BAR_SEARCH:
			startActivity(new Intent(this, SearchActivity.class));
			finish();
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
						finish();
						break;
					case 1:
						startActivity(new Intent(MainActivity.this,
								AddListNoteActivity.class));
						finish();
						break;
					case 2:
						startActivity(new Intent(MainActivity.this,
								AddReminderActivity.class));
						finish();
						break;
					}
				}
			});
			dialogAddNoteOption.show();
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}

}
