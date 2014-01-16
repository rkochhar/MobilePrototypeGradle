package com.example.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FiltersUpdator {


    public void unblockGroup(String groupToBeUnblocked, Context appContext){
        String filename = "filter.json";

        JSONArray existingFilteredGroups = readFromFiltersFile(appContext);
        JSONArray newFilteredGroups= new JSONArray();

        JSONObject updatedData= new JSONObject();

        int i=0;
        while(i<existingFilteredGroups.length()){
            try {
                if(((String)existingFilteredGroups.get(i)).equalsIgnoreCase(groupToBeUnblocked)){
                    break;
                }
                newFilteredGroups.put(existingFilteredGroups.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
        }

        try {
            updatedData.put("blockedGroups",newFilteredGroups);
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

    public void updateFilters(String groupToBeFiltered,Context appContext){
        String filename = "filter.json";

        JSONArray existingFilteredGroups = readFromFiltersFile(appContext);

        JSONObject updatedData= new JSONObject();

        int i=0;
        while(i<existingFilteredGroups.length()){
            try {
                if(((String)existingFilteredGroups.get(i)).equalsIgnoreCase(groupToBeFiltered)){
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
        }

        existingFilteredGroups.put(groupToBeFiltered);

        try {
            updatedData.put("blockedGroups",existingFilteredGroups);
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

        Log.e("Read","Current Value-"+sb.toString());

        return remindersList;

    }

}

