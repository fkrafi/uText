package com.therap.javafest.utext;

import greendroid.app.GDActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.therap.javafest.utext.lib.Note;
import com.therap.javafest.utext.lib.NoteComparator;
import com.therap.javafest.utext.lib.NoteRetriever;

public class SearchActivity extends GDActivity implements OnClickListener,
		OnGesturePerformedListener {

	private static final int REQUEST_SPEECH = 1111;

	private GestureLibrary mLibrary;
	private GestureOverlayView govCharacterRecognizer;

	private Context context;
	private ListView lvNotes;
	private ImageButton ibASR;
	private EditText etSearchFor;

	private NoteRetriever noteRetriever;
	private NoteListViewItemAdapter adapter;
	private ArrayList<Note> allNotes, notes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_search);
		renderView();
		mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mLibrary.load()) {
			finish();
		}
	}

	private void renderView() {
		context = this;
		ibASR = (ImageButton) findViewById(R.id.ibASR);
		ibASR.setOnClickListener(this);

		govCharacterRecognizer = (GestureOverlayView) findViewById(R.id.govCharacterRecognizer);
		govCharacterRecognizer.addOnGesturePerformedListener(this);

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

		lvNotes = (ListView) findViewById(R.id.lvNotes);

		noteRetriever = new NoteRetriever(context);

		notes = new ArrayList<Note>();
		allNotes = new ArrayList<Note>();

		allNotes.addAll(noteRetriever.getAll());
		notes.clear();
		notes.addAll(allNotes);

		Collections.sort(notes, new NoteComparator(
				NoteComparator.DEFAULT_COMPARE));

		adapter = new NoteListViewItemAdapter(this, notes);
		lvNotes.setAdapter(adapter);

		lvNotes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				Intent intent = null;
				Note item = notes.get(pos);
				if (item.getType() == Note.MULTIMEDIA_NOTE) {
					intent = new Intent(context,
							ViewMultiMediaNoteActivity.class);
					intent.putExtra("mid", item.getId());
				} else if (item.getType() == Note.LIST_NOTE) {
					intent = new Intent(context, ViewListNoteActivity.class);
					intent.putExtra("lsid", item.getId());
				} else if (item.getType() == Note.REMINDER) {
					intent = new Intent(context, ViewReminderActivity.class);
					intent.putExtra("rid", item.getId());
				}
				try {
					startActivity(intent);
					finish();
				} catch (Exception exp) {
					Toast.makeText(context, exp.getMessage(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	private void Search() {
		String pattern = "";
		String tokens[] = etSearchFor.getText().toString().trim().split(" ");
		for (String token : tokens) {
			pattern += "(" + token + ")";
		}
		Log.d("pattern", pattern);
		Pattern r = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		notes.clear();
		for (Note n : allNotes) {
			String text = n.getText();
			Matcher m = r.matcher(text);
			int count = m.groupCount();
			n.setCount(count);
			if (count > 0) {
				notes.add(n);
			}
			Log.d("find", n.getText() + " : " + String.valueOf(count));
		}
		Log.d("notes", String.valueOf(notes.size()));
		Collections.sort(notes, new NoteComparator(
				NoteComparator.SEARCH_COMPARE));
		adapter = new NoteListViewItemAdapter(this, notes);
		lvNotes.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_SPEECH && resultCode == RESULT_OK) {
			etSearchFor.setText(data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS).get(0));
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ibASR:
			try {
				Intent intentASR = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intentASR.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				// intentASR.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
				intentASR.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
				startActivityForResult(intentASR, REQUEST_SPEECH);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(context, "Speech Recognizer Not Available",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			break;
		}
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
		if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
			String result = predictions.get(0).name;
			etSearchFor.setText(etSearchFor.getText().toString() + result);
		}
	}
}
