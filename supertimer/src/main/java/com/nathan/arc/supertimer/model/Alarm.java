package com.nathan.arc.supertimer.model;


import java.io.Serializable;


/**
 * this is Alarm class present for alarm object implements
 * Serializable to support in transfer object Alarm
 * through intent
 */
public class Alarm implements Serializable {

    private long id;           // alarm's id this was create at the adding time,unix time
    private String alarm_Name;  // alarm's name
    private int onOff;          // alarm's on off
    private DateTime mDateTime; //start time
    private long repeatTime;    // repeat time ->millisecond
    private int intentType;           // Activity/BroadCast/Service/Foreground Service
    private long learnId;

    // first constructor is used at the import data from database
    public Alarm(long id, String alarm_Name, int onOff, int intentType) {
        this.id = id;
        this.alarm_Name = alarm_Name;
        this.onOff = onOff;
        this.intentType = intentType;
        this.learnId = -1;
    }

    //second constructor for setting repeat alarm
    public Alarm(long id, String alarm_Name, DateTime dateTime, long repeatTime,
                 int intentType, int onOff) {
        this.id = id;
        this.alarm_Name = alarm_Name;
        this.onOff = onOff;
        this.mDateTime = dateTime;
        this.repeatTime = repeatTime;
        this.intentType = intentType;
        this.learnId = -1;
    }

    //second constructor for setting repeat alarm
    public Alarm(long id, String alarm_Name, DateTime dateTime, long repeatTime,
                 int intentType, long learnId, int onOff) {
        this.id = id;
        this.alarm_Name = alarm_Name;
        this.onOff = onOff;
        this.mDateTime = dateTime;
        this.repeatTime = repeatTime;
        this.intentType = intentType;
        this.learnId = learnId;
    }

    // default
    public Alarm() {
        this.learnId = -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlarm_Name() {
        return alarm_Name;
    }

    public void setAlarm_Name(String alarm_Name) {
        this.alarm_Name = alarm_Name;
    }

    public int getOnOff() {
        return onOff;
    }

    public void setOnOff(int onOff) {
        this.onOff = onOff;
    }

    public DateTime getDateTime() {
        return mDateTime;
    }

    public void setDateTime(DateTime dateTime) {
        mDateTime = dateTime;
    }

    public long getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(long repeatTime) {
        this.repeatTime = repeatTime;
    }

    public int getIntentType() {
        return intentType;
    }

    public void setIntentType(int intentType) {
        this.intentType = intentType;
    }

    public long getLearnId() {
        return learnId;
    }

    public void setLearnId(long learnId) {
        this.learnId = learnId;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", alarm_Name='" + alarm_Name + '\'' +
                ", onOff=" + onOff +
                ", mDateTime=" + mDateTime +
                ", repeatTime=" + repeatTime +
                ", intentType=" + intentType +
                ", learnId=" + learnId +
                '}';
    }

}

