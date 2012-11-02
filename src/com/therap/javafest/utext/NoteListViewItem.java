package com.therap.javafest.utext;

public class NoteListViewItem {

	private String note;
	private String datetime;
	private boolean list;
	private boolean reminder;
	private boolean gallery;
	private boolean audio;
	private boolean location;

	public NoteListViewItem() {
		note = datetime = "";
		list = reminder = audio = location = false;
	}

	public NoteListViewItem(String note, String datetime, boolean list,
			boolean reminder, boolean gallery, boolean audio, boolean location) {
		this.note = note;
		this.datetime = datetime;
		this.list = list;
		this.reminder = reminder;
		this.gallery = gallery;
		this.audio = audio;
		this.location = location;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setList(boolean list) {
		this.list = list;
	}

	public boolean getList() {
		return list;
	}

	public void setReminder(boolean reminder) {
		this.reminder = reminder;
	}

	public boolean getReminder() {
		return reminder;
	}

	public void setGallery(boolean gallery) {
		this.gallery = gallery;
	}

	public boolean getGallery() {
		return gallery;
	}

	public void setAudio(boolean audio) {
		this.audio = audio;
	}

	public boolean getAudio() {
		return audio;
	}

	public void setLocation(boolean location) {
		this.location = location;
	}

	public boolean getLocation() {
		return location;
	}
}
