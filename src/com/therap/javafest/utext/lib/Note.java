package com.therap.javafest.utext.lib;

public class Note {
	private String id = "";
	private int type = 1;
	private String text = "";
	private String datetime = "";
	private boolean hasImage = false;
	private boolean hasAudio = false;
	private boolean hasVideo = false;
	private boolean hasLocation = false;
	private int count = 0;
	private int important = 0;

	private String rDateTime = "";

	public static final int MULTIMEDIA_NOTE = 1;
	public static final int LIST_NOTE = 2;
	public static final int REMINDER = 3;

	public Note() {
	}

	public Note(String id, int type, String text, String datetime,
			int important, boolean hasImage, boolean hasAudio,
			boolean hasVideo, boolean hasLocation, int count) {
		this.id = id;
		this.type = type;
		this.text = text;
		this.datetime = datetime;
		this.hasImage = hasImage;
		this.hasAudio = hasAudio;
		this.hasVideo = hasVideo;
		this.hasLocation = hasLocation;
		this.count = count;
		this.important = important;
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

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setImportant(int important) {
		this.important = important;
	}

	public int getImportant() {
		return important;
	}

	public void setRDateTime(String rDateTime) {
		this.rDateTime = rDateTime;
	}

	public String getRDateTime() {
		return rDateTime;
	}

}
