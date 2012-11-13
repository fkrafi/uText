package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.therap.javafest.utext.lib.LocationData;
import com.therap.javafest.utext.lib.Note;
import com.therap.javafest.utext.lib.ReminderNote;
import com.therap.javafest.utext.sqlitedb.LocationDataDB;
import com.therap.javafest.utext.sqlitedb.ReminderNoteDB;

public class ViewReminderActivity extends GDActivity {

	private static final int ACTION_BAR_DELETE = 1;
	private static final int ACTION_BAR_EDIT = 2;

	private int rid;

	private Context context;
	private Intent intent;

	private ReminderNoteDB reminderNoteDB;
	private LocationDataDB locationDataDB;

	private ImageView ivImportant;
	private Button bDate, bTime;
	private TextView tvDateTime, tvLocation, tvText;

	private int rmonth, ryear, rday, rhour, rminute;

	private SpanableText st;

	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_view_reminder);
		Init();
		renderView();

	}

	private void Init() {
		context = this;
		intent = getIntent();
		st = new SpanableText(context);

		rid = Integer.parseInt(intent.getStringExtra("rid"));
		reminderNoteDB = new ReminderNoteDB(context);
		locationDataDB = new LocationDataDB(context);
	}

	private void renderView() {
		addActionBarItem(Type.Trashcan, ACTION_BAR_DELETE);
		addActionBarItem(Type.Edit, ACTION_BAR_EDIT);

		ReminderNote rn = reminderNoteDB.selectByLsid(rid);

		Timestamp ts = Timestamp.valueOf(rn.modified);
		Date date = new Date(ts.getTime());
		DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
		tvDateTime = (TextView) findViewById(R.id.tvDateTime);
		tvDateTime.setText(dateFormat.format(date).toString());

		String str[] = rn.rdate.split(" ");
		ryear = Integer.parseInt(str[0]);
		rmonth = Integer.parseInt(str[1]);
		rday = Integer.parseInt(str[2]);
		rhour = Integer.parseInt(str[3]);
		rminute = Integer.parseInt(str[4]);
		date = (new GregorianCalendar(ryear, rmonth, rday, rhour, rminute, 0))
				.getTime();
		bDate = (Button) findViewById(R.id.bDate);
		dateFormat = new SimpleDateFormat("E, dd M yyyy");
		bDate.setText(dateFormat.format(date).toString());
		bDate.setEnabled(false);

		bTime = (Button) findViewById(R.id.bTime);
		dateFormat = new SimpleDateFormat("hh:mm a");
		bTime.setText(dateFormat.format(date));
		bTime.setEnabled(false);

		ivImportant = (ImageView) findViewById(R.id.ivImportant);
		if (rn.is_important == 1) {
			ivImportant.setImageResource(R.drawable.ic_imageview_star_yellow);
		}

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		LocationData locationData = locationDataDB.selectByNoteId(rid,
				Note.REMINDER);
		tvLocation.setText("");
		if (locationData != null) {
			tvLocation.setText(locationData.place);
		}

		tvText = (TextView) findViewById(R.id.tvText);
		SpannableString ss = st.convertToSpannableString(rn.text);
		tvText.setText(ss, BufferType.SPANNABLE);
	}

	private class DeleteNoteThread extends Thread {
		public void run() {
			reminderNoteDB.delete(rid);
			locationDataDB.delete(rid, Note.REMINDER);
			progressDialog.dismiss();
		}
	}

	private void backward() {
		Intent intent = new Intent(context,
				MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		backward();
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_DELETE:
			AlertDialog.Builder quitDialog = new AlertDialog.Builder(
					context);
			quitDialog.setTitle("Do you want to delete the reminder?");
			quitDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							progressDialog = new ProgressDialog(
									context);
							progressDialog
									.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setMessage("Deleting Your Reminder");
							progressDialog.show();
							progressDialog
									.setOnDismissListener(new OnDismissListener() {
										public void onDismiss(DialogInterface di) {
											backward();
											Toast.makeText(
													context,
													"Deleted Successfully!",
													Toast.LENGTH_LONG).show();
										}
									});
							DeleteNoteThread deleteNoteThread = new DeleteNoteThread();
							deleteNoteThread.start();
						}

					});
			quitDialog.setNegativeButton("No", null);
			quitDialog.show();
			break;
		case ACTION_BAR_EDIT:
			Intent intentEdit = new Intent(context,
					EditReminderActivity.class);
			intentEdit.putExtra("rid", String.valueOf(rid));
			startActivity(intentEdit);
			finish();
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}
}
