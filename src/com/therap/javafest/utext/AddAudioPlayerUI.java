package com.therap.javafest.utext;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AddAudioPlayerUI extends LinearLayout implements OnClickListener,
		OnCompletionListener, OnSeekBarChangeListener {

	private Context context;

	Uri audioUri = null;
	private boolean playing = false;
	private MediaPlayer mediaPlayer;
	private SeekBar sbProgressBar;
	private LayoutInflater inflater;
	private ImageButton ibPlayStop, ibDelete;

	private Handler handler = new Handler();

	public AddAudioPlayerUI(Context context) {
		super(context);
		this.context = context;

		Init();
	}

	public AddAudioPlayerUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		Init();
	}

	private void Init() {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.add_audio_player, this);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);

		ibPlayStop = (ImageButton) findViewById(R.id.ibPlayStop);
		ibPlayStop.setOnClickListener(this);

		sbProgressBar = (SeekBar) findViewById(R.id.sbProgressBar);
		sbProgressBar.setOnSeekBarChangeListener(this);

		ibDelete = (ImageButton) findViewById(R.id.ibDelete);
		ibDelete.setOnClickListener(this);
	}

	public void setAudioUri(Uri audioUri) {
		this.audioUri = audioUri;
	}

	public Uri getUri(Uri audioUri) {
		return audioUri;
	}

	private void playStop() {
		if (audioUri == null)
			return;
		if (playing == false) {
			mediaPlayer = MediaPlayer.create(context, audioUri);
			try {
				mediaPlayer.start();
				playing = true;
				sbProgressBar.setProgress(0);
				sbProgressBar.setMax(100);
				updateProgressBar();
				ibPlayStop.setImageResource(R.drawable.ic_menu_stop);
			} catch (Exception exp) {
			}
		} else {
			sbProgressBar.setProgress(0);
			mediaPlayer.stop();
			playing = false;
			ibPlayStop.setImageResource(R.drawable.ic_menu_play);
		}
	}

	public void updateProgressBar() {
		handler.postDelayed(mUpdateTimeTask, 1000);
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mediaPlayer.getDuration();
			long currentDuration = mediaPlayer.getCurrentPosition();
			// Updating progress bar
			int progress = (int) (getProgressPercentage(currentDuration,
					totalDuration));
			sbProgressBar.setProgress(progress);
			// Running this thread after 1000 milliseconds
			handler.postDelayed(this, 1000);
		}
	};

	public int getProgressPercentage(long currentDuration, long totalDuration) {
		Double percentage = (double) 0;

		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);

		// calculating percentage
		percentage = (((double) currentSeconds) / totalSeconds) * 100;

		// return percentage
		return percentage.intValue();
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			mediaPlayer.seekTo(progress);
			sbProgressBar.setProgress(progress);
		}

	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onCompletion(MediaPlayer mp) {
		handler.removeCallbacks(mUpdateTimeTask);
		playing = false;
		mp.release();
		sbProgressBar.setProgress(0);
		ibPlayStop.setImageResource(R.drawable.ic_menu_play);
		Log.d("Here", "Audio End");
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ibPlayStop:
			playStop();
			break;
		case R.id.ibDelete:
			((ViewManager) this.getParent()).removeView(this);
			break;
		}
	}

}
