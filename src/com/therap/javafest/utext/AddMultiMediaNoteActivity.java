package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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

import com.therap.javafest.utext.lib.ImageProcessing;
import com.therap.javafest.utext.sqlitedb.AudioDataDB;
import com.therap.javafest.utext.sqlitedb.ImageDataDB;
import com.therap.javafest.utext.sqlitedb.MultiMediaNoteDB;
import com.therap.javafest.utext.sqlitedb.VideoDataDB;

public class AddMultiMediaNoteActivity extends GDActivity implements
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_add_multi_media_note);

		addActionBarItem(Type.Save, ACTION_BAR_SAVE);

		Init();
	}

	private void Init() {
		videoDataDB = new VideoDataDB(AddMultiMediaNoteActivity.this);
		imageDataDB = new ImageDataDB(AddMultiMediaNoteActivity.this);
		audioDataDB = new AudioDataDB(AddMultiMediaNoteActivity.this);
		multiMediaNoteDB = new MultiMediaNoteDB(AddMultiMediaNoteActivity.this);
		ip = new ImageProcessing(AddMultiMediaNoteActivity.this);

		etNoteText = (EditText) findViewById(R.id.etNoteText);

		ibASR = (ImageButton) findViewById(R.id.ibASR);
		ibASR.setOnClickListener(this);

		bGallery = (Button) findViewById(R.id.bGallery);
		bGallery.setOnClickListener(this);

		bAudio = (Button) findViewById(R.id.bAudio);
		bAudio.setOnClickListener(this);

		bLocation = (Button) findViewById(R.id.bLocation);
		bLocation.setOnClickListener(this);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);

		llMultimedia = (LinearLayout) findViewById(R.id.llMultimedia);

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
					audioDataDB.insert(mid, audioPlayerUI.getUri().toString());
				} else if (view.getClass() == ImageViewerUI.class) {
					imageViewerUI = (ImageViewerUI) view;
					imageDataDB.insert(mid, imageViewerUI.getBitmapUri()
							.toString());
				} else if (view.getClass() == VideoPlayerUI.class) {
					videoPlayerUI = (VideoPlayerUI) view;
					videoDataDB.insert(mid, videoPlayerUI.getVideoUri()
							.toString());
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
				progressDialog.setOnDismissListener(new OnDismissListener() {
					public void onDismiss(DialogInterface di) {
						Toast.makeText(AddMultiMediaNoteActivity.this,
								"Saved Successfully!", Toast.LENGTH_LONG)
								.show();
					}
				});
				SaveNoteThread saveNoteThread = new SaveNoteThread();
				saveNoteThread.start();
				Intent intent = new Intent(AddMultiMediaNoteActivity.this,
						MainActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(AddMultiMediaNoteActivity.this,
						"Cannot Save Empty Multimedia Note!", Toast.LENGTH_LONG)
						.show();
			}
			break;
		}
		return super.onHandleActionBarItemClick(item, position);
	}

	@Override
	public void onBackPressed() {
		String text = etNoteText.getText().toString();
		int count = llMultimedia.getChildCount();
		if (text.length() > 0 || count > 0) {
			AlertDialog.Builder quitDialog = new AlertDialog.Builder(
					AddMultiMediaNoteActivity.this);
			quitDialog.setTitle("Do you want to quit without saving the note?");
			quitDialog.setPositiveButton("Ok, Quit!",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}

					});
			quitDialog.setNegativeButton("No", null);
			quitDialog.show();
		} else {
			super.onBackPressed();
		}
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
			audioPlayerUI = new AudioPlayerUI(AddMultiMediaNoteActivity.this);
			audioPlayerUI.setAudioUri(audioUri);
			llMultimedia.addView(audioPlayerUI);
		} else if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
			bitmapUri = intent.getData();
			imageViewerUI = new ImageViewerUI(AddMultiMediaNoteActivity.this);
			imageViewerUI.setImage(bitmapUri, this.getContentResolver());
			llMultimedia.addView(imageViewerUI);
		} else if (requestCode == REQUEST_IMAGE_CAMERA
				&& resultCode == RESULT_OK) {
			bitmapUri = ip.saveBitmap((Bitmap) intent.getExtras().get("data"));
			imageViewerUI = new ImageViewerUI(AddMultiMediaNoteActivity.this);
			imageViewerUI.setImage(bitmapUri, this.getContentResolver());
			llMultimedia.addView(imageViewerUI);
		} else if (requestCode == REQUEST_VIDEO_CAMERA
				&& resultCode == RESULT_OK) {
			Uri videoUri = intent.getData();
			videoPlayerUI = new VideoPlayerUI(AddMultiMediaNoteActivity.this);
			videoPlayerUI.setVideoUri(videoUri);
			llMultimedia.addView(videoPlayerUI);
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bGallery:
			dialogAddNoteOption = new Dialog(AddMultiMediaNoteActivity.this);
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
