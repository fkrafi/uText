package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.therap.javafest.utext.lib.ReminderNote;
import com.therap.javafest.utext.sqlitedb.ReminderNoteDB;

public class ViewReminderActivity extends GDActivity {

	private static final int ACTION_BAR_DELETE = 1;
	private static final int ACTION_BAR_EDIT = 2;

	private int rid;
	private Intent intent;

	private ReminderNoteDB reminderNoteDB;

	private ImageView ivImportant;
	private Button bDate, bTime;
	private TextView tvDateTime, tvLocation, tvText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_view_reminder);

		addActionBarItem(Type.Trashcan, ACTION_BAR_DELETE);
		addActionBarItem(Type.Edit, ACTION_BAR_EDIT);
		Init();
	}

	private void Init() {
		intent = getIntent();
		rid = Integer.parseInt(intent.getStringExtra("rid"));

		ReminderNote rn = new ReminderNote();

		reminderNoteDB = new ReminderNoteDB(ViewReminderActivity.this);

		rn = reminderNoteDB.selectByLsid(rid);

		Timestamp ts = Timestamp.valueOf(rn.modified);
		Date date = new Date(ts.getTime());
		DateFormat dateFormat = new SimpleDateFormat("E, dd M yyyy hh:mm a");
		tvDateTime = (TextView) findViewById(R.id.tvDateTime);
		tvDateTime.setText(dateFormat.format(date).toString());

		bDate = (Button) findViewById(R.id.bDate);
		bDate.setText(rn.rdate);
		bDate.setEnabled(false);

		bTime = (Button) findViewById(R.id.bTime);
		bTime.setText(rn.rtime);
		bTime.setEnabled(false);

		ivImportant = (ImageView) findViewById(R.id.ivImportant);
		if (rn.is_important == 1) {
			ivImportant.setImageResource(R.drawable.ic_menu_star_yellow);
		}

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocation.setText("Dhaka, Bangladesh");

		tvText = (TextView) findViewById(R.id.tvText);
		tvText.setText(rn.text);

	}

	private void delete() {
		Intent intent = new Intent(ViewReminderActivity.this,
				MainActivity.class);
		reminderNoteDB.delete(rid);
		Toast.makeText(ViewReminderActivity.this, "Successfully Deleted!",
				Toast.LENGTH_LONG).show();
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ViewReminderActivity.this,
				MainActivity.class);
		startActivity(intent);
		super.onBackPressed();
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_DELETE:
			AlertDialog.Builder quitDialog = new AlertDialog.Builder(
					ViewReminderActivity.this);
			quitDialog.setTitle("Do you want to delete the reminder?");
			quitDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							delete();
						}

					});
			quitDialog.setNegativeButton("No", null);
			quitDialog.show();
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}
}
