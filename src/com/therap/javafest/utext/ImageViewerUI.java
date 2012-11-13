package com.therap.javafest.utext;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.therap.javafest.utext.lib.ImageProcessing;

public class ImageViewerUI extends LinearLayout implements OnClickListener {

	private Uri bitmapUri;
	private Bitmap bitmap;

	private ImageProcessing ip;
	private Context context;
	private ImageButton ibDelete;
	private ImageView ivImage;
	private TextView tvMediaType;
	private LayoutInflater inflater;

	public ImageViewerUI(Context context) {
		super(context);
		this.context = context;
		Init();
		renderView();
	}

	private void Init() {
		ip = new ImageProcessing(context);
	}

	private void renderView() {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.image_viewer_ui, this);

		ibDelete = (ImageButton) findViewById(R.id.ibDelete);
		ibDelete.setOnClickListener(this);

		ivImage = (ImageView) findViewById(R.id.ivImage);
		ivImage.setOnClickListener(this);

		tvMediaType = (TextView) findViewById(R.id.tvMediaType);
		tvMediaType.setText("1");
	}

	public void setImage(Uri bitmapUri, ContentResolver cr) {
		this.bitmapUri = bitmapUri;
		bitmap = ip.uriToBitmap(bitmapUri, cr);
		ivImage.setImageBitmap(ip.resize(bitmap, 250));
		ivImage.setScaleType(ScaleType.FIT_XY);
	}

	public void setBitmapUri(Uri bitmapUri) {
		this.bitmapUri = bitmapUri;
	}

	public Uri getBitmapUri() {
		return bitmapUri;
	}

	public Bitmap getImage() {
		return bitmap;
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
		case R.id.ivImage:
			Intent intent = new Intent(Intent.ACTION_VIEW, bitmapUri);
			intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
			intent.setDataAndType(bitmapUri, "image/jpeg");
			context.startActivity(intent);
			break;
		case R.id.ibDelete:
			((ViewManager) this.getParent()).removeView(this);
			break;
		}

	}

}
