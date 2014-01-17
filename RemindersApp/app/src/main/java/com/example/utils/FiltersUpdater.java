package com.example.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FiltersUpdater extends AsyncTask {


    public void reinitializeFilters(JSONArray filteredGroupList,Context appContext){
        String filename = "filter.json";

        JSONObject updatedData= new JSONObject();

        try {
            updatedData.put("blockedGroups",filteredGroupList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FileOutputStream outputStream;
        try {
            outputStream = appContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write( updatedData.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray readFromFiltersFile(Context appContext){

        JSONArray remindersList = new JSONArray();

        FileInputStream fis;
        StringBuilder sb = new StringBuilder();
        try {
            fis = appContext.openFileInput("filter.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject filterData = new JSONObject(sb.toString());

            if(filterData!=null)
                remindersList = (JSONArray) filterData.get("blockedGroups");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return remindersList;

    }

    @Override
    protected Object doInBackground(Object[] objects) {

        Context context = (Context) objects[0];
        JSONArray filteredGroups = (JSONArray) objects[1];

        reinitializeFilters(filteredGroups,context);

        return null;
    }

}

