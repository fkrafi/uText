package com.therap.javafest.backup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;

import com.therap.javafest.utext.lib.AudioData;
import com.therap.javafest.utext.lib.ChildNote;
import com.therap.javafest.utext.lib.ImageData;
import com.therap.javafest.utext.lib.ListNote;
import com.therap.javafest.utext.lib.MultiMediaNote;
import com.therap.javafest.utext.lib.ReminderNote;
import com.therap.javafest.utext.lib.VideoData;
import com.therap.javafest.utext.sqlitedb.AudioDataDB;
import com.therap.javafest.utext.sqlitedb.ChildNoteDB;
import com.therap.javafest.utext.sqlitedb.ImageDataDB;
import com.therap.javafest.utext.sqlitedb.ListNoteDB;
import com.therap.javafest.utext.sqlitedb.MultiMediaNoteDB;
import com.therap.javafest.utext.sqlitedb.ReminderNoteDB;
import com.therap.javafest.utext.sqlitedb.UTextDBHelper;
import com.therap.javafest.utext.sqlitedb.VideoDataDB;

public class DBExport {
	private AudioDataDB audioDataDB;
	private ChildNoteDB childNoteDB;
	private ImageDataDB imageDataDB;
	private ListNoteDB listNoteDB;
	private MultiMediaNoteDB multiMediaNoteDB;
	private ReminderNoteDB reminderNoteDB;
	private VideoDataDB videoDataDB;

	ArrayList<AudioData> audioData;
	ArrayList<ChildNote> childNote;
	ArrayList<ImageData> imageData;
	ArrayList<ListNote> listNote;
	ArrayList<MultiMediaNote> multiMediaNote;
	ArrayList<ReminderNote> reminderNote;
	ArrayList<VideoData> videoData;

	private Context context;

	private Writer writer;
	File root = Environment.getExternalStorageDirectory();
	File exportDir = new File(root.getAbsolutePath() + File.separator + "uText"
			+ File.separator + "export");

	public DBExport(Context context) {
		this.context = context;
		audioDataDB = new AudioDataDB(this.context);
		childNoteDB = new ChildNoteDB(this.context);
		imageDataDB = new ImageDataDB(this.context);
		listNoteDB = new ListNoteDB(this.context);
		multiMediaNoteDB = new MultiMediaNoteDB(this.context);
		reminderNoteDB = new ReminderNoteDB(this.context);
		videoDataDB = new VideoDataDB(this.context);

		audioData = audioDataDB.selectAll();
		childNote = childNoteDB.selectAll();
		imageData = imageDataDB.selectAll();
		listNote = listNoteDB.selectAll();
		multiMediaNote = multiMediaNoteDB.selectAll();
		reminderNote = reminderNoteDB.selectAll();
		videoData = videoDataDB.selectAll();
	}

