package com.cigital.insecurepay.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cigital.insecurepay.common.DBHelper;

import java.util.Date;

public class LoginDBHelper extends DBHelper {
    public static final String LOGIN_TRIALS = "LoginTrials";
    public static final String CUST_USERNAME = "cust_username";
    public static final String TRIALS = "trials";
    public static final String CURR_TIME = "curr_time";


    public LoginDBHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("LoginDBHelper", "Inside Oncreate DB");
        db.execSQL("create table " + LOGIN_TRIALS + " (" + CUST_USERNAME + " text primary key, " + TRIALS + " int, " + CURR_TIME + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add trials to SqlLite DB
    public void addTrial(String username, int trial) {
        Log.d("LoginDBHelper", "addtrial");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRIALS, trial);
        values.put(CUST_USERNAME, username);
        values.put(CURR_TIME, "");
        db.insert(LOGIN_TRIALS, null, values);
    }

    // Update trials to SqlLite DB with incremented trials
    public void updateTrial(String username, int trial) {
        Log.d("LoginDBHelper", "updateTrial");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRIALS, trial);
        if (getTrial(username) == 2) {
            Log.d("LoginDBHelper", "After 3rd trial");
            Date d1 = new Date();
            values.put(CURR_TIME, String.valueOf(d1.getTime()));
            Log.d("LoginDBHelper", String.valueOf(d1.getTime()));
        }
        if (trial < 2)
            values.put(CURR_TIME, "");
        db.update(LOGIN_TRIALS, values, CUST_USERNAME + "='" + username + "'", null);

    }

    // Fetch trials from SqlLite DB
    public int getTrial(String username) {
        Log.d("LoginDBHelper", "getTrial");
        int user_trial = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + TRIALS + " from " + LOGIN_TRIALS + " where " + CUST_USERNAME + "='" + username + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            user_trial = cursor.getInt(0);
        }
        return user_trial;
    }

    public long getTimestamp(String username) {
        Log.d("LoginDBHelper", "getTimestamp");
        SQLiteDatabase db = this.getReadableDatabase();
        long entry_time = 0;
        Cursor cursor = db.rawQuery("select " + CURR_TIME + " from " + LOGIN_TRIALS + " where " + CUST_USERNAME + "='" + username + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            entry_time = cursor.getLong(0);
        }
        return entry_time;
    }
}
