package com.therap.javafest.utext;

import java.io.InputStream;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class AddMultiMediaNoteActivity extends GDActivity implements
		OnClickListener {

	private static final int ACTION_BAR_SAVE = 1;
	private static final int VR_REQUEST_SPEECH = 1111;
	private static final int VR_REQUEST_AUDIO_RECORDING = 2222;
	private static final int VR_REQUEST_GALLERY = 3333;
	private static final int VR_REQUEST_IMAGE = 4444;
	private static final int VR_REQUEST_VIDEO = 5555;

	private boolean important = false;

	private ImageButton ibASR;
	private EditText etNoteText;
	private LinearLayout llMultimedia;
	private AddAudioPlayerUI audioPlayerUI;
	private AddImageViewerUI imageViewerUI;
	private Button bGallery, bAudio, bLocation, bImportant, bFromGallery,
			bFromCamera, bFromCamRecorder;

	private Dialog dialogAddNoteOption;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_multi_media_add_note);

		addActionBarItem(Type.Save, ACTION_BAR_SAVE);

		Init();
	}

	private void Init() {

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

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		return super.onHandleActionBarItemClick(item, position);
	}

	private void start_voice_recognization() {
		try {
			Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			// i.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
			i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
			startActivityForResult(i, VR_REQUEST_SPEECH);
		} catch (Exception exp) {
			Toast.makeText(this, exp.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == VR_REQUEST_SPEECH && resultCode == RESULT_OK) {
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
		} else if (requestCode == VR_REQUEST_AUDIO_RECORDING
				&& resultCode == RESULT_OK) {
			try {
				Uri audioUri = intent.getData();
				audioPlayerUI = new AddAudioPlayerUI(
						AddMultiMediaNoteActivity.this);
				audioPlayerUI.setAudioUri(audioUri);
				llMultimedia.addView(audioPlayerUI);
			} catch (Exception exp) {
				Toast.makeText(this, exp.getMessage(), Toast.LENGTH_LONG)
						.show();
			}

		} else if (requestCode == VR_REQUEST_GALLERY && resultCode == RESULT_OK) {
			try {
				Uri bitmapUri = intent.getData();
				InputStream imageStream = getContentResolver().openInputStream(
						bitmapUri);
				Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
				imageViewerUI = new AddImageViewerUI(
						AddMultiMediaNoteActivity.this);
				imageViewerUI.setImage(bitmap);
				llMultimedia.addView(imageViewerUI);
			} catch (Exception exp) {
				Toast.makeText(AddMultiMediaNoteActivity.this,
						exp.getMessage(), Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == VR_REQUEST_IMAGE && resultCode == RESULT_OK) {
			try {
				Bitmap bitmap = (Bitmap) intent.getExtras().get("data");

				imageViewerUI = new AddImageViewerUI(
						AddMultiMediaNoteActivity.this);
				imageViewerUI.setImage(bitmap);
				llMultimedia.addView(imageViewerUI);
			} catch (Exception exp) {
				Toast.makeText(AddMultiMediaNoteActivity.this,
						exp.getMessage(), Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == VR_REQUEST_VIDEO && resultCode == RESULT_OK) {
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
			try {
				start_voice_recognization();
			} catch (Exception exp) {
				Toast.makeText(AddMultiMediaNoteActivity.this,
						exp.getMessage(), Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.bAudio:
			Intent intent = new Intent(
					MediaStore.Audio.Media.RECORD_SOUND_ACTION);
			startActivityForResult(intent, VR_REQUEST_AUDIO_RECORDING);
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
		case R.id.bFromGallery:
			dialogAddNoteOption.dismiss();
			Intent intentGallery = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intentGallery, VR_REQUEST_GALLERY);
			break;
		case R.id.bFromCamera:
			dialogAddNoteOption.dismiss();
			Intent intentImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intentImage, VR_REQUEST_IMAGE);
			break;
		case R.id.bFromCamRecorder:
			dialogAddNoteOption.dismiss();
			Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(intentVideo, VR_REQUEST_VIDEO);
			break;
		}
	}

}
