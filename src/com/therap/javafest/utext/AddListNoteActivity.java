package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddListNoteActivity extends GDActivity implements OnClickListener {
	private static final int ACTION_BAR_SAVE = 1;
	private boolean important = false;

	private ListNoteAddItemUI item;
	private EditText etListNoteTitle;
	private LinearLayout llListNoteItemsWrapper;
	private Button bAddItem, bLocation, bImportant;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_add_list_note);
		addActionBarItem(Type.Save, ACTION_BAR_SAVE);

		Init();
	}

	private void Init() {
		etListNoteTitle = (EditText) findViewById(R.id.etListNoteTitle);

		llListNoteItemsWrapper = (LinearLayout) findViewById(R.id.llListNoteItemsWrapper);

		bAddItem = (Button) findViewById(R.id.bAddItem);
		bAddItem.setOnClickListener(this);

		bLocation = (Button) findViewById(R.id.bLocation);
		bLocation.setOnClickListener(this);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		etListNoteTitle.getText().toString();
		int count = llListNoteItemsWrapper.getChildCount();
		ListNoteAddItemUI listitem;
		for (int i = 0; i < count; i++) {
			listitem = (ListNoteAddItemUI) llListNoteItemsWrapper.getChildAt(i);
			Toast.makeText(AddListNoteActivity.this, listitem.getText(),
					Toast.LENGTH_SHORT).show();
		}
		return super.onHandleActionBarItemClick(item, position);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bAddItem:
			try {
				item = new ListNoteAddItemUI(AddListNoteActivity.this);
				llListNoteItemsWrapper.addView(item);
			} catch (Exception exp) {
				Toast.makeText(AddListNoteActivity.this, exp.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bImportant:
			if (important) {
				important = false;
				bImportant.setCompoundDrawablesWithIntrinsicBounds(
						getBaseContext().getResources().getDrawable(
								R.drawable.ic_menu_star), null, null, null);
			} else {
				important = true;
				bImportant.setCompoundDrawablesWithIntrinsicBounds(
						getBaseContext().getResources().getDrawable(
								R.drawable.ic_menu_star_yellow), null, null,
						null);
			}
			break;
		}
	}
}
