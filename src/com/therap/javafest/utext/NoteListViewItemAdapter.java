package com.therap.javafest.utext;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.therap.javafest.utext.lib.Note;

public class NoteListViewItemAdapter extends BaseAdapter {
	private ArrayList<Note> data;
	private static LayoutInflater inflater = null;

	public NoteListViewItemAdapter(Context context, ArrayList<Note> data) {
		this.data = data;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = inflater.inflate(R.layout.notes_listview_item_row, null);
		}
		Note item = data.get(position);

		if (item != null) {
			TextView tvId = (TextView) v.findViewById(R.id.tvId);
			TextView tvType = (TextView) v.findViewById(R.id.tvType);
			TextView tvText = (TextView) v.findViewById(R.id.tvText);
			TextView tvDateTime = (TextView) v.findViewById(R.id.tvDateTime);
			ImageView ivList = (ImageView) v.findViewById(R.id.ivList);
			ImageView ivReminder = (ImageView) v.findViewById(R.id.ivReminder);
			ImageView ivGallery = (ImageView) v.findViewById(R.id.ivGallery);
			ImageView ivAudio = (ImageView) v.findViewById(R.id.ivAudio);
			ImageView ivVideo = (ImageView) v.findViewById(R.id.ivVideo);
			ImageView ivLocation = (ImageView) v.findViewById(R.id.ivLocation);

			tvId.setText(item.getId());
			tvType.setText(String.valueOf(item.getType()));
			tvText.setText(item.getText());

			Timestamp ts = Timestamp.valueOf(item.getDateTime());
			Date date = new Date(ts.getTime());
			DateFormat dateFormat = new SimpleDateFormat("E, dd M yyyy hh:mm a");
			tvDateTime.setText(dateFormat.format(date).toString());

			ivList.setVisibility(View.INVISIBLE);
			ivReminder.setVisibility(View.INVISIBLE);
			ivGallery.setVisibility(View.INVISIBLE);
			ivAudio.setVisibility(View.INVISIBLE);
			ivVideo.setVisibility(View.INVISIBLE);
			ivLocation.setVisibility(View.INVISIBLE);

			if (item.getType() == Note.LIST_NOTE) {
				ivList.setVisibility(View.VISIBLE);
			} else if (item.getType() == Note.REMINDER) {
				ivReminder.setVisibility(View.VISIBLE);
			}

			if (item.getHasImage() == true) {
				ivGallery.setVisibility(View.VISIBLE);
			}
			if (item.getHasAudio() == true) {
				ivAudio.setVisibility(View.VISIBLE);
			}
			if (item.getHasVideo() == true) {
				ivVideo.setVisibility(View.VISIBLE);
			}
			if (item.getHasLocation() == true) {
				ivLocation.setVisibility(View.VISIBLE);
			}
		}
		return v;
	}
}
