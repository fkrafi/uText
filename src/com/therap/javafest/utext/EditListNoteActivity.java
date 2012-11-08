package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
			item.setText(c.text);
			item.setDone(c.is_complete);
			llListNoteItemsWrapper.addView(item);
		}
	}

	public void onClick(View view) {
		// TODO Auto-generated method stub

	}

}
