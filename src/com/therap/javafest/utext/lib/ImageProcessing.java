package com.therap.javafest.utext.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

public class ImageProcessing {
	Context context;

	public ImageProcessing(Context context) {
		this.context = context;
	}

	public Bitmap resize(Bitmap bitmap, int iwidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		double density = context.getResources().getDisplayMetrics().density;
		double bounding = 250.0 * density;
		float xScale = (float) (bounding / width);
		float yScale = (float) (bounding / height);
		float scale = (xScale <= yScale) ? xScale : yScale;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	public Uri saveBitmap(Bitmap bitmap) {
		String bitmapTempPath = Environment.getExternalStorageDirectory()
				+ File.separator + "uText" + File.separator + "images"
				+ File.separator + String.valueOf(System.currentTimeMillis())
				+ ".jpg";
		try {
			FileOutputStream fos = new FileOutputStream(bitmapTempPath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(new File(bitmapTempPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Bitmap uriToBitmap(Uri bitmapUri, ContentResolver cr) {
		InputStream imageStream;
		try {
			imageStream = cr.openInputStream(bitmapUri);
			return BitmapFactory.decodeStream(imageStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
