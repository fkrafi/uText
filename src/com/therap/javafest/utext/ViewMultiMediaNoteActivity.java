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

import com.therap.javafest.utext.lib.AudioData;
import com.therap.javafest.utext.lib.ImageData;
import com.therap.javafest.utext.lib.MultiMediaNote;
import com.therap.javafest.utext.lib.VideoData;
import com.therap.javafest.utext.sqlitedb.AudioDataDB;
import com.therap.javafest.utext.sqlitedb.ImageDataDB;
import com.therap.javafest.utext.sqlitedb.MultiMediaNoteDB;
import com.therap.javafest.utext.sqlitedb.VideoDataDB;

public class ViewMultiMediaNoteActivity extends GDActivity {

	private static final int ACTION_BAR_DELETE = 1;
	private static final int ACTION_BAR_EDIT = 2;

	private int mid;
	private Intent intent;

	private ImageView ivImportant;
	private LinearLayout llMultimedia;
	private TextView tvDateTime, tvLocation, tvText;

	private AudioDataDB audioDataDB;
	private ImageDataDB imageDataDB;
	private VideoDataDB videoDataDB;
	private MultiMediaNoteDB multiMediaNoteDB;

	private AudioPlayerUI audioPlayerUI;
	private ImageViewerUI imageViewerUI;
	private VideoPlayerUI videoPlayerUI;

	private ArrayList<ImageData> id;
	private ArrayList<AudioData> ad;
	private ArrayList<VideoData> vd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_view_multi_media_note);

		addActionBarItem(Type.Trashcan, ACTION_BAR_DELETE);
		addActionBarItem(Type.Edit, ACTION_BAR_EDIT);

		Init();
	}

	private void Init() {
		intent = getIntent();
		mid = Integer.parseInt(intent.getStringExtra("mid"));
		MultiMediaNote data = new MultiMediaNote();

		audioDataDB = new AudioDataDB(ViewMultiMediaNoteActivity.this);
		imageDataDB = new ImageDataDB(ViewMultiMediaNoteActivity.this);
		videoDataDB = new VideoDataDB(ViewMultiMediaNoteActivity.this);
		multiMediaNoteDB = new MultiMediaNoteDB(ViewMultiMediaNoteActivity.this);

		data = multiMediaNoteDB.selectByMid(mid);

		tvDateTime = (TextView) findViewById(R.id.tvDateTime);
		Timestamp ts = Timestamp.valueOf(data.modified);
		Date date = new Date(ts.getTime());
		DateFormat dateFormat = new SimpleDateFormat("E, dd M yyyy hh:mm a");
		tvDateTime.setText(dateFormat.format(date).toString());

		tvText = (TextView) findViewById(R.id.tvText);
		tvText.setText(data.text);

		ivImportant = (ImageView) findViewById(R.id.ivImportant);
		if (data.is_important == 1) {
			ivImportant.setImageResource(R.drawable.ic_menu_star_yellow);
		}

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocation.setText("Dhaka, Bangladesh");

		llMultimedia = (LinearLayout) findViewById(R.id.llMultimedia);

		id = imageDataDB.selectByMid(mid);
		for (ImageData i : id) {
			imageViewerUI = new ImageViewerUI(ViewMultiMediaNoteActivity.this);
			imageViewerUI.setImage(i.bitmapUri, this.getContentResolver());
			imageViewerUI.setId(i.iid);
			imageViewerUI.setDeleteEnable(false);
			llMultimedia.addView(imageViewerUI);
		}

		ad = audioDataDB.selectByMid(mid);
		for (AudioData a : ad) {
			audioPlayerUI = new AudioPlayerUI(ViewMultiMediaNoteActivity.this);
			audioPlayerUI.setAudioUri(a.audioUri);
			audioPlayerUI.setId(a.aid);
			audioPlayerUI.setDeleteEnable(false);
			llMultimedia.addView(audioPlayerUI);
		}

		vd = videoDataDB.selectByMid(mid);
		for (VideoData v : vd) {
			videoPlayerUI = new VideoPlayerUI(ViewMultiMediaNoteActivity.this);
			videoPlayerUI.setVideoUri(v.videoUri);
			videoPlayerUI.setId(v.vid);
			videoPlayerUI.setDeleteEnable(false);
			llMultimedia.addView(videoPlayerUI);
		}

	}

	private void delete() {
		Intent intent = new Intent(ViewMultiMediaNoteActivity.this,
				MainActivity.class);
		for (AudioData a : ad) {
			audioDataDB.delete(mid, a.aid);
		}
		for (ImageData i : id) {
			imageDataDB.delete(mid, i.iid);
		}
		for (VideoData v : vd) {
			videoDataDB.delete(mid, v.vid);
		}
		multiMediaNoteDB.delete(mid);
		Toast.makeText(ViewMultiMediaNoteActivity.this,
				"Successfully Deleted!", Toast.LENGTH_LONG).show();
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ViewMultiMediaNoteActivity.this,
				MainActivity.class);
		startActivity(intent);
		super.onBackPressed();
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_DELETE:
			AlertDialog.Builder quitDialog = new AlertDialog.Builder(
					ViewMultiMediaNoteActivity.this);
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
			Intent intent = new Intent(ViewMultiMediaNoteActivity.this,
					EditMultiMediaNoteActivity.class);
			intent.putExtra("mid", mid);
			startActivity(intent);
			finish();
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}
}
