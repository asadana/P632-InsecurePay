package com.cigital.insecurepay.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.ForgotPasswordVO;
import com.cigital.insecurepay.VOs.LoginValidationVO;
import com.cigital.insecurepay.common.Connectivity;
import com.google.gson.Gson;

public class ForgotPassword extends AppCompatActivity {

    private EditText accountNoView;
    private EditText textSSNNoView;
    private EditText usernameView;
    private ForgotPasswordTask forgotPassTask = null;
    protected Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    forgotPassTask = new ForgotPasswordTask(Integer.parseInt(accountNo), sSNNo, username);
                    forgotPassTask.execute(accountNo, sSNNo, username);
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

    public class ForgotPasswordTask extends AsyncTask<String, String, LoginValidationVO> {

        private final int accountNo;
        private final String sSNNo;
        private final String username;

        ForgotPasswordTask(int accountNo, String sSNNo, String username) {
            this.accountNo = accountNo;
            this.sSNNo = sSNNo;
            this.username = username;
        }

        @Override
        protected LoginValidationVO doInBackground(String... params) {
            // TODO: attempt authentication against a network service.
            Log.d(this.getClass().getSimpleName(), "In background, validating user credentials");

            LoginValidationVO loginValidationVO = null;
            try {
                //Parameters contain credentials which are capsuled to ForgotPasswordVO objects
                ForgotPasswordVO sendVo = new ForgotPasswordVO(accountNo, sSNNo, username);
                //sendToServer contains JSON object that has credentials
                String sendToServer = gson.toJson(sendVo);
                //Passing the context of LoginActivity to Connectivity
                Connectivity con_login = new Connectivity(ForgotPassword.this.getApplicationContext(), getString(R.string.forgot_password_path), ((CommonVO) getIntent().getSerializableExtra(getString(R.string.common_VO))).getServerAddress(), sendToServer);
                //Call post and since there are white spaces in the response, trim is called
                String responseFromServer = con_login.post().trim();
                //Convert serverResponse to respectiveVO

                loginValidationVO = gson.fromJson(responseFromServer, LoginValidationVO.class);

                Thread.sleep(2000);
                return loginValidationVO;

            } catch (Exception e) {
                return loginValidationVO;
            }
        }

        @Override
        protected void onPostExecute(final LoginValidationVO loginValidationVO) {

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
