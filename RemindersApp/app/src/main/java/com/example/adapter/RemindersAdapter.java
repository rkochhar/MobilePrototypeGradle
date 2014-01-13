package com.example.adapter;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dto.Record;
import com.example.dto.RecordTypeEnum;
import com.example.receiver.ReminderReceiver;
import com.example.remindersapp.R;
import com.example.utils.CommonUtils;

public class RemindersAdapter extends BaseAdapter{

	private List<Record> reminders;

    @Override
	public int getCount() {
		return reminders.size();
	}

	@Override
	public Object getItem(int index) {
		return reminders.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int index, View view, ViewGroup parent) {
		if(view == null)
		{
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.reminder_list_item, parent, false);
		}

        final Record reminderRecord = reminders.get(index);
		
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(reminderRecord.getName());
		
		TextView date = (TextView) view.findViewById(R.id.date);
		date.setText(CommonUtils.formatDate(reminderRecord.getDate()));

		ImageView image = (ImageView) view.findViewById(R.id.typeImage);
		
		if(reminderRecord.getType().equals(RecordTypeEnum.BIRTHDAY.name()))
		{
			image.setImageResource(R.drawable.birthday);
		}
		else
		{
			image.setImageResource(R.drawable.anniversary);
		}

        final ImageView alarmImage = (ImageView) view.findViewById(R.id.alarmImage);

        alarmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!reminderRecord.isAlarmSet())
                {
                    Intent intent = new Intent(view.getContext(), ReminderReceiver.class);
                    intent.putExtra("selectedReminder", reminderRecord);
                    //intent.putExtra("selectedIndex", selectedIndex);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, intent , PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);

                    alarmManager.set(AlarmManager.RTC, reminderRecord.getDate().getTime() , pendingIntent);
                    reminderRecord.setAlarmSet(true);
                    Toast.makeText(view.getContext(), "Alarm is set", Toast.LENGTH_LONG).show();
                    alarmImage.setImageResource(R.drawable.clock_disable);
                }
            }
        });
		
		return view;
	}

	public RemindersAdapter(List<Record> reminders)
	{
		this.reminders = reminders;
	}

	public void setReminders(List<Record> reminders) {
		this.reminders = reminders;
	}

	public List<Record> getReminders() {
		return reminders;
	}
	
}
