package com.therap.javafest.utext.backup;

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
import com.therap.javafest.utext.lib.LocationData;
import com.therap.javafest.utext.lib.MultiMediaNote;
import com.therap.javafest.utext.lib.ReminderNote;
import com.therap.javafest.utext.lib.VideoData;
import com.therap.javafest.utext.sqlitedb.AudioDataDB;
import com.therap.javafest.utext.sqlitedb.ChildNoteDB;
import com.therap.javafest.utext.sqlitedb.DBHelper;
import com.therap.javafest.utext.sqlitedb.ImageDataDB;
import com.therap.javafest.utext.sqlitedb.ListNoteDB;
import com.therap.javafest.utext.sqlitedb.LocationDataDB;
import com.therap.javafest.utext.sqlitedb.MultiMediaNoteDB;
import com.therap.javafest.utext.sqlitedb.ReminderNoteDB;
import com.therap.javafest.utext.sqlitedb.VideoDataDB;

public class DBExport {
	private AudioDataDB audioDataDB;
	private ChildNoteDB childNoteDB;
	private ImageDataDB imageDataDB;
	private ListNoteDB listNoteDB;
	private MultiMediaNoteDB multiMediaNoteDB;
	private ReminderNoteDB reminderNoteDB;
	private VideoDataDB videoDataDB;
	private LocationDataDB locationDataDB;

	ArrayList<AudioData> audioData;
	ArrayList<ChildNote> childNote;
	ArrayList<ImageData> imageData;
	ArrayList<ListNote> listNote;
	ArrayList<MultiMediaNote> multiMediaNote;
	ArrayList<ReminderNote> reminderNote;
	ArrayList<VideoData> videoData;
	ArrayList<LocationData> locationData;

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
		locationDataDB = new LocationDataDB(this.context);

