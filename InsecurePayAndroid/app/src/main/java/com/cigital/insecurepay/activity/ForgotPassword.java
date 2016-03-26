package com.cigital.insecurepay.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.ForgotPasswordVO;
import com.cigital.insecurepay.VOs.LoginValidationVO;
import com.cigital.insecurepay.common.PostAsyncCommonTask;
import com.google.gson.Gson;

public class ForgotPassword extends AppCompatActivity {

    private Gson gson = new Gson();
    private EditText accountNoView;
    private EditText textSSNNoView;
    private EditText usernameView;
    private ForgotPasswordTask forgotPassTask = null;
    private CommonVO commonVOObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleObj = getIntent().getExtras();
        commonVOObj = (CommonVO) bundleObj.get(getString(R.string.common_VO));
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // To allow Screenshots
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accountNoView = (EditText) findViewById(R.id.etForgotPassword_AccountNo);
        textSSNNoView = (EditText) findViewById(R.id.etForgotPassword_SSNNo);
        usernameView = (EditText) findViewById(R.id.etForgotPassword_username);
        Button mSendButton = (Button) findViewById(R.id.btn_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display Account No and SSN in log
                String accountNo = accountNoView.getText().toString();
                String sSNNo = textSSNNoView.getText().toString();
                String username = usernameView.getText().toString();
                boolean cancel = false;
                View focusView = null;

                // Check for a valid Account No and SSN, if the user entered one.
                if (TextUtils.isEmpty(accountNo) || TextUtils.isEmpty(username)) {
                    accountNoView.setError(getString(R.string.error_field_required));
                    focusView = accountNoView;
                    cancel = true;
                }
                if (TextUtils.isEmpty(sSNNo) || isSSNInvalid(sSNNo)) {
                    textSSNNoView.setError(getString(R.string.error_invalid_field));
                    focusView = textSSNNoView;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; focus the first form field with an error.
                    focusView.requestFocus();
                } else {
                    // Parameters contain credentials which are capsuled to ForgotPasswordVO objects
                    ForgotPasswordVO forgotPasswordVO = new ForgotPasswordVO(Integer.parseInt(accountNo), sSNNo, username);
                    forgotPassTask = new ForgotPasswordTask(ForgotPassword.this, commonVOObj.getServerAddress(),
                            getString(R.string.forgot_password_path), forgotPasswordVO);
                    forgotPassTask.execute();
                }
            }
        });
    }


    private boolean isSSNInvalid(String ssn) {
        return ssn.length() < 4;
    }

    /**
     * Represents an asynchronous validation task used to authenticate
     * the user.
     */
    public class ForgotPasswordTask extends PostAsyncCommonTask<ForgotPasswordVO> {

        public ForgotPasswordTask(Context contextObj, String serverAddress, String path, ForgotPasswordVO objToBeSent) {
            super(contextObj, serverAddress, path, objToBeSent, ForgotPasswordVO.class);
        }

        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            LoginValidationVO loginValidationVO = null;
            try {
                Log.d("REMOVE ME: ", "doInBackground: " + resultObj);
                // Convert serverResponse to respectiveVO
                loginValidationVO = gson.fromJson(resultObj, LoginValidationVO.class);

            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "postSuccess: ", e);
            }
            if (loginValidationVO != null) {
                if (!loginValidationVO.isUsernameExists()) {
                    usernameView.setError(getString(R.string.error_username_does_not_exist));
                    usernameView.requestFocus();
                } else {
                    if (!loginValidationVO.isValidUser()) {
                        Toast.makeText(ForgotPassword.this.getApplicationContext(), getString(R.string.information_mismatch), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPassword.this.getApplicationContext(), getString(R.string.default_password_link_sent), Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }
}
