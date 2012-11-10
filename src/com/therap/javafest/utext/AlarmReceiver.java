package com.therap.javafest.utext;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

public class AlarmReceiver extends BroadcastReceiver {

	private final String REMINDER_BUNDLE = "ReminderBundle";
	private NotificationManager mNotificationManager;
	private int SIMPLE_NOTFICATION_ID;

	public AlarmReceiver() {
	}

	public AlarmReceiver(Context context, Bundle extras, int timeoutInSeconds,
			int requsetCode) {
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra(REMINDER_BUNDLE, extras);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				requsetCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		time.add(Calendar.SECOND, timeoutInSeconds);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
				pendingIntent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle alarmBundle = intent.getBundleExtra(REMINDER_BUNDLE);
		String title = alarmBundle.getString("TITLE");
		try {
			Uri uri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			Ringtone mRingtone = RingtoneManager.getRingtone(context, uri);
			mRingtone.play();

			mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notifyDetails = new Notification(
					R.drawable.ic_listview_reminder, "uText Reminder",
					System.currentTimeMillis());
			notifyDetails.setLatestEventInfo(context, "uText Reminder", title,
					null);
			notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notifyDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
