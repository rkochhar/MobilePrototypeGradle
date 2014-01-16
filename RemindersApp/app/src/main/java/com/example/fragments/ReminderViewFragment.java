package com.example.fragments;

import com.example.dto.Record;
import com.example.dto.RecordTypeEnum;
import com.example.remindersapp.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderViewFragment extends Fragment
{
    private static DateFormat DATEFORMAT = new SimpleDateFormat("MMMM dd", Locale.US);

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.reminder_view_fragment, container, false);
		Bundle bundle = this.getArguments();
		Record record = bundle != null ? (Record) bundle.getSerializable("selected") : null;
		if(record != null)
		{
			updateView(record, fragmentView);
		}
		return fragmentView;
	}
	
	public void updateView(Record record, View view)
	{
        TextView remainingText = (TextView) view.findViewById(R.id.remainingTime);

        Calendar event = Calendar.getInstance();
        event.setTime(record.getDate());

        Calendar current = Calendar.getInstance();
        current.getTime();

        boolean past = (event.getTimeInMillis() - current.getTimeInMillis()) < 0;
        if(past)
        {
            event.add(Calendar.YEAR, 1);
        }

        long remaining = event.getTimeInMillis() - current.getTimeInMillis();
        int remainingDays = (int) (remaining / (24 * 60 * 60 * 1000));
        remainingText.setText("The event occurs in " + remainingDays + " days");

		TextView detailsName = (TextView) view.findViewById(R.id.detailsName);
        detailsName.setText(record.getName());

        int type = Integer.parseInt(record.getType());
        TextView detailsType = (TextView) view.findViewById(R.id.detailsType);
        detailsType.setText(RecordTypeEnum.getEnumValue(type));

        TextView detailsDate = (TextView) view.findViewById(R.id.detailsDate);
        detailsDate.setText(DATEFORMAT.format(record.getDate()));

        if(type == RecordTypeEnum.BIRTHDAY.getType())
        {
            ((ImageView)view.findViewById(R.id.detailsImage)).setImageResource(R.drawable.birthday);
        }
        else
        {
            ((ImageView)view.findViewById(R.id.detailsImage)).setImageResource(R.drawable.anniversary);
        }
		
	}

}
