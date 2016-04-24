package com.cigital.insecurepay.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cigital.insecurepay.common.DBHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * LoginDBHelper is a class that extends {@link DBHelper} that is used to
 * keep track of login attempts and account lockout status in 'LoginTrials' table.
 */
public class LoginDBHelper extends DBHelper {

    //Initialize column variable names
    public static final String TABLE_NAME_LOGIN = "LoginTrials";
    private static final String CUSTOMER_USERNAME = "cust_username";
    private static final String TRIALS = "trials";
    private static final String CURRENT_TIME = "curr_time";
    private static final String isLocked = "isLocked";

    private DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private Context contextObj;

    /**
     * LoginDBHelper is the parametrized constructor of this class.
     *
     * @param context Contains the context of the parent.
     */
    public LoginDBHelper(Context context) {
        super(context);
        this.contextObj = context;
    }

    /**
     * onCreate is an overridden function that is called when database is created.
     *
     * @param sqLiteDatabase Contains the database to be used to store tables.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);
        Log.d(this.getClass().getSimpleName(), "onCreate: " +
                "Creating " + TABLE_NAME_LOGIN + " table.");

        sqLiteDatabase.execSQL("create table " + TABLE_NAME_LOGIN + " (" + CUSTOMER_USERNAME +
                " text primary key, " + TRIALS + " int, " + CURRENT_TIME + " DATETIME , " +
                isLocked + " int )");
    }

    /**
     * onUpgrade is an overridden function that is called when the database needs to be updated
     * from a previous version.
     *
     * @param sqLiteDatabase Contains the database object.
     * @param oldVersion     Contains the oldVersion number.
     * @param newVersion     Contains the newVersion number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(this.getClass().getSimpleName(),
                "onUpgrade: Removing old table " + TABLE_NAME_LOGIN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOGIN + ";");
        onCreate(sqLiteDatabase);
    }

    /**
     * addTrial is a function that is called to make entry in table when first
     * unsuccessful login attempt is made.
     *
     * @param username Username used to login into the database
     */
    public void addTrial(String username) {
        Log.d(this.getClass().getSimpleName(), "addTrial: Adding new user " + username);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //prepares row entry for insertion
        values.put(TRIALS, 1);
        values.put(CUSTOMER_USERNAME, username);
        values.put(isLocked, 0);
        values.put(CURRENT_TIME, format.print(DateTime.now()));
        //inserts the row in database
        sqLiteDatabase.insert(TABLE_NAME_LOGIN, null, values);
    }

    /**
     * updateTrial is a function that is called to update the number of
     * unsuccessful trails made for logging in.
     *
     * @param username      Username used to login into the database.
     * @param trialCount    Contains the number of trials.
     * @param isLocked      Specifies if the account is locked or not.
     */
    public void updateTrial(String username, int trialCount, boolean isLocked) {
        Log.d(this.getClass().getSimpleName(), "updateTrial: Updating trial count.");

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //prepares row entry for insertion
        values.put(TRIALS, trialCount);
        values.put(LoginDBHelper.isLocked, isLocked);
        values.put(CURRENT_TIME, format.print(DateTime.now()));
        //updates the row of the database
        sqLiteDatabase.update(TABLE_NAME_LOGIN, values,
                CUSTOMER_USERNAME + "='" + username + "'", null);
    }

    /**
     * getTrial is a function that is called to check number of unsuccessful
     * login trials made to determine lockout.
     *
     * @param username Username used to login into the database.
     */
    public int getTrial(String username) {
        Log.d(this.getClass().getSimpleName(),
                "getTrial: Retrieving the trials of user: " + username);

        int userTrial = -1;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select " + TRIALS + " from " +
                TABLE_NAME_LOGIN + " where " + CUSTOMER_USERNAME + "='" + username + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            userTrial = cursor.getInt(0);
        }
        if (cursor != null)
            cursor.close();
        return userTrial;
    }

    /**
     * getTimestamp is a function that is used to get the timestamp when account got locked.
     * If the time is greater than a particular duration then account is unlocked.
     *
     * @param username Username used to login into the database
     *
     * @return DateTime Return an object of {@link DateTime}.
     */
    public DateTime getTimestamp(String username) {
        Log.d(this.getClass().getSimpleName(), "getTimestamp: getting the time stamp.");

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        DateTime entryTime = null;
        Cursor cursor = sqLiteDatabase.rawQuery("select " + CURRENT_TIME + " from " +
                TABLE_NAME_LOGIN + " where " + CUSTOMER_USERNAME + "='" + username + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            entryTime = format.withOffsetParsed().parseDateTime(cursor.getString(0));
        }
        if (cursor != null)
            cursor.close();
        return entryTime;
    }

    /**
     * isLocked is a function that is called to check if the account is locked due
     * to repeated unsuccessful login attempts.
     *
     * @param username Username used to login into the database.
     *
     * @return boolean  Return a boolean value depending if the account is locked.
     */
    public boolean isLocked(String username) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        boolean locked = false;
        Cursor cursor = sqLiteDatabase.rawQuery("select " + isLocked + " from " +
                TABLE_NAME_LOGIN + " where " + CUSTOMER_USERNAME + "='" + username + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            locked = cursor.getInt(0) == 1;
        }
        if (cursor != null)
            cursor.close();
        return locked;
    }

    /**
     * deleteTrial is a function that is called to delete the entry from table on successful login.
     *
     * @param username Username used to login into the database
     */
    public void deleteTrial(String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME_LOGIN, CUSTOMER_USERNAME + "='" + username + "'", null);
    }

}