		audioData = audioDataDB.selectAll();
		childNote = childNoteDB.selectAll();
		imageData = imageDataDB.selectAll();
		listNote = listNoteDB.selectAll();
		multiMediaNote = multiMediaNoteDB.selectAll();
		reminderNote = reminderNoteDB.selectAll();
		videoData = videoDataDB.selectAll();
		locationData = locationDataDB.selectAll();
	}

	public void ExportAsCSV() {
		String data = "";

		for (AudioData ad : audioData) {
			data += "\"" + String.valueOf(ad.aid) + "\",";
			data += "\"" + String.valueOf(ad.mid) + "\",";
			data += "\"" + ad.created + "\",";
			data += "\"" + ad.modified + "\",";
			data += "\"" + ad.audioUri + "\"\n";
		}

		for (ChildNote cd : childNote) {
			data += "\"" + String.valueOf(cd.cid) + "\",";
			data += "\"" + String.valueOf(cd.lsid) + "\",";
			data += "\"" + cd.modified + "\",";
			data += "\"" + cd.text + "\",";
			data += "\"" + String.valueOf(cd.is_complete) + "\"\n";
		}

		for (ImageData id : imageData) {
			data += "\"" + String.valueOf(id.iid) + "\",";
			data += "\"" + String.valueOf(id.mid) + "\",";
			data += "\"" + id.created + "\",";
			data += "\"" + id.modified + "\",";
			data += "\"" + id.bitmapUri + "\"\n";
		}

		for (ListNote ln : listNote) {
			data += "\"" + String.valueOf(ln.lsid) + "\",";
			data += "\"" + String.valueOf(ln.title) + "\",";
			data += "\"" + ln.created + "\",";
			data += "\"" + ln.modified + "\",";
			data += "\"" + String.valueOf(ln.is_important) + "\"\n";
		}

		for (MultiMediaNote mmn : multiMediaNote) {
			data += "\"" + String.valueOf(mmn.mid) + "\",";
			data += "\"" + mmn.created + "\",";
			data += "\"" + mmn.modified + "\",";
			data += "\"" + mmn.text + "\",";
			data += "\"" + String.valueOf(mmn.is_important) + "\"\n";
		}

		for (ReminderNote rn : reminderNote) {
			data += "\"" + String.valueOf(rn.rid) + "\",";
			data += "\"" + rn.created + "\",";
			data += "\"" + rn.modified + "\",";
			data += "\"" + rn.rdate + "\",";
			data += "\"" + rn.text + "\",";
			data += "\"" + String.valueOf(rn.is_important) + "\"\n";
		}

		for (VideoData vd : videoData) {
			data += "\"" + String.valueOf(vd.vid) + "\",";
			data += "\"" + String.valueOf(vd.mid) + "\",";
			data += "\"" + vd.created + "\",";
			data += "\"" + vd.modified + "\",";
			data += "\"" + vd.videoUri.toString() + "\"\n";
		}

		for (LocationData ld : locationData) {
			data += "\"" + String.valueOf(ld.lid) + "\",";
			data += "\"" + String.valueOf(ld.nid) + "\",";
			data += "\"" + String.valueOf(ld.ntype) + "\",";
			data += "\"" + ld.created + "\",";
			data += "\"" + ld.modified + "\",";
			data += "\"" + String.valueOf(ld.longitude) + "\",";
			data += "\"" + String.valueOf(ld.latitude) + "\",";
			data += "\"" + ld.place + "\"\n";
		}

		File csvFile = new File(exportDir, "export.csv");
		try {
			writer = new BufferedWriter(new FileWriter(csvFile));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ExportAsXML() {
		String data = "<?xml version=\"1.0\"?>\n<database name=\"utextdb.db\" version=\"3\" >\n";

		data += "\t<!-- Table " + DBHelper.DB_TABLE_AUDIO_DATA + " -->\n";
		for (AudioData ad : audioData) {
			data += DBHelper.AUDIO_DATA_COLUMN_AID + " : "
					+ String.valueOf(ad.aid) + "\n";
			data += DBHelper.AUDIO_DATA_COLUMN_MID + " : "
					+ String.valueOf(ad.mid) + "\n";
			data += DBHelper.AUDIO_DATA_COLUMN_CREATED + " : " + ad.created
					+ "\n";
			data += DBHelper.AUDIO_DATA_COLUMN_MODIFIED + " : " + ad.modified
					+ "\n";
			data += "\t\t<column name=\""
					+ DBHelper.AUDIO_DATA_COLUMN_AUDIO_URI + "\" >"
					+ ad.audioUri + "\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + DBHelper.DB_TABLE_CHILD_NOTE + " -->\n";
		for (ChildNote cd : childNote) {
			data += "\t<table name=\"" + DBHelper.DB_TABLE_CHILD_NOTE
					+ "\" >\n";
			data += DBHelper.CHILD_NOTE_COLUMN_CID + " : "
					+ String.valueOf(cd.cid) + "\n";
			data += DBHelper.CHILD_NOTE_COLUMN_LSID + " : "
					+ String.valueOf(cd.lsid) + "\n";
			data += DBHelper.CHILD_NOTE_COLUMN_TEXT + " : " + cd.text + "\n";
			data += "\t\t<column name=\""
					+ DBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE + "\" >"
					+ String.valueOf(cd.is_complete) + "\n";
			data += DBHelper.CHILD_NOTE_COLUMN_MODIFIED + " : " + cd.modified
					+ "\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + DBHelper.DB_TABLE_IMAGE_DATA + " -->\n";
		for (ImageData id : imageData) {
			data += "\t<table name=\"" + DBHelper.DB_TABLE_IMAGE_DATA
					+ "\" >\n";
			data += DBHelper.IMAGE_DATA_COLUMN_IID + " : "
					+ String.valueOf(id.iid) + "\n";
			data += DBHelper.IMAGE_DATA_COLUMN_MID + " : "
					+ String.valueOf(id.mid) + "\n";
			data += DBHelper.IMAGE_DATA_COLUMN_CREATED + " : " + id.created
					+ "\n";
			data += DBHelper.IMAGE_DATA_COLUMN_MODIFIED + " : " + id.modified
					+ "\n";
			data += "\t\t<column name=\""
					+ DBHelper.IMAGE_DATA_COLUMN_IMAGE_URI + "\" >"
					+ id.bitmapUri + "\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + DBHelper.DB_TABLE_LIST_NOTE + " -->\n";
		for (ListNote ln : listNote) {
			data += "\t<table name=\"" + DBHelper.DB_TABLE_LIST_NOTE + "\" >\n";
			data += DBHelper.LIST_NOTE_COLUMN_LSID + " : "
					+ String.valueOf(ln.lsid) + "\n";
			data += DBHelper.LIST_NOTE_COLUMN_TITLE + " : "
					+ String.valueOf(ln.title) + "\n";
			data += DBHelper.LIST_NOTE_COLUMN_CREATED + " : " + ln.created
					+ "\n";
			data += DBHelper.LIST_NOTE_COLUMN_MODIFIED + " : " + ln.modified
					+ "\n";
			data += "\t\t<column name=\""
					+ DBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT + "\" >"
					+ String.valueOf(ln.is_important) + "\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + DBHelper.DB_TABLE_MULTIMEDIA_NOTE + " -->\n";
		for (MultiMediaNote mmn : multiMediaNote) {
			data += "\t<table name=\"" + DBHelper.DB_TABLE_MULTIMEDIA_NOTE
					+ "\" >\n";
			data += DBHelper.MULTIMEDIA_NOTE_COLUMN_MID + " : "
					+ String.valueOf(mmn.mid) + "\n";
			data += "\t\t<column name=\""
					+ DBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED + "\" >"
					+ mmn.created + "\n";
			data += "\t\t<column name=\""
					+ DBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED + "\" >"
					+ mmn.modified + "\n";
			data += "\t\t<column name=\""
					+ DBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT + " : " + mmn.text
					+ "\n";
			data += "\t\t<column name=\""
					+ DBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT + "\" >"
					+ String.valueOf(mmn.is_important) + "\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + DBHelper.DB_TABLE_REMINDER + " -->\n";
		for (ReminderNote rn : reminderNote) {
			data += "\t<table name=\"" + DBHelper.DB_TABLE_REMINDER + "\" >\n";
			data += DBHelper.REMINDER_COLUMN_RID + " : "
					+ String.valueOf(rn.rid) + "\n";
			data += DBHelper.REMINDER_COLUMN_CREATED + " : " + rn.created
					+ "\n";
			data += "\t\t<column name=\""
					+ DBHelper.REMINDER_COLUMN_REMINDER_DATE + "\" >"
					+ rn.rdate + "\n";
			data += DBHelper.REMINDER_COLUMN_MODIFIED + " : " + rn.modified
					+ "\n";
			data += DBHelper.REMINDER_COLUMN_TEXT + " : " + rn.text + "\n";
			data += "\t\t<column name=\""
					+ DBHelper.REMINDER_COLUMN_IS_IMPORTANT + "\" >"
					+ String.valueOf(rn.is_important) + "\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + DBHelper.DB_TABLE_VIDEO_DATA + " -->\n";
		for (VideoData vd : videoData) {
			data += "\t<table name=\"" + DBHelper.DB_TABLE_VIDEO_DATA
					+ "\" >\n";
			data += DBHelper.VIDEO_DATA_COLUMN_VID + " : "
					+ String.valueOf(vd.vid) + "\n";
			data += DBHelper.VIDEO_DATA_COLUMN_MID + " : "
					+ String.valueOf(vd.mid) + "\n";
			data += DBHelper.VIDEO_DATA_COLUMN_CREATED + " : " + vd.created
					+ "\n";
			data += DBHelper.VIDEO_DATA_COLUMN_MODIFIED + " : " + vd.modified
					+ "\n";
			data += "\t\t<column name=\""
					+ DBHelper.VIDEO_DATA_COLUMN_VIDEO_URI + "\" >"
					+ vd.videoUri.toString() + "\n";
			data += "\t</table>\n";
		}

		data += "\t<!-- Table " + DBHelper.DB_TABLE_LOCATION_DATA + " -->\n";
		for (LocationData ld : locationData) {
			data += "\t<table name=\"" + DBHelper.DB_TABLE_LOCATION_DATA
					+ "\" >\n";
			data += DBHelper.LOCATION_DATA_COLUMN_LID + " : "
					+ String.valueOf(ld.lid) + "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_NID + " : "
					+ String.valueOf(ld.nid) + "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_NTYPE + " : "
					+ String.valueOf(ld.ntype) + "\n";
			data += "\t\t<column name=\""
					+ DBHelper.LOCATION_DATA_COLUMN_CREATED + "\" >"
					+ ld.created + "\n";
			data += "\t\t<column name=\""
					+ DBHelper.LOCATION_DATA_COLUMN_MODIFIED + "\" >"
					+ ld.modified + "\n";
			data += "\t\t<column name=\""
					+ DBHelper.LOCATION_DATA_COLUMN_LONGITUDE + "\" >"
					+ String.valueOf(ld.longitude) + "\n";
			data += "\t\t<column name=\""
					+ DBHelper.LOCATION_DATA_COLUMN_LATITUDE + "\" >"
					+ String.valueOf(ld.latitude) + "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_PLACE + " : " + ld.place
					+ "\n";
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

	public void ExportAsTxt() {
		String data = "";

		data += DBHelper.DB_TABLE_AUDIO_DATA + "\n";
		data += "-----------------------------------------\n";
		for (AudioData ad : audioData) {
			data += DBHelper.AUDIO_DATA_COLUMN_AID + " : "
					+ String.valueOf(ad.aid) + "\n";
			data += DBHelper.AUDIO_DATA_COLUMN_MID + " : "
					+ String.valueOf(ad.mid) + "\n";
			data += DBHelper.AUDIO_DATA_COLUMN_CREATED + " : " + ad.created
					+ "\n";
			data += DBHelper.AUDIO_DATA_COLUMN_MODIFIED + " : " + ad.modified
					+ "\n";
			data += DBHelper.AUDIO_DATA_COLUMN_AUDIO_URI + " : " + ad.audioUri
					+ "\n";
			data += "=====================================\n";
		}

		data += DBHelper.DB_TABLE_CHILD_NOTE + "\n";
		data += "-----------------------------------------\n";
		for (ChildNote cd : childNote) {
			data += DBHelper.CHILD_NOTE_COLUMN_CID + " : "
					+ String.valueOf(cd.cid) + "\n";
			data += DBHelper.CHILD_NOTE_COLUMN_LSID + " : "
					+ String.valueOf(cd.lsid) + "\n";
			data += DBHelper.CHILD_NOTE_COLUMN_TEXT + " : " + cd.text + "\n";
			data += DBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE + " : "
					+ String.valueOf(cd.is_complete) + "\n";
			data += DBHelper.CHILD_NOTE_COLUMN_MODIFIED + " : " + cd.modified
					+ "\n";
			data += "=====================================\n";
		}

		data += DBHelper.DB_TABLE_IMAGE_DATA + "\n";
		data += "-----------------------------------\n";
		for (ImageData id : imageData) {
			data += DBHelper.IMAGE_DATA_COLUMN_IID + " : "
					+ String.valueOf(id.iid) + "\n";
			data += DBHelper.IMAGE_DATA_COLUMN_MID + " : "
					+ String.valueOf(id.mid) + "\n";
			data += DBHelper.IMAGE_DATA_COLUMN_CREATED + " : " + id.created
					+ "\n";
			data += DBHelper.IMAGE_DATA_COLUMN_MODIFIED + " : " + id.modified
					+ "\n";
			data += DBHelper.IMAGE_DATA_COLUMN_IMAGE_URI + " : " + id.bitmapUri
					+ "\n";
			data += "=====================================\n";
		}

		data += DBHelper.DB_TABLE_LIST_NOTE + "\n";
		data += "------------------------------\n";
		for (ListNote ln : listNote) {
			data += DBHelper.LIST_NOTE_COLUMN_LSID + " : "
					+ String.valueOf(ln.lsid) + "\n";
			data += DBHelper.LIST_NOTE_COLUMN_TITLE + " : "
					+ String.valueOf(ln.title) + "\n";
			data += DBHelper.LIST_NOTE_COLUMN_CREATED + " : " + ln.created
					+ "\n";
			data += DBHelper.LIST_NOTE_COLUMN_MODIFIED + " : " + ln.modified
					+ "\n";
			data += DBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT + " : "
					+ String.valueOf(ln.is_important) + "\n";
			data += "=====================================\n";
		}

		data += DBHelper.DB_TABLE_MULTIMEDIA_NOTE + "\n";
		data += "-----------------------------------------\n";
		for (MultiMediaNote mmn : multiMediaNote) {
			data += DBHelper.MULTIMEDIA_NOTE_COLUMN_MID + " : "
					+ String.valueOf(mmn.mid) + "\n";
			data += DBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED + " : "
					+ mmn.created + "\n";
			data += DBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED + " : "
					+ mmn.modified + "\n";
			data += DBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT + " : " + mmn.text
					+ "\n";
			data += DBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT + " : "
					+ String.valueOf(mmn.is_important) + "\n";
			data += "=====================================\n";
		}

		data += DBHelper.DB_TABLE_REMINDER + "\n";
		data += "-----------------------------------------\n";
		for (ReminderNote rn : reminderNote) {
			data += DBHelper.REMINDER_COLUMN_RID + " : "
					+ String.valueOf(rn.rid) + "\n";
			data += DBHelper.REMINDER_COLUMN_CREATED + " : " + rn.created
					+ "\n";
			data += DBHelper.REMINDER_COLUMN_REMINDER_DATE + " : " + rn.rdate
					+ "\n";
			data += DBHelper.REMINDER_COLUMN_MODIFIED + " : " + rn.modified
					+ "\n";
			data += DBHelper.REMINDER_COLUMN_TEXT + " : " + rn.text + "\n";
			data += DBHelper.REMINDER_COLUMN_IS_IMPORTANT + " : "
					+ String.valueOf(rn.is_important) + "\n";
			data += "=====================================\n";
		}

		data += DBHelper.DB_TABLE_VIDEO_DATA + "\n";
		data += "-----------------------------------------\n";
		for (VideoData vd : videoData) {
			data += DBHelper.VIDEO_DATA_COLUMN_VID + " : "
					+ String.valueOf(vd.vid) + "\n";
			data += DBHelper.VIDEO_DATA_COLUMN_MID + " : "
					+ String.valueOf(vd.mid) + "\n";
			data += DBHelper.VIDEO_DATA_COLUMN_CREATED + " : " + vd.created
					+ "\n";
			data += DBHelper.VIDEO_DATA_COLUMN_MODIFIED + " : " + vd.modified
					+ "\n";
			data += DBHelper.VIDEO_DATA_COLUMN_VIDEO_URI + " : "
					+ vd.videoUri.toString() + "\n";
			data += "=====================================\n";
		}

		data += DBHelper.DB_TABLE_LOCATION_DATA + "\n";
		data += "-----------------------------------------\n";
		for (LocationData ld : locationData) {
			data += DBHelper.LOCATION_DATA_COLUMN_LID + " : "
					+ String.valueOf(ld.lid) + "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_NID + " : "
					+ String.valueOf(ld.nid) + "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_NTYPE + " : "
					+ String.valueOf(ld.ntype) + "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_CREATED + " : " + ld.created
					+ "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_MODIFIED + " : "
					+ ld.modified + "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_LONGITUDE + " : "
					+ String.valueOf(ld.longitude) + "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_LATITUDE + " : "
					+ String.valueOf(ld.latitude) + "\n";
			data += DBHelper.LOCATION_DATA_COLUMN_PLACE + " : " + ld.place
					+ "\n";
			data += "=====================================\n";
		}

		File txtFile = new File(exportDir, "export.txt");
		try {
			writer = new BufferedWriter(new FileWriter(txtFile));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String ExportAsSQL() {
		String data = "";
		for (AudioData ad : audioData) {
			data += "INSERT INTO " + DBHelper.DB_TABLE_AUDIO_DATA + "("
					+ DBHelper.AUDIO_DATA_COLUMN_AID + ", "
					+ DBHelper.AUDIO_DATA_COLUMN_MID + ", "
					+ DBHelper.AUDIO_DATA_COLUMN_CREATED + ", "
					+ DBHelper.AUDIO_DATA_COLUMN_MODIFIED + ", "
					+ DBHelper.AUDIO_DATA_COLUMN_AUDIO_URI + ")VALUES(\""
					+ String.valueOf(ad.aid) + "\", \""
					+ String.valueOf(ad.mid) + "\", \"" + ad.created + "\", \""
					+ ad.modified + "\", \"" + ad.audioUri + "\")\n";
		}

		for (ChildNote cd : childNote) {
			data += "INSERT INTO " + DBHelper.DB_TABLE_CHILD_NOTE + "("
					+ DBHelper.CHILD_NOTE_COLUMN_CID + ", "
					+ DBHelper.CHILD_NOTE_COLUMN_LSID + ", "
					+ DBHelper.CHILD_NOTE_COLUMN_MODIFIED + ", "
					+ DBHelper.CHILD_NOTE_COLUMN_TEXT + ", "
					+ DBHelper.CHILD_NOTE_COLUMN_IS_COMPLETE + ")VALUES("
					+ String.valueOf(cd.cid) + "\", \""
					+ String.valueOf(cd.lsid) + "\", \"" + cd.modified
					+ "\", \"" + cd.text + "\", \""
					+ String.valueOf(cd.is_complete) + "\")\n";
		}

		for (ImageData id : imageData) {
			data += "INSERT INTO " + DBHelper.DB_TABLE_IMAGE_DATA + "("
					+ DBHelper.IMAGE_DATA_COLUMN_IID + ", "
					+ DBHelper.IMAGE_DATA_COLUMN_MID + ", "
					+ DBHelper.IMAGE_DATA_COLUMN_CREATED + ", "
					+ DBHelper.IMAGE_DATA_COLUMN_MODIFIED + ", "
					+ DBHelper.IMAGE_DATA_COLUMN_IMAGE_URI + ")VALUES(\""
					+ String.valueOf(id.iid) + "\", \""
					+ String.valueOf(id.mid) + "\", \"" + id.created + "\", \""
					+ id.modified + "\", \"" + id.bitmapUri + "\")\n";
		}

		for (ListNote ln : listNote) {
			data += "INSERT INTO " + DBHelper.DB_TABLE_LIST_NOTE + "("
					+ DBHelper.LIST_NOTE_COLUMN_LSID + ", "
					+ DBHelper.LIST_NOTE_COLUMN_TITLE + ", "
					+ DBHelper.LIST_NOTE_COLUMN_CREATED + ", "
					+ DBHelper.LIST_NOTE_COLUMN_MODIFIED + ", "
					+ DBHelper.LIST_NOTE_COLUMN_IS_IMPORTANT + ")VALUES(\""
					+ String.valueOf(ln.lsid) + "\", \""
					+ String.valueOf(ln.title) + "\", \"" + ln.created
					+ "\", \"" + ln.modified + "\", \""
					+ String.valueOf(ln.is_important) + "\")\n";
		}

		for (MultiMediaNote mmn : multiMediaNote) {
			data += "INSERT INTO " + DBHelper.DB_TABLE_MULTIMEDIA_NOTE + "("
					+ DBHelper.MULTIMEDIA_NOTE_COLUMN_MID + ", "
					+ DBHelper.MULTIMEDIA_NOTE_COLUMN_CREATED + ", "
					+ DBHelper.MULTIMEDIA_NOTE_COLUMN_MODIFIED + ", "
					+ DBHelper.MULTIMEDIA_NOTE_COLUMN_TEXT + ", "
					+ DBHelper.MULTIMEDIA_NOTE_COLUMN_IS_IMPORTANT
					+ ")VALUES(\"" + String.valueOf(mmn.mid) + "\", \""
					+ mmn.created + "\", \"" + mmn.modified + "\", \""
					+ mmn.text + "\", \"" + String.valueOf(mmn.is_important)
					+ "\")\n";
		}

		for (ReminderNote rn : reminderNote) {
			data += "INSERT INTO " + DBHelper.DB_TABLE_REMINDER + "("
					+ DBHelper.REMINDER_COLUMN_RID + ", "
					+ DBHelper.REMINDER_COLUMN_CREATED + ", "
					+ DBHelper.REMINDER_COLUMN_MODIFIED + ", "
					+ DBHelper.REMINDER_COLUMN_REMINDER_DATE + ", "
					+ DBHelper.REMINDER_COLUMN_TEXT + ", "
					+ DBHelper.REMINDER_COLUMN_IS_IMPORTANT + ")VALUES(\""
					+ String.valueOf(rn.rid) + "\", \"" + rn.created + "\", \""
					+ rn.modified + "\", \"" + rn.rdate + "\", \"" + rn.text
					+ "\", " + String.valueOf(rn.is_important) + "\")\n";
		}

		for (VideoData vd : videoData) {
			data += "INSERT INTO " + DBHelper.DB_TABLE_VIDEO_DATA + "("
					+ DBHelper.VIDEO_DATA_COLUMN_VID + ", "
					+ DBHelper.VIDEO_DATA_COLUMN_MID + ", "
					+ DBHelper.VIDEO_DATA_COLUMN_CREATED + ", "
					+ DBHelper.VIDEO_DATA_COLUMN_MODIFIED + ", "
					+ DBHelper.VIDEO_DATA_COLUMN_VIDEO_URI + ")VALUES(\""
					+ String.valueOf(vd.vid) + "\", \""
					+ String.valueOf(vd.mid) + "\", \"" + vd.created + "\", \""
					+ vd.modified + "\", \"" + vd.videoUri.toString() + "\")\n";
		}

		for (LocationData ld : locationData) {
			data += "INSERT INTO " + DBHelper.DB_TABLE_LOCATION_DATA + "("
					+ DBHelper.LOCATION_DATA_COLUMN_LID + ", "
					+ DBHelper.LOCATION_DATA_COLUMN_NID + ", "
					+ DBHelper.LOCATION_DATA_COLUMN_NTYPE + ", "
					+ DBHelper.LOCATION_DATA_COLUMN_CREATED + ", "
					+ DBHelper.LOCATION_DATA_COLUMN_MODIFIED + ", "
					+ DBHelper.LOCATION_DATA_COLUMN_LONGITUDE + ", "
					+ DBHelper.LOCATION_DATA_COLUMN_LATITUDE + ", "
					+ DBHelper.LOCATION_DATA_COLUMN_PLACE + ")VALUES(\""
					+ String.valueOf(ld.lid) + "\", \""
					+ String.valueOf(ld.nid) + "\", \""
					+ String.valueOf(ld.ntype) + "\", \"" + ld.created
					+ "\", \"" + ld.modified + "\", \""
					+ String.valueOf(ld.longitude) + "\", \""
					+ String.valueOf(ld.latitude) + "\", \"" + ld.place
					+ "\")\n";
		}
		return data;
	}
}
