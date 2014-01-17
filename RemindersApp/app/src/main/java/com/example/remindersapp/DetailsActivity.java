package com.example.remindersapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.dto.Record;
import com.example.fragments.ReminderViewFragment;

public class DetailsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        Record record = (Record) getIntent().getSerializableExtra("selected");
        setContentView(R.layout.details_layout);
        ReminderViewFragment reminderViewFragment = (ReminderViewFragment) getFragmentManager().findFragmentById(R.id.fragmentDetails);
        reminderViewFragment.updateView(record, reminderViewFragment.getView());
    }
}
