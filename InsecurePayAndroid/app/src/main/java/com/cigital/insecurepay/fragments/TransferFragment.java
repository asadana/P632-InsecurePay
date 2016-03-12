package com.cigital.insecurepay.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.AccountVO;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.TransferFundsVO;
import com.cigital.insecurepay.activity.TransferActivity;
import com.cigital.insecurepay.common.GetAsyncCommonTask;
import com.google.gson.Gson;

public class TransferFragment extends Fragment {

    private CommonVO commonVO;
    private EditText etTransfer_Details;
    private EditText etTransfer_CustUsername;
    private EditText etTransfer_Amount;
    private Button btnTransfer;
    private Intent intent;
    private TransferFundsVO transferFundsVO;
    private Gson gson = new Gson();
    private TransferValidationTask transfervalidationtask;
    private CustAccountFetchTask custAccountFetchTask;

    public TransferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewObj = inflater.inflate(R.layout.fragment_transfer, container, false);

        initValues(viewObj);
        addListeners();
        return viewObj;
    }

    private void initValues(View viewObj) {
        Log.i(this.getClass().getSimpleName(), "Initializing values.");
        // Initializing all objects from fragment_transfer
        etTransfer_Amount = (EditText) viewObj.findViewById(R.id.ettransferAmount);
        etTransfer_Details = (EditText) viewObj.findViewById(R.id.ettransferDetails);
        etTransfer_CustUsername = (EditText) viewObj.findViewById(R.id.etCust_username);
        btnTransfer = (Button) viewObj.findViewById(R.id.btn_transfer);
        // Initializing commonVO and transferfundsVO object
        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        transferFundsVO = new TransferFundsVO();
        transferFundsVO.setFromAccount(commonVO.getAccountVO());


    }

    // Initializing listeners where needed
    private void addListeners() {
        Log.i(this.getClass().getSimpleName(), "Adding Listeners");


        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custUsername;
                String transferDetails;
                float transferAmount;
                transferDetails = etTransfer_Details.getText().toString();
                transferAmount = Float.parseFloat(String.valueOf(etTransfer_Amount.getText()));
                custUsername = etTransfer_CustUsername.getText().toString();

                if (transferAmount == 0) {
                    etTransfer_Amount.setError("Enter Amount");
                }
                if (custUsername == null) {
                    etTransfer_CustUsername.setError("Enter Username");
                }
                transferFundsVO.setTransferAmount(transferAmount);
                transferFundsVO.setTransferDetails(transferDetails);
                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.username), custUsername);
                transfervalidationtask = new TransferValidationTask(getContext(), commonVO.getServerAddress(),
                        getString(R.string.transfer_validation_path), contentValues);
                transfervalidationtask.execute();


            }
        });
    }

    private class TransferValidationTask extends GetAsyncCommonTask<String> {


        public TransferValidationTask(Context contextObj, String serverAddress, String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, String.class);
        }

        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            Log.d(this.getClass().getSimpleName(), "postSuccess: " + resultObj);
            if (resultObj != null) {
                int custNo = Integer.parseInt(resultObj);
                if (custNo == -1) {
                    etTransfer_CustUsername.setError("Invalid User");
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(getString(R.string.cust_no), custNo);
                    custAccountFetchTask = new CustAccountFetchTask(getContext(), commonVO.getServerAddress(),
                            getString(R.string.account_details_path), contentValues);
                    custAccountFetchTask.execute();
                }
            }
        }
    }

    private class CustAccountFetchTask extends GetAsyncCommonTask<AccountVO> {

        public CustAccountFetchTask(Context contextObj, String serverAddress, String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, AccountVO.class);
        }

        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            AccountVO accountVOObj = objReceived;
            Log.d(this.getClass().getSimpleName(), "Customer Balance: " + accountVOObj.getAccountBalance());
            transferFundsVO.setToAccount(accountVOObj);
            intent = new Intent(getContext(), TransferActivity.class);
            intent.putExtra(getString(R.string.transferFunds_VO), transferFundsVO);
            intent.putExtra(getString(R.string.common_VO), commonVO);
            startActivity(intent);
        }
    }
}

