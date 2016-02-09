package com.cigital.insecurepay.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cigital.insecurepay.common.DBHelper;

/**
 * Created by Amish on 07-02-2016.
 */
public class LoginDBHelper extends DBHelper {
    public static final String LOGIN_TRIALS = "LoginTrials";
    public static final String CUST_USERNAME = "cust_username";
    public static final String TRIALS = "trials";

    public LoginDBHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LOGIN_TRIALS + " (" + CUST_USERNAME + " text primary key, " + TRIALS + " int)");
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
        db.insert(LOGIN_TRIALS, null, values);
    }

    // Update trials to SqlLite DB with incremented trials
    public void updateTrial(String username, int trial) {
        Log.d("LoginDBHelper", "updateTrial");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRIALS, trial);
        db.update(LOGIN_TRIALS, values, CUST_USERNAME + "='" + username + "'", null);

    }

    // Fetch trials from SqlLite DB
    public int getTrial(String username) {
        Log.d("LoginDBHelper", "getTrial");
        int user_trial = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + TRIALS + " from " + LOGIN_TRIALS + " where " + CUST_USERNAME + "='" + username + "'", null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            user_trial = cursor.getInt(0);
        }
        return user_trial;
    }
}
