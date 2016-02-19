package com.cigital.insecurepay.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cigital.insecurepay.DBHelper.LoginDBHelper;
import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.LoginVO;
import com.cigital.insecurepay.VOs.LoginValidationVO;
import com.cigital.insecurepay.common.Connectivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via username,password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    final Context context = this;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private TextView mForgotPasswordView;
    private View mLoginFormView;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;
    private CheckBox mRememberMeCheck;

    private String userAddress;
    private String userPort;
    private String userPath;

    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        mForgotPasswordView=(TextView)findViewById(R.id.btn_forgot_password);
        populateAutoComplete();

        // Setting default values to userUrl
        userAddress = (getString(R.string.defaultAddress));
        userPort = (getString(R.string.defaultPort));
        userPath = (getString(R.string.defaultPath));

        mForgotPasswordView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this.getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mUsernameSignInButton = (Button) findViewById(R.id.sign_in_button);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display username and password in log
                Log.i("", "Username : " + mUsernameView.getText().toString());
                Log.i("", "Password : " + mPasswordView.getText().toString());
                // Log.i("Insecure Data Storage", "Server Address : " + mServerAddressView.getText().toString());
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        /*Fetch checkbox*/
        mRememberMeCheck = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        //if the flag was true then get username and password and display
        if (saveLogin) {
            mUsernameView.setText(loginPreferences.getString("username", ""));
            mPasswordView.setText(loginPreferences.getString("password", ""));
            mRememberMeCheck.setChecked(true);
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        //Store server address
        String server_address = userAddress + ":" + userPort + "" + userPath;


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //Store credentials if password Remember Me is true
            saveLoginPreferences();
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password, server_address);
            mAuthTask.execute(username, password, server_address);
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void saveLoginPreferences() {
        if (mRememberMeCheck.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", mUsernameView.getText().toString());
            loginPrefsEditor.putString("password", mPasswordView.getText().toString());
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }

    /**
     * Creates a menu option in the current activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    /**
     * Function triggered when any of the options in the menu is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemChange_server) {
            Log.i(this.getClass().getSimpleName(), "Url change selected");
            changeUrl();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a dialog for the user to enter the new server url
     */
    private void changeUrl() {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogView = layoutInflater.inflate(R.layout.dialog_change_url, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(dialogView);

        // String array to store different parts of the url server address
        final String[] userUrl = new String[3];

        // EditText variables to fetch user inputs from the dialog
        final EditText etUrlAddress = (EditText) dialogView.findViewById(R.id.etUrlAddress);
        final EditText etUrlPort = (EditText) dialogView.findViewById(R.id.etUrlPort);
        final EditText etUrlPath = (EditText) dialogView.findViewById(R.id.etUrlPath);

        Log.i("Server Address", "Initial address: " + userAddress + ":" + userPort + "" + userPath);

        // When OK is clicked
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                userUrl[0] = etUrlAddress.getText().toString();
                userUrl[1] = etUrlPort.getText().toString();
                userUrl[2] = etUrlPath.getText().toString();

                if (!userUrl[0].isEmpty()) {
                    userAddress = userUrl[0];
                }
                if (!userUrl[1].isEmpty()) {
                    userPort = userUrl[1];
                }
                if (!userUrl[2].isEmpty()) {
                    userPath = userUrl[2];
                }

                Log.i("Server Address Update", "Storing address: " + userAddress + ":" + userPort + "" + userPath);
            }
            // When Cancel is clicked
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    public class UserLoginTask extends AsyncTask<String, String, LoginValidationVO> {

        private final String mUsername;
        private final String mPassword;
        private final String mServerAddress;

        UserLoginTask(String username, String password, String serverAddress) {
            mUsername = username;
            mPassword = password;
            mServerAddress = serverAddress;
        }

        @Override
        protected LoginValidationVO doInBackground(String... params) {
            // TODO: attempt authentication against a network service.
            Log.d(this.getClass().getSimpleName(), "In background, validating user credentials");
            LoginValidationVO loginValidationVO = null;
            try {
                //Check after account lockout
                LoginDBHelper db = new LoginDBHelper(LoginActivity.this);
                int lock = db.isLocked(mUsername);
                if (lock == 1) {
                    Long trialTime = db.getTimestamp(mUsername);
                    long currTime = System.currentTimeMillis();
                    long timeDiff = currTime - trialTime;
                    if (timeDiff > 60000) {
                        lock = 0;
                        db.resetTrial(mUsername);
                    }
                }

                if (lock == 0) {
                    Log.d(this.getClass().getSimpleName(), "Sending credentials");
                    //Parameters contain credentials which are capsuled to LoginVO objects
                    LoginVO sendVo = new LoginVO(mUsername, mPassword);

                    //sendToServer contains JSON object that has credentials
                    String sendToServer = gson.toJson(sendVo);
                    //Passing the context of LoginActivity to Connectivity
                    Connectivity con_login = new Connectivity(LoginActivity.this.getApplicationContext(), getString(R.string.login_path), mServerAddress, sendToServer);
                    //Call post and since there are white spaces in the response, trim is called
                    String responseFromServer = con_login.post().trim();
                    //Convert serverResponse to respectiveVO
                    loginValidationVO = gson.fromJson(responseFromServer, LoginValidationVO.class);
                    //If the user is a valid user. Call customer service to get the user which is to be displayed in the next activity

                    Thread.sleep(2000);

                }
            } catch (Exception e) {
                return loginValidationVO;
            }
            return loginValidationVO;
        }

        @Override
        protected void onPostExecute(final LoginValidationVO loginValidationVO) {
            mAuthTask = null;
            showProgress(false);

            // If login successful reset the trials if exists any
            LoginDBHelper db = new LoginDBHelper(LoginActivity.this);
            int trial = db.getTrial(mUsername);
            int lock = db.isLocked(mUsername);

            if (loginValidationVO.isValidUser()) {
                Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_successful), Toast.LENGTH_LONG).show();
                if (trial != -1) {
                    db.updateTrial(mUsername, 0);
                }
                try {
                    Log.d(this.getClass().getSimpleName(), "Move to next activity");
                    // Move to Home Page if successful login
                    Intent intent = new Intent(LoginActivity.this.getApplicationContext(), HomePage.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(this.getClass().getSimpleName(), "Exception ", e);
                }
            } else {
                if (trial == -1 && loginValidationVO.isUsernameExists()) {
                    db.addTrial(mUsername, 1);
                } else {
                    if (loginValidationVO.isUsernameExists())
                        db.updateTrial(mUsername, trial + 1);
                }
                    /*
                    Update trial to database if login failed.
                    Account Lockout if number of trials exceeds or equals to 3
                    */
                if (trial + 1 == 3)
                    Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_failed_account_locked), Toast.LENGTH_LONG).show();
                if (lock == 1)
                    Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_failed_still_account_locked), Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                }

                if (!loginValidationVO.isUsernameExists()) {
                    mUsernameView.setError("Username does not exist");
                    mUsernameView.requestFocus();
                }
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

}



