package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.therap.javafest.utext.lib.Note;
import com.therap.javafest.utext.lib.NoteComparator;
import com.therap.javafest.utext.lib.NoteRetriever;
import com.therap.javafest.utext.lib.ReminderNote;
import com.therap.javafest.utext.sqlitedb.ReminderNoteDB;

public class MainActivity extends GDActivity {

	private Context context;

	private static final int ACTION_BAR_SYNC = 1;
	private static final int ACTION_BAR_SEARCH = 2;
	private static final int ACTION_BAR_ADD = 3;

	private ListView lvNotes;
	private Dialog dialogAddNoteOption;

	private NoteRetriever noteRetriever;
	private NoteListViewItemAdapter adapter;
	private ArrayList<Note> allNotes, notes;

	private ReminderNoteDB reminderNoteDB;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_main);
		addActionBarItem(Type.Share, ACTION_BAR_SYNC);
		addActionBarItem(Type.Search, ACTION_BAR_SEARCH);
		addActionBarItem(Type.Add, ACTION_BAR_ADD);
		renderView();
		createFolders();
		renderAllReminder();
	}

	private void renderView() {
		context = MainActivity.this;
		reminderNoteDB = new ReminderNoteDB(context);

		lvNotes = (ListView) findViewById(R.id.lvNotes);

		noteRetriever = new NoteRetriever(context);

		notes = new ArrayList<Note>();
		allNotes = new ArrayList<Note>();

		allNotes.addAll(noteRetriever.getAll());
		notes = allNotes;

		Collections.sort(notes, new NoteComparator(
				NoteComparator.DEFAULT_COMPARE));

		adapter = new NoteListViewItemAdapter(this, notes);
		lvNotes.setAdapter(adapter);

		lvNotes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				Intent intent = null;
				Note item = notes.get(pos);
				if (item.getType() == Note.MULTIMEDIA_NOTE) {
					intent = new Intent(context,
							ViewMultiMediaNoteActivity.class);
					intent.putExtra("mid", item.getId());
				} else if (item.getType() == Note.LIST_NOTE) {
					intent = new Intent(context, ViewListNoteActivity.class);
					intent.putExtra("lsid", item.getId());
				} else if (item.getType() == Note.REMINDER) {
					intent = new Intent(context, ViewReminderActivity.class);
					intent.putExtra("rid", item.getId());
				}
				try {
					startActivity(intent);
					finish();
				} catch (Exception exp) {
					Toast.makeText(context, exp.getMessage(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});

	}

	private void renderAllReminder() {
		ArrayList<ReminderNote> reminderNote = reminderNoteDB.selectAll();
		for (ReminderNote r : reminderNote) {
			long date1 = NoteComparator.StringToDate(r.rdate + " " + r.rtime)
					.getTime();
			long date2 = (new Date()).getTime();
			long diffTime = date1 - date2;
			if (diffTime > 0) {
				Bundle bundle = new Bundle();
				bundle.putString("TITLE", r.text);
				Toast.makeText(this,String.valueOf(diffTime), Toast.LENGTH_LONG).show();
				new AlarmReceiver(this, bundle, (int)diffTime, r.rid);
			}else{
				try{
					Intent intent = new Intent(this, AlarmReceiver.class);
					PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			        alarmManager.cancel(pendingIntent);
				}catch(Exception exp){
					exp.printStackTrace();
				}
			}
		}
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
			startActivity(new Intent(this, ExportDatabaseActivity.class));
			finish();
			break;
		case ACTION_BAR_SEARCH:
			startActivity(new Intent(this, SearchActivity.class));
			finish();
			break;
		case ACTION_BAR_ADD:
			dialogAddNoteOption = new Dialog(context);
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
						startActivity(new Intent(context,
								AddMultiMediaNoteActivity.class));
						finish();
						break;
					case 1:
						startActivity(new Intent(context,
								AddListNoteActivity.class));
						finish();
						break;
					case 2:
						startActivity(new Intent(context,
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
