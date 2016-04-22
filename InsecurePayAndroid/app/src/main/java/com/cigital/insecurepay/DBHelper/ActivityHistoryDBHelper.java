package com.cigital.insecurepay.DBHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cigital.insecurepay.VOs.TransactionVO;
import com.cigital.insecurepay.common.DBHelper;

/**
 * ActivityHistoryDBHelper is a class that is used to add entries to 'Transfers' table.
 * It dumps all the transfer details brought from the server when requested for checking transaction history.
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

    public ActivityHistoryDBHelper(Context context) {
        super(context);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * addTransfer is called to store the transaction details of accounts to local 'Transfer' table.
     *
     * @param transactionVO Object that is used to pass transaction data from the server to user when
     *                      requested for activity history
     *        accountNo     Specifies the account number associated with the transactions
     */
    public void addTransfer(TransactionVO transactionVO, int accountNo) {
        Log.d("ActivityHistoryDBHelper", "addTransfer");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //prepares all columns from transactionVO for insertion in database
        values.put(ACCOUNT_NUMBER, accountNo);
        values.put(TYPE, transactionVO.getType());
        values.put(TRANSFER_DATE, transactionVO.getDate());
        values.put(TRANSFER_AMOUNT, transactionVO.getTransactionAmount());
        values.put(TRANSFER_DETAILS, transactionVO.getDescription());
        values.put(FINAL_AMOUNT, transactionVO.getFinalAmount());
        //inserts the values in database
        db.insert(TRANSFERS, null, values);
    }
}
