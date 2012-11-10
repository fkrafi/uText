package com.therap.javafest.utext;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class AddMapActivity extends MapActivity {
	public static final String RESULT_ADDRESS = "address";
	private Button addMap;
	private Button backBtn;
	private EditText mapEdit;
	private MapView map;
	private Address address;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		try {
			createViews();
		} catch (Exception exp) {
			Toast.makeText(this, exp.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void createViews() {
		addMap = (Button) findViewById(R.id.map_add_btn);
		backBtn = (Button) findViewById(R.id.back_btn);
		backBtn.setEnabled(false);
		mapEdit = (EditText) findViewById(R.id.map_edit);
		map = (MapView) findViewById(R.id.map);
		map.setBuiltInZoomControls(true);
		addMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveCurrentAddress();
			}
		});
		backBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (address != null) {
					Intent i = new Intent();
					i.putExtra(RESULT_ADDRESS, address);
					setResult(RESULT_OK, i);
				}
				finish();
			}
		});
	}

	private void saveCurrentAddress() {
		String addressString = mapEdit.getText().toString();
		Geocoder g = new Geocoder(this);
		List<Address> addresses;
		try {
			addresses = g.getFromLocationName(addressString, 1);
			if (addresses.size() > 0) {
				address = addresses.get(0);
				AddressOverlay overlay = new AddressOverlay(address);
				map.getOverlays().add(overlay);
				map.invalidate();
				final MapController mapController = map.getController();
				mapController.animateTo(overlay.getGeoPoint(), new Runnable() {
					public void run() {
						mapController.setZoom(12);
					}
				});
				backBtn.setEnabled(true);
			} else {
				Toast.makeText(this, "Unable to load map", Toast.LENGTH_LONG)
						.show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected boolean isLocationDisplayed() {
		return true;
	}
}
