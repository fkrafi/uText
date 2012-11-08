package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.therap.javafest.utext.lib.ReminderNote;
import com.therap.javafest.utext.sqlitedb.ReminderNoteDB;

public class EditReminderActivity extends GDActivity implements OnClickListener {
	private static final int ACTION_BAR_SAVE = 1;
	private static final int REQUEST_SPEECH = 1111;
	private static final int DATE_PICKER_ID = 1010;
	private static final int TIME_PICKER_ID = 2020;

	int important = 0;

	private EditText etNoteText;
	private ImageButton ibASR;
	private Button bDate, bTime, bImportant, bLocation;

	private ProgressDialog progressDialog;

	private int rid;
	private Intent intent;

	private ReminderNoteDB reminderNoteDB;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setActionBarContentView(R.layout.activity_edit_reminder);
			addActionBarItem(Type.Save, ACTION_BAR_SAVE);
			Init();
		} catch (Exception exp) {
			Toast.makeText(this, exp.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void Init() {
		intent = getIntent();
		rid = Integer.parseInt(intent.getStringExtra("rid"));

		ReminderNote rn = new ReminderNote();

		reminderNoteDB = new ReminderNoteDB(EditReminderActivity.this);

		rn = reminderNoteDB.selectByLsid(rid);

		bDate = (Button) findViewById(R.id.bDate);
		bDate.setText(rn.rdate);
		bDate.setEnabled(false);

		bTime = (Button) findViewById(R.id.bTime);
		bTime.setText(rn.rtime);
		bTime.setEnabled(false);

		etNoteText = (EditText) findViewById(R.id.etNoteText);
		etNoteText.setText(rn.text);

		ibASR = (ImageButton) findViewById(R.id.ibASR);
		ibASR.setOnClickListener(this);

		bLocation = (Button) findViewById(R.id.bLocation);
		bLocation.setOnClickListener(this);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);
		if (rn.is_important == 1) {
			important = 1;
			bImportant.setCompoundDrawablesWithIntrinsicBounds(
					getBaseContext().getResources().getDrawable(
							R.drawable.ic_menu_star_yellow), null, null, null);
		}

	}

	private class SaveNoteThread extends Thread {
		public void run() {
			reminderNoteDB.insert(bDate.getText().toString(), bTime.getText()
					.toString(), etNoteText.getText().toString(), important);
			progressDialog.dismiss();
			finish();
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_SAVE:
			String text = etNoteText.getText().toString().trim();
			if (text.length() > 0) {
				progressDialog = new ProgressDialog(this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("Saving Your Reminder");
				progressDialog.show();
				SaveNoteThread saveNoteThread = new SaveNoteThread();
				saveNoteThread.start();
				Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent(EditReminderActivity.this,
						MainActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(EditReminderActivity.this,
						"Cannot Save Empty Reminder Note!", Toast.LENGTH_LONG)
						.show();
			}
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_SPEECH && resultCode == RESULT_OK) {
			String text = etNoteText.getText().toString();
			String sentence = data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS).get(0);
			int index = etNoteText.getSelectionStart();
			if (index >= 0 && index < text.length()) {
				text = text.substring(0, index) + sentence
						+ text.substring(index + 1);
			} else {
				etNoteText.setText(text + sentence);
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		if (id == DATE_PICKER_ID) {
			return (new DatePickerDialog(EditReminderActivity.this,
					datePickerListener, c.get(Calendar.YEAR),
					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)));
		} else if (id == TIME_PICKER_ID) {
			return (new TimePickerDialog(EditReminderActivity.this,
					timePickerListener, c.get(Calendar.HOUR_OF_DAY),
					c.get(Calendar.MINUTE), false));
		}
		return super.onCreateDialog(id);
	}

	private OnDateSetListener datePickerListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Date date = (new GregorianCalendar(year, monthOfYear, dayOfMonth))
					.getTime();
			DateFormat dateFormat = new SimpleDateFormat("E, dd M yyyy");
			bDate.setText(dateFormat.format(date).toString());

		}
	};

	private OnTimeSetListener timePickerListener = new OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hour, int minute) {
			Calendar c = Calendar.getInstance();
			Date date = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH), hour, minute);
			DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
			bTime.setText(dateFormat.format(date).toString());
		}
	};

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bDate:
			showDialog(DATE_PICKER_ID);
			break;
		case R.id.bTime:
			showDialog(TIME_PICKER_ID);
			break;
		case R.id.bImportant:
			if (important == 1) {
				important = 0;
				bImportant.setCompoundDrawablesWithIntrinsicBounds(
						getBaseContext().getResources().getDrawable(
								R.drawable.ic_menu_star), null, null, null);
			} else {
				important = 1;
				bImportant.setCompoundDrawablesWithIntrinsicBounds(
						getBaseContext().getResources().getDrawable(
								R.drawable.ic_menu_star_yellow), null, null,
						null);
			}
			break;
		case R.id.ibASR:
			Intent intentASR = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intentASR.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			// intentASR.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
			intentASR.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
			startActivityForResult(intentASR, REQUEST_SPEECH);
			break;
		}
	}

}
