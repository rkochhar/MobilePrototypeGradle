package com.example.remindersapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddReminderActivity extends Activity {

    int year,day,month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        setCurrentDateOnView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_reminder, menu);
        return true;
    }


    public void addNewReminder(View view){

        Intent mainActivityIntent = new Intent(this, MainActivity.class);

        EditText desc= (EditText) findViewById(R.id.desc);
        EditText date= (EditText) findViewById(R.id.date);
        Spinner remindertype= (Spinner) findViewById(R.id.typeSpinner);


        if( date.getText().toString().isEmpty()){
            Toast errMsg=Toast.makeText(this, "Please fill in the date.", Toast.LENGTH_LONG);
            errMsg.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            errMsg.show();
            return;
        }
        if(desc.getText().toString().isEmpty()){
            Toast errMsg=Toast.makeText(this, "Please add reminder description.", Toast.LENGTH_LONG);
            errMsg.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            errMsg.show();

            return;
        }


        JSONObject newReminder= new JSONObject();

        try {
            newReminder.put("name", desc.getText().toString());
            newReminder.put("date", date.getText().toString());


            if(remindertype.getSelectedItem().toString().equalsIgnoreCase("birthday")){
                newReminder.put("type", "1");
            }
            else
            {
                newReminder.put("type", "2");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        writeToRemindersFile(newReminder.toString());

        Toast.makeText(this, "New Reminder added successfully!",Toast.LENGTH_LONG).show();

        startActivity(mainActivityIntent);
    }


    public JSONArray readFromRemindersFile(){

        JSONArray remindersList = new JSONArray();

        FileInputStream fis;
        try {
            fis = openFileInput("reminders.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            Log.v("info",sb.toString());

            remindersList = new JSONArray(sb.toString());


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return remindersList;

    }

    public void writeToRemindersFile(String newReminder){
        String filename = "reminders.json";

        JSONArray existingReminders = readFromRemindersFile();
        JSONObject obj=null;
        try {
            obj = new JSONObject(newReminder);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        existingReminders.put(obj);

        String string="";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write( existingReminders.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setCurrentDateOnView() {

        DatePicker dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into datepicker
        dpResult.init(year, month, day, null);
    }


    public void selectNewDate(View view){
        showDialog(1);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // set date picker as current date
        return new DatePickerDialog(this, datePickerListener,
                year, month,day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            EditText dateText = (EditText) findViewById(R.id.date);
            DatePicker datePickerView = (DatePicker) findViewById(R.id.dpResult);

            // set selected date into textview
            dateText.setText(new StringBuilder().append(selectedYear)
                    .append("-").append(selectedMonth+1).append("-").append(selectedDay));

            datePickerView.init(selectedYear, selectedMonth,selectedDay,null);

        }
    };



}
