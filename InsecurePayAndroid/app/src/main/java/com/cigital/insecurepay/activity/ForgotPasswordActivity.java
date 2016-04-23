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

/**
 * ForgotPasswordActivity is an activity that allows the user to reset his/her password
 * by entering certain account information.
 * ForgotPasswordActivity extends {@link AppCompatActivity}.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    // UI Components
    private EditText etAccountNo;
    private EditText etSSN;
    private EditText etUsername;

    private CommonVO commonVOObj;
    private Gson gsonObj;

    private ForgotPasswordTask forgotPasswordTask = null;

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

        // Getting values from intent and initializing commonVOObj with it
        Bundle bundleObj = getIntent().getExtras();
        commonVOObj = (CommonVO) bundleObj.get(getString(R.string.common_VO));
        setContentView(R.layout.activity_forgot_password);

        gsonObj = new Gson();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // To allow Screenshots
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing UI components
        etAccountNo = (EditText) findViewById(R.id.etForgotPassword_AccountNo);
        etSSN = (EditText) findViewById(R.id.etForgotPassword_SSNNo);
        etUsername = (EditText) findViewById(R.id.etForgotPassword_username);

        // Initializing the button and attaching a listener
        Button mSendButton = (Button) findViewById(R.id.btn_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display Account No and SSN in log
                String accountNo = etAccountNo.getText().toString();
                String sSNNo = etSSN.getText().toString();
                String username = etUsername.getText().toString();
                boolean cancel = false;
                View focusView = null;

                // Check for a valid Account No and SSN, if the user entered one.
                if (TextUtils.isEmpty(accountNo) || TextUtils.isEmpty(username)) {
                    etAccountNo.setError(getString(R.string.error_field_required));
                    focusView = etAccountNo;
                    cancel = true;
                }
                if (TextUtils.isEmpty(sSNNo) || isSSNInvalid(sSNNo)) {
                    etSSN.setError(getString(R.string.error_invalid_field));
                    focusView = etSSN;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; focus the first form field with an error.
                    focusView.requestFocus();
                } else {
                    // Parameters contain credentials which are capsuled to ForgotPasswordVO objects
                    ForgotPasswordVO forgotPasswordVO =
                            new ForgotPasswordVO(Integer.parseInt(accountNo), sSNNo, username);

                    forgotPasswordTask = new ForgotPasswordTask(
                            ForgotPasswordActivity.this,
                            commonVOObj.getServerAddress(),
                            getString(R.string.forgot_password_path), forgotPasswordVO);
                    forgotPasswordTask.execute();
                }
            }
        });

        Log.d(this.getClass().getSimpleName(), "onCreate: Initializing finished");
    }

    /**
     * isSSNInvalid is a function that checks if ssn at least 4 in length
     */
    private boolean isSSNInvalid(String ssn) {
        return ssn.length() < 4;
    }

    /**
     * ForgotPasswordTask extends {@link PostAsyncCommonTask} to asynchronously communicate
     * with the server and perform post to reset password for the user.
     */
    public class ForgotPasswordTask extends PostAsyncCommonTask<ForgotPasswordVO> {

        /**
         * ForgotPasswordTask is the parametrized constructor of ForgotPasswordTask
         *
         * @param contextObj    Contains the context of the parent activity.
         * @param serverAddress Contains the server url/address .
         * @param path          Contains the sub-path to the service that needs to be used.
         * @param objToBeSent   Object of the VO class being sent to the server
         */
        public ForgotPasswordTask(Context contextObj, String serverAddress,
                                  String path, ForgotPasswordVO objToBeSent) {
            super(contextObj, serverAddress, path, objToBeSent, ForgotPasswordVO.class);
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
            LoginValidationVO loginValidationVO = null;

            try {
                Log.d(this.getClass().getSimpleName(),
                        "postSuccess: Response from server: " + resultObj);

                // Convert serverResponse to respectiveVO
                loginValidationVO = ForgotPasswordActivity.this.gsonObj.fromJson(
                        resultObj, LoginValidationVO.class);

            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "postSuccess: ", e);
            }

            if (loginValidationVO != null) {
                // If condition checks if the username used doesn't exist
                if (!loginValidationVO.isUsernameExists()) {
                    etUsername.setError(getString(R.string.error_username_does_not_exist));
                    etUsername.requestFocus();
                } else {
                    // If condition checks if the details entered were not a match
                    if (!loginValidationVO.isValidUser()) {
                        Toast.makeText(
                                ForgotPasswordActivity.this.getApplicationContext(),
                                getString(R.string.information_mismatch),
                                Toast.LENGTH_LONG
                        ).show();
                    } else {
                        Toast.makeText(
                                ForgotPasswordActivity.this.getApplicationContext(),
                                getString(R.string.default_password_link_sent),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            }
        }
    }
}
