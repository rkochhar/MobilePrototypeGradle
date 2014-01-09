package com.example.remindersapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.fragments.ReminderViewFragment;
import com.example.fragments.RemindersListFragment;
import com.example.fragments.RemindersListFragment.OnReminderSelectedListener;

public class MainActivity extends Activity implements OnReminderSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (findViewById(R.id.mainLayout) != null) 
        {
        	if (savedInstanceState != null) {
                return;
            }
	        RemindersListFragment remindersListFragment = new RemindersListFragment();
	        getFragmentManager().beginTransaction().add(R.id.mainLayout, remindersListFragment).commit();
        }
    }

	@Override
	public void onReminderSelected(int position) {
		ReminderViewFragment displayFragment = null;
		
		if (findViewById(R.id.mainLayout) != null)
		{
			displayFragment = new ReminderViewFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			displayFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.mainLayout, displayFragment);
            transaction.addToBackStack(null);
            transaction.commit();
		}
		else
		{
			displayFragment = (ReminderViewFragment) this.getFragmentManager().findFragmentById(R.id.fragment2);
			displayFragment.updateView(position, displayFragment.getView());
		}
	}
    
}
