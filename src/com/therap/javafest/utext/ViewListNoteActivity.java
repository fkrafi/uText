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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
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

public class ViewListNoteActivity extends GDActivity {
	private static final int ACTION_BAR_DELETE = 1;
	private static final int ACTION_BAR_EDIT = 2;

	private Context context;
	private int lsid;
	private Intent intent;

	private ListNoteDB listNoteDB;
	private ChildNoteDB childNoteDB;
	private LocationDataDB locationDataDB;

	private ImageView ivImportant;
	private LinearLayout llListNoteItemsWrapper;
	private TextView tvDateTime, tvLocation, tvTitle;

	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_view_list_note);
		addActionBarItem(Type.Trashcan, ACTION_BAR_DELETE);
		addActionBarItem(Type.Edit, ACTION_BAR_EDIT);
		renderView();
	}

	private void renderView() {
		context = this;
		intent = getIntent();
		lsid = Integer.parseInt(intent.getStringExtra("lsid"));

		ListNote ln = new ListNote();
		listNoteDB = new ListNoteDB(context);
		ln = listNoteDB.selectByLsid(lsid);

		ivImportant = (ImageView) findViewById(R.id.ivImportant);
		if (ln.is_important == 1) {
			ivImportant.setImageResource(R.drawable.ic_imageview_star_yellow);
		}

		Timestamp ts = Timestamp.valueOf(ln.modified);
		Date date = new Date(ts.getTime());
		DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
		tvDateTime = (TextView) findViewById(R.id.tvDateTime);
		tvDateTime.setText(dateFormat.format(date).toString());

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(ln.title);

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		LocationData locationData = new LocationData();
		locationDataDB = new LocationDataDB(context);
		locationData = locationDataDB.selectByNoteId(lsid, Note.LIST_NOTE);
		tvLocation.setText("");
		if (locationData != null) {
			tvLocation.setText(locationData.place);
		}

		llListNoteItemsWrapper = (LinearLayout) findViewById(R.id.llListNoteItemsWrapper);

		ArrayList<ChildNote> cd = new ArrayList<ChildNote>();

		childNoteDB = new ChildNoteDB(context);
		cd = childNoteDB.selectByLsid(lsid);
		for (ChildNote c : cd) {
			ListNoteItemUI item = new ListNoteItemUI(context);
			item.setText(c.text);
			item.setId(c.cid);
			item.setDone(c.is_complete);
			item.setCheckDone(false);
			item.setDeleteEnable(false);
			item.setTextEditable(false);
			item.setTextFocused(false);
			llListNoteItemsWrapper.addView(item);
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(context, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private class DeleteNoteThread extends Thread {
		public void run() {
			int count = llListNoteItemsWrapper.getChildCount();
			for (int i = 0; i < count; i++) {
				childNoteDB
						.delete(llListNoteItemsWrapper.getChildAt(i).getId());
			}
			listNoteDB.delete(lsid);
			progressDialog.dismiss();
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_DELETE:
			AlertDialog.Builder quitDialog = new AlertDialog.Builder(context);
			quitDialog.setTitle("Do you want to delete this list note?");
			quitDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							progressDialog = new ProgressDialog(context);
							progressDialog
									.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog
									.setMessage("Deleting your list note");
							progressDialog.show();
							progressDialog
									.setOnDismissListener(new OnDismissListener() {
										public void onDismiss(DialogInterface di) {
											Toast.makeText(context,
													"Deleted Successfully!",
													Toast.LENGTH_LONG).show();
											Intent intent = new Intent(context,
													MainActivity.class);
											startActivity(intent);
											finish();
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
			intent = new Intent(context, EditListNoteActivity.class);
			intent.putExtra("lsid", String.valueOf(lsid));
			startActivity(intent);
			finish();
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}
}
