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