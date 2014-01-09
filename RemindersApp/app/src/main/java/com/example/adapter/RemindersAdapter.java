package com.example.adapter;

import java.util.List;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dto.Record;
import com.example.dto.RecordTypeEnum;
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
		
		Record reminderRecord = reminders.get(index);
		
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(reminderRecord.getName());
		
		TextView date = (TextView) view.findViewById(R.id.date);
		date.setText(CommonUtils.formatDate(reminderRecord.getDate()));
		
		TextView type = (TextView) view.findViewById(R.id.type);
		type.setText(reminderRecord.getType());
		type.setTypeface(Typeface.MONOSPACE);
		
		ImageView image = (ImageView) view.findViewById(R.id.typeImage);
		
		Drawable birthdayDrawable = parent.getResources().getDrawable(R.drawable.birthday);
		Drawable anniversaryDrawable = parent.getResources().getDrawable(R.drawable.anniversary);
		
		if(reminderRecord.getType().equals(RecordTypeEnum.BIRTHDAY.name()))
		{
			image.setImageDrawable(birthdayDrawable);
		}
		else
		{
			image.setImageDrawable(anniversaryDrawable);
		}
		
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
