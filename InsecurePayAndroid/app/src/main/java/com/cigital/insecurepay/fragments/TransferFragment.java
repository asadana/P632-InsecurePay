package com.cigital.insecurepay.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.AccountVO;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.TransferFundsVO;
import com.cigital.insecurepay.activity.LoginActivity;
import com.cigital.insecurepay.common.ResponseWrapper;
import com.google.gson.Gson;

public class TransferFragment extends Fragment {

    private EditText etTransferDetails;
    private EditText etCustUsername;
    private EditText etTransferAmount;
    private Button btnTransfer;
    private TransferFundsVO transferFundsVO;

    private Gson gson = new Gson();

    private TransferValidationTask transfervalidationtask = null;

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
        etTransferAmount = (EditText) viewObj.findViewById(R.id.ettransferAmount);
        etTransferDetails = (EditText) viewObj.findViewById(R.id.ettransferDetails);
        etCustUsername = (EditText) viewObj.findViewById(R.id.etCust_username);
        btnTransfer = (Button) viewObj.findViewById(R.id.btn_transfer);
        // Initializing commonVO and transferfundsVO object
        CommonVO commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
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
                transferDetails = etTransferDetails.getText().toString();
                transferAmount = Float.parseFloat(String.valueOf(etTransferAmount.getText()));
                custUsername = etCustUsername.getText().toString();

                if (transferAmount == 0) {
                    etTransferAmount.setError("Enter Amount");
                }
                if (custUsername == null) {
                    etCustUsername.setError("Enter Username");
                }
                transferFundsVO.setTransferAmount(transferAmount);
                transferFundsVO.setTransferDetails(transferDetails);
                transfervalidationtask = new TransferValidationTask(custUsername);
                transfervalidationtask.execute();


            }
        });
    }

    private class TransferValidationTask extends AsyncTask<String, String, Integer> {
        private String custUsername;

        public TransferValidationTask(String custUsername) {
            this.custUsername = custUsername;
        }

        @Override
        protected Integer doInBackground(String... params) {
            Log.d(this.getClass().getSimpleName(), "Background");
            try {
                // Fetching the connectivity object and setting context and path
                LoginActivity.connectivityObj.setConnectionParameters(getContext(), getString(R.string.transfer_validation_path));
                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.username), custUsername);

                ResponseWrapper responseWrapperObj = LoginActivity.connectivityObj.get(contentValues);

                //Converts customer details to CustomerVO
                String custNo = responseWrapperObj.getResponseString();
                Log.d(this.getClass().getSimpleName(), custNo);
                if (custNo != null) {
                    return Integer.parseInt(custNo);
                }

            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Error while connecting: ", e);
            }
            return -1;
        }

        protected void onPostExecute(Integer custNo) {
            if (custNo == -1) {
                etCustUsername.setError("Invalid User");
            } else {
                CustAccountFetchTask custAccountFetchTask = new CustAccountFetchTask(custNo);
                custAccountFetchTask.execute();
            }


        }


    }

    private class CustAccountFetchTask extends AsyncTask<String, String, AccountVO> {
        private int custNo;

        CustAccountFetchTask(int custNo) {
            this.custNo = custNo;
        }

        @Override
        protected AccountVO doInBackground(String... params) {
            Log.d(this.getClass().getSimpleName(), "Background");
            AccountVO accountDetails = null;
            try {

                // Fetching the connectivity object and setting context and path
                LoginActivity.connectivityObj.setConnectionParameters(getContext(), getString(R.string.account_details_path));

                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.cust_no), custNo);

                ResponseWrapper responseWrapperObj = LoginActivity.connectivityObj.get(contentValues);

                //Converts customer details to CustomerVO
                accountDetails = gson.fromJson(responseWrapperObj.getResponseString(), AccountVO.class);

                Log.d(this.getClass().getSimpleName(), "Customer Balance: " + accountDetails.getAccountBalance());
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Error while connecting: ", e);
            }
            return accountDetails;
        }

        protected void onPostExecute(AccountVO accountDetails) {
            transferFundsVO.setToAccount(accountDetails);
            TransferTask transferTask = new TransferTask(transferFundsVO);
            transferTask.execute();
        }

    }

    private class TransferTask extends AsyncTask<String, String, String> {
        private TransferFundsVO transferFundsVO;

        TransferTask(TransferFundsVO transferFundsVO) {
            this.transferFundsVO = transferFundsVO;
        }

        @Override
        protected String doInBackground(String... params) {

            Log.d(this.getClass().getSimpleName(), "In background, sending transfer details");
            String amountTransferred = null;
            try {
                //sendToServer contains JSON object that has credentials
                String sendToServer = gson.toJson(transferFundsVO);
                // Fetching the connectivity object and setting context and path
                LoginActivity.connectivityObj.setConnectionParameters(getContext(), getString(R.string.transfer_funds_path));
                LoginActivity.connectivityObj.setSendToServer(sendToServer);

                ResponseWrapper responseWrapperObj = LoginActivity.connectivityObj.post();
                //Call post and since there are white spaces in the response, trim is called
                amountTransferred = responseWrapperObj.getResponseString().trim();
                Log.d("Response from server", amountTransferred);
                Thread.sleep(2000);

            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Exception thrown in transfer funds", e);
            }

            return amountTransferred;
        }

        @Override
        protected void onPostExecute(final String amountTransferred) {

            if (amountTransferred.equals("false")) {
                Toast.makeText(TransferFragment.this.getContext(), "Amount was not transferred", Toast.LENGTH_LONG).show();
            } else {
                if (amountTransferred.equals("true")) {
                    Toast.makeText(TransferFragment.this.getContext(), "Transaction successful", Toast.LENGTH_LONG).show();
                    transferFundsVO.getToAccount().setAccountBalance(transferFundsVO.getToAccount().getAccountBalance() - transferFundsVO.getTransferAmount());
                    // build notification 
                    // the addAction re-use the same intent to keep the example short
                    Notification n = new Notification.Builder(getActivity())
                            .setContentTitle("New mail from " + "test@gmail.com")
                            .setContentText("Subject")
                            .setSmallIcon(R.drawable.ic_transfer_funds)
                            .setContentIntent(null)
                            .setAutoCancel(true).build();


                    NotificationManager notificationManager =
                            (NotificationManager) TransferFragment.this.getContext().getSystemService(TransferFragment.this.getContext().NOTIFICATION_SERVICE);

                    notificationManager.notify(0, n);
                } else {
                    Log.e(this.getClass().getSimpleName(), "Invalid response on transfer funds");
                }
            }
        }
    }
}
