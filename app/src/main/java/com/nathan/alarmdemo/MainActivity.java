package com.nathan.alarmdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nathan.arc.supertimer.model.Alarm;
import com.nathan.arc.supertimer.model.DateTime;
import com.nathan.arc.supertimer.presenter.ISuperAlarm;
import com.nathan.arc.supertimer.presenter.impl.ISuperAlarmImpl;
import com.nathan.arc.supertimer.storage.tools.AlarmTools;
import com.nathan.arc.supertimer.storage.tools.Constants;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private ISuperAlarm mISuperAlarm;
    private long currentId ;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm alarm = convertLocalDateTimeToAlarm();
                Intent intent = new Intent(mContext,AlarmCustomerReceiver.class);
                //mISuperAlarm.addAlarm(alarm,intent);
                mISuperAlarm.addRepeatAlarm(alarm,intent);
                mIntent = intent;
            }
        });
        mISuperAlarm = new ISuperAlarmImpl(mContext);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm alarm = mISuperAlarm.queryAlarm(currentId);
                mISuperAlarm.deleteAlarm(alarm,mIntent);
            }
        });
        this.getExternalFilesDir(null);
    }

    public Alarm convertLocalDateTimeToAlarm() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Alarm alarm = new Alarm();
        Instant instant = localDateTime.toInstant(ZoneOffset.of(ZoneOffset.systemDefault().getRules().getOffset(localDateTime).getId()));
        long time = instant.toEpochMilli();
        Log.d("zyf", "convertLocalDateTimeToDate timeYYY: "+time);
        DateTime dateTime = AlarmTools.convertMillisecondsToDateTime(time+5*1000);//5s later start timer
        alarm.setDateTime(dateTime);
        alarm.setId(time);
        alarm.setAlarm_Name("test");
        alarm.setOnOff(1);
        alarm.setIntentType(Constants.BROADCAST_INENT);
        alarm.setRepeatTime(10*1000);
        currentId = time;
        return alarm;
    }


}
