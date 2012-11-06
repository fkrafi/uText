package com.therap.javafest.utext;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ListNoteAddItemUI extends LinearLayout implements
		OnCheckedChangeListener, OnClickListener {

	CheckBox cbDone;
	EditText etText;
	ImageButton ibDelete;
	Context context;
	LinearLayout layout;

	public ListNoteAddItemUI(Context context) {
		super(context);
		this.context = context;
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) layoutInflater.inflate(
				R.layout.listnote_add_item_ui, this);
		Init();
	}

	public ListNoteAddItemUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) layoutInflater.inflate(
				R.layout.listnote_add_item_ui, this);
		Init();
	}

	private void Init() {
		cbDone = (CheckBox) layout.findViewById(R.id.cbDone);
		cbDone.setOnCheckedChangeListener(this);
		etText = (EditText) layout.findViewById(R.id.etText);
		ibDelete = (ImageButton) layout.findViewById(R.id.ibDelete);
		ibDelete.setOnClickListener(this);
	}

	public void onCheckedChanged(CompoundButton cb, boolean checked) {
		if (checked == true) {
			etText.setPaintFlags(etText.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			etText.setPaintFlags(etText.getPaintFlags()
					& (~Paint.STRIKE_THRU_TEXT_FLAG));
		}
	}

	public void onClick(View view) {
		((ViewManager) this.getParent()).removeView(this);

	}

	public void setText(String text) {
		etText.setText(text);
	}

	public String getText() {
		return etText.getText().toString();
	}

	public void setDone(boolean checked) {
		cbDone.setChecked(checked);
	}

	public boolean isChecked() {
		return cbDone.isChecked();
	}
}
