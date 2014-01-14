package com.example.receiver;

import com.example.dto.Record;
import com.example.dto.RecordTypeEnum;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.example.remindersapp.MainActivity;
import com.example.remindersapp.R;

public class ReminderReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		android.os.Debug.waitForDebugger();
		Record record = (Record) intent.getExtras().get("selectedReminder");
		
		Toast.makeText(context, "Happy " + record.getType(), Toast.LENGTH_LONG).show();
		
		Intent notificationIntent = new Intent(context, MainActivity.class);
		TaskStackBuilder stack = TaskStackBuilder.create(context);
		stack.addParentStack(MainActivity.class);
		stack.addNextIntent(notificationIntent);
		
		PendingIntent pendingIntent = stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder n = new NotificationCompat.Builder(context)
        .setContentTitle("New reminder for " + record.getType())
        .setSmallIcon(record.getType().equals(RecordTypeEnum.ANNIVERSARY.name()) ? R.drawable.anniversary : R.drawable.birthday)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true);
    
  
		NotificationManager notificationManager = 
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(intent.getExtras().getInt("selectedIndex"), n.build()); 
	}

}
