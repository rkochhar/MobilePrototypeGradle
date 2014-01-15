package com.example.fragments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.accounts.Account;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.authenticator.AuthenticatorService;
import com.example.dto.Record;
import com.example.dto.RecordTypeEnum;
import com.example.provider.ReminderContract;
import com.example.remindersapp.AddReminderActivity;
import com.example.remindersapp.R;

public class RemindersListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	OnReminderSelectedListener callback;

    SimpleCursorAdapter simpleCursorAdapter;

    private static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	
	public RemindersListFragment()
	{
		setHasOptionsMenu(true);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View fragmentView = inflater.inflate(R.layout.reminder_fragment, container, false);
        ListView list = (ListView) fragmentView.findViewById(R.id.listView1);

        simpleCursorAdapter = new SimpleCursorAdapter(this.getActivity(), R.layout.reminder_list_item, null, new String[] {ReminderContract.Entry.COLUMN_NAME_NAME, ReminderContract.Entry.COLUMN_NAME_DATE, ReminderContract.Entry.COLUMN_NAME_TYPE, ReminderContract.Entry.COLUMN_NAME_SET, ReminderContract.Entry.COLUMN_NAME_GROUP}, new int[] {R.id.name, R.id.date, R.id.typeImage, R.id.alarmImage, R.id.groupName}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            public boolean setViewValue(View view, Cursor cursor, int columnIndex){
                if(view.getId() == R.id.typeImage){
                    final ImageView typeImage = (ImageView) view;
                    if(cursor.getInt(columnIndex) == RecordTypeEnum.BIRTHDAY.getType())
                    {
                        typeImage.setImageResource(R.drawable.birthday);
                    }
                    else
                    {
                        typeImage.setImageResource(R.drawable.anniversary);
                    }

                    return true;
                }
                else if(view.getId() == R.id.alarmImage) {
                    boolean isAlarmSet = cursor.getInt(columnIndex) > 0;
                    if(isAlarmSet)
                    {
                        ((ImageView)view).setImageResource(R.drawable.clock_disable);
                    }
                    else
                    {
                        ((ImageView)view).setImageResource(R.drawable.clock);
                    }
                    return true;
                }
                return false;
            }
        });


        list.setAdapter(simpleCursorAdapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {

                Cursor c = (Cursor) simpleCursorAdapter.getItem(position);
                int nameIndex = c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_NAME);
                int dateIndex = c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_DATE);
                int typeIndex = c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_TYPE);
                int isAlarmSetIndex = c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_SET);
                Record record = null;
                try {
                    record = new Record(c.getString(nameIndex), DATEFORMAT.parse(c.getString(dateIndex)), c.getString(typeIndex));
                    record.setAlarmSet(c.getInt(isAlarmSetIndex) > 0);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                callback.onReminderSelected(record);
				
			}
		});

        getLoaderManager().initLoader(0, null, this);
		return fragmentView;
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//Need to remove this. Orientation change is adding fragment twice currently.
		menu.clear();
    	menu.add("New");
        menu.add("Refresh");
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	final String operation = item.getTitle().toString();
    	
    	if(operation.equalsIgnoreCase("New")){
    		
    		Intent newReminderIntent = new Intent(this.getActivity(),AddReminderActivity.class);
    		startActivity(newReminderIntent);
    		
    	}
        else if(operation.equalsIgnoreCase("Refresh"))
        {
            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            Account account = AuthenticatorService.getAccount("com.example.remindersapp.account");
            ContentResolver.requestSync(account, ReminderContract.CONTENT_AUTHORITY, settingsBundle);
        }
    	
		return true;
    	
    }
    

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ReminderContract.Entry.CONTENT_URI,
                null, "", null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        simpleCursorAdapter.swapCursor(null);
    }

    public interface OnReminderSelectedListener {
		public void onReminderSelected(Record record);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try 
        {
            callback = (OnReminderSelectedListener) activity;
        } 
        catch (ClassCastException e) 
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnReminderSelectedListener");
        }
	}

    private Record getRecord(Cursor c)
    {
        int nameIndex = c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_NAME);
        int dateIndex = c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_DATE);
        int typeIndex = c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_TYPE);
        int isAlarmSetIndex = c.getColumnIndex(ReminderContract.Entry.COLUMN_NAME_SET);
        Record record = null;
        try {
            record = new Record(c.getString(nameIndex), DATEFORMAT.parse(c.getString(dateIndex)), c.getString(typeIndex));
            record.setAlarmSet(c.getInt(isAlarmSetIndex) > 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return record;
    }
}
