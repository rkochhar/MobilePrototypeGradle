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
import com.example.utils.FiltersUpdater;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;


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
        setupSummary();
        addCheckboxPreferences(customCategory);

    }

    public void reFocusReminders(View view){

        PreferenceCategory customCategory= (PreferenceCategory) findPreference("customPrefer");

        final int count = customCategory.getPreferenceCount();
        int i=0;
        JSONArray updatedFilteredGroup= new JSONArray();
        while (i<count){

            CheckBoxPreference checkboxPreference = (CheckBoxPreference)customCategory.getPreference(i);
            if(!checkboxPreference.isChecked())
                   updatedFilteredGroup.put(checkboxPreference.getTitle());
            i++;
        }

        FiltersUpdater updaterObj= new FiltersUpdater();
        updaterObj.execute(getApplicationContext(), updatedFilteredGroup);

        Intent mainActivityIntent=  new Intent(this,MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivityIntent);
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

        HashSet<String> filteredGroupsSet = new HashSet<String>();
        FiltersUpdater obj1 = new FiltersUpdater();
        JSONArray filterList = obj1.readFromFiltersFile(this.getApplicationContext());

        int in=0;
        while(in <filterList.length()){

            try {
                filteredGroupsSet.add(filterList.getString(in));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            in++;
        }


        while(!distinctGroups.isLast()){

            groupName= distinctGroups.getString(distinctGroups.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_GROUP));
            distinctGroups.moveToNext();
            CheckBoxPreference obj=new CheckBoxPreference(this);
            obj.setOnPreferenceChangeListener(checkboxPreferenceChangeListener);
            obj.setTitle(groupName);
            obj.setKey(groupName);
            if(!filteredGroupsSet.contains(groupName)){
                obj.setDefaultValue(true);
                obj.setSummary(visible);
            }
            else{
                obj.setDefaultValue(false);
                obj.setSummary(hidden);
            }
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

                if(stringValue.equalsIgnoreCase("true"))
                   preference.setSummary(visible);
               else
                   preference.setSummary(hidden);

            return true;
        }

    };

}




