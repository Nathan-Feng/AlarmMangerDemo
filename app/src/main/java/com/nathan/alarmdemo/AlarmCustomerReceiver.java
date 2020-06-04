package com.nathan.alarmdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmCustomerReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Log.d("AlarmCustomerReceiver", "zyf onReceive: AlarmCustomerReceiver");
    }

}