package com.example.remindersapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.dto.Record;
import com.example.fragments.ReminderViewFragment;
import com.example.fragments.RemindersListFragment.OnReminderSelectedListener;
import com.example.provider.ReminderContract;

public class MainActivity extends Activity implements OnReminderSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSyncAccount(this);
        setContentView(R.layout.activity_main);
    }

	@Override
	public void onReminderSelected(Record record) {
		ReminderViewFragment displayFragment = null;
		
		if (findViewById(R.id.mainLayout) != null)
		{
            Intent detailsIntent = new Intent(this, DetailsActivity.class);
            detailsIntent.putExtra("selected", record);
            startActivity(detailsIntent);
		}
		else
		{
			displayFragment = (ReminderViewFragment) this.getFragmentManager().findFragmentById(R.id.fragment2);
			displayFragment.updateView(record, displayFragment.getView());
		}
	}

    public void createSyncAccount(Context context) {
        Account account = new Account("reminderAccount", "com.example.remindersapp.account");
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        if(accountManager.addAccountExplicitly(account, null, null))
        {
            ContentResolver.setIsSyncable(account, ReminderContract.CONTENT_AUTHORITY, 1);
            //ContentResolver.setSyncAutomatically(account, ReminderContract.CONTENT_AUTHORITY, true);
        }
    }

}
