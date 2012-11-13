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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.therap.javafest.utext.lib.Note;
import com.therap.javafest.utext.sqlitedb.LocationDataDB;
import com.therap.javafest.utext.sqlitedb.ReminderNoteDB;

public class AddReminderActivity extends GDActivity implements OnClickListener {

	private static final int ACTION_BAR_SAVE = 1;
	private static final int REQUEST_SPEECH = 1111;
	private static final int DATE_PICKER_ID = 1010;
	private static final int TIME_PICKER_ID = 2020;
	private static final int REQUEST_MAP_LOCATION = 10;

	private Context context;
	private Address address;

	int important = 0;
	private Date date;

	private ReminderNoteDB reminderNoteDB;
	private LocationDataDB locationDataDB;

	private EditText etNoteText;
	private ImageButton ibASR, ibLVDelete;
	private Button bDate, bTime, bImportant, bLocation;
	private LinearLayout llLocationWrapper;
	private TextView tvLocation, tvLocationLongitude, tvLocationLatitude;

	private int rmonth, ryear, rday, rhour, rminute;

	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_add_reminder);
		Init();
		renderView();
	}

	private void Init() {
		context = this;

		date = (new GregorianCalendar()).getTime();
		Calendar c = Calendar.getInstance();
		rmonth = c.get(Calendar.MONTH);
		ryear = c.get(Calendar.YEAR);
		rday = c.get(Calendar.DAY_OF_MONTH);
		rhour = c.get(Calendar.HOUR_OF_DAY);
		rminute = c.get(Calendar.MINUTE);

		reminderNoteDB = new ReminderNoteDB(context);
		locationDataDB = new LocationDataDB(context);
	}

	private void renderView() {
		addActionBarItem(Type.Save, ACTION_BAR_SAVE);

		bDate = (Button) findViewById(R.id.bDate);
		DateFormat dateFormat = new SimpleDateFormat("E, dd M yyyy");
		bDate.setText(dateFormat.format(date).toString());
		bDate.setOnClickListener(this);

		bTime = (Button) findViewById(R.id.bTime);
		dateFormat = new SimpleDateFormat("hh:mm a");
		bTime.setText(dateFormat.format(date));
		bTime.setOnClickListener(this);

		etNoteText = (EditText) findViewById(R.id.etNoteText);
		ibASR = (ImageButton) findViewById(R.id.ibASR);
		ibASR.setOnClickListener(this);

		bLocation = (Button) findViewById(R.id.bLocation);
		bLocation.setOnClickListener(this);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);

		llLocationWrapper = (LinearLayout) findViewById(R.id.llLocationWrapper);
		llLocationWrapper.setVisibility(View.INVISIBLE);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocationLongitude = (TextView) findViewById(R.id.tvLocationLongitude);
		tvLocationLatitude = (TextView) findViewById(R.id.tvLocationLatitude);
		ibLVDelete = (ImageButton) findViewById(R.id.ibLVDelete);
		ibLVDelete.setOnClickListener(this);

	}

	private class SaveNoteThread extends Thread {
		public void run() {
			String t = String.valueOf(ryear) + " " + String.valueOf(rmonth)
					+ " " + String.valueOf(rday) + " " + String.valueOf(rhour)
					+ " " + String.valueOf(rminute);
			long rid = reminderNoteDB.insert(t,
					etNoteText.getText().toString(), important);
			if (llLocationWrapper.getVisibility() == View.VISIBLE) {
				locationDataDB.insert(rid, Note.REMINDER, Double
						.parseDouble(tvLocationLongitude.getText().toString()),
						Double.parseDouble(tvLocationLatitude.getText()
								.toString()), tvLocation.getText().toString());
			}
			progressDialog.dismiss();
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
				progressDialog.setOnDismissListener(new OnDismissListener() {
					public void onDismiss(DialogInterface di) {
						startActivity(new Intent(context, MainActivity.class));
						finish();
						Toast.makeText(context, "Saved Successfully!",
								Toast.LENGTH_LONG).show();
					}
				});
				SaveNoteThread saveNoteThread = new SaveNoteThread();
				saveNoteThread.start();
			} else {
				Toast.makeText(context, "Cannot Save Empty Reminder Note!",
						Toast.LENGTH_LONG).show();
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
		} else if (requestCode == REQUEST_MAP_LOCATION
				&& resultCode == RESULT_OK) {
			address = data.getParcelableExtra(AddMapActivity.RESULT_ADDRESS);
			tvLocation.setText(address.getAddressLine(0).toString() + ", "
					+ address.getAddressLine(1).toString() + ", "
					+ address.getCountryCode().toString());
			tvLocationLongitude.setText(String.valueOf(address.getLongitude()));
			tvLocationLatitude.setText(String.valueOf(address.getLatitude()));
			llLocationWrapper.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		if (id == DATE_PICKER_ID) {
			return (new DatePickerDialog(context, datePickerListener,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH)));
		} else if (id == TIME_PICKER_ID) {
			return (new TimePickerDialog(context, timePickerListener,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false));
		}
		return super.onCreateDialog(id);
	}

	private OnDateSetListener datePickerListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Date date = (new GregorianCalendar(year, monthOfYear, dayOfMonth))
					.getTime();
			ryear = year;
			rmonth = monthOfYear;
			rday = dayOfMonth;
			DateFormat dateFormat = new SimpleDateFormat("E, dd M yyyy");
			bDate.setText(dateFormat.format(date).toString());

		}
	};

	private OnTimeSetListener timePickerListener = new OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hour, int minute) {
			Calendar c = Calendar.getInstance();
			Date date = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH), hour, minute);
			rhour = hour;
			rminute = minute;
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
			try {
				Intent intentASR = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intentASR.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intentASR.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
				startActivityForResult(intentASR, REQUEST_SPEECH);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(context, "Google Voice Search Not Installed!",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.bLocation:
			Intent i = new Intent(this, AddMapActivity.class);
			startActivityForResult(i, REQUEST_MAP_LOCATION);
			break;
		case R.id.ibLVDelete:
			tvLocation.setText("");
			tvLocationLongitude.setText("");
			tvLocationLatitude.setText("");
			llLocationWrapper.setVisibility(View.INVISIBLE);
			break;
		}
	}
}
