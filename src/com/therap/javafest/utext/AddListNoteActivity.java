package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.therap.javafest.utext.sqlitedb.ChildNoteDB;
import com.therap.javafest.utext.sqlitedb.ListNoteDB;

public class AddListNoteActivity extends GDActivity implements OnClickListener {
	private static final int ACTION_BAR_SAVE = 1;

	private int important = 0;

	private ListNoteItemUI item;
	private EditText etListNoteTitle;
	private LinearLayout llListNoteItemsWrapper;
	private Button bAddItem, bLocation, bImportant;

	private ListNoteDB listNoteDB;
	private ChildNoteDB childNoteDB;

	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_add_list_note);
		addActionBarItem(Type.Save, ACTION_BAR_SAVE);
		Init();
	}

	private void Init() {
		listNoteDB = new ListNoteDB(AddListNoteActivity.this);
		childNoteDB = new ChildNoteDB(AddListNoteActivity.this);

		etListNoteTitle = (EditText) findViewById(R.id.etListNoteTitle);

		llListNoteItemsWrapper = (LinearLayout) findViewById(R.id.llListNoteItemsWrapper);

		bAddItem = (Button) findViewById(R.id.bAddItem);
		bAddItem.setOnClickListener(this);

		bLocation = (Button) findViewById(R.id.bLocation);
		bLocation.setOnClickListener(this);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);
	}

	private class SaveNoteThread extends Thread {
		public void run() {
			long lsid = listNoteDB.insert(etListNoteTitle.getText().toString(),
					important);
			int count = llListNoteItemsWrapper.getChildCount();
			for (int i = 0; i < count; i++) {
				item = (ListNoteItemUI) llListNoteItemsWrapper.getChildAt(i);
				childNoteDB.insert(lsid, item.getText().toString(),
						item.isDone());
			}
			progressDialog.dismiss();
			finish();
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_SAVE:
			String title = etListNoteTitle.getText().toString();
			if (title.trim().length() > 0) {
				progressDialog = new ProgressDialog(this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("Saving Your List Note");
				progressDialog.show();
				SaveNoteThread saveNoteThread = new SaveNoteThread();
				saveNoteThread.start();
				Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent(AddListNoteActivity.this,
						MainActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(AddListNoteActivity.this,
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
			AlertDialog.Builder quitDialog = new AlertDialog.Builder(
					AddListNoteActivity.this);
			quitDialog.setTitle("Do you want to quit without saving the note?");
			quitDialog.setPositiveButton("Ok, Quit!",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}

					});
			quitDialog.setNegativeButton("No", null);
			quitDialog.show();
		} else {
			super.onBackPressed();
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bAddItem:
			item = new ListNoteItemUI(AddListNoteActivity.this);
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
		}
	}
}
