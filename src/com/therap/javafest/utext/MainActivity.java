package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

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
					exp.fillInStackTrace();
				}
			}
		});

	}

	private void renderAllReminder() {
		ArrayList<ReminderNote> reminderNote = reminderNoteDB.selectAll();
		for (ReminderNote rn : reminderNote) {
			try {
				String str[] = rn.rdate.split(" ");
				int ryear = Integer.parseInt(str[0]);
				int rmonth = Integer.parseInt(str[1]);
				int rday = Integer.parseInt(str[2]);
				int rhour = Integer.parseInt(str[3]);
				int rminute = Integer.parseInt(str[4]);
				Date date1 = (new GregorianCalendar(ryear, rmonth, rday, rhour,
						rminute, 0)).getTime();
				Calendar c = Calendar.getInstance();
				Date date2 = (new GregorianCalendar(c.get(Calendar.YEAR),
						c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
						c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), 0))
						.getTime();
				if (date1.getTime() >= date2.getTime()) {
					Intent intent = new Intent(context, AlarmReceiver.class);
					intent.putExtra("text", rn.text);
					intent.putExtra("rid", String.valueOf(rn.rid));
					PendingIntent sender = PendingIntent.getBroadcast(this,
							rn.rid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
					am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), sender);
				} else {
					PendingIntent pendingIntent = PendingIntent.getBroadcast(
							getBaseContext(), rn.rid, new Intent(this,
									AlarmReceiver.class),
							PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
					alarmManager.cancel(pendingIntent);
				}
			} catch (Exception exp) {
				exp.printStackTrace();
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
