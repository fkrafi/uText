package com.therap.javafest.utext;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class AddressOverlay extends Overlay {

	private Address address;
	private GeoPoint geoPoint;

	public AddressOverlay(Address address) {
		super();
		setAddress(address);
		Double convertedLongitude = address.getLongitude() * 1E6;
		Double convertedLatitude = address.getLatitude() * 1E6;
		setGeopoint(new GeoPoint(convertedLatitude.intValue(),
				convertedLongitude.intValue()));
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Point locationPoint = new Point();
		Projection projection = mapView.getProjection();
		projection.toPixels(geoPoint, locationPoint);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		canvas.drawCircle(locationPoint.x, locationPoint.y, 4, paint);
	}

	private void setGeopoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	private void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public GeoPoint getGeoPoint() {
		return this.geoPoint;
	}

}
