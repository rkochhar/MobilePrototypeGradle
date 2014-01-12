package com.example.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.content.Context;
import android.util.Log;

import com.example.dto.Record;
import com.example.dto.RecordTypeEnum;

public class CommonUtils {

    private static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static DateFormat DATEFORMAT_LONG = DateFormat.getDateInstance();

    public static String formatDate(Date date)
    {
        return DATEFORMAT_LONG.format(date);
    }

    public static List<Record> getReminders(InputStream inputStream,Context appContext) throws ParseException
    {
        String remindersString="[]";
        FileInputStream fis;
        try {
            fis = appContext.openFileInput("reminders.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            remindersString=sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


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
