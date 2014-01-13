package com.example.fragments;

import com.example.dto.Record;
import com.example.remindersapp.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReminderViewFragment extends Fragment
{
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
		TextView detailsName = (TextView) view.findViewById(R.id.detailsName);
        detailsName.setText(record.getName());

        TextView detailsType = (TextView) view.findViewById(R.id.detailsType);
        detailsType.setText(record.getType());

        TextView detailsDate = (TextView) view.findViewById(R.id.detailsDate);
        detailsDate.setText(record.getDate().toString());
		
	}

}
