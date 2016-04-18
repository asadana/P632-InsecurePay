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

/**
 * TransferActivity is an activity class that displays all the transfer information, giving the
 * user an option to review and confirm or cancel the transaction.
 */
public class TransferActivity extends AppCompatActivity {

    // View objects
    private TextView tvTransferAmount;
    private TextView tvFromAccountNo;
    private TextView tvToAccountNo;
    private TextView tvFromCustomerNo;
    private TextView tvToCustomerNo;
    private TextView tvTransferDetails;
    private TransferFundsVO transferFundsVO;

    private CommonVO commonVO;

    /**
     * onCreate is the first method called when the Activity is being created.
     * It populates and initializes the text views.
     *
     * @param savedInstanceState Object that is used to pass data to this activity while
     *                           creating it.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(this.getClass().getSimpleName(), "onCreate: Initializing started");

        setContentView(R.layout.activity_transfer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting up UI components
        tvTransferAmount = (TextView) findViewById(R.id.tvTransfer_filledAmount);
        tvFromAccountNo = (TextView) findViewById(R.id.tvTransfer_filledfromAccountNo);
        tvToAccountNo = (TextView) findViewById(R.id.tvTransfer_filledtoAccountNo);
        tvFromCustomerNo = (TextView) findViewById(R.id.tvTransfer_filledfromCustNo);
        tvToCustomerNo = (TextView) findViewById(R.id.tvTransfer_filledtoCustNo);
        tvTransferDetails = (TextView) findViewById(R.id.tvTransfer_filledDetails);

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);

        commonVO = (CommonVO) getIntent().getSerializableExtra(getString(R.string.common_VO));
        transferFundsVO = (TransferFundsVO) getIntent().getSerializableExtra(getString(R.string.transferFunds_VO));

        initValues();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(this.getClass().getSimpleName(), "onCreate: Cancel selected.");
                onBackPressed();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTransfer();
            }
        });

        Log.d(this.getClass().getSimpleName(), "onCreate: Initializing finished");
    }

    /**
     * initValues populates the UI components.
     */
    private void initValues() {
        Log.i(this.getClass().getSimpleName(), "initValues: Initializing values.");

        // Initializing all objects from fragment_transfer
        tvFromAccountNo.setText(Integer.toString(transferFundsVO.getFromAccount().getAccNo()));
        tvFromCustomerNo.setText(Integer.toString(transferFundsVO.getFromAccount().getCustNo()));
        tvToAccountNo.setText(Integer.toString(transferFundsVO.getToAccount().getAccNo()));
        tvToCustomerNo.setText(Integer.toString(transferFundsVO.getToAccount().getCustNo()));
        tvTransferDetails.setText(transferFundsVO.getTransferDetails());
        tvTransferAmount.setText(Float.toString(transferFundsVO.getTransferAmount()));
    }

    /**
     * onClickTransfer handles tasks to be performed when user clicks Transfer button
     */
    private void onClickTransfer() {
        Log.d(this.getClass().getSimpleName(), "onClickTransfer: Transferring funds.");

        // Creating an instance of TransferTask
        TransferTask transferTask = new TransferTask(this, commonVO.getServerAddress(),
                getString(R.string.transfer_funds_path), transferFundsVO);
        transferTask.execute();
    }

    /**
     * TransferTask extends PostAsyncCommonTask to asynchronously communicate with the
     * server and perform post to transfer funds for the user.
     */
    private class TransferTask extends PostAsyncCommonTask<TransferFundsVO> {

        /**
         * TransferTask is the parametrized constructor of TransferTask
         *
         * @param contextObj    Contains the context of the parent activity.
         * @param serverAddress Contains the server url/address .
         * @param path          Contains the sub-path to the service that needs to be used.
         * @param objToBeSent   Object of the VO class being sent to the server
         */
        public TransferTask(Context contextObj, String serverAddress,
                            String path, TransferFundsVO objToBeSent) {
            super(contextObj, serverAddress, path, objToBeSent, TransferFundsVO.class);
        }

        /**
         * postSuccess is called when the server responds with a non-error code response.
         * This function performs all the tasks to be done in postExecute when server response
         * is not an error.
         *
         * @param amountTransferred Contains the string sent from the server as part of the
         *                          response.
         */
        @Override
        protected void postSuccess(String amountTransferred) {
            super.postSuccess(amountTransferred);

            Log.d(this.getClass().getSimpleName(),
                    "postSuccess: Response from server: " + amountTransferred);

            // Handles the case when transfer could not take place
            if (amountTransferred.equals("false")) {
                Toast.makeText(TransferActivity.this.getApplicationContext(),
                        getString(R.string.transfer_unsuccessful), Toast.LENGTH_LONG).show();
            } else {
                // Case when transfer is successful
                if (amountTransferred.equals("true")) {
                    Toast.makeText(TransferActivity.this.getApplicationContext(),
                            getString(R.string.transfer_successful), Toast.LENGTH_LONG).show();

                    // Creating an intent to be injecting in the notification
                    Intent intentNew = new Intent(TransferActivity.this.getApplicationContext(),
                            HomePageActivity.class);
                    intentNew.putExtra(getString(R.string.common_VO), commonVO);
                    intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    PendingIntent pIntent = PendingIntent.getActivity(
                            TransferActivity.this.getApplicationContext(),
                            (int) System.currentTimeMillis(), intentNew, 0);

                    // Notification message
                    String transferMessage = "Amount Transferred from Acc No "
                            + tvFromAccountNo.getText() + " to Acc No "
                            + tvToAccountNo.getText();

                    // Creating a notification
                    Notification n = new Notification.Builder(getApplicationContext())
                            .setContentTitle(getString(R.string.transfer_successful))
                            .setContentText(transferMessage)
                            .setSmallIcon(R.drawable.ic_stat_transfer)
                            .setStyle(new Notification.BigTextStyle().bigText(transferMessage))
                            .setContentIntent(pIntent)
                            .setAutoCancel(true).build();

                    NotificationManager notificationManager =
                            (NotificationManager) getApplicationContext()
                                    .getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, n);

                    // Transferring control to HomePageActivity
                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                    intent.putExtra(getString(R.string.common_VO), commonVO);
                    startActivity(intent);

                    // Terminating TransferActivity
                    TransferActivity.this.finish();
                } else {
                    Log.e(this.getClass().getSimpleName(),
                            "postSuccess: Invalid response on transfer funds");
                }
            }
        }
    }
}
