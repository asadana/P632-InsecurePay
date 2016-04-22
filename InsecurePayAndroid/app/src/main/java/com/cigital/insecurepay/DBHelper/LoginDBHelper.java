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

/**
 * LoginDBHelper is a class that is used to keep track of login attempts and account lockout status in 'LoginTrials' table.
 */
public class LoginDBHelper extends DBHelper {

    //Initialize column variable names
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

    /**
     * addTrial is called to make entry in 'LoginTrials' table when first unsuccessful login attempt is made.
     *
     * @param username Username used to login into the database
     *
     */
    public void addTrial(String username) {
        Log.d("LoginDBHelper", "addtrial");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //prepares row entry for insertion
        values.put(TRIALS, 1);
        values.put(CUST_USERNAME, username);
        values.put(isLocked, 0);
        values.put(CURR_TIME, format.print(DateTime.now()));
        //inserts the row in database
        db.insert(contextObj.getString(R.string.tableLoginTrials), null, values);
    }

    /**
     * updateTrial is called to update the number of unsuccessful trails made for logging in
     *
     * @param username Username used to login into the database
     *        trial    Trail count
     *        islocked Specifies if the account is locked or not
     */
    public void updateTrial(String username, int trial, boolean islocked) {
        Log.d("LoginDBHelper", "updateTrial");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //prepares row entry for insertion
        values.put(TRIALS, trial);
        values.put(isLocked, islocked);
        values.put(CURR_TIME, format.print(DateTime.now()));
        //updates the row of the database
        db.update(contextObj.getString(R.string.tableLoginTrials), values,
                    CUST_USERNAME + "='" + username + "'", null);
    }

    /**
     * getTrial is called to check number of unsuccessful login trials made to determine lockout
     *
     * @param username Username used to login into the database
     */
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

    /**
     * getTimestamp is used to get the timestamp when account got locked. If the time is greater than a particular
     * duration then account is unlocked
     *
     * @param username Username used to login into the database
     */
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

    /**
     * isLocked is called to check if the account is locked due to 3 or more unsuccessful login attempts
     *
     * @param username Username used to login into the database
     */
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

    /**
     * deleteTrial is called to delete the entry from table on successful login
     *
     * @param username Username used to login into the database
     */
    public void deleteTrial(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(contextObj.getString(R.string.tableLoginTrials),
                    CUST_USERNAME + "='" + username + "'", null);
    }

}
