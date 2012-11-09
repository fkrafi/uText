package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.therap.javafest.utext.lib.ChildNote;
import com.therap.javafest.utext.lib.ListNote;
import com.therap.javafest.utext.sqlitedb.ChildNoteDB;
import com.therap.javafest.utext.sqlitedb.ListNoteDB;

public class EditListNoteActivity extends GDActivity implements OnClickListener {
	private static final int ACTION_BAR_SAVE = 1;

	private int important = 0;
	private int lsid;
	private Intent intent;
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
		setActionBarContentView(R.layout.activity_edit_list_note);
		addActionBarItem(Type.Save, ACTION_BAR_SAVE);

		Init();
	}

	private void Init() {
		intent = getIntent();
		lsid = Integer.parseInt(intent.getStringExtra("lsid"));

		ListNote ln = new ListNote();
		listNoteDB = new ListNoteDB(EditListNoteActivity.this);
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

		childNoteDB = new ChildNoteDB(EditListNoteActivity.this);
		cd = childNoteDB.selectByLsid(lsid);
		for (ChildNote c : cd) {
			ListNoteItemUI item = new ListNoteItemUI(EditListNoteActivity.this);
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
			progressDialog.dismiss();
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_SAVE:
			String title = etListNoteTitle.getText().toString();
			if (title.trim().length() > 0) {
				progressDialog = new ProgressDialog(EditListNoteActivity.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("Saving your list note");
				progressDialog.show();
				progressDialog.setOnDismissListener(new OnDismissListener() {
					public void onDismiss(DialogInterface di) {
						Toast.makeText(EditListNoteActivity.this,
								"Saved Successfully!", Toast.LENGTH_LONG)
								.show();
						Intent i = new Intent(EditListNoteActivity.this,
								ViewListNoteActivity.class);
						i.putExtra("lsid", String.valueOf(lsid));
						startActivity(i);
						finish();
					}
				});
				SaveNoteThread saveNoteThread = new SaveNoteThread();
				saveNoteThread.start();
			} else {
				Toast.makeText(EditListNoteActivity.this,
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
					EditListNoteActivity.this);
			quitDialog.setTitle("Do you want to quit without saving the note?");
			quitDialog.setPositiveButton("Ok, Quit!",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(EditListNoteActivity.this,
									ViewListNoteActivity.class);
							i.putExtra("lsid", String.valueOf(lsid));
							startActivity(i);
							finish();
						}

					});
			quitDialog.setNegativeButton("No", null);
			quitDialog.show();
		} else {
			Intent i = new Intent(EditListNoteActivity.this,
					ViewListNoteActivity.class);
			i.putExtra("lsid", String.valueOf(lsid));
			startActivity(i);
			finish();
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bAddItem:
			item = new ListNoteItemUI(EditListNoteActivity.this);
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
