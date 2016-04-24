package com.cigital.insecurepay.DBHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cigital.insecurepay.VOs.TransactionVO;
import com.cigital.insecurepay.common.DBHelper;

/**
 * ActivityHistoryDBHelper is a class that extends {@link DBHelper} is used to
 * add entries to 'Transfers' table. It dumps all the transfer details brought from the
 * server when requested for checking transaction history.
 */
public class ActivityHistoryDBHelper extends DBHelper {

    //Initialize column variable names
    private static final String TABLE_NAME_TRANSFERS = "Transfers";
    private static final String TYPE = "Type";
    private static final String TRANSFER_DETAILS = "Transfer_details";
    private static final String TRANSFER_DATE = "Transfer_date";
    private static final String FINAL_AMOUNT = "Final_amount";
    private static final String TRANSFER_AMOUNT = "Transfer_amount";
    private static final String ACCOUNT_NUMBER = "AccountNo";

    /**
     * ActivityHistoryDBHelper is the parametrized constructor of this class.
     *
     * @param context Contains the context of the parent.
     */
    public ActivityHistoryDBHelper(Context context) {
        super(context);
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
                "Creating " + TABLE_NAME_TRANSFERS + " table.");

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
        Log.d(this.getClass().getSimpleName(),
                "onUpgrade: Removing old table " + TABLE_NAME_TRANSFERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRANSFERS + ";");
        onCreate(sqLiteDatabase);
    }

    /**
     * addTransfer is a function that is called to store the transaction details
     * of accounts to local table.
     *
     * @param transactionVO Object that is used to pass transaction data from the server to user
     *                      when requested for activity history.
     * @param accountNo     Specifies the account number associated with the transactions.
     */
    public void addTransfer(TransactionVO transactionVO, int accountNo) {
        Log.d(this.getClass().getSimpleName(), "addTransfer: Adding transfer details.");

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //prepares all columns from transactionVO for insertion in database
        values.put(ACCOUNT_NUMBER, accountNo);
        values.put(TYPE, transactionVO.getType());
        values.put(TRANSFER_DATE, transactionVO.getDate());
        values.put(TRANSFER_AMOUNT, transactionVO.getTransactionAmount());
        values.put(TRANSFER_DETAILS, transactionVO.getDescription());
        values.put(FINAL_AMOUNT, transactionVO.getFinalAmount());
        //inserts the values in database
        sqLiteDatabase.insert(TABLE_NAME_TRANSFERS, null, values);
    }
}
