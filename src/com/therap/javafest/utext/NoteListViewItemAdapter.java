package com.therap.javafest.utext;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteListViewItemAdapter extends ArrayAdapter<NoteListViewItem> {
	private ArrayList<NoteListViewItem> items;

	public NoteListViewItemAdapter(Context context, int textViewResourceId,
			ArrayList<NoteListViewItem> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.notes_listview_item_row, null);
		}

		NoteListViewItem item = items.get(position);

		if (item != null) {
			TextView tvNote = (TextView) v.findViewById(R.id.tvNote);
			TextView tvDateTime = (TextView) v.findViewById(R.id.tvDateTime);
			ImageView ivList = (ImageView) v.findViewById(R.id.ivList);
			ImageView ivReminder = (ImageView) v.findViewById(R.id.ivReminder);
			ImageView ivGallery = (ImageView) v.findViewById(R.id.ivGallery);
			ImageView ivAudio = (ImageView) v.findViewById(R.id.ivAudio);
			ImageView ivLocation = (ImageView) v.findViewById(R.id.ivLocation);

			tvNote.setText(item.getNote());
			tvDateTime.setText(item.getDatetime());

			if (item.getList() == false) {
				ivList.setVisibility(View.INVISIBLE);
			}
			if (item.getReminder() == false) {
				ivReminder.setVisibility(View.INVISIBLE);
			}
			if (item.getGallery() == false) {
				ivGallery.setVisibility(View.INVISIBLE);
			}
			if (item.getAudio() == false) {
				ivAudio.setVisibility(View.INVISIBLE);
			}
			if (item.getLocation() == false) {
				ivLocation.setVisibility(View.INVISIBLE);
			}
		}
		return super.getView(position, convertView, parent);
	}
}
