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
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.LoginLockoutVO;
import com.cigital.insecurepay.VOs.LoginVO;
import com.cigital.insecurepay.VOs.LoginValidationVO;
import com.cigital.insecurepay.common.Connectivity;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.net.CookieHandler;
import java.net.CookieManager;
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
    public static Connectivity connectivityObj;
    private final Context context = this;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask authTask = null;
    // UI references.
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private View progressView;
    private View loginFormView;
    private SharedPreferences loginPreferences;
    private CheckBox rememberMeCheck;
    private String userAddress;
    private String userPath;
    private Gson gson = new Gson();
    private Intent intent;
    private CommonVO commonVO = new CommonVO();
    // Initializing cookieManager
    private CookieManager cookieManager = new CookieManager();
//    static final String COOKIES_HEADER = "CookieID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userAddress = getString(R.string.default_address);
        userPath = getString(R.string.default_path);

        commonVO.setServerAddress(userAddress + userPath);
/*
        // Creating a new Connectivity object in commonVO
        commonVO.setConnectivityObj(new Connectivity(commonVO.getServerAddress()));
        // Setting application context and login path
        commonVO.getConnectivityObj().setConnectionParameters(getApplicationContext(), getString(R.string.login_path));
*/
        connectivityObj = new Connectivity(commonVO.getServerAddress());
        connectivityObj.setConnectionParameters(getApplicationContext(), getString(R.string.login_path));

        CookieHandler.setDefault(cookieManager);

        // Set up the login form.
        usernameView = (AutoCompleteTextView) findViewById(R.id.username);

        TextView forgotPasswordView = (TextView) findViewById(R.id.btn_forgot_password);
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
                intent = new Intent(LoginActivity.this.getApplicationContext(), ForgotPassword.class);
                intent.putExtra(getString(R.string.common_VO), commonVO);
                startActivity(intent);
            }
        });

        Button mUsernameSignInButton = (Button) findViewById(R.id.sign_in_button);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        rememberMeCheck = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
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
        if (authTask != null) {
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            authTask = new UserLoginTask(username, password);
            authTask.execute();
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

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

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    public class UserLoginTask extends AsyncTask<String, String, LoginLockoutVO> {

        private final String username;
        private final String password;

        UserLoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected LoginLockoutVO doInBackground(String... params) {
            // TODO: attempt authentication against a network service.
            Log.d(this.getClass().getSimpleName(), "In background, validating user credentials");
            LoginValidationVO loginValidationVO;
            LoginLockoutVO lockoutVO = new LoginLockoutVO();
            try {
                //Check after account lockout
                LoginDBHelper db = new LoginDBHelper(LoginActivity.this);


                lockoutVO.setTrialTime(db.getTimestamp(username));
                lockoutVO.setIsLocked(false);

                if (lockoutVO.getTrialTime() == null) {
                    lockoutVO.setAddUser(true);
                } else {
                    Log.d(this.getClass().getSimpleName(), lockoutVO.getTrialTime().toString());
                }

                if (!(lockoutVO.isAddUser() || Minutes.minutesBetween(DateTime.now(), lockoutVO.getTrialTime()).getMinutes() > Integer.parseInt(getString(R.string.account_lockout_duration)))) {
                    lockoutVO.setIsLocked(db.isLocked(username));
                }

                if (!lockoutVO.isLocked()) {
                    Log.d(this.getClass().getSimpleName(), "Sending credentials");
                    //Parameters contain credentials which are capsuled to LoginVO objects
                    LoginVO sendVo = new LoginVO(username, password);

                    //sendToServer contains JSON object that has credentials
                    String sendToServer = gson.toJson(sendVo);
/*                    commonVO.getConnectivityObj().setSendToServer(sendToServer);
                    //Call post and since there are white spaces in the response, trim is called
                    String responseFromServer = commonVO.getConnectivityObj().post().trim();
                    */

                    connectivityObj.setSendToServer(sendToServer);
                    //Call post and since there are white spaces in the response, trim is called
                    String responseFromServer = connectivityObj.post().trim();
                    Log.d(this.getClass().getSimpleName(), responseFromServer);
                    //Convert serverResponse to respectiveVO
                    loginValidationVO = gson.fromJson(responseFromServer, LoginValidationVO.class);

                    lockoutVO.setLoginValidationVO(loginValidationVO);

                    if (!lockoutVO.getLoginValidationVO().isUsernameExists())
                        return lockoutVO;

                    if (lockoutVO.getLoginValidationVO().isValidUser()) {
                        // delete row
                        db.deleteTrial(username);
                    } else {

                        if (lockoutVO.isAddUser()) {
                            lockoutVO.setTrialCount(1);
                            db.addTrial(username);
                        } else {
                            lockoutVO.setTrialCount(db.getTrial(username) + 1);
                            if (lockoutVO.getTrialCount() > 3)
                                lockoutVO.setIsLocked(true);
                            db.updateTrial(username, lockoutVO.getTrialCount(), lockoutVO.isLocked());
                        }


                    }


                }

            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "err", e);
                return lockoutVO;
            }
            return lockoutVO;
        }

        @Override
        protected void onPostExecute(final LoginLockoutVO lockoutVO) {
            authTask = null;
            showProgress(false);


            if (lockoutVO.isLocked()) {
                Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_failed_account_locked), Toast.LENGTH_LONG).show();
            } else if (!lockoutVO.getLoginValidationVO().isUsernameExists()) {
                usernameView.setError(getString(R.string.error_username_does_not_exist));
                usernameView.requestFocus();
            } else if (lockoutVO.getLoginValidationVO().isValidUser()) {
                try {
                    Log.d(this.getClass().getSimpleName(), "Move to next activity");
                    Log.d("REMOVE ME", cookieManager.getCookieStore().getCookies().toString());
                    // Move to Home Page if successful login
                    Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.login_successful), Toast.LENGTH_LONG).show();
                    intent = new Intent(getApplicationContext(), HomePage.class);
                    commonVO.setUsername(username);
                    commonVO.setCustNo(lockoutVO.getLoginValidationVO().getCustNo());
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

        @Override
        protected void onCancelled() {
            authTask = null;
            showProgress(false);
        }

    }

}



