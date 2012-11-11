package com.therap.javafest.utext.lib;

import java.util.ArrayList;

import android.content.Context;

import com.therap.javafest.utext.sqlitedb.AudioDataDB;
import com.therap.javafest.utext.sqlitedb.ImageDataDB;
import com.therap.javafest.utext.sqlitedb.ListNoteDB;
import com.therap.javafest.utext.sqlitedb.LocationDataDB;
import com.therap.javafest.utext.sqlitedb.MultiMediaNoteDB;
import com.therap.javafest.utext.sqlitedb.ReminderNoteDB;
import com.therap.javafest.utext.sqlitedb.VideoDataDB;

public class NoteRetriever {
	private ArrayList<Note> notes;
	private MultiMediaNoteDB multiMediaNoteDB;
	private ImageDataDB imageDataDB;
	private AudioDataDB audioDataDB;
	private VideoDataDB videoDataDB;
	private ListNoteDB listNoteDB;
	private LocationDataDB locationDataDB;
	private ReminderNoteDB reminderNoteDB;

	public NoteRetriever(Context context) {
		notes = new ArrayList<Note>();

		multiMediaNoteDB = new MultiMediaNoteDB(context);
		imageDataDB = new ImageDataDB(context);
		audioDataDB = new AudioDataDB(context);
		videoDataDB = new VideoDataDB(context);
		listNoteDB = new ListNoteDB(context);
		locationDataDB = new LocationDataDB(context);
		reminderNoteDB = new ReminderNoteDB(context);

		notes.addAll(getAllMultiMediaNote());
		notes.addAll(getAllListNote());
		notes.addAll(getAllReminder());
	}

	public ArrayList<Note> getAll() {
		return notes;
	}

	public ArrayList<Note> getAllMultiMediaNote() {
		ArrayList<Note> mNotes = multiMediaNoteDB.selectForList();
		for (Note mn : mNotes) {
			mn.setHasImage(imageDataDB.hasImage(Integer.parseInt(mn.getId())));
			mn.setHasAudio(audioDataDB.hasAudio(Integer.parseInt(mn.getId())));
			mn.setHasVideo(videoDataDB.hasVideo(Integer.parseInt(mn.getId())));
			mn.setHasLocation(locationDataDB.hasLocation(
					Integer.parseInt(mn.getId()), Note.MULTIMEDIA_NOTE));
		}
		return mNotes;
	}

	public ArrayList<Note> getAllListNote() {
		ArrayList<Note> lNotes = listNoteDB.selectForList();
		for (Note ln : lNotes) {
			ln.setHasLocation(locationDataDB.hasLocation(
					Integer.parseInt(ln.getId()), Note.LIST_NOTE));
		}
		return lNotes;
	}

	public ArrayList<Note> getAllReminder() {
		ArrayList<Note> rNotes = reminderNoteDB.selectForList();
		for (Note rn : rNotes) {
			rn.setHasLocation(locationDataDB.hasLocation(
					Integer.parseInt(rn.getId()), Note.REMINDER));
		}
		return rNotes;
	}
}
