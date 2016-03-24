package com.cigital.insecurepay.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.TransactionVO;
import com.cigital.insecurepay.common.GetAsyncCommonTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class ActivityHistoryFragment extends Fragment {

    private ListView lvTransactionList;
    private EditText etAccountNumber;
    private View viewObj;
    private Button btnSubmit;

    private CommonVO commonVO;

    private TransactionAdapter adapter;
    private int accountNumber;

    private AccountNoValidationTask accountnovalidationtask;
    private ActivityHistoryFetchTask activityHistoryFetchTask;

    public ActivityHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewObj = inflater.inflate(R.layout.fragment_activity_history, container, false);
        etAccountNumber = (EditText) viewObj.findViewById(R.id.etActivityHistory_AccountNo);
        btnSubmit = (Button) viewObj.findViewById(R.id.btnSubmit);
        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        etAccountNumber.setText(String.valueOf(commonVO.getAccountVO().getAccNo()));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountNo = String.valueOf(etAccountNumber.getText());
                if (accountNo.equals("")) {
                    etAccountNumber.setError("Enter Account Number");
                    etAccountNumber.requestFocus();
                    return;
                }

                accountNumber = Integer.parseInt(accountNo);
                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.account_no), accountNumber);
                accountnovalidationtask = new AccountNoValidationTask(getContext(), commonVO.getServerAddress(),
                        getString(R.string.accountno_validation_path), contentValues);
                accountnovalidationtask.execute();
            }
        });


        lvTransactionList = (ListView) viewObj.findViewById(R.id.lvActivityHistory_transactionList);
        return viewObj;
    }

    private class AccountNoValidationTask extends GetAsyncCommonTask<String> {

        public AccountNoValidationTask(Context contextObj, String serverAddress, String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, String.class);
        }

        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            Log.d(this.getClass().getSimpleName(), "postSuccess: " + resultObj);
            // Checking the server response
            switch (resultObj) {
                case "false":
                    etAccountNumber.setError("Invalid Account No");
                    etAccountNumber.requestFocus();
                    break;
                case "true":
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(getString(R.string.account_no), accountNumber);
                    activityHistoryFetchTask = new ActivityHistoryFetchTask(getContext(), commonVO.getServerAddress(),
                            getString(R.string.activity_history_path), contentValues);
                    activityHistoryFetchTask.execute();
                    break;
                default:
                    Log.e(this.getClass().getSimpleName(), "Invalid response on Account No Validation");
                    break;
            }
        }
    }

    private class ActivityHistoryFetchTask extends GetAsyncCommonTask<String> {

        public ActivityHistoryFetchTask(Context contextObj, String serverAddress, String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, String.class);
        }

        @Override
        protected void postSuccess(String resultObj) {
            Log.d(this.getClass().getSimpleName(), "postSuccess: " + resultObj);
            // Checking the server response
            Gson gson = new Gson();
            List<TransactionVO> result = gson.fromJson(resultObj, new TypeToken<List<TransactionVO>>() {
            }.getType());
            adapter = new TransactionAdapter(getContext(), R.layout.transaction_format, result);
            lvTransactionList.setAdapter(adapter);

        }
    }
}

