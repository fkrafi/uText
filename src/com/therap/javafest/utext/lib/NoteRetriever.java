package com.therap.javafest.utext.lib;

import java.util.ArrayList;

import android.content.Context;

import com.therap.javafest.utext.sqlitedb.AudioDataDB;
import com.therap.javafest.utext.sqlitedb.ImageDataDB;
import com.therap.javafest.utext.sqlitedb.ListNoteDB;
import com.therap.javafest.utext.sqlitedb.MultiMediaNoteDB;
import com.therap.javafest.utext.sqlitedb.ReminderNoteDB;
import com.therap.javafest.utext.sqlitedb.VideoDataDB;

public class NoteRetriever {
	private ArrayList<NoteListItem> notes;
	private MultiMediaNoteDB multiMediaNoteDB;
	private ImageDataDB imageDataDB;
	private AudioDataDB audioDataDB;
	private VideoDataDB videoDataDB;
	private ListNoteDB listNoteDB;
	private ReminderNoteDB reminderNoteDB;

	public NoteRetriever(Context context) {
		notes = new ArrayList<NoteListItem>();

		multiMediaNoteDB = new MultiMediaNoteDB(context);
		imageDataDB = new ImageDataDB(context);
		audioDataDB = new AudioDataDB(context);
		videoDataDB = new VideoDataDB(context);
		listNoteDB = new ListNoteDB(context);
		reminderNoteDB = new ReminderNoteDB(context);

		notes.addAll(getAllMultiMediaNote());
		notes.addAll(getAllListNote());
		notes.addAll(getAllReminder());
	}

	public ArrayList<NoteListItem> getAll() {
		return notes;
	}

	public ArrayList<NoteListItem> getAllMultiMediaNote() {
		ArrayList<NoteListItem> mNotes = multiMediaNoteDB.selectForList();
		for (NoteListItem mn : mNotes) {
			mn.setHasImage(imageDataDB.hasImage(Integer.parseInt(mn.getId())));
			mn.setHasAudio(audioDataDB.hasAudio(Integer.parseInt(mn.getId())));
			mn.setHasVideo(videoDataDB.hasVideo(Integer.parseInt(mn.getId())));
			mn.setHasLocation(true);
		}
		return mNotes;
	}

	public ArrayList<NoteListItem> getAllListNote() {
		ArrayList<NoteListItem> lNotes = listNoteDB.selectForList();
		for (NoteListItem ln : lNotes) {
			ln.setHasLocation(true);
		}
		return lNotes;
	}

	public ArrayList<NoteListItem> getAllReminder() {
		ArrayList<NoteListItem> rNotes = reminderNoteDB.selectForList();
		for (NoteListItem rn : rNotes) {
			rn.setHasLocation(true);
		}
		return rNotes;
	}
}
