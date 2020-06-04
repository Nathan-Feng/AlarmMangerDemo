package com.nathan.arc.supertimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nathan.arc.supertimer.model.Alarm;
import com.nathan.arc.supertimer.model.DateTime;
import com.nathan.arc.supertimer.presenter.ISuperAlarm;
import com.nathan.arc.supertimer.presenter.impl.ISuperAlarmImpl;
import com.nathan.arc.supertimer.storage.tools.AlarmTools;
import com.nathan.arc.supertimer.storage.tools.Constants;


public class AlarmReceiver extends BroadcastReceiver {
    // this hold pendingIntend id if one pendingIntend trigger. The PendingIntent'id is alarm'id
    private ISuperAlarm mISuperAlarm;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Log.d("AlarmReceiver", "zyf onReceive: AlarmReceiver 1");
        if (mISuperAlarm == null) {
            mISuperAlarm = new ISuperAlarmImpl(context);
        }
        if (intent != null) {
            // this hold information
            Bundle temp= intent.getBundleExtra(Constants.KEY_INTENT);
            Alarm alarm = (Alarm) temp.getSerializable(Constants.ALARM_ID);
            Intent sender = temp.getParcelable(Constants.SENDER_INTENT);
            if (alarm != null && temp != null) {
                long triggerAtMillis = AlarmTools.convertDateTimeToMilliseconds(alarm.getDateTime());
                DateTime newTime = AlarmTools.convertMillisecondsToDateTime(triggerAtMillis+alarm.getRepeatTime());
                alarm.setDateTime(newTime);
                Log.d("AlarmReceiver", "zyf onReceive: AlarmReceiver alarmID:"+alarm.getId());
                mISuperAlarm.addRepeatAlarm(alarm,sender);
            }
        }
    }

}