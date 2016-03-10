package com.cigital.insecurepay.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.TransferFundsVO;
import com.cigital.insecurepay.common.Connectivity;
import com.cigital.insecurepay.common.ResponseWrapper;
import com.google.gson.Gson;

public class TransferActivity extends AppCompatActivity {

    // View objects
    private TextView tvtransferAmount;
    private TextView tvFromAccountNo;
    private TextView tvToAccountNo;
    private TextView tvFromCustomerNo;
    private TextView tvToCustomerNo;
    private TextView tvtransferDetails;
    private Button btnCancel;
    private Button btnTransfer;
    private TransferFundsVO transferFundsVO;
    private CommonVO commonVO;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvtransferAmount = (TextView) findViewById(R.id.tvTransfer_filledAmount);
        tvFromAccountNo = (TextView) findViewById(R.id.tvTransfer_filledfromAccountNo);
        tvToAccountNo = (TextView) findViewById(R.id.tvTransfer_filledtoAccountNo);
        tvFromCustomerNo = (TextView) findViewById(R.id.tvTransfer_filledfromCustNo);
        tvToCustomerNo = (TextView) findViewById(R.id.tvTransfer_filledtoCustNo);
        tvtransferDetails = (TextView) findViewById(R.id.tvTransfer_filledDetails);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnTransfer = (Button) findViewById(R.id.btnTransfer);

        commonVO = (CommonVO) getIntent().getSerializableExtra(getString(R.string.common_VO));
        transferFundsVO = (TransferFundsVO) getIntent().getSerializableExtra(getString(R.string.transferFunds_VO));

        initValues();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onClickCancel();
            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTransfer();
            }
        });


    }

    private void initValues() {
        Log.i(this.getClass().getSimpleName(), "Initializing values.");
        // Initializing all objects from fragment_transfer
        tvFromAccountNo.setText(Integer.toString(transferFundsVO.getFromAccount().getAccNo()));
        tvFromCustomerNo.setText(Integer.toString(transferFundsVO.getFromAccount().getCustNo()));
        tvToAccountNo.setText(Integer.toString(transferFundsVO.getToAccount().getAccNo()));
        tvToCustomerNo.setText(Integer.toString(transferFundsVO.getToAccount().getCustNo()));
        tvtransferDetails.setText(transferFundsVO.getTransferDetails());
        tvtransferAmount.setText(Float.toString(transferFundsVO.getTransferAmount()));

    }

    private void onClickTransfer() {
        Log.d("OnClickTransfer", "transfer");
        TransferTask transferTask = new TransferTask(transferFundsVO);
        transferTask.execute();
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
                Connectivity connectivityObj = new Connectivity(commonVO.getServerAddress());
                connectivityObj.setConnectionParameters(getString(R.string.transfer_funds_path));
                connectivityObj.setSendToServer(sendToServer);

                ResponseWrapper responseWrapperObj = connectivityObj.post();
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
                Toast.makeText(TransferActivity.this.getApplicationContext(), "Amount was not transferred", Toast.LENGTH_LONG).show();
            } else {
                if (amountTransferred.equals("true")) {
                    Toast.makeText(TransferActivity.this.getApplicationContext(), "Transaction successful", Toast.LENGTH_LONG).show();
                    transferFundsVO.getToAccount().setAccountBalance(transferFundsVO.getToAccount().getAccountBalance() - transferFundsVO.getTransferAmount());
                    // build notification 
                    // the addAction re-use the same intent to keep the example short
                   /* Notification n = new Notification.Builder(getActivity())
                            .setContentTitle("New mail from " + "test@gmail.com")
                            .setContentText("Subject")
                            .setSmallIcon(R.drawable.ic_transfer_funds)
                            .setContentIntent(null)
                            .setAutoCancel(true).build();*/


                    //NotificationManager notificationManager =
                    //(NotificationManager) TransferFragment.this.getContext().getSystemService(TransferFragment.this.getContext().NOTIFICATION_SERVICE);

                    // notificationManager.notify(0, n);
                } else {
                    Log.e(this.getClass().getSimpleName(), "Invalid response on transfer funds");
                }
            }
        }
    }
}
