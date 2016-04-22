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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TransferFragment extends Fragment and is used for transferring funds.
 */
public class TransferFragment extends Fragment {

    //View objects
    private EditText etTransfer_Details;
    private EditText etTransfer_CustomerUsername;
    private EditText etTransfer_Amount;
    private Button btnTransfer;
    private TransferFundsVO transferFundsVO;
    private Gson gson = new Gson();
    private TransferValidationTask transfervalidationtask;

    private CommonVO commonVO;
    private static final Pattern sPattern = Pattern.compile("^-?[0-9]\\d*(\\.\\d+)?$");

    public TransferFragment() {
        // Required empty public constructor
    }

    // onCreateView is called when the class's view is being generated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewObj = inflater.inflate(R.layout.fragment_transfer, container, false);
        initValues(viewObj);
        addListeners();
        return viewObj;
    }

    // Initializes all the variables
    private void initValues(View viewObj) {
        Log.i(this.getClass().getSimpleName(), "Initializing values.");
        etTransfer_Amount = (EditText) viewObj.findViewById(R.id.ettransferAmount);
        etTransfer_Details = (EditText) viewObj.findViewById(R.id.ettransferDetails);
        etTransfer_CustomerUsername = (EditText) viewObj.findViewById(R.id.etCust_username);
        btnTransfer = (Button) viewObj.findViewById(R.id.btn_transfer);
        // Initializing commonVO and transferFundsVO object
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
                String customerUsername;
                String transferDetails;
                float transferAmount;
                String amount = String.valueOf(etTransfer_Amount.getText());
                Matcher m = sPattern.matcher(amount);
                transferDetails = etTransfer_Details.getText().toString();
                customerUsername = etTransfer_CustomerUsername.getText().toString();

                if (!m.matches()) {
                    etTransfer_Amount.setError("Enter Valid Amount");
                    etTransfer_Amount.requestFocus();
                    return;
                }
                //Ensures recepient of funds is filled
                if (customerUsername.equals("")) {
                    etTransfer_CustomerUsername.setError("Enter Username");
                    etTransfer_CustomerUsername.requestFocus();
                    return;
                }

                //Ensures funds are not transferred to oneself
                if (commonVO.getUsername().equals(customerUsername)) {
                    etTransfer_CustomerUsername.setError("Enter different Username");
                    etTransfer_CustomerUsername.requestFocus();
                    return;
                }
                transferAmount = Float.parseFloat(amount);
                transferFundsVO.setTransferAmount(transferAmount);
                transferFundsVO.setTransferDetails(transferDetails);
                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.username), customerUsername);
                //prepares for validation of receiver of funds
                transfervalidationtask = new TransferValidationTask(getContext(), commonVO.getServerAddress(),
                        getString(R.string.transfer_validation_path), contentValues);
                transfervalidationtask.execute();


            }
        });
    }

    /**
     * TransferValidationTask is used to ensure that the receiver of the funds is valid.
     */
    private class TransferValidationTask extends GetAsyncCommonTask<String> {

        //calls common Async task
        public TransferValidationTask(Context contextObj, String serverAddress, String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, String.class);
        }

        /**
         * postSuccess is called when the server responds with a non-error code response.
         * This function performs all the tasks to be done in postExecute when server response
         * is not an error.
         *
         * @param resultObj Contains the string sent from the server as part of the response.
         *                  Server sends the Customer No in response
         *
         */
        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            Log.d(this.getClass().getSimpleName(), "postSuccess: " + resultObj);
            if (resultObj != null) {
                //Server sends the Customer No in response
                int customerNumber = Integer.parseInt(resultObj);
                //If Customer is invalid, Server sends -1 as Customer No
                if (customerNumber == -1) {
                    etTransfer_CustomerUsername.setError("Invalid User");
                    etTransfer_CustomerUsername.requestFocus();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(getString(R.string.cust_no), customerNumber);
                    //If valid customer is entered, call customer account fetch task
                    CustomerAccountFetchTask customerAccountFetchTask = new CustomerAccountFetchTask(getContext(), commonVO.getServerAddress(),
                            getString(R.string.account_details_path), contentValues);
                    customerAccountFetchTask.execute();
                }
            }
        }
    }

    /**
     * CustomerAccountFetchTask is used to get the Account information of the customer
     *
     */
    private class CustomerAccountFetchTask extends GetAsyncCommonTask<AccountVO> {

        public CustomerAccountFetchTask(Context contextObj, String serverAddress, String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, AccountVO.class);
        }


        /**
         * postSuccess is called when the server responds with a non-error code response.
         * This function performs all the tasks to be done in postExecute when server response
         * is not an error.
         *
         * @param resultObj Contains the string sent from the server as part of the response.
         */
        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            AccountVO accountVOObj = objReceived;
            Log.d(this.getClass().getSimpleName(), "Customer Balance: " + accountVOObj.getAccountBalance());
            transferFundsVO.setToAccount(accountVOObj);
            //prepares for passing data to Transfer Activity
            Intent intent = new Intent(getContext(), TransferActivity.class);
            intent.putExtra(getString(R.string.transferFunds_VO), transferFundsVO);
            intent.putExtra(getString(R.string.common_VO), commonVO);
            startActivity(intent);
        }
    }
}

