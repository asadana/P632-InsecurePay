package com.cigital.insecurepay.DBHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cigital.insecurepay.VOs.TransactionVO;
import com.cigital.insecurepay.common.DBHelper;

public class ActivityHistoryDBHelper extends DBHelper {

    public static final String TYPE = "Type";
    public static final String TRANSFER_DETAILS = "Transfer_details";
    public static final String TRANSFER_DATE = "Transfer_date";
    public static final String FINAL_AMOUNT = "Final_amount";
    public static final String TRANSFER_AMOUNT = "Transfer_amount";
    public static final String TRANSFERS = "Transfers";
    public static final String ACCOUNTNO = "AccountNo";

    public ActivityHistoryDBHelper(Context context) {
        super(context);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addTransfer(TransactionVO transactionVO, int accountNo) {
        Log.d("ActivityHistoryDBHelper", "addTransfer");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNTNO, accountNo);
        values.put(TYPE, transactionVO.getType());
        values.put(TRANSFER_DATE, transactionVO.getDate());
        values.put(TRANSFER_AMOUNT, transactionVO.getTransactionAmount());
        values.put(TRANSFER_DETAILS, transactionVO.getDescription());
        values.put(FINAL_AMOUNT, transactionVO.getFinalAmount());
        db.insert(TRANSFERS, null, values);
    }
}
