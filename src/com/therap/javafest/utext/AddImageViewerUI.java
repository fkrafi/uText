package com.therap.javafest.utext;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddImageViewerUI extends LinearLayout implements OnClickListener {

	Bitmap bitmap;

	private Context context;
	private ImageButton ibDelete;
	private ImageView ivImage;
	private TextView tvMediaType;
	private LayoutInflater inflater;

	public AddImageViewerUI(Context context) {
		super(context);
		this.context = context;
		Init();
	}

	public AddImageViewerUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		Init();
	}

	private void Init() {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.add_image_viewer_ui, this);

		ibDelete = (ImageButton) findViewById(R.id.ibDelete);
		ibDelete.setOnClickListener(this);

		ivImage = (ImageView) findViewById(R.id.ivImage);
		ivImage.setOnClickListener(this);

		tvMediaType = (TextView) findViewById(R.id.tvMediaType);
		tvMediaType.setText("1");
	}

	private Bitmap resize(Bitmap bitmap, int iwidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		double ratio = width / height;
		int iheight = (int) (iwidth / ratio);
		return Bitmap.createBitmap(iwidth, iheight, bitmap.getConfig());
	}

	public void setImage(Bitmap bitmap) {
		this.bitmap = bitmap;
		ivImage.setImageBitmap(resize(bitmap, 250));
		// ivImage.setImageBitmap(bitmap);
		ivImage.setScaleType(ScaleType.FIT_XY);
	}

	public Bitmap getImage() {
		return bitmap;
	}

	public void setMediaType(String type) {
		tvMediaType.setText(type);
	}

	public String getMediaType() {
		return tvMediaType.getText().toString();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ivImage:
			break;
		case R.id.ibDelete:
			((ViewManager) this.getParent()).removeView(this);
			break;
		}

	}

}
