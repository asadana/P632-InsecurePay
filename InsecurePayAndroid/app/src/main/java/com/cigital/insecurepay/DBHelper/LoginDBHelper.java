package com.cigital.insecurepay.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.common.DBHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LoginDBHelper extends DBHelper {

    public static final String CUST_USERNAME = "cust_username";
    public static final String TRIALS = "trials";
    public static final String CURR_TIME = "curr_time";
    public static final String isLocked = "isLocked";
    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private Context contextObj;

    public LoginDBHelper(Context context) {
        super(context);
        this.contextObj = context;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add trials to SqlLite DB
    public void addTrial(String username) {
        Log.d("LoginDBHelper", "addtrial");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRIALS, 1);
        values.put(CUST_USERNAME, username);
        values.put(isLocked, 0);
        values.put(CURR_TIME, format.print(DateTime.now()));
        db.insert(contextObj.getString(R.string.tableLoginTrials), null, values);
    }

    // Update trials to SqlLite DB with incremented trials
    public void updateTrial(String username, int trial, boolean locked) {
        Log.d("LoginDBHelper", "updateTrial");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRIALS, trial);
        values.put(isLocked, locked);
        values.put(CURR_TIME, format.print(DateTime.now()));
        db.update(contextObj.getString(R.string.tableLoginTrials), values,
                    CUST_USERNAME + "='" + username + "'", null);
    }

    // Fetch trials from SqlLite DB
    public int getTrial(String username) {
        Log.d("LoginDBHelper", "getTrial");
        int userTrial = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + TRIALS + " from " +
                                    contextObj.getString(R.string.tableLoginTrials) +
                                    " where " + CUST_USERNAME + "='" + username + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            userTrial = cursor.getInt(0);
        }
        if (cursor != null)
            cursor.close();
        return userTrial;
    }

    public DateTime getTimestamp(String username) {
        Log.d("LoginDBHelper", "getTimestamp");
        SQLiteDatabase db = this.getReadableDatabase();
        DateTime entryTime = null;
        Cursor cursor = db.rawQuery("select " + CURR_TIME + " from " +
                                    contextObj.getString(R.string.tableLoginTrials) +
                                    " where " + CUST_USERNAME + "='" + username + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            entryTime = format.withOffsetParsed().parseDateTime(cursor.getString(0));
        }
        if (cursor != null)
            cursor.close();return entryTime;
    }

    //Checks if the account is locked due to 3 or more unsuccessful login attempts
    public boolean isLocked(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean locked = false;
        Cursor cursor = db.rawQuery("select " + isLocked + " from " +
                                    contextObj.getString(R.string.tableLoginTrials) +
                                    " where " + CUST_USERNAME + "='" + username + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            locked = cursor.getInt(0) == 1;
        }
        if (cursor != null)
            cursor.close();
        return locked;
    }

    public void deleteTrial(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(contextObj.getString(R.string.tableLoginTrials),
                    CUST_USERNAME + "='" + username + "'", null);
    }


}
