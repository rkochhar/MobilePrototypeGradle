package com.example.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.dto.Record;
import com.example.dto.ServerDTO;
import com.example.provider.ReminderContract;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver contentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
    }
    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        try{
            ServerDTO dto = getReminders(getRemindersFromServer().getEntity().getContent());
            //Compare existing items in db with the server and update accordingly
            Cursor remindersCursor = contentResolver.query(ReminderContract.Entry.CONTENT_URI, null, "", null, null);

            Set<String> availableReminders = new HashSet<String>();
            while(remindersCursor.moveToNext())
            {
                availableReminders.add(remindersCursor.getString(remindersCursor.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_ENTRY_ID)));
            }
            List<ContentValues> contentValues = new ArrayList<ContentValues>();

            for (ServerDTO.Items item : dto.getItems()) {
                if( availableReminders.add(item.getKey().getId()) )
                {
                    ContentValues values = new ContentValues();
                    values.put(ReminderContract.Entry.COLUMN_NAME_ENTRY_ID, item.getKey().getId());
                    values.put(ReminderContract.Entry.COLUMN_NAME_NAME, item.getReminderMsg());
                    values.put(ReminderContract.Entry.COLUMN_NAME_DATE, item.getDate());
                    values.put(ReminderContract.Entry.COLUMN_NAME_TYPE, item.getType());
                    values.put(ReminderContract.Entry.COLUMN_NAME_SET, false);
                    values.put(ReminderContract.Entry.COLUMN_NAME_IS_LOCAL, false);
                    values.put(ReminderContract.Entry.COLUMN_NAME_GROUP, item.getGroup());
                    contentValues.add(values);
                }
            }
            if(contentValues.size() != 0)
            {
                contentResolver.bulkInsert(ReminderContract.Entry.CONTENT_URI, contentValues.toArray(new ContentValues[contentValues.size()]));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static ServerDTO getReminders(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            String reminders =  writer.toString();
            Log.i("Output", reminders);
            Gson gson = new GsonBuilder().create();
            //Type recordListType = new TypeToken<List<Record>>(){}.getType();
            return gson.fromJson(reminders, ServerDTO.class);
        } else {
            return null;
        }
    }

    private HttpResponse getRemindersFromServer()
    {
        HttpResponse response = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("https://reminder-list.appspot.com/_ah/api/reminderendpoint/v1/reminder"));
            response = client.execute(request);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
