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
import android.text.SpannableString;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.therap.javafest.utext.lib.AudioData;
import com.therap.javafest.utext.lib.ImageData;
import com.therap.javafest.utext.lib.LocationData;
import com.therap.javafest.utext.lib.MultiMediaNote;
import com.therap.javafest.utext.lib.Note;
import com.therap.javafest.utext.lib.VideoData;
import com.therap.javafest.utext.sqlitedb.AudioDataDB;
import com.therap.javafest.utext.sqlitedb.ImageDataDB;
import com.therap.javafest.utext.sqlitedb.LocationDataDB;
import com.therap.javafest.utext.sqlitedb.MultiMediaNoteDB;
import com.therap.javafest.utext.sqlitedb.VideoDataDB;

public class ViewMultiMediaNoteActivity extends GDActivity {

	private static final int ACTION_BAR_DELETE = 1;
	private static final int ACTION_BAR_EDIT = 2;

	private int mid;
	private Context context;
	private Intent intent;

	private ImageView ivImportant;
	private LinearLayout llMultimedia;
	private TextView tvDateTime, tvLocation, tvText;

	private AudioDataDB audioDataDB;
	private ImageDataDB imageDataDB;
	private VideoDataDB videoDataDB;
	private LocationDataDB locationDataDB;
	private MultiMediaNoteDB multiMediaNoteDB;

	private AudioPlayerUI audioPlayerUI;
	private ImageViewerUI imageViewerUI;
	private VideoPlayerUI videoPlayerUI;

	private ArrayList<ImageData> id;
	private ArrayList<AudioData> ad;
	private ArrayList<VideoData> vd;

	private SpanableText st;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_view_multi_media_note);
		Init();
		renderView();
	}

	private void Init() {
		context = this;
		intent = getIntent();
		st = new SpanableText(context);
		mid = Integer.parseInt(intent.getStringExtra("mid"));

		audioDataDB = new AudioDataDB(context);
		imageDataDB = new ImageDataDB(context);
		videoDataDB = new VideoDataDB(context);
		locationDataDB = new LocationDataDB(context);
		multiMediaNoteDB = new MultiMediaNoteDB(context);
	}

	private void renderView() {
		addActionBarItem(Type.Trashcan, ACTION_BAR_DELETE);
		addActionBarItem(Type.Edit, ACTION_BAR_EDIT);

		MultiMediaNote data = multiMediaNoteDB.selectByMid(mid);

		tvDateTime = (TextView) findViewById(R.id.tvDateTime);
		DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
		tvDateTime.setText(dateFormat.format(
				new Date((Timestamp.valueOf(data.modified)).getTime()))
				.toString());

		tvText = (TextView) findViewById(R.id.tvText);
		SpannableString ss = st.convertToSpannableString(data.text);
		tvText.setText(ss, BufferType.SPANNABLE);

		ivImportant = (ImageView) findViewById(R.id.ivImportant);
		if (data.is_important == 1) {
			ivImportant.setImageResource(R.drawable.ic_imageview_star_yellow);
		}

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		LocationData locationData = locationDataDB.selectByNoteId(mid,
				Note.MULTIMEDIA_NOTE);
		tvLocation.setText("");
		if (locationData != null) {
			tvLocation.setText(locationData.place);
		}

		llMultimedia = (LinearLayout) findViewById(R.id.llMultimedia);

		id = imageDataDB.selectByMid(mid);
		for (ImageData i : id) {
			imageViewerUI = new ImageViewerUI(context);
			imageViewerUI.setImage(i.bitmapUri, this.getContentResolver());
			imageViewerUI.setId(i.iid);
			imageViewerUI.setDeleteEnable(false);
			llMultimedia.addView(imageViewerUI);
		}

		ad = audioDataDB.selectByMid(mid);
		for (AudioData a : ad) {
			audioPlayerUI = new AudioPlayerUI(context);
			audioPlayerUI.setAudioUri(a.audioUri);
			audioPlayerUI.setId(a.aid);
			audioPlayerUI.setDeleteEnable(false);
			llMultimedia.addView(audioPlayerUI);
		}

		vd = videoDataDB.selectByMid(mid);
		for (VideoData v : vd) {
			videoPlayerUI = new VideoPlayerUI(context);
			videoPlayerUI.setVideoUri(v.videoUri);
			videoPlayerUI.setId(v.vid);
			videoPlayerUI.setDeleteEnable(false);
			llMultimedia.addView(videoPlayerUI);
		}
	}

	private class DeleteNoteThread extends Thread {
		public void run() {
			for (AudioData a : ad) {
				audioDataDB.delete(a.aid);
			}
			for (ImageData i : id) {
				imageDataDB.delete(i.iid);
			}
			for (VideoData v : vd) {
				videoDataDB.delete(v.vid);
			}
			multiMediaNoteDB.delete(mid);
			locationDataDB.delete(mid, Note.MULTIMEDIA_NOTE);
			progressDialog.dismiss();
		}
	}

	private void backward() {
		Intent intent = new Intent(context, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		backward();
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_DELETE:
			AlertDialog.Builder quitDialog = new AlertDialog.Builder(context);
			quitDialog.setTitle("Do you want to delete the note?");
			quitDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							progressDialog = new ProgressDialog(context);
							progressDialog
									.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog
									.setMessage("Deleting Your Multimedia Note");
							progressDialog.show();
							progressDialog
									.setOnDismissListener(new OnDismissListener() {
										public void onDismiss(DialogInterface di) {
											backward();
											Toast.makeText(context,
													"Deleted Successfully!",
													Toast.LENGTH_LONG).show();
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
			Intent intent = new Intent(context,
					EditMultiMediaNoteActivity.class);
			intent.putExtra("mid", String.valueOf(mid));
			startActivity(intent);
			finish();
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}
}
