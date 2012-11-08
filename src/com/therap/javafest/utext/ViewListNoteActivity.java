package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.therap.javafest.utext.lib.ChildNote;
import com.therap.javafest.utext.lib.ListNote;
import com.therap.javafest.utext.sqlitedb.ChildNoteDB;
import com.therap.javafest.utext.sqlitedb.ListNoteDB;

public class ViewListNoteActivity extends GDActivity {
	private static final int ACTION_BAR_DELETE = 1;
	private static final int ACTION_BAR_EDIT = 2;

	private int lsid;
	private Intent intent;

	private ListNoteDB listNoteDB;
	private ChildNoteDB childNoteDB;

	private ImageView ivImportant;
	private LinearLayout llListNoteItemsWrapper;
	private TextView tvDateTime, tvLocation, tvTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_view_list_note);

		addActionBarItem(Type.Trashcan, ACTION_BAR_DELETE);
		addActionBarItem(Type.Edit, ACTION_BAR_EDIT);

		Init();
	}

	private void Init() {
		intent = getIntent();
		lsid = Integer.parseInt(intent.getStringExtra("lsid"));

		ListNote ln = new ListNote();
		listNoteDB = new ListNoteDB(ViewListNoteActivity.this);
		ln = listNoteDB.selectByLsid(lsid);

		ivImportant = (ImageView) findViewById(R.id.ivImportant);
		if (ln.is_important == 1) {
			ivImportant.setImageResource(R.drawable.ic_imageview_star_yellow);
		}

		Timestamp ts = Timestamp.valueOf(ln.modified);
		Date date = new Date(ts.getTime());
		DateFormat dateFormat = new SimpleDateFormat("E, dd M yyyy hh:mm a");
		tvDateTime = (TextView) findViewById(R.id.tvDateTime);
		tvDateTime.setText(dateFormat.format(date).toString());

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(ln.title);

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocation.setText("Dhaka, Bangladesh");

		llListNoteItemsWrapper = (LinearLayout) findViewById(R.id.llListNoteItemsWrapper);

		ArrayList<ChildNote> cd = new ArrayList<ChildNote>();

		childNoteDB = new ChildNoteDB(ViewListNoteActivity.this);
		cd = childNoteDB.selectByLsid(lsid);
		for (ChildNote c : cd) {
			ListNoteItemUI item = new ListNoteItemUI(ViewListNoteActivity.this);
			item.setText(c.text);
			item.setId(c.cid);
			item.setDone(c.is_complete);
			item.setCheckDone(false);
			item.setDeleteEnable(false);
			item.setTextEditable(false);
			llListNoteItemsWrapper.addView(item);
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ViewListNoteActivity.this,
				MainActivity.class);
		startActivity(intent);
		super.onBackPressed();
	}

	private void delete() {
		Intent intent = new Intent(ViewListNoteActivity.this,
				MainActivity.class);

		int count = llListNoteItemsWrapper.getChildCount();
		for (int i = 0; i < count; i++) {
			childNoteDB.delete(llListNoteItemsWrapper.getChildAt(i).getId());
		}

		listNoteDB.delete(lsid);

		Toast.makeText(ViewListNoteActivity.this, "Successfully Deleted!",
				Toast.LENGTH_LONG).show();
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_DELETE:
			AlertDialog.Builder quitDialog = new AlertDialog.Builder(
					ViewListNoteActivity.this);
			quitDialog.setTitle("Do you want to delete the note?");
			quitDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							delete();
						}

					});
			quitDialog.setNegativeButton("No", null);
			quitDialog.show();
			break;
		case ACTION_BAR_EDIT:
			intent = new Intent(ViewListNoteActivity.this,
					EditListNoteActivity.class);
			intent.putExtra("lsid", String.valueOf(lsid));
			startActivity(intent);
			finish();
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}
}
