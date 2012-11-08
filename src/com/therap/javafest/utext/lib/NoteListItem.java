package com.therap.javafest.utext.lib;

public class NoteListItem {
	private String id = null;
	private int type = 1;
	private String text = null;
	private String datetime = null;
	private boolean hasImage = false;
	private boolean hasAudio = false;
	private boolean hasVideo = false;
	private boolean hasLocation = false;

	public static final int MULTIMEDIA_NOTE = 1;
	public static final int LIST_NOTE = 2;
	public static final int REMINDER = 3;

	public NoteListItem() {
	}

	public NoteListItem(String id, int type, String text, String datetime,
			boolean hasImage, boolean hasAudio, boolean hasVideo,
			boolean hasLocation) {
		this.id = id;
		this.type = type;
		this.text = text;
		this.datetime = datetime;
		this.hasImage = hasImage;
		this.hasAudio = hasAudio;
		this.hasVideo = hasVideo;
		this.hasLocation = hasLocation;

	}

	public NoteListItem(String id, int type, String text, String datetime) {
		this.id = id;
		this.type = type;
		this.text = text;
		this.datetime = datetime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public void setDateTime(String datetime) {
		this.datetime = datetime;
	}

	public String getDateTime() {
		return datetime;
	}

	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}

	public boolean getHasImage() {
		return hasImage;
	}

	public void setHasAudio(boolean hasAudio) {
		this.hasAudio = hasAudio;
	}

	public boolean getHasAudio() {
		return hasAudio;
	}

	public void setHasVideo(boolean hasVideo) {
		this.hasVideo = hasVideo;
	}

	public boolean getHasVideo() {
		return hasVideo;
	}

	public void setHasLocation(boolean hasLocation) {
		this.hasLocation = hasLocation;
	}

	public boolean getHasLocation() {
		return hasLocation;
	}
}
