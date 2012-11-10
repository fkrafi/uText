package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.therap.javafest.utext.lib.ChildNote;
import com.therap.javafest.utext.lib.ListNote;
import com.therap.javafest.utext.lib.LocationData;
import com.therap.javafest.utext.lib.Note;
import com.therap.javafest.utext.sqlitedb.ChildNoteDB;
import com.therap.javafest.utext.sqlitedb.ListNoteDB;
import com.therap.javafest.utext.sqlitedb.LocationDataDB;

public class EditListNoteActivity extends GDActivity implements OnClickListener {
	private static final int ACTION_BAR_SAVE = 1;
	private static final int REQUEST_MAP_LOCATION = 10;

	private Context context;
	private int important = 0;
	private Address address;
	private int lsid;
	private Intent intent;

	private ListNoteItemUI item;
	private ImageButton ibDelete;
	private EditText etListNoteTitle;
	private Button bAddItem, bLocation, bImportant;
	private LinearLayout llListNoteItemsWrapper, llLocationWrapper;
	private TextView tvLocation, tvLocationLongitude, tvLocationLatitude;

	private ListNoteDB listNoteDB;
	private ChildNoteDB childNoteDB;
	private LocationDataDB locationDataDB;

	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_edit_list_note);
		addActionBarItem(Type.Save, ACTION_BAR_SAVE);
		renderView();
	}

	private void renderView() {
		context = this;
		intent = getIntent();
		lsid = Integer.parseInt(intent.getStringExtra("lsid"));

		ListNote ln = new ListNote();
		listNoteDB = new ListNoteDB(context);
		ln = listNoteDB.selectByLsid(lsid);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);
		if (ln.is_important == 1) {
			important = 1;
			bImportant.setCompoundDrawablesWithIntrinsicBounds(
					getBaseContext().getResources().getDrawable(
							R.drawable.ic_menu_star_yellow), null, null, null);
		}

		etListNoteTitle = (EditText) findViewById(R.id.etListNoteTitle);
		etListNoteTitle.setText(ln.title);

		llListNoteItemsWrapper = (LinearLayout) findViewById(R.id.llListNoteItemsWrapper);

		ArrayList<ChildNote> cd = new ArrayList<ChildNote>();
		childNoteDB = new ChildNoteDB(context);
		cd = childNoteDB.selectByLsid(lsid);
		for (ChildNote c : cd) {
			ListNoteItemUI item = new ListNoteItemUI(context);
			item.setId(c.cid);
			item.setText(c.text);
			item.setDone(c.is_complete);
			llListNoteItemsWrapper.addView(item);
		}

		bAddItem = (Button) findViewById(R.id.bAddItem);
		bAddItem.setOnClickListener(this);

		bLocation = (Button) findViewById(R.id.bLocation);
		bLocation.setOnClickListener(this);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);

		llLocationWrapper = (LinearLayout) findViewById(R.id.llLocationWrapper);
		llLocationWrapper.setVisibility(View.INVISIBLE);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocationLongitude = (TextView) findViewById(R.id.tvLocationLongitude);
		tvLocationLatitude = (TextView) findViewById(R.id.tvLocationLatitude);
		ibDelete = (ImageButton) findViewById(R.id.ibDelete);
		ibDelete.setOnClickListener(this);

		LocationData locationData = new LocationData();
		locationDataDB = new LocationDataDB(context);
		locationData = locationDataDB.selectByNoteId(lsid, Note.LIST_NOTE);
		if (locationData != null) {
			tvLocation.setText(locationData.place);
			tvLocationLongitude.setText(String.valueOf(locationData.longitude));
			tvLocationLatitude.setText(String.valueOf(locationData.latitude));
			llLocationWrapper.setVisibility(View.VISIBLE);
		}
	}

	private class SaveNoteThread extends Thread {
		public void run() {
			listNoteDB.update(lsid, etListNoteTitle.getText().toString(),
					important);
			childNoteDB.deleteAllChildByLsid(lsid);
			int count = llListNoteItemsWrapper.getChildCount();
			for (int i = 0; i < count; i++) {
				item = (ListNoteItemUI) llListNoteItemsWrapper.getChildAt(i);
				childNoteDB.insert(lsid, item.getText().toString(),
						item.isDone());
			}
			if (llLocationWrapper.getVisibility() == View.VISIBLE) {
				locationDataDB.update(lsid, Double.parseDouble(tvLocationLongitude.getText()
						.toString()), Double.parseDouble(tvLocationLatitude.getText().toString()),
						tvLocation.getText().toString());
			}else{
				locationDataDB.delete(lsid, Note.LIST_NOTE);
			}
			progressDialog.dismiss();
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_SAVE:
			String title = etListNoteTitle.getText().toString();
			if (title.trim().length() > 0) {
				progressDialog = new ProgressDialog(context);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("Saving your list note");
				progressDialog.show();
				progressDialog.setOnDismissListener(new OnDismissListener() {
					public void onDismiss(DialogInterface di) {
						Toast.makeText(context, "Saved Successfully!",
								Toast.LENGTH_LONG).show();
						Intent i = new Intent(context,
								ViewListNoteActivity.class);
						i.putExtra("lsid", String.valueOf(lsid));
						startActivity(i);
						finish();
					}
				});
				SaveNoteThread saveNoteThread = new SaveNoteThread();
				saveNoteThread.start();
			} else {
				Toast.makeText(context,
						"Cannot Save List Note With Empty Title!",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}

	@Override
	public void onBackPressed() {
		String title = etListNoteTitle.getText().toString();
		if (title.trim().length() > 0) {
			AlertDialog.Builder quitDialog = new AlertDialog.Builder(context);
			quitDialog.setTitle("Do you want to quit without saving the note?");
			quitDialog.setPositiveButton("Ok, Quit!",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(context,
									ViewListNoteActivity.class);
							i.putExtra("lsid", String.valueOf(lsid));
							startActivity(i);
							finish();
						}

					});
			quitDialog.setNegativeButton("No", null);
			quitDialog.show();
		} else {
			Intent i = new Intent(context, ViewListNoteActivity.class);
			i.putExtra("lsid", String.valueOf(lsid));
			startActivity(i);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_MAP_LOCATION && resultCode == RESULT_OK) {
			address = data.getParcelableExtra(AddMapActivity.RESULT_ADDRESS);
			tvLocation.setText(address.getAddressLine(0).toString() + ", "
					+ address.getAddressLine(1).toString() + ", "
					+ address.getCountryCode().toString());
			tvLocationLongitude.setText(String.valueOf(address.getLongitude()));
			tvLocationLatitude.setText(String.valueOf(address.getLatitude()));
			llLocationWrapper.setVisibility(View.VISIBLE);
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bAddItem:
			item = new ListNoteItemUI(context);
			llListNoteItemsWrapper.addView(item);
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
		case R.id.bLocation:
			Intent i = new Intent(this, AddMapActivity.class);
			startActivityForResult(i, REQUEST_MAP_LOCATION);
			break;
		case R.id.ibDelete:
			tvLocation.setText("");
			tvLocationLongitude.setText("");
			tvLocationLatitude.setText("");
			llLocationWrapper.setVisibility(View.INVISIBLE);
			break;
		}
	}

}
