package com.example.remindersapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AddReminderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_reminder);
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
		EditText type= (EditText) findViewById(R.id.type);
		
		JSONObject newReminder= new JSONObject();
		
		try {
			newReminder.put("name", desc.getText().toString());
			newReminder.put("date", date.toString());
			if(type.getText().toString().equalsIgnoreCase("birthday")){
				newReminder.put("type", "1");
			}
			else
			{
				newReminder.put("type", "2");
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//writeToRemindersFile(newReminder.toString());
		
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return remindersList;
		
	}
	
	public void writeToRemindersFile(String newReminder){
		String filename = "reminders.json";
		
		JSONArray existingReminders = readFromRemindersFile();
		
		String string="";
		FileOutputStream outputStream;

		try {
		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
		  outputStream.write(string.getBytes());
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
}
