package com.therap.javafest.utext;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
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
import android.widget.TextView.BufferType;

public class ListNoteItemUI extends LinearLayout implements
		OnCheckedChangeListener, OnClickListener {

	CheckBox cbDone;
	EditText etText;
	ImageButton ibDelete;
	Context context;
	LinearLayout layout;

	public ListNoteItemUI(Context context) {
		super(context);
		this.context = context;
		Init();
	}

	public ListNoteItemUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		Init();
	}

	private void Init() {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) layoutInflater.inflate(
				R.layout.listnote_add_item_ui, this);

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

	public void setText(SpannableString text) {
		etText.setText(text, BufferType.SPANNABLE);
	}

	public String getText() {
		return etText.getText().toString();
	}

	public void setTextEditable(boolean editable) {
		etText.setEnabled(editable);
	}

	public void setDone(int is_complete) {
		cbDone.setChecked(is_complete == 1);
	}

	public int isDone() {
		if (cbDone.isChecked() == true) {
			return 1;
		}
		return 0;
	}

	public void setDeleteEnable(boolean enable) {
		if (enable == true) {
			ibDelete.setVisibility(View.VISIBLE);
		} else {
			ibDelete.setVisibility(View.INVISIBLE);
		}
	}

	public void setCheckDone(boolean flag) {
		cbDone.setClickable(flag);
	}

	public void setTextFocused(boolean b) {
		etText.setFocusable(b);
	}
}