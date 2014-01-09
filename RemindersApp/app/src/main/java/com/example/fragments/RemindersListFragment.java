package com.example.fragments;

import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.RemindersAdapter;
import com.example.dto.Record;
import com.example.receiver.ReminderReceiver;
import com.example.remindersapp.AddReminderActivity;
import com.example.remindersapp.R;
import com.example.utils.CommonUtils;

public class RemindersListFragment extends Fragment
{
	private RemindersAdapter remindersAdapter;
	
	private ActionMode actionMode;
	
	private int selectedIndex;
	
	OnReminderSelectedListener callback;
	
	public RemindersListFragment()
	{
		setHasOptionsMenu(true);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View fragmentView = inflater.inflate(R.layout.reminder_fragment, container, false);
		
		ListView list = (ListView) fragmentView.findViewById(R.id.listView1);;
		List<Record> reminders = null;
        
        try 
        {
        	reminders = CommonUtils.getReminders(getActivity().getAssets().open("reminders.json"));
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}
        remindersAdapter = new RemindersAdapter(reminders);
        list.setAdapter(remindersAdapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				selectedIndex = position;
				if(actionMode != null)
				{
					return false;
				}
				else
				{
					actionMode = adapterView.startActionMode(actionModeCallback);
					adapterView.setSelected(true);
				}
				return true;
			}
		});
        
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				callback.onReminderSelected(position);
				
			}
		});
        
		return fragmentView;
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//Need to remove this. Orientation change is adding fragment twice currently.
		menu.clear();
    	menu.add("Sort by date");
    	menu.add("New");
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	final String operation = item.getTitle().toString();
    	
    	if(operation.equalsIgnoreCase("Sort by Date")){
    		remindersAdapter.setReminders(CommonUtils.sortRemindersByDate(remindersAdapter.getReminders()));
    		remindersAdapter.notifyDataSetChanged();
    	}
    	else if(operation.equalsIgnoreCase("New")){
    		
    		Intent newReminderIntent = new Intent(this.getActivity(),AddReminderActivity.class);
    		startActivity(newReminderIntent);
    		
    	}
    	
		return true;
    	
    }
    
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
		
    	@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			MenuItem alarmMenuOption = menu.findItem(R.id.alarm);
			if(remindersAdapter.getReminders().get(selectedIndex).isAlarmSet())
			{
	            alarmMenuOption.setIcon(R.drawable.clock_disable);
	            return true;
			}
			else
			{
				alarmMenuOption.setIcon(R.drawable.clock);
				return true;
			}
		}
		
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			actionMode = null;
			
		}
		
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater menuInflater = mode.getMenuInflater();
			menuInflater.inflate(R.menu.reminder_list_menu, menu);
			return true;
		}
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			
			if(item.getItemId() == R.id.delete)
			{
				remindersAdapter.getReminders().remove(selectedIndex);
				remindersAdapter.notifyDataSetChanged();
				mode.finish();
				return true;
			}
			else if(item.getItemId() == R.id.alarm)
			{
				Record selectedRecord = remindersAdapter.getReminders().get(selectedIndex);
				if(!selectedRecord.isAlarmSet())
				{
					Intent intent = new Intent(getView().getContext(), ReminderReceiver.class);
					intent.putExtra("selectedReminder", selectedRecord);
					intent.putExtra("selectedIndex", selectedIndex);
					PendingIntent pendingIntent = PendingIntent.getBroadcast(getView().getContext(), 0, intent , PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager alarmManager = (AlarmManager) getView().getContext().getSystemService(Context.ALARM_SERVICE);
					
					alarmManager.set(AlarmManager.RTC, selectedRecord.getDate().getTime() , pendingIntent);
					selectedRecord.setAlarmSet(true);
					Toast.makeText(getView().getContext(), "Alarm is set", Toast.LENGTH_LONG).show();
				}
				mode.finish();
				return true;
			}
			
			else
			{
				return false;
			}
		}
	};
	
	public interface OnReminderSelectedListener {
		public void onReminderSelected(int position);
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
	
}