	public void ExportAsCSV() {
		String data = "";

		for (AudioData ad : audioData) {
			data += "\"" + String.valueOf(ad.aid) + "\",";
			data += "\"" + String.valueOf(ad.mid) + "\",";
			data += "\"" + ad.created + "\",";
			data += "\"" + ad.modified + "\",";
			data += "\"" + ad.audioUri + "\",";
			data += "\"" + String.valueOf(ad.is_active) + "\",";
			data += "\"" + String.valueOf(ad.is_cloud) + "\"\n";
		}

		for (ChildNote cd : childNote) {
			data += "\"" + String.valueOf(cd.cid) + "\",";
			data += "\"" + String.valueOf(cd.lsid) + "\",";
			data += "\"" + cd.text + "\",";
			data += "\"" + String.valueOf(cd.is_complete) + "\",";
			data += "\"" + cd.modified + "\",";
			data += "\"" + String.valueOf(cd.is_active) + "\",";
			data += "\"" + String.valueOf(cd.is_cloud) + "\"\n";
		}

		for (ImageData id : imageData) {
			data += "\"" + String.valueOf(id.iid) + "\",";
			data += "\"" + String.valueOf(id.mid) + "\",";
			data += "\"" + id.created + "\",";
			data += "\"" + id.modified + "\",";
			data += "\"" + id.bitmapUri + "\",";
			data += "\"" + String.valueOf(id.is_active) + "\",";
			data += "\"" + String.valueOf(id.is_cloud) + "\"\n";
		}

		for (ListNote ln : listNote) {
			data += "\"" + String.valueOf(ln.lsid) + "\",";
			data += "\"" + String.valueOf(ln.title) + "\",";
			data += "\"" + ln.created + "\",";
			data += "\"" + ln.modified + "\",";
			data += "\"" + String.valueOf(ln.is_important) + "\",";
			data += "\"" + String.valueOf(ln.is_active) + "\",";
			data += "\"" + String.valueOf(ln.is_cloud) + "\"\n";
		}

		for (MultiMediaNote mmn : multiMediaNote) {
			data += "\"" + String.valueOf(mmn.mid) + "\",";
			data += "\"" + mmn.created + "\",";
			data += "\"" + mmn.modified + "\",";
			data += "\"" + mmn.text + "\",";
			data += "\"" + String.valueOf(mmn.is_important) + "\",";
			data += "\"" + String.valueOf(mmn.is_active) + "\",";
			data += "\"" + String.valueOf(mmn.is_cloud) + "\"\n";
		}

		for (ReminderNote rn : reminderNote) {
			data += "\"" + String.valueOf(rn.rid) + "\",";
			data += "\"" + rn.created + "\",";
			data += "\"" + rn.rdate + "\",";
			data += "\"" + rn.rtime + "\",";
			data += "\"" + rn.modified + "\",";
			data += "\"" + rn.text + "\",";
			data += "\"" + String.valueOf(rn.is_important) + "\",";
			data += "\"" + String.valueOf(rn.is_active) + "\",";
			data += "\"" + String.valueOf(rn.is_cloud) + "\"\n";
		}

		for (VideoData vd : videoData) {
			data += "\"" + String.valueOf(vd.vid) + "\",";
			data += "\"" + String.valueOf(vd.mid) + "\",";
			data += "\"" + vd.created + "\",";
			data += "\"" + vd.modified + "\",";
			data += "\"" + vd.videoUri.toString() + "\",";
			data += "\"" + String.valueOf(vd.is_active) + "\",";
			data += "\"" + String.valueOf(vd.is_cloud) + "\"\n";
		}

		File csvFile = new File(exportDir, "export.csv");
		try {
			writer = new BufferedWriter(new FileWriter(csvFile));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ExportAsXML() {
		String data = "<?xml version=\"1.0\"?>\n<database name=\"utextdb.db\" version=\"3\" >\n";

		data += "\t<!-- Table " + UTextDBHelper.DB_TABLE_AUDIO_DATA + " -->";
		for (AudioData ad : audioData) {
			data += "\t<table name=\"" + UTextDBHelper.DB_TABLE_AUDIO_DATA
					+ "\" >\n";

			data += "\t\t<column name=\"" + UTextDBHelper.AUDIO_DATA_COLUMN_AID
					+ "\" >" + String.valueOf(ad.aid) + "</column>\n";
			data += "\t\t<column name=\"" + UTextDBHelper.AUDIO_DATA_COLUMN_MID
					+ "\" >" + String.valueOf(ad.mid) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.AUDIO_DATA_COLUMN_CREATED + "\" >"
					+ ad.created + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.AUDIO_DATA_COLUMN_MODIFIED + "\" >"
					+ ad.modified + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.AUDIO_DATA_COLUMN_DATA + "\" >"
					+ ad.audioUri + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.AUDIO_DATA_COLUMN_IS_ACTIVE + "\" >"
					+ String.valueOf(ad.is_active) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.AUDIO_DATA_COLUMN_IS_CLOUD + "\" >"
					+ String.valueOf(ad.is_cloud) + "</column>\n";

			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + UTextDBHelper.DB_TABLE_CHILD_NOTE + " -->";
		for (ChildNote cd : childNote) {
			data += "\t<table name=\"" + UTextDBHelper.DB_TABLE_CHILD_NOTE
					+ "\" >\n";
			data += "\t\t<column name=\"" + UTextDBHelper.CHILD_NOTE_COLUMN_CID
					+ "\" >" + String.valueOf(cd.cid) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.CHILD_NOTE_COLUMN_LSID + "\" >"
					+ String.valueOf(cd.lsid) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.CHILD_NOTE_COLUMN_TEXT + "\" >" + cd.text
					+ "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE + "\" >"
					+ String.valueOf(cd.is_complete) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.CHILD_NOTE_COLUMN_MODIFIED + "\" >"
					+ cd.modified + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.CHILD_NOTE_COLUMN_IS_ACTIVE + "\" >"
					+ String.valueOf(cd.is_active) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.CHILD_NOTE_COLUMN_IS_CLOUD + "\" >"
					+ String.valueOf(cd.is_cloud) + "</column>\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + UTextDBHelper.DB_TABLE_IMAGE_DATA + " -->";
		for (ImageData id : imageData) {
			data += "\t<table name=\"" + UTextDBHelper.DB_TABLE_IMAGE_DATA
					+ "\" >\n";
			data += "\t\t<column name=\"" + UTextDBHelper.IMAGE_DATA_COLUMN_IID
					+ "\" >" + String.valueOf(id.iid) + "</column>\n";
			data += "\t\t<column name=\"" + UTextDBHelper.IMAGE_DATA_COLUMN_MID
					+ "\" >" + String.valueOf(id.mid) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.IMAGE_DATA_COLUMN_CREATED + "\" >"
					+ id.created + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.IMAGE_DATA_COLUMN_MODIFIED + "\" >"
					+ id.modified + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.IMAGE_DATA_COLUMN_DATA + "\" >"
					+ id.bitmapUri + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.IMAGE_DATA_COLUMN_IS_ACTIVE + "\" >"
					+ String.valueOf(id.is_active) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.IMAGE_DATA_COLUMN_IS_CLOUD + "\" >"
					+ String.valueOf(id.is_cloud) + "</column>\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + UTextDBHelper.DB_TABLE_LIST_NOTE + " -->";
		for (ListNote ln : listNote) {
			data += "\t<table name=\"" + UTextDBHelper.DB_TABLE_LIST_NOTE
					+ "\" >\n";
			data += "\t\t<column name=\"" + UTextDBHelper.LIST_NOTE_COLUMN_LSID
					+ "\" >" + String.valueOf(ln.lsid) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.LIST_NOTE_COLUMN_TITLE + "\" >"
					+ String.valueOf(ln.title) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.LIST_NOTE_COLUMN_CREATED + "\" >"
					+ ln.created + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.LIST_NOTE_COLUMN_MODIFIED + "\" >"
					+ ln.modified + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT + "\" >"
					+ String.valueOf(ln.is_important) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.LIST_NOTE_COLUMN_IS_ACTIVE + "\" >"
					+ String.valueOf(ln.is_active) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.LIST_NOTE_COLUMN_IS_CLOUD + "\" >"
					+ String.valueOf(ln.is_cloud) + "</column>\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + UTextDBHelper.DB_TABLE_MULTIMEDIA_NOTE
				+ " -->";
		for (MultiMediaNote mmn : multiMediaNote) {
			data += "\t<table name=\"" + UTextDBHelper.DB_TABLE_MULTIMEDIA_NOTE
					+ "\" >\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MID + "\" >"
					+ String.valueOf(mmn.mid) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED + "\" >"
					+ mmn.created + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED + "\" >"
					+ mmn.modified + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT + "\" >"
					+ mmn.text + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT
					+ "\" >" + String.valueOf(mmn.is_important) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_ACTIVE + "\" >"
					+ String.valueOf(mmn.is_active) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.MULTIMEDIA_NOTE_COLUMN_IS_CLOUD + "\" >"
					+ String.valueOf(mmn.is_cloud) + "</column>\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + UTextDBHelper.DB_TABLE_REMINDER + " -->";
		for (ReminderNote rn : reminderNote) {
			data += "\t<table name=\"" + UTextDBHelper.DB_TABLE_REMINDER
					+ "\" >\n";
			data += "\t\t<column name=\"" + UTextDBHelper.REMINDER_COLUMN_RID
					+ "\" >" + String.valueOf(rn.rid) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.REMINDER_COLUMN_CREATED + "\" >"
					+ rn.created + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.REMINDER_COLUMN_REMINDER_DATE + "\" >"
					+ rn.rdate + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.REMINDER_COLUMN_REMINDER_TIME + "\" >"
					+ rn.rtime + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.REMINDER_COLUMN_MODIFIED + "\" >"
					+ rn.modified + "</column>\n";
			data += "\t\t<column name=\"" + UTextDBHelper.REMINDER_COLUMN_TEXT
					+ "\" >" + rn.text + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.REMINDER_COLUMN_IS_IMPORTANT + "\" >"
					+ String.valueOf(rn.is_important) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.REMINDER_COLUMN_IS_ACTIVE + "\" >"
					+ String.valueOf(rn.is_active) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.REMINDER_COLUMN_IS_CLOUD + "\" >"
					+ String.valueOf(rn.is_cloud) + "</column>\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + UTextDBHelper.DB_TABLE_VIDEO_DATA + " -->";
		for (VideoData vd : videoData) {
			data += "\t<table name=\"" + UTextDBHelper.DB_TABLE_VIDEO_DATA
					+ "\" >\n";
			data += "\t\t<column name=\"" + UTextDBHelper.VIDEO_DATA_COLUMN_VID
					+ "\" >" + String.valueOf(vd.vid) + "</column>\n";
			data += "\t\t<column name=\"" + UTextDBHelper.VIDEO_DATA_COLUMN_MID
					+ "\" >" + String.valueOf(vd.mid) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.VIDEO_DATA_COLUMN_CREATED + "\" >"
					+ vd.created + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.VIDEO_DATA_COLUMN_MODIFIED + "\" >"
					+ vd.modified + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.VIDEO_DATA_COLUMN_DATA + "\" >"
					+ vd.videoUri.toString() + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.VIDEO_DATA_COLUMN_IS_ACTIVE + "\" >"
					+ String.valueOf(vd.is_active) + "</column>\n";
			data += "\t\t<column name=\""
					+ UTextDBHelper.VIDEO_DATA_COLUMN_IS_CLOUD + "\" >"
					+ String.valueOf(vd.is_cloud) + "</column>\n";
			data += "\t</table>\n";
		}
		data += "</database>";

		File xmlFile = new File(exportDir, "export.xml");
		try {
			writer = new BufferedWriter(new FileWriter(xmlFile));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
