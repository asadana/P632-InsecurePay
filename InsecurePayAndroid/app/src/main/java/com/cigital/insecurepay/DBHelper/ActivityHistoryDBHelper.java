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
    public static final String TYPE = "Type";
    public static final String TRANSFER_DETAILS = "Transfer_details";
    public static final String TRANSFER_DATE = "Transfer_date";
    public static final String FINAL_AMOUNT = "Final_amount";
    public static final String TRANSFER_AMOUNT = "Transfer_amount";
    public static final String TRANSFERS = "Transfers";
    public static final String ACCOUNT_NUMBER = "AccountNo";

    /**
     * ActivityHistoryDBHelper is the parametrized constructor of this class.
     *
     * @param context Contains the context of the parent.
     */
    public ActivityHistoryDBHelper(Context context) {
        super(context);
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

    /**
     * addTransfer is a function that is called to store the transaction details
     * of accounts to local table.
     *
     * @param transactionVO Object that is used to pass transaction data from the server to user
     *                      when requested for activity history.
     * @param accountNo     Specifies the account number associated with the transactions.
     */
    public void addTransfer(TransactionVO transactionVO, int accountNo) {
        Log.d(this.getClass().getSimpleName(), "addTransfer: Adding transfers.");

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
        sqLiteDatabase.insert(TRANSFERS, null, values);
    }
}
