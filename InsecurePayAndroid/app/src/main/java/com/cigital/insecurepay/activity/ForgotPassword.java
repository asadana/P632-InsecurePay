package com.cigital.insecurepay.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cigital.insecurepay.DBHelper.LoginDBHelper;
import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.LoginVO;
import com.cigital.insecurepay.VOs.LoginValidationVO;
import com.cigital.insecurepay.common.Connectivity;

public class ForgotPassword extends AppCompatActivity {

    private EditText mAccountNoView;
    private EditText mSSNNoView;
    //private ForgotPasswordTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAccountNoView = (EditText) findViewById(R.id.etxt_AccountNo);
        mSSNNoView = (EditText) findViewById(R.id.etxt_SSNNo);
        Button mSendButton = (Button) findViewById(R.id.btn_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display Account No and SSN in log
                String accountNo = mAccountNoView.getText().toString();
                String ssnNo = mSSNNoView.getText().toString();
                Log.d("SSN ", mSSNNoView.getText().toString());

                boolean cancel = false;
                View focusView = null;

                // Check for a valid Account No and SSN, if the user entered one.
                if (!TextUtils.isEmpty(accountNo)) {
                    mAccountNoView.setError(getString(R.string.error_field_required));
                    focusView = mAccountNoView;
                    cancel = true;
                }
                if (!TextUtils.isEmpty(ssnNo) && !isSSNValid(ssnNo)) {
                    mSSNNoView.setError(getString(R.string.error_invalid_field));
                    focusView = mSSNNoView;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; focus the first form field with an error.
                    focusView.requestFocus();
                } else {

                   // mAuthTask = new ForgotPasswordTask(accountNo, ssnNo, server_address);
                   // mAuthTask.ForgotPasswordTask(accountNo, ssnNo, server_address);
                }
            }
        });
    }

    private boolean isSSNValid(String ssn) {
        //TODO: Replace this with your own logic
        return ssn.length() > 4;
    }

    /**
     * Represents an asynchronous validation task used to authenticate
     * the user.
     */

}
