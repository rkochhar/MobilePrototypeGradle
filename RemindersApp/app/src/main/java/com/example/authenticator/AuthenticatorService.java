package com.example.authenticator;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service
{

    private Authenticator authenticator;

    @Override
    public void onCreate() {
        authenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }

    public static Account getAccount(String type)
    {
        return new Account("reminderAccount", type);
    }
}
