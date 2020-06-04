package com.nathan.arc.supertimer.storage.tools;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.nathan.arc.supertimer.model.DateTime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public final class AlarmTools {


    public static PendingIntent getBroadCastPendingIntent(Context context, long id, @NonNull Intent intent, int flag) {

        return PendingIntent.getBroadcast(context, (int) id, intent, flag);
    }

    public static PendingIntent getActivityPendingIntent(Context context, long id, @NonNull Intent intent, int flag) {

        return PendingIntent.getActivity(context, (int) id, intent, flag);
    }

    public static PendingIntent getServicePendingIntent(Context context, long id, @NonNull Intent intent, int flag) {

        return PendingIntent.getService(context, (int) id, intent, flag);
    }

    public static PendingIntent getForgroundservicePendingIntent(Context context, long id, @NonNull Intent intent, int flag) {

        return PendingIntent.getForegroundService(context, (int) id, intent, flag);
    }


    public static long convertDateTimeToMilliseconds(DateTime dateTime) {
        LocalDateTime ldt = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(), dateTime.getHour(),
                dateTime.getMinute(), dateTime.getSecond());
        Instant instant = ldt.toInstant(getSystemDefault());
        long time = instant.toEpochMilli();
        return time;
    }


    public static ZoneOffset getSystemDefault() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return ZoneOffset.of(ZoneOffset.systemDefault().getRules().getOffset(localDateTime).getId());
    }

    public static DateTime convertMillisecondsToDateTime(long time) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(time).atZone(getSystemDefault()).toLocalDateTime();
        DateTime dateTime = new DateTime(localDateTime.getYear(), localDateTime.getMonth().getValue(),
                localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute(),
                localDateTime.getSecond());
        return dateTime;
    }


    // 1.LocalDate转Date
    public void convertLocalDateToDate() {
        LocalDate nowLocalDate = LocalDate.now();
        Date date = Date.from(nowLocalDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
    }

    //2.LocalDateTime转Date
    public void convertLocalDateTimeToDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneOffset.ofHours(8)).toInstant());
    }

    //3.Date转LocalDateTime(LocalDate)
    public void convertDateToLocalDate() {
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        LocalDate localDate = date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDate();
    }


    //4.LocalDate转时间戳
    public void convertLocalDateToUnixTimestamp() {
        LocalDate localDate = LocalDate.now();
        long longtimestamp = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
    }

    //5.LocalDateTime转时间戳
    public void convertLocalDateTimeToUnixstamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        long longtimestamp = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    //6.时间戳转LocalDateTime(LocalDate)
    public void convertLocalDateTimeToLocalDate() {
        long timestamp = System.currentTimeMillis();
        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }
}