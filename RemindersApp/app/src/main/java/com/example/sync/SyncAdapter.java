package com.example.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.example.dto.Record;
import com.example.dto.ServerDTO;
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
import java.util.List;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver contentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
    }
    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.i("PerformSync", "Performing sync");
        try{
            ServerDTO dto = getReminders(getRemindersFromServer().getEntity().getContent());
            //Log.i("PerformSync", dto.getItems().size()+"");
            //Compare existing items in db with the server and update accordingly
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
            Type recordListType = new TypeToken<List<Record>>(){}.getType();
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
