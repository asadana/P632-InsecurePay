package com.cigital.insecurepay.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBHelper is a class that extends {@link SQLiteOpenHelper}.
 * This class creates local Database 'InsecurePayDB' and two tables 'LoginTrials' and 'Transfers'.
 * LoginTrials maintains the number of login attempts and account lockout status
 * Transfers table is used to dump all the transaction details when ActivityHistory is checked.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_LOGIN = "LoginTrials";
    public static final String CUSTOMER_USERNAME = "cust_username";
    public static final String TRIALS = "trials";
    public static final String CURRENT_TIME = "curr_time";
    public static final String isLocked = "isLocked";
    public static final String ACCOUNT_NUMBER = "AccountNo";
    protected static final String TYPE = "Type";
    protected static final String TABLE_NAME_TRANSFERS = "Transfers";
    protected static final String TRANSFER_DETAILS = "Transfer_details";
    protected static final String TRANSFER_DATE = "Transfer_date";
    protected static final String FINAL_AMOUNT = "Final_amount";
    protected static final String TRANSFER_AMOUNT = "Transfer_amount";
    //Initialize variables used as column names in database
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "InsecurePayDB";

    /**
     * DBHelper is the parametrized constructor of this class.
     *
     * @param context Contains the context object of the parent.
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate is an overridden function that is called when database is created.
     *
     * @param sqLiteDatabase Contains the database to be used to store tables.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOGIN + ";");
        sqLiteDatabase.execSQL("create table " + TABLE_NAME_LOGIN + " (" + CUSTOMER_USERNAME +
                " text primary key, " + TRIALS + " int, " + CURRENT_TIME + " DATETIME , " +
                isLocked + " int )");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRANSFERS + ";");
        sqLiteDatabase.execSQL("create table " + TABLE_NAME_TRANSFERS + " (" + ACCOUNT_NUMBER +
                " int, " + TYPE + " int, " + TRANSFER_DATE + " date, " + TRANSFER_AMOUNT +
                " real ," + TRANSFER_DETAILS + " text , " + FINAL_AMOUNT + " real )");

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
    }
}