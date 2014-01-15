package com.example.receiver;

import com.example.dto.Record;
import com.example.dto.RecordTypeEnum;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.example.provider.ReminderContract;
import com.example.remindersapp.MainActivity;
import com.example.remindersapp.R;

public class ReminderReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

        int recordId = intent.getExtras().getInt("selectedReminder");

		Cursor c = context.getContentResolver().query(ReminderContract.Entry.CONTENT_URI.buildUpon().appendPath(""+recordId).build(), null, "", null, null);
        c.moveToNext();
        String reminderMsg = c.getString(c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_NAME));
        String reminderType = RecordTypeEnum.getEnumValue(c.getInt(c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_TYPE)));
		Toast.makeText(context, ("Happy " +  reminderType), Toast.LENGTH_LONG).show();
		
		Intent notificationIntent = new Intent(context, MainActivity.class);
		TaskStackBuilder stack = TaskStackBuilder.create(context);
		stack.addParentStack(MainActivity.class);
		stack.addNextIntent(notificationIntent);
		
		PendingIntent pendingIntent = stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder n = new NotificationCompat.Builder(context)
        .setContentTitle("New reminder for " + reminderMsg)
        .setSmallIcon(reminderType.equals(RecordTypeEnum.ANNIVERSARY.name()) ? R.drawable.anniversary : R.drawable.birthday)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true);
    
  
		NotificationManager notificationManager = 
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(recordId, n.build());
	}

}
