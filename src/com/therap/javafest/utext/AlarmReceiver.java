package com.therap.javafest.utext;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	private NotificationManager mNotificationManager;
	private int SIMPLE_NOTFICATION_ID;

	@Override
	public void onReceive(Context context, Intent intent) {
		String text = intent.getStringExtra("text");
		int rid = Integer.valueOf(intent.getStringExtra("rid"));
		try {
			mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notifyDetails = new Notification(
					R.drawable.ic_notification_alarm, "uText Reminder",
					System.currentTimeMillis());
			Intent i = new Intent(context, ViewReminderActivity.class);
			i.putExtra("rid", String.valueOf(rid));
			PendingIntent myIntent = PendingIntent.getActivity(context, rid, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			notifyDetails.setLatestEventInfo(context, "uText Reminder", text,
					myIntent);
			notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notifyDetails);

			isMessage(context, text);
			isRingerModeChange(context, text);

			Uri uri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			Ringtone mRingtone = RingtoneManager.getRingtone(context, uri);
			mRingtone.play();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void isMessage(Context context, String text) {
		try {
			String temp[] = text.split("\\(");
			if (temp.length >= 4 && temp[0].equals("sms")) {
				String phoneNumber = "";
				String t1[] = temp[2].split("\\)");
				if (t1.length > 0) {
					phoneNumber = t1[0].trim();
				}
				String message = "";
				if (t1.length > 1 && t1[1].equals("msg")) {
					message = temp[3].replaceAll("\\)", "");
				}
				Log.d("sms", "set");
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(phoneNumber, null, message, null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void isRingerModeChange(Context context, String text) {
		try {
			AudioManager am = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (text.toLowerCase().indexOf("ringer(normal)") >= 0) {
				am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			} else if (text.toLowerCase().indexOf("ringer(silent)") >= 0) {
				am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			} else if (text.toLowerCase().indexOf("ringer(vibrate)") >= 0) {
				am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
