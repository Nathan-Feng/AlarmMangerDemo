package com.nathan.arc.supertimer.presenter.impl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.nathan.arc.supertimer.AlarmReceiver;
import com.nathan.arc.supertimer.model.Alarm;
import com.nathan.arc.supertimer.presenter.ISuperAlarm;
import com.nathan.arc.supertimer.storage.database.DataBaseManager;
import com.nathan.arc.supertimer.storage.tools.AlarmTools;
import com.nathan.arc.supertimer.storage.tools.Constants;

import java.util.List;

import static com.nathan.arc.supertimer.storage.tools.Constants.ALARM_ID;
import static com.nathan.arc.supertimer.storage.tools.Constants.KEY_INTENT;
import static com.nathan.arc.supertimer.storage.tools.Constants.SENDER_INTENT;


public final class ISuperAlarmImpl implements ISuperAlarm {

    private Context mContext;
    private AlarmManager alarmManager;
    private DataBaseManager mDataBaseManager;

    public ISuperAlarmImpl(Context context) {
        this.mContext = context;
        // create alarm manager ALARM_SERVICE
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mDataBaseManager = new DataBaseManager(mContext);
    }

    @Override
    public void addAlarm(@NonNull Alarm alarm, @NonNull Intent intent) {
        long triggerAtMillis = AlarmTools.convertDateTimeToMilliseconds(alarm.getDateTime());
        PendingIntent pendingIntent =  createPendingIntent(alarm,intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerAtMillis,pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerAtMillis,pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,triggerAtMillis,pendingIntent);
        }
        mDataBaseManager.insert(alarm);
    }

    @Override
    public void addRepeatAlarm(@NonNull Alarm alarm, @NonNull Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            prepareCreatePendingIntent(alarm,intent);
        } else {
            long triggerAtMillis = AlarmTools.convertDateTimeToMilliseconds(alarm.getDateTime());
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,triggerAtMillis,
                    alarm.getRepeatTime(),createPendingIntent(alarm,intent));
            mDataBaseManager.insert(alarm);
        }
    }

    @Override
    public void deleteAlarm(@NonNull Alarm alarm, @NonNull Intent intent) {
        alarmManager.cancel(createPendingIntent(alarm,intent));
        mDataBaseManager.delete(alarm.getId());
        //cancel repeat mission if exist
        Alarm twinsAlarm = findAlarmTwins(alarm);
        if (twinsAlarm != null){
            alarmManager.cancel(createPendingIntent(twinsAlarm,intent));
        }
    }

    @Override
    public void updateAlarm(@NonNull Alarm alarm, @NonNull Intent intent) {
        addAlarm(alarm,intent);
        mDataBaseManager.insert(alarm);
    }

    @Override
    public void updateRepeatAlarm(@NonNull Alarm alarm, @NonNull Intent intent) {
        addRepeatAlarm(alarm,intent);
        mDataBaseManager.insert(alarm);
    }

    @Override
    public Alarm queryAlarm(long id) {
        List<Alarm> alarmList = getAlarmList();
        for (Alarm alarm1 :alarmList){
            if (alarm1.getId() == id){
                return alarm1;
            }
        }
       return null;
    }

    @Override
    public List<Alarm> getAlarmList() {
        //remove twins alarm;
        List<Alarm> alarmList = mDataBaseManager.getAlarmList();
        if (alarmList != null && !alarmList.isEmpty()){
            for (Alarm alarm : alarmList){
                if (alarm.getLearnId() != -1){
                    alarmList.remove(alarm);
                }
            }
        }
        return alarmList;
    }

    private Alarm findAlarmTwins(Alarm alarm){
        Alarm dbAlarm = mDataBaseManager.query(alarm.getId());
        if (dbAlarm != null && dbAlarm.getLearnId() != -1) {
            Alarm oldAlarm = mDataBaseManager.query(dbAlarm.getLearnId());
            return oldAlarm;
        }
        return null;
    }

    private void prepareCreatePendingIntent(Alarm alarm,Intent intent){
        long triggerAtMillis = AlarmTools.convertDateTimeToMilliseconds(alarm.getDateTime());
        if (alarm.getLearnId() == -1){ // new one
            PendingIntent pendingIntent =  createPendingIntent(alarm,intent);
            Log.d("zyf", "add new RepeatAlarm: start ***1***"+triggerAtMillis);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerAtMillis,pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerAtMillis,pendingIntent);
            }
            mDataBaseManager.insert(alarm);//insert to Database
            sendPrivateAlarm(alarm,intent);
        } else {
            Alarm dbAlarm = mDataBaseManager.query(alarm.getId());
            if (dbAlarm != null && dbAlarm.getLearnId() != -1) {
                Alarm oldAlarm = mDataBaseManager.query(dbAlarm.getLearnId());
                if (oldAlarm != null) {
                    PendingIntent pendingIntent =  createPendingIntent(oldAlarm,intent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerAtMillis,pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerAtMillis,pendingIntent);
                    }
                    sendPrivateAlarm(alarm,intent);
                }
            }
        }
    }

    private PendingIntent createPendingIntent(Alarm alarm, Intent intent){
        PendingIntent pendingIntent = null;
        switch (alarm.getIntentType()){
            case Constants.ACTIVITY_INENT:
                pendingIntent = AlarmTools.getActivityPendingIntent(mContext,alarm.getId(),intent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case Constants.BROADCAST_INENT:
                Log.d("zyf", "createPendingIntent: "+(int)alarm.getId());
                pendingIntent = AlarmTools.getBroadCastPendingIntent(mContext,alarm.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case Constants.SERVICE_INTENT:
                pendingIntent = AlarmTools.getServicePendingIntent(mContext,alarm.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case Constants.FOREGROUND_SERVICE_INTENT:
                pendingIntent = AlarmTools.getForgroundservicePendingIntent(mContext,alarm.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            default:
                break;
        }
        return pendingIntent;
    }

    private void sendPrivateAlarm(Alarm alarm, Intent sender){
        if (alarm.getLearnId() == -1){//new one need to insert clone Alarm
            alarm.setLearnId(alarm.getId());//different id in pending intent request code
            alarm.setId(alarm.getId()+1);//different id in pending intent request code
            mDataBaseManager.insert(alarm);
        }
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALARM_ID,alarm);
        bundle.putParcelable(SENDER_INTENT,sender);
        intent.putExtra(KEY_INTENT,bundle);
        long triggerAtMillis = AlarmTools.convertDateTimeToMilliseconds(alarm.getDateTime());
        PendingIntent pendingIntent =  createPendingIntent(alarm,intent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerAtMillis,pendingIntent);
    }



}
