package com.nathan.arc.supertimer.presenter;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.nathan.arc.supertimer.model.Alarm;

import java.util.List;

public interface ISuperAlarm {

    void addAlarm(@NonNull Alarm alarm, @NonNull Intent intent);

    void addRepeatAlarm(@NonNull Alarm alarm, @NonNull Intent intent);

    void deleteAlarm(@NonNull Alarm alarm, @NonNull Intent intent);

    void updateAlarm(@NonNull Alarm alarm, @NonNull Intent intent);

    void updateRepeatAlarm(@NonNull Alarm alarm, @NonNull Intent intent);

    Alarm queryAlarm(long id);

    List<Alarm> getAlarmList();
}
