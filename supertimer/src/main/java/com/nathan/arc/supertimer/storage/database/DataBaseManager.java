package com.nathan.arc.supertimer.storage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nathan.arc.supertimer.model.Alarm;
import com.nathan.arc.supertimer.model.DateTime;
import com.nathan.arc.supertimer.storage.tools.AlarmTools;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class DataBaseManager extends SQLiteOpenHelper {

    // DATAbase constants
    private static final int DATABASE_VERSION = 1; // data version
    private static final String DATABASE_NAME = "alarm.db"; // database name
    private static final String TABLE_NAME = "alarm";  // table name
    private static final String COL_ID = "id";          // this column store alarm' ids
    private static final String COL_NAME = "alarm_name";    // this column store alarm'names
    private static final String COL_TOGGLE = "toggle";  // this column store alarm' state
    private static final String COL_DATETIME = "datetime";  // this column store alarm' state
    private static final String COL_REPEATTIME = "repeattime";  // this column store alarm' state
    private static final String COL_TYPE = "type";  // this column store alarm' state
    private static final String COL_LEARN = "learnId";  // this column store learn alarm' id

    // this string defines table and data type use for onCreate method
    private String CREATE_TABLE_ALARM = "CREATE TABLE " + TABLE_NAME + " ("
            + COL_ID + " INTEGER, "  // this column contain alarm's id
            + COL_NAME + " TEXT, "      // alarm's name
            + COL_DATETIME + " INTEGER, "      // alarm's startTime
            + COL_REPEATTIME + " INTEGER, "      // alarm's repeat time
            + COL_TYPE + " INTEGER, "      // alarm's type
            + COL_LEARN + " INTEGER, "     // learn id same as id
            + COL_TOGGLE + " INTEGER) ";    // alarm's toggle detail 1 is on 0 is off

    // TODO:   this is data base constructor
    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create data base for the first time
        db.execSQL(CREATE_TABLE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format(" DROP TABLE IF EXISTS %s", CREATE_TABLE_ALARM));
        onCreate(db);

    }

    // TODO: insert alarm to database
    public void insert(Alarm alarm) {
        if (query(alarm.getId()) != null){
            update(alarm);
            return;
        }
        // getting write data
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            // ContentValues like a box to contain value in there
            ContentValues values = new ContentValues();
            // put value to each column
            values.put(COL_ID, alarm.getId());
            values.put(COL_NAME, alarm.getAlarm_Name());
            values.put(COL_TOGGLE, alarm.getOnOff());
            values.put(COL_DATETIME, AlarmTools.convertDateTimeToMilliseconds(alarm.getDateTime()));
            values.put(COL_REPEATTIME, alarm.getRepeatTime());
            values.put(COL_LEARN, alarm.getLearnId());
            values.put(COL_TYPE, alarm.getIntentType());
            // insert to table
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // finally close it
            if (db != null) {
                db.close();
            }
        }


    }

    // TODO: this method update new alarm to database
    public void update(Alarm alarm) {
        SQLiteDatabase db = null;
        String where = COL_ID + " = " + alarm.getId();
        try {
            // getting write data
            db = this.getWritableDatabase();
            // ContentValues like a box to contain value in there
            ContentValues values = new ContentValues();

            values.put(COL_ID, alarm.getId());
            values.put(COL_NAME, alarm.getAlarm_Name());
            values.put(COL_TOGGLE, alarm.getOnOff());
            values.put(COL_DATETIME, AlarmTools.convertDateTimeToMilliseconds(alarm.getDateTime()));
            values.put(COL_REPEATTIME, alarm.getRepeatTime());
            values.put(COL_TYPE, alarm.getIntentType());
            values.put(COL_LEARN, alarm.getLearnId());
            db.update(TABLE_NAME, values, where, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // always close at the finally
            if (db != null) {
                db.close();
            }
        }


    }

    // TODO: this delete row in databse if the row has alarm'id equal alarmId
    public void delete(long alarmId) {
        // getting write data
        SQLiteDatabase db = null;
        String where = COL_ID + " = " + alarmId;

        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_NAME, where, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    // TODO: check Alarm from database if exist given alarm id
    public Alarm query(long alarmID) {
        // getting read data
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE id = "+alarmID;
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            // method moveToFirst return true if cursor not empty
            if (cursor.moveToFirst()) {
                do {
                    DateTime dateTime = AlarmTools.convertMillisecondsToDateTime(cursor.getLong(3));
                    Alarm alarm = new Alarm(cursor.getLong(0), cursor.getString(1),
                            dateTime,cursor.getLong(3),
                            cursor.getInt(4),cursor.getLong(5),cursor.getInt(6));
                    return alarm;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getAlarmList: exception cause " + e.getCause() + " message " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    // TODO: get all Alarm from database and return arrayList alarm
    public ArrayList<Alarm> getAlarmList() {
        // getting read data
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Alarm> alarmArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            // method moveToFirst return true if cursor not empty
            if (cursor.moveToFirst()) {
                do {
                    DateTime dateTime = AlarmTools.convertMillisecondsToDateTime(cursor.getLong(3));
                    Alarm alarm = new Alarm(cursor.getLong(0), cursor.getString(1),
                            dateTime,cursor.getLong(3),
                            cursor.getInt(4),cursor.getLong(5),cursor.getInt(6));
                    alarmArrayList.add(alarm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getAlarmList: exception cause " + e.getCause() + " message " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return alarmArrayList;
    }

}
