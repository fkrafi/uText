package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageNoteActivity extends GDActivity implements OnClickListener {

	ImageView ivImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_image_note);

		init();
	}

	private void init() {
		ivImage = (ImageView) findViewById(R.id.ivImage);
		ivImage.setOnClickListener(this);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ivImage:
			Toast.makeText(this, "ImageView Clicked", Toast.LENGTH_LONG).show();
			break;
		}
	}
}
