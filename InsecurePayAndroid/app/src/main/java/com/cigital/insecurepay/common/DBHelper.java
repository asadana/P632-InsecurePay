package com.cigital.insecurepay.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBHelper is a class that creates local Database 'InsecurePayDB' and two tables 'LoginTrials' and 'Transfers'.
 * LoginTrials maintains the number of login attempts and account lockout status
 * Transfers table is used to dump all the transaction details when Activity History is checked.
 */
public class DBHelper extends SQLiteOpenHelper {

    //Initialize variables used as column names in database
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "InsecurePayDB";
    protected static final String TYPE = "Type";
    protected static final String TRANSFER_DETAILS = "Transfer_details";
    protected static final String TRANSFER_DATE = "Transfer_date";
    protected static final String FINAL_AMOUNT = "Final_amount";
    protected static final String TRANSFER_AMOUNT = "Transfer_amount";
    protected static final String TRANSFERS = "Transfers";
    public static final String LOGIN_TRIALS = "LoginTrials";
    public static final String CUSTOMER_USERNAME = "Cust_username";
    public static final String TRIALS = "trials";
    public static final String CURRENT_TIME = "current_time";
    public static final String isLocked = "isLocked";
    public static final String ACCOUNT_NUMBER = "AccountNo";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate is called for creation of the local database and two tables 'LoginTrials' and 'Transfers'.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS LoginTrials;");
        db.execSQL("create table " + LOGIN_TRIALS + " (" + CUSTOMER_USERNAME + " text primary key, " + TRIALS + " int, " + CURRENT_TIME + " DATETIME , " + isLocked + " int )");
        db.execSQL("DROP TABLE IF EXISTS Transfers;");
        db.execSQL("create table " + TRANSFERS + " (" + ACCOUNT_NUMBER + " int, " + TYPE + " int, " + TRANSFER_DATE + " date, " + TRANSFER_AMOUNT + " real ," + TRANSFER_DETAILS + " text , " + FINAL_AMOUNT + " real )");

    }

    /**
     * onUpgrade can be called for upgrading of the local database from older version to newer.
     * Needs to be overridden for the SQLLiteDatabase usage
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}