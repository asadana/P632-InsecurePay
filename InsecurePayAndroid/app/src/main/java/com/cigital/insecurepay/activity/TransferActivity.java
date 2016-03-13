package com.cigital.insecurepay.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    private TextView tvTransferAmount;
    private TextView tvFromAccountNo;
    private TextView tvToAccountNo;
    private TextView tvFromCustomerNo;
    private TextView tvToCustomerNo;
    private TextView tvTransferDetails;
    private Button btnCancel;
    private Button btnConfirm;
    private TransferFundsVO transferFundsVO;
    private CommonVO commonVO;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTransferAmount = (TextView) findViewById(R.id.tvTransfer_filledAmount);
        tvFromAccountNo = (TextView) findViewById(R.id.tvTransfer_filledfromAccountNo);
        tvToAccountNo = (TextView) findViewById(R.id.tvTransfer_filledtoAccountNo);
        tvFromCustomerNo = (TextView) findViewById(R.id.tvTransfer_filledfromCustNo);
        tvToCustomerNo = (TextView) findViewById(R.id.tvTransfer_filledtoCustNo);
        tvTransferDetails = (TextView) findViewById(R.id.tvTransfer_filledDetails);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        commonVO = (CommonVO) getIntent().getSerializableExtra(getString(R.string.common_VO));
        transferFundsVO = (TransferFundsVO) getIntent().getSerializableExtra(getString(R.string.transferFunds_VO));

        initValues();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClickCancel", "cancel");
                onBackPressed();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
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
        tvTransferDetails.setText(transferFundsVO.getTransferDetails());
        tvTransferAmount.setText(Float.toString(transferFundsVO.getTransferAmount()));

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
                Toast.makeText(TransferActivity.this.getApplicationContext(), getString(R.string.transfer_unsuccessful), Toast.LENGTH_LONG).show();
            } else {
                if (amountTransferred.equals("true")) {
                    Toast.makeText(TransferActivity.this.getApplicationContext(), getString(R.string.transfer_successful), Toast.LENGTH_LONG).show();

                    Intent intentNew= new Intent(TransferActivity.this.getApplicationContext(), HomePage.class);
                    intentNew.putExtra(getString(R.string.common_VO), commonVO);
                    intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pIntent= PendingIntent.getActivity(TransferActivity.this.getApplicationContext(), (int) System.currentTimeMillis(),intentNew, 0);

                    // build notification 
                    // the addAction re-use the same intent to keep the example short
                    Notification n = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Transfer successful")
                            .setContentText("Amount Transferred from Acc No " + tvFromAccountNo.getText() + " to Acc No " + tvToAccountNo.getText())
                            .setSmallIcon(R.drawable.ic_transfer_funds)
                            .setContentIntent(pIntent)
                            .setAutoCancel(true).build();


                    NotificationManager notificationManager =
                            (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

                    notificationManager.notify(0, n);
                    intent = new Intent(getApplicationContext(), HomePage.class);
                    intent.putExtra(getString(R.string.common_VO), commonVO);
                    startActivity(intent);
                } else {
                    Log.e(this.getClass().getSimpleName(), "Invalid response on transfer funds");
                }
            }
        }
    }
}
