package com.example.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dto.Record;
import com.example.dto.RecordTypeEnum;

public class CommonUtils {

	private static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	private static DateFormat DATEFORMAT_LONG = DateFormat.getDateInstance();
	
	public static String formatDate(Date date)
	{
		return DATEFORMAT_LONG.format(date);
	}
	
	public static List<Record> getReminders(InputStream inputStream) throws ParseException
	{
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int read;
		try 
		{
			read = inputStream.read();
			while (read != -1)
			{
				byteArrayOutputStream.write(read);
				read = inputStream.read();
			}
			inputStream.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		String remindersString = byteArrayOutputStream.toString();
		
		List<Record> records = new ArrayList<Record>();
		try 
		{
			JSONArray reminders = new JSONArray(remindersString);
			for(int i=0; i < reminders.length(); i++)
			{
				JSONObject reminder = reminders.getJSONObject(i);
				records.add(new Record(reminder.getString("name"), DATEFORMAT.parse(reminder.getString("date")), RecordTypeEnum.getEnumValue(reminder.getInt("type"))));
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return records;
	}
	
	public static List<Record> sortRemindersByDate(List<Record> reminders)
	{
		Collections.sort(reminders, new RecordDateComparator());
		return reminders;
	}
}
