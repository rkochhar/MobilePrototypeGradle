package com.example.remindersapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.example.provider.ReminderContract;
import com.example.utils.FiltersUpdator;



public class AddPreferenceActivity extends PreferenceActivity {

    private Spannable visible;
    private Spannable hidden;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }


    private void setupSimplePreferencesScreen() {

        addPreferencesFromResource(R.xml.pref_general);
        setContentView(R.layout.preference_layout);
        PreferenceCategory customCategory= (PreferenceCategory) findPreference("customPrefer");
        addCheckboxPreferences(customCategory);

    }

    public void reFocusReminders(View view){

        Intent mainActivity=  new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }

    public void setupSummary(){
         visible = new SpannableString( "Reminders visible" );
        visible.setSpan( new ForegroundColorSpan(Color.LTGRAY), 0, visible.length(), 0 );

         hidden = new SpannableString( "Reminders from this group will be hidden." );
        hidden.setSpan( new ForegroundColorSpan(Color.LTGRAY), 0, hidden.length(), 0 );
    }

    public void addCheckboxPreferences(PreferenceCategory test){

        ContentResolver contentResolver = this.getContentResolver();
        Cursor distinctGroups=contentResolver.query(ReminderContract.Entry.CONTENT_URI,new String[]{"distinct "+ReminderContract.Entry.COLUMN_NAME_GROUP},"",null,null);

        String groupName="";

        distinctGroups.moveToFirst();

        while(!distinctGroups.isLast()){

            groupName= distinctGroups.getString(distinctGroups.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_GROUP));
            distinctGroups.moveToNext();
            CheckBoxPreference obj=new CheckBoxPreference(this);
            obj.setOnPreferenceChangeListener(checkboxPreferenceChangeListener);
            obj.setTitle(groupName);
            obj.setKey(groupName);
            obj.setDefaultValue(true);
            test.addPreference(obj);
        }
    }


    private static Preference.OnPreferenceChangeListener checkboxPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();

            Spannable visible = new SpannableString( "Reminders visible" );
            visible.setSpan( new ForegroundColorSpan(Color.LTGRAY), 0, visible.length(), 0 );

            Spannable hidden = new SpannableString( "Reminders from this group will be hidden." );
            hidden.setSpan( new ForegroundColorSpan(Color.LTGRAY), 0, hidden.length(), 0 );

                if(stringValue.equalsIgnoreCase("true")){
                    preference.setSummary(visible);
                    new FiltersUpdator().unblockGroup(preference.getTitle().toString(),preference.getContext());
                }
            else{
                    preference.setSummary(hidden);
                    new FiltersUpdator().updateFilters(preference.getTitle().toString(), preference.getContext());
                }

            return true;
        }

    };

}




