package com.cigital.insecurepay.DBHelper;

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

    public void addTrial(String username, int trial) {
            Log.d("LoginDBHelper", "Trial no:" + trial);

    }

    public void updateTrial(String username, int trial) {
        if (getTrial(username) > 0) {
        } else {
            addTrial(username, trial);
        }
    }

    public int getTrial(String username) {

        Cursor cursor = db.rawQuery("select " + TRIALS + " from " + LOGIN_TRIALS + " where " + CUST_USERNAME + "='" + username + "'", null);
        cursor.moveToFirst();
        int user_trial = -1;
        while (cursor.moveToNext()) {
            user_trial = cursor.getInt(0);
        }
        return user_trial;
    }
}
