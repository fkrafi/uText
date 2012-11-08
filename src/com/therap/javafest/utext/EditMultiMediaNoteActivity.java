package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.therap.javafest.utext.lib.AudioData;
import com.therap.javafest.utext.lib.ImageData;
import com.therap.javafest.utext.lib.ImageProcessing;
import com.therap.javafest.utext.lib.MultiMediaNote;
import com.therap.javafest.utext.lib.VideoData;
import com.therap.javafest.utext.sqlitedb.AudioDataDB;
import com.therap.javafest.utext.sqlitedb.ImageDataDB;
import com.therap.javafest.utext.sqlitedb.MultiMediaNoteDB;
import com.therap.javafest.utext.sqlitedb.VideoDataDB;

public class EditMultiMediaNoteActivity extends GDActivity implements
		OnClickListener {

	private static final int ACTION_BAR_SAVE = 1;
	private static final int REQUEST_SPEECH = 1111;
	private static final int REQUEST_AUDIO_RECORDING = 2222;
	private static final int REQUEST_GALLERY = 3333;
	private static final int REQUEST_IMAGE_CAMERA = 4444;
	private static final int REQUEST_VIDEO_CAMERA = 5555;

	private int important = 0;

	private Uri bitmapUri;
	private ImageProcessing ip;

	private AudioDataDB audioDataDB;
	private ImageDataDB imageDataDB;
	private VideoDataDB videoDataDB;
	private MultiMediaNoteDB multiMediaNoteDB;

	private ImageButton ibASR;
	private EditText etNoteText;
	private LinearLayout llMultimedia;
	private AudioPlayerUI audioPlayerUI;
	private ImageViewerUI imageViewerUI;
	private VideoPlayerUI videoPlayerUI;
	private Button bGallery, bAudio, bLocation, bImportant, bFromGallery,
			bFromCamera, bFromCamRecorder;

	private Dialog dialogAddNoteOption;
	private ProgressDialog progressDialog;

	private Intent intent;
	private int mid;

	private ArrayList<ImageData> id;
	private ArrayList<AudioData> ad;
	private ArrayList<VideoData> vd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_edit_multi_media_note);

		addActionBarItem(Type.Save, ACTION_BAR_SAVE);

		Init();
	}

	private void Init() {
		intent = getIntent();
		mid = Integer.parseInt(intent.getStringExtra("mid"));

		MultiMediaNote data = new MultiMediaNote();
		
		videoDataDB = new VideoDataDB(EditMultiMediaNoteActivity.this);
		imageDataDB = new ImageDataDB(EditMultiMediaNoteActivity.this);
		audioDataDB = new AudioDataDB(EditMultiMediaNoteActivity.this);
		multiMediaNoteDB = new MultiMediaNoteDB(EditMultiMediaNoteActivity.this);
		ip = new ImageProcessing(EditMultiMediaNoteActivity.this);

		ibASR = (ImageButton) findViewById(R.id.ibASR);
		ibASR.setOnClickListener(this);
		bGallery = (Button) findViewById(R.id.bGallery);
		bGallery.setOnClickListener(this);
		bAudio = (Button) findViewById(R.id.bAudio);
		bAudio.setOnClickListener(this);
		bLocation = (Button) findViewById(R.id.bLocation);
		bLocation.setOnClickListener(this);

		audioDataDB = new AudioDataDB(EditMultiMediaNoteActivity.this);
		imageDataDB = new ImageDataDB(EditMultiMediaNoteActivity.this);
		videoDataDB = new VideoDataDB(EditMultiMediaNoteActivity.this);
		multiMediaNoteDB = new MultiMediaNoteDB(EditMultiMediaNoteActivity.this);

		data = multiMediaNoteDB.selectByMid(mid);

		etNoteText = (EditText) findViewById(R.id.etNoteText);
		etNoteText.setText(data.text);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);
		if (data.is_important == 1) {
			bImportant.setCompoundDrawablesWithIntrinsicBounds(
					getBaseContext().getResources().getDrawable(
							R.drawable.ic_menu_star_yellow), null, null, null);
		}

		llMultimedia = (LinearLayout) findViewById(R.id.llMultimedia);

		id = imageDataDB.selectByMid(mid);
		for (ImageData i : id) {
			imageViewerUI = new ImageViewerUI(EditMultiMediaNoteActivity.this);
			imageViewerUI.setImage(i.bitmapUri, this.getContentResolver());
			imageViewerUI.setId(i.iid);
			imageViewerUI.setDeleteEnable(false);
			llMultimedia.addView(imageViewerUI);
		}

		ad = audioDataDB.selectByMid(mid);
		for (AudioData a : ad) {
			audioPlayerUI = new AudioPlayerUI(EditMultiMediaNoteActivity.this);
			audioPlayerUI.setAudioUri(a.audioUri);
			audioPlayerUI.setId(a.aid);
			audioPlayerUI.setDeleteEnable(false);
			llMultimedia.addView(audioPlayerUI);
		}

		vd = videoDataDB.selectByMid(mid);
		for (VideoData v : vd) {
			videoPlayerUI = new VideoPlayerUI(EditMultiMediaNoteActivity.this);
			videoPlayerUI.setVideoUri(v.videoUri);
			videoPlayerUI.setId(v.vid);
			videoPlayerUI.setDeleteEnable(false);
			llMultimedia.addView(videoPlayerUI);
		}
	}

	private class SaveNoteThread extends Thread {
		public void run() {
			String text = etNoteText.getText().toString();
			long mid = multiMediaNoteDB.insert(text, important);
			int count = llMultimedia.getChildCount();
			for (int i = 0; i < count; i++) {
				View view = llMultimedia.getChildAt(i);
				if (view.getClass() == AudioPlayerUI.class) {
					audioPlayerUI = (AudioPlayerUI) view;
					if (audioPlayerUI.getId() == R.string.default_id) {
						audioDataDB.insert(mid, audioPlayerUI.getUri()
								.toString());
					}
				} else if (view.getClass() == ImageViewerUI.class) {
					imageViewerUI = (ImageViewerUI) view;
					if (imageViewerUI.getId() == R.string.default_id) {
						imageDataDB.insert(mid, imageViewerUI.getBitmapUri()
								.toString());
					}
				} else if (view.getClass() == VideoPlayerUI.class) {
					videoPlayerUI = (VideoPlayerUI) view;
					if (videoPlayerUI.getId() == R.string.default_id) {
						videoDataDB.insert(mid, videoPlayerUI.getVideoUri()
								.toString());
					}
				}
			}
			progressDialog.dismiss();
			finish();
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_SAVE:
			String text = etNoteText.getText().toString();
			int count = llMultimedia.getChildCount();
			if (text.trim().length() > 0 || count > 0) {
				progressDialog = new ProgressDialog(this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("Saving Your Multimedia Note");
				progressDialog.show();
				SaveNoteThread saveNoteThread = new SaveNoteThread();
				saveNoteThread.start();
				Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent(EditMultiMediaNoteActivity.this,
						ViewMultiMediaNoteActivity.class);
				intent.putExtra("mid", mid);
				startActivity(intent);
			} else {
				Toast.makeText(EditMultiMediaNoteActivity.this,
						"Cannot Save Empty Multimedia Note!", Toast.LENGTH_LONG)
						.show();
			}
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == REQUEST_SPEECH && resultCode == RESULT_OK) {
			String text = etNoteText.getText().toString();
			String sentence = intent.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS).get(0);
			int index = etNoteText.getSelectionStart();
			if (index >= 0 && index < text.length()) {
				text = text.substring(0, index) + sentence
						+ text.substring(index + 1);
			} else {
				etNoteText.setText(text + sentence);
			}
		} else if (requestCode == REQUEST_AUDIO_RECORDING
				&& resultCode == RESULT_OK) {
			Uri audioUri = intent.getData();
			audioPlayerUI = new AudioPlayerUI(EditMultiMediaNoteActivity.this);
			audioPlayerUI.setAudioUri(audioUri);
			llMultimedia.addView(audioPlayerUI);
		} else if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
			bitmapUri = intent.getData();
			imageViewerUI = new ImageViewerUI(EditMultiMediaNoteActivity.this);
			imageViewerUI.setImage(bitmapUri, this.getContentResolver());
			llMultimedia.addView(imageViewerUI);
		} else if (requestCode == REQUEST_IMAGE_CAMERA
				&& resultCode == RESULT_OK) {
			bitmapUri = ip.saveBitmap((Bitmap) intent.getExtras().get("data"));
			imageViewerUI = new ImageViewerUI(EditMultiMediaNoteActivity.this);
			imageViewerUI.setImage(bitmapUri, this.getContentResolver());
			llMultimedia.addView(imageViewerUI);
		} else if (requestCode == REQUEST_VIDEO_CAMERA
				&& resultCode == RESULT_OK) {
			Uri videoUri = intent.getData();
			videoPlayerUI = new VideoPlayerUI(EditMultiMediaNoteActivity.this);
			videoPlayerUI.setVideoUri(videoUri);
			llMultimedia.addView(videoPlayerUI);
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bGallery:
			dialogAddNoteOption = new Dialog(EditMultiMediaNoteActivity.this);
			dialogAddNoteOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogAddNoteOption.setContentView(R.layout.gallery_dialog_items);
			bFromGallery = (Button) dialogAddNoteOption
					.findViewById(R.id.bFromGallery);
			bFromGallery.setOnClickListener(this);
			bFromCamera = (Button) dialogAddNoteOption
					.findViewById(R.id.bFromCamera);
			bFromCamera.setOnClickListener(this);
			bFromCamRecorder = (Button) dialogAddNoteOption
					.findViewById(R.id.bFromCamRecorder);
			bFromCamRecorder.setOnClickListener(this);
			dialogAddNoteOption.show();
			break;
		case R.id.ibASR:
			Intent intentASR = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intentASR.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			// intentASR.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
			intentASR.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
			startActivityForResult(intentASR, REQUEST_SPEECH);
			break;
		case R.id.bAudio:
			Intent intent = new Intent(
					MediaStore.Audio.Media.RECORD_SOUND_ACTION);
			startActivityForResult(intent, REQUEST_AUDIO_RECORDING);
			break;
		case R.id.bImportant:
			if (important == 1) {
				important = 0;
				bImportant.setCompoundDrawablesWithIntrinsicBounds(
						getBaseContext().getResources().getDrawable(
								R.drawable.ic_menu_star), null, null, null);
			} else {
				important = 1;
				bImportant.setCompoundDrawablesWithIntrinsicBounds(
						getBaseContext().getResources().getDrawable(
								R.drawable.ic_menu_star_yellow), null, null,
						null);
			}
			break;
		case R.id.bFromGallery:
			dialogAddNoteOption.dismiss();
			Intent intentGallery = new Intent(Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intentGallery.setType("image/*");
			startActivityForResult(intentGallery, REQUEST_GALLERY);
			break;
		case R.id.bFromCamera:
			dialogAddNoteOption.dismiss();
			Intent intentImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intentImage, REQUEST_IMAGE_CAMERA);
			break;
		case R.id.bFromCamRecorder:
			dialogAddNoteOption.dismiss();
			Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(intentVideo, REQUEST_VIDEO_CAMERA);
			break;
		}
	}
}
