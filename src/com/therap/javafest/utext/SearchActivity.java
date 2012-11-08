package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchActivity extends GDActivity implements OnClickListener {

	private static final int VR_REQUEST_SPEECH = 1111;

	private EditText etSearchFor;
	private ImageButton ibASR;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_search);
		Init();
	}

	private void Init() {
		ibASR = (ImageButton) findViewById(R.id.ibASR);
		ibASR.setOnClickListener(this);

		etSearchFor = (EditText) findViewById(R.id.etSearchFor);
		etSearchFor.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				Search();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}

	private void Search() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == VR_REQUEST_SPEECH && resultCode == RESULT_OK) {
			etSearchFor.setText(data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS).get(0));
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ibASR:
			Intent intentASR = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intentASR.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			// intentASR.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
			intentASR.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
			startActivityForResult(intentASR, VR_REQUEST_SPEECH);
			break;
		}
	}
}
