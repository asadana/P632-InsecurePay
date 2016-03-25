package com.cigital.insecurepay.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "InsecurePayDB";
    protected static final String TYPE = "Type";
    protected static final String TRANSFER_DETAILS = "Transfer_details";
    protected static final String TRANSFER_DATE = "Transfer_date";
    protected static final String FINAL_AMOUNT = "Final_amount";
    protected static final String TRANSFER_AMOUNT = "Transfer_amount";
    protected static final String TRANSFERS = "Transfers";
    public static final String LOGIN_TRIALS = "LoginTrials";
    public static final String CUST_USERNAME = "cust_username";
    public static final String TRIALS = "trials";
    public static final String CURR_TIME = "curr_time";
    public static final String isLocked = "isLocked";
    public static final String ACCOUNTNO = "AccountNo";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS LoginTrials;");
        db.execSQL("create table " + LOGIN_TRIALS + " (" + CUST_USERNAME + " text primary key, " + TRIALS + " int, " + CURR_TIME + " DATETIME , " + isLocked + " int )");
        db.execSQL("DROP TABLE IF EXISTS Transfers;");
        db.execSQL("create table " + TRANSFERS + " (" + ACCOUNTNO + " int, " + TYPE + " int, " + TRANSFER_DATE + " date, " + TRANSFER_AMOUNT + " real ," + TRANSFER_DETAILS + " text , " + FINAL_AMOUNT + " real )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}