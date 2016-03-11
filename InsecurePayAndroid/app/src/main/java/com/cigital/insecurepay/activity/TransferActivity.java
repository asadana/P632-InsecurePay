package com.cigital.insecurepay.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
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
import com.cigital.insecurepay.common.PostAsyncCommonTask;

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
        Log.d(this.getClass().getSimpleName(), "onClickTransfer: Transferring");
        TransferTask transferTask = new TransferTask(this, commonVO.getServerAddress(),
                getString(R.string.transfer_funds_path), transferFundsVO);
        transferTask.execute();
    }

    private class TransferTask extends PostAsyncCommonTask<TransferFundsVO> {

        public TransferTask(Context contextObj, String serverAddress, String path, TransferFundsVO objToBeSent) {
            super(contextObj, serverAddress, path, objToBeSent, TransferFundsVO.class);
        }

        @Override
        protected void postSuccess(String amountTransferred) {
            super.postSuccess(amountTransferred);

            if (amountTransferred.equals("false")) {
                Toast.makeText(TransferActivity.this.getApplicationContext(), "Amount was not transferred", Toast.LENGTH_LONG).show();
            } else {
                if (amountTransferred.equals("true")) {
                    Toast.makeText(TransferActivity.this.getApplicationContext(), "Transaction successful", Toast.LENGTH_LONG).show();
                    transferFundsVO.getToAccount().setAccountBalance(transferFundsVO.getToAccount().getAccountBalance() - transferFundsVO.getTransferAmount());

                    // build notification 
                    // the addAction re-use the same intent to keep the example short
                    Notification n = new Notification.Builder(getApplicationContext())
                            .setContentTitle("New mail from " + "test@gmail.com")
                            .setContentText("Subject")
                            .setSmallIcon(R.drawable.ic_transfer_funds)
                            .setContentIntent(null)
                            .setAutoCancel(true).build();


                    NotificationManager notificationManager =
                            (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

                    notificationManager.notify(0, n);
                } else {
                    Log.e(this.getClass().getSimpleName(), "Invalid response on transfer funds");
                }
            }
        }
    }
}
