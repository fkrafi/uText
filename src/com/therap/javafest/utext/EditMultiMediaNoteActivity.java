package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
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
import android.widget.TextView;
import android.widget.Toast;

import com.therap.javafest.utext.lib.AudioData;
import com.therap.javafest.utext.lib.ImageData;
import com.therap.javafest.utext.lib.ImageProcessing;
import com.therap.javafest.utext.lib.LocationData;
import com.therap.javafest.utext.lib.MultiMediaNote;
import com.therap.javafest.utext.lib.Note;
import com.therap.javafest.utext.lib.VideoData;
import com.therap.javafest.utext.sqlitedb.AudioDataDB;
import com.therap.javafest.utext.sqlitedb.ImageDataDB;
import com.therap.javafest.utext.sqlitedb.LocationDataDB;
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
	private static final int REQUEST_MAP_LOCATION = 10;

	private Context context;
	private Address address;
	private int important = 0;

	private Uri bitmapUri;
	private ImageProcessing ip;

	private AudioDataDB audioDataDB;
	private ImageDataDB imageDataDB;
	private VideoDataDB videoDataDB;
	private LocationDataDB locationDataDB;
	private MultiMediaNoteDB multiMediaNoteDB;

	private EditText etNoteText;
	private ImageButton ibASR, ibLVDelete;
	private AudioPlayerUI audioPlayerUI;
	private ImageViewerUI imageViewerUI;
	private VideoPlayerUI videoPlayerUI;
	private LinearLayout llMultimedia, llLocationWrapper;
	private Button bGallery, bAudio, bLocation, bImportant, bFromGallery,
			bFromCamera, bFromCamRecorder;
	private TextView tvLocation, tvLocationLongitude, tvLocationLatitude;

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
		Init();
		renderView();
	}

	private void Init() {
		context = this;
		intent = getIntent();
		mid = Integer.parseInt(intent.getStringExtra("mid"));

		videoDataDB = new VideoDataDB(context);
		imageDataDB = new ImageDataDB(context);
		audioDataDB = new AudioDataDB(context);
		multiMediaNoteDB = new MultiMediaNoteDB(context);

		ip = new ImageProcessing(context);

		audioDataDB = new AudioDataDB(context);
		imageDataDB = new ImageDataDB(context);
		videoDataDB = new VideoDataDB(context);
		multiMediaNoteDB = new MultiMediaNoteDB(context);
	}

	private void renderView() {
		addActionBarItem(Type.Save, ACTION_BAR_SAVE);
		ibASR = (ImageButton) findViewById(R.id.ibASR);
		ibASR.setOnClickListener(this);
		bGallery = (Button) findViewById(R.id.bGallery);
		bGallery.setOnClickListener(this);
		bAudio = (Button) findViewById(R.id.bAudio);
		bAudio.setOnClickListener(this);
		bLocation = (Button) findViewById(R.id.bLocation);
		bLocation.setOnClickListener(this);

		MultiMediaNote data = multiMediaNoteDB.selectByMid(mid);

		etNoteText = (EditText) findViewById(R.id.etNoteText);
		etNoteText.setText(data.text);

		bImportant = (Button) findViewById(R.id.bImportant);
		bImportant.setOnClickListener(this);
		if (data.is_important == 1) {
			important = 1;
			bImportant.setCompoundDrawablesWithIntrinsicBounds(
					getBaseContext().getResources().getDrawable(
							R.drawable.ic_menu_star_yellow), null, null, null);
		}

		llMultimedia = (LinearLayout) findViewById(R.id.llMultimedia);

		id = imageDataDB.selectByMid(mid);
		for (ImageData i : id) {
			imageViewerUI = new ImageViewerUI(context);
			imageViewerUI.setImage(i.bitmapUri, this.getContentResolver());
			imageViewerUI.setId(i.iid);
			llMultimedia.addView(imageViewerUI);
		}

		ad = audioDataDB.selectByMid(mid);
		for (AudioData a : ad) {
			audioPlayerUI = new AudioPlayerUI(context);
			audioPlayerUI.setAudioUri(a.audioUri);
			audioPlayerUI.setId(a.aid);
			llMultimedia.addView(audioPlayerUI);
		}

		vd = videoDataDB.selectByMid(mid);
		for (VideoData v : vd) {
			videoPlayerUI = new VideoPlayerUI(context);
			videoPlayerUI.setVideoUri(v.videoUri);
			videoPlayerUI.setId(v.vid);
			llMultimedia.addView(videoPlayerUI);
		}

		llLocationWrapper = (LinearLayout) findViewById(R.id.llLocationWrapper);
		llLocationWrapper.setVisibility(View.INVISIBLE);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocationLongitude = (TextView) findViewById(R.id.tvLocationLongitude);
		tvLocationLatitude = (TextView) findViewById(R.id.tvLocationLatitude);

		ibLVDelete = (ImageButton) findViewById(R.id.ibLVDelete);
		ibLVDelete.setOnClickListener(this);

		LocationData locationData = new LocationData();
		locationDataDB = new LocationDataDB(context);
		locationData = locationDataDB.selectByNoteId(mid, Note.MULTIMEDIA_NOTE);
		if (locationData != null) {
			tvLocation.setText(locationData.place);
			tvLocationLongitude.setText(String.valueOf(locationData.longitude));
			tvLocationLatitude.setText(String.valueOf(locationData.latitude));
			llLocationWrapper.setVisibility(View.VISIBLE);
		}
	}

	private void backward() {
		Intent intent = new Intent(context, ViewMultiMediaNoteActivity.class);
		intent.putExtra("mid", String.valueOf(mid));
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		backward();
	}

	private class SaveNoteThread extends Thread {
		public void run() {
			String text = etNoteText.getText().toString();
			multiMediaNoteDB.update(mid, text, important);
			audioDataDB.deleteAllByMid(mid);
			imageDataDB.deleteAllByMid(mid);
			videoDataDB.deleteAllByMid(mid);
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
			if (llLocationWrapper.getVisibility() == View.VISIBLE) {
				locationDataDB.update(mid, Double
						.parseDouble(tvLocationLongitude.getText().toString()),
						Double.parseDouble(tvLocationLatitude.getText()
								.toString()), tvLocation.getText().toString());
			} else {
				locationDataDB.delete(mid, Note.MULTIMEDIA_NOTE);
			}
			progressDialog.dismiss();
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
				progressDialog.setOnDismissListener(new OnDismissListener() {
					public void onDismiss(DialogInterface di) {
						backward();
						Toast.makeText(context, "Saved Successfully!",
								Toast.LENGTH_LONG).show();
					}
				});
				progressDialog.show();
				SaveNoteThread saveNoteThread = new SaveNoteThread();
				saveNoteThread.start();
			} else {
				Toast.makeText(context, "Cannot Save Empty Multimedia Note!",
						Toast.LENGTH_LONG).show();
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
			audioPlayerUI = new AudioPlayerUI(context);
			audioPlayerUI.setAudioUri(audioUri);
			llMultimedia.addView(audioPlayerUI);
		} else if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
			bitmapUri = intent.getData();
			imageViewerUI = new ImageViewerUI(context);
			imageViewerUI.setImage(bitmapUri, this.getContentResolver());
			llMultimedia.addView(imageViewerUI);
		} else if (requestCode == REQUEST_IMAGE_CAMERA
				&& resultCode == RESULT_OK) {
			bitmapUri = ip.saveBitmap((Bitmap) intent.getExtras().get("data"));
			imageViewerUI = new ImageViewerUI(context);
			imageViewerUI.setImage(bitmapUri, this.getContentResolver());
			llMultimedia.addView(imageViewerUI);
		} else if (requestCode == REQUEST_VIDEO_CAMERA
				&& resultCode == RESULT_OK) {
			Uri videoUri = intent.getData();
			videoPlayerUI = new VideoPlayerUI(context);
			videoPlayerUI.setVideoUri(videoUri);
			llMultimedia.addView(videoPlayerUI);
		} else if (requestCode == REQUEST_MAP_LOCATION
				&& resultCode == RESULT_OK) {
			address = intent.getParcelableExtra(AddMapActivity.RESULT_ADDRESS);
			tvLocation.setText(address.getAddressLine(0).toString() + ", "
					+ address.getAddressLine(1).toString() + ", "
					+ address.getCountryCode().toString());
			tvLocationLongitude.setText(String.valueOf(address.getLongitude()));
			tvLocationLatitude.setText(String.valueOf(address.getLatitude()));
			llLocationWrapper.setVisibility(View.VISIBLE);
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bGallery:
			dialogAddNoteOption = new Dialog(context);
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
			try {
				Intent intentASR = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intentASR.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intentASR.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
				startActivityForResult(intentASR, REQUEST_SPEECH);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(context, "Google Voice Search Not Installed!",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.bAudio:
			try {
				startActivityForResult(new Intent(
						MediaStore.Audio.Media.RECORD_SOUND_ACTION),
						REQUEST_AUDIO_RECORDING);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(context, "Sound Recorder Not Found!!",
						Toast.LENGTH_LONG).show();
			}
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
			try {
				Intent intentGallery = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intentGallery.setType("image/*");
				startActivityForResult(intentGallery, REQUEST_GALLERY);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(context, "External Media Gallery Not Found!",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.bFromCamera:
			dialogAddNoteOption.dismiss();
			try {
				startActivityForResult(new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAMERA);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(context, "Image Camera Not Found!",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.bFromCamRecorder:
			try {
				dialogAddNoteOption.dismiss();
				startActivityForResult(new Intent(
						MediaStore.ACTION_VIDEO_CAPTURE), REQUEST_VIDEO_CAMERA);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(context, "Video Capture Not Found!",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.bLocation:
			startActivityForResult(new Intent(this, AddMapActivity.class),
					REQUEST_MAP_LOCATION);
			break;
		case R.id.ibLVDelete:
			tvLocation.setText("");
			tvLocationLongitude.setText("");
			tvLocationLatitude.setText("");
			llLocationWrapper.setVisibility(View.INVISIBLE);
			break;
		}
	}
}
