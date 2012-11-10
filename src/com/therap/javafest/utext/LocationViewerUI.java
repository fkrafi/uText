package com.therap.javafest.utext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LocationViewerUI extends LinearLayout implements OnClickListener {

	private Context context;
	private ImageButton ibDelete;
	private TextView tvMediaType;
	private LayoutInflater inflater;
	private TextView tvLocation, tvLocationX, tvLocationY;

	public LocationViewerUI(Context context) {
		super(context);
		this.context = context;
		Init();
	}

	public LocationViewerUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		Init();
	}

	private void Init() {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.location_viewer_ui, this);

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocation = (TextView) findViewById(R.id.tvLocationX);
		tvLocation = (TextView) findViewById(R.id.tvLocationY);

		ibDelete = (ImageButton) findViewById(R.id.ibDelete);
		ibDelete.setOnClickListener(this);

		tvMediaType = (TextView) findViewById(R.id.tvMediaType);
		tvMediaType.setOnClickListener(this);
	}

	public void setLocation(String location) {
		tvLocation.setText(location);
	}

	public String getLocation() {
		return tvLocation.getText().toString();
	}

	public void setLocationX(double locationX) {
		tvLocationX.setText(String.valueOf(locationX));
	}

	public double getLocationX() {
		return Double.parseDouble(tvLocationX.getText().toString());
	}

	public void setLocationY(double locationY) {
		tvLocationY.setText(String.valueOf(locationY));
	}

	public double getLocationY() {
		return Double.parseDouble(tvLocationX.getText().toString());
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
		((ViewManager) this.getParent()).removeView(this);
	}
}
