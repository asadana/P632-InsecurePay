package com.cigital.insecurepay.activity;

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
import android.view.WindowManager;
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
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.LoginLockoutVO;
import com.cigital.insecurepay.VOs.LoginVO;
import com.cigital.insecurepay.VOs.LoginValidationVO;
import com.cigital.insecurepay.common.PostAsyncCommonTask;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

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
    private final Context context = this;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask userLoginTask = null;
    // UI references.
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private SharedPreferences loginPreferences;
    private CheckBox rememberMeCheck;
    private String userAddress;
    private String userPath;
    private Gson gson = new Gson();
    private Intent intent;
    private CommonVO commonVO;
    private LoginLockoutVO lockoutVO;
    private LoginVO loginVOObj;
    private LoginDBHelper loginDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // To allow Screenshots
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        // To disable screenshots in this activity
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        userAddress = getString(R.string.default_address);
        userPath = getString(R.string.default_path);

        commonVO = new CommonVO();
        commonVO.setServerAddress(userAddress + userPath);

        // Set up the login form.
        usernameView = (AutoCompleteTextView) findViewById(R.id.username);

        TextView forgotPasswordView = (TextView) findViewById(R.id.btnForgotPassword);
        populateAutoComplete();

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        forgotPasswordView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(this.getClass().getSimpleName(), "forgot password view");
                intent = new Intent(LoginActivity.this.getApplicationContext(), ForgotPasswordActivity.class);
                intent.putExtra(getString(R.string.common_VO), commonVO);
                startActivity(intent);
            }
        });

        Button mUsernameSignInButton = (Button) findViewById(R.id.btnSignIn);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        rememberMeCheck = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences(getString(R.string.sharedPreferenceLogin), MODE_PRIVATE);
        boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);
        //if the flag was true then get username and password and display
        if (saveLogin) {
            usernameView.setText(loginPreferences.getString(getString(R.string.username), ""));
            passwordView.setText(loginPreferences.getString(getString(R.string.password), ""));
            rememberMeCheck.setChecked(true);
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
            Snackbar.make(usernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
        if (userLoginTask != null) {
            return;
        }

        // Display username and password in log
        Log.i(this.getClass().getSimpleName(), "Username : " + usernameView.getText().toString());
        Log.i(this.getClass().getSimpleName(), "Password : " + passwordView.getText().toString());

        // Reset errors.
        usernameView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //Store credentials if password Remember Me is true
            saveLoginPreferences();
            lockoutVO = new LoginLockoutVO();
            try {
                //Check after account lockout
                loginDBHelper = new LoginDBHelper(LoginActivity.this);

                lockoutVO.setTrialTime(loginDBHelper.getTimestamp(username));
                lockoutVO.setIsLocked(false);

                if (lockoutVO.getTrialTime() == null) {
                    lockoutVO.setAddUser(true);
                } else {
                    Log.d(this.getClass().getSimpleName(), lockoutVO.getTrialTime().toString());
                    Log.d(this.getClass().getSimpleName(), DateTime.now().toString());
                    Log.d(this.getClass().getSimpleName(), Minutes.minutesBetween(lockoutVO.getTrialTime(), DateTime.now()).getMinutes() + "");
                }

                if (!lockoutVO.isAddUser()) {
                    lockoutVO.setIsLocked(loginDBHelper.isLocked(username));
                    if (Minutes.minutesBetween(lockoutVO.getTrialTime(), DateTime.now()).getMinutes() > Integer.parseInt(getString(R.string.account_lockout_duration)) && lockoutVO.isLocked()) {
                        loginDBHelper.deleteTrial(username);
                        lockoutVO.setAddUser(true);
                        lockoutVO.setIsLocked(false);
                    }
                }

                Log.d(this.getClass().getSimpleName(), Boolean.toString(lockoutVO.isLocked()));

                if (!lockoutVO.isLocked()) {
                    //Parameters contain credentials which are capsuled to LoginVO objects
                    loginVOObj = new LoginVO(username, password);
                    // perform the user login attempt.
                    userLoginTask = new UserLoginTask(this, commonVO.getServerAddress(),
                            getString(R.string.login_path), loginVOObj);
                    userLoginTask.execute();
                } else {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_failed_account_locked), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "attemptLogin: ", e);
            }
        }
    }

    /*
    Checks if the password length is at least 2
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    /*
    Stores the username and password if Remember Me is checked
     */
    private void saveLoginPreferences() {
        SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
        if (rememberMeCheck.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", usernameView.getText().toString());
            loginPrefsEditor.putString("password", passwordView.getText().toString());
            loginPrefsEditor.apply();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.apply();
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

        usernameView.setAdapter(adapter);
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
        alertDialogBuilder.setTitle(R.string.prompt_server_addr);

        // EditText variables to fetch user inputs from the dialog
        final EditText etUrlAddress = (EditText) dialogView.findViewById(R.id.etUrlAddress);
        final EditText etUrlPath = (EditText) dialogView.findViewById(R.id.etUrlPath);

        Log.i(this.getClass().getSimpleName(), "Initial address: " + userAddress + userPath);

        // When OK is clicked
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (!etUrlAddress.getText().toString().isEmpty()) {
                    userAddress = etUrlAddress.getText().toString();
                }
                if (!etUrlPath.getText().toString().isEmpty()) {
                    userPath = etUrlPath.getText().toString();
                }
                commonVO.setServerAddress(userAddress + userPath);
                Log.i(this.getClass().getSimpleName(), "Storing address: " + userAddress + userPath);
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

    public class UserLoginTask extends PostAsyncCommonTask<LoginVO> {


        public UserLoginTask(Context contextObj, String serverAddress, String path, LoginVO objToBeSent) {
            super(contextObj, serverAddress, path, objToBeSent, LoginVO.class);
            Log.d(this.getClass().getSimpleName(), "Sending credentials");
        }

        @Override
        protected void onCancelled() {
            userLoginTask = null;
        }

        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            Log.d(this.getClass().getSimpleName(), "postSuccess: " + resultObj);
            //Convert serverResponse to respectiveVO
            LoginValidationVO loginValidationVO = gson.fromJson(resultObj, LoginValidationVO.class);

            lockoutVO.setLoginValidationVO(loginValidationVO);

            if (lockoutVO.getLoginValidationVO().isUsernameExists()) {
                if (!lockoutVO.getLoginValidationVO().isValidUser()) {
                    if (lockoutVO.isAddUser()) {
                        lockoutVO.setTrialCount(1);
                        loginDBHelper.addTrial(loginVOObj.getUsername());
                    } else {
                        lockoutVO.setTrialCount(loginDBHelper.getTrial(loginVOObj.getUsername()) + 1);
                        Log.d(this.getClass().getSimpleName(), "Trials " + lockoutVO.getTrialCount());
                        if (lockoutVO.getTrialCount() > 3)
                            lockoutVO.setIsLocked(true);
                        loginDBHelper.updateTrial(loginVOObj.getUsername(), lockoutVO.getTrialCount(), lockoutVO.isLocked());
                    }
                }
            }


            userLoginTask = null;

            Log.d(this.getClass().getSimpleName(), Boolean.toString(lockoutVO.isLocked()));

            if (lockoutVO.isLocked()) {
                Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_failed_account_locked), Toast.LENGTH_LONG).show();
            } else if (!lockoutVO.getLoginValidationVO().isUsernameExists()) {
                usernameView.setError(getString(R.string.error_username_does_not_exist));
                usernameView.requestFocus();
            } else if (lockoutVO.getLoginValidationVO().isValidUser()) {
                try {
                    // Move to Home Page if successful login
                    Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_successful), Toast.LENGTH_LONG).show();
                    intent = new Intent(getApplicationContext(), HomePageActivity.class);
                    commonVO.setUsername(loginVOObj.getUsername());
                    commonVO.setCustomerNumber(lockoutVO.getLoginValidationVO().getCustomerNumber());
                    intent.putExtra(getString(R.string.common_VO), commonVO);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(this.getClass().getSimpleName(), "Exception ", e);
                }
            } else {
                Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                passwordView.setError(getString(R.string.error_incorrect_password));
            }
        }
    }
}






