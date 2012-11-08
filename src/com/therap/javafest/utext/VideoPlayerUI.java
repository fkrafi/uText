package com.therap.javafest.utext;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoPlayerUI extends LinearLayout implements OnClickListener {
	private Context context;
	private LayoutInflater inflater;

	private ImageButton ibDelete, ibPlay;
	private TextView tvMediaType;

	private Uri videoUri;

	public VideoPlayerUI(Context context) {
		super(context);
		this.context = context;

		Init();
	}

	private void Init() {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.video_player_ui, this);

		ibPlay = (ImageButton) findViewById(R.id.ibPlay);
		ibPlay.setOnClickListener(this);

		ibDelete = (ImageButton) findViewById(R.id.ibDelete);
		ibDelete.setOnClickListener(this);

		tvMediaType = (TextView) findViewById(R.id.tvMediaType);
		tvMediaType.setText("3");

	}

	public void setVideoUri(Uri videoUri) {
		this.videoUri = videoUri;
	}

	public Uri getVideoUri() {
		return videoUri;
	}

	public int getMediaType() {
		return Integer.parseInt(tvMediaType.getText().toString());
	}

	public void setDeleteEnable(boolean enable) {
		if (enable == true) {
			ibDelete.setVisibility(View.VISIBLE);
		} else {
			ibDelete.setVisibility(View.INVISIBLE);
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ibPlay:
			Intent intentVideo = new Intent(Intent.ACTION_VIEW, videoUri);
			context.startActivity(intentVideo);
			break;
		case R.id.ibDelete:
			((ViewManager) this.getParent()).removeView(this);
			break;
		}
	}
}
