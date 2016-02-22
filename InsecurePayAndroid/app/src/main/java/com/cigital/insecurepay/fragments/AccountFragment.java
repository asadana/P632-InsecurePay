package com.cigital.insecurepay.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cigital.insecurepay.DBHelper.LoginDBHelper;
import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.ChangePasswordVO;
import com.cigital.insecurepay.VOs.CustomerVO;
import com.cigital.insecurepay.common.Connectivity;
import com.google.gson.Gson;

/**
 * AccountFragment extends Fragment and is used to display and handle Account Management
 * related view operations.
 */
public class AccountFragment extends Fragment {

    TextView tvName;
    TextView tvAccountNumber;
    TextView tvSSN;
    TextView tvUserDOB;
    EditText etEmail;
    EditText etPhone;
    EditText etAddress;
    Button btnUpdateInfo;
    Button btnChangePassword;

    private String sUserName = "foo";
    private AccountFetchTask accountFetchTask = null;
    private CustomerVO customerVOObj;

    private Gson gson = new Gson();
    private String serverAddress;

    private OnFragmentInteractionListener mListener;
    private TextWatcher twEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Email Address value changed.");
        }
    };
    private TextWatcher twAddress = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Address value changed.");
        }
    };
    private TextWatcher twPhone = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Phone number value changed.");
        }
    };

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String serverAddress) {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewObj = inflater.inflate(R.layout.fragment_account, container, false);

        // Initializing all objects from fragment_account
        tvName = (TextView) viewObj.findViewById(R.id.tvAccount_fillName);
        tvAccountNumber = (TextView) viewObj.findViewById(R.id.tvAccount_fillAccountNumber);
        tvSSN = (TextView) viewObj.findViewById(R.id.tvAccount_fillSSN);
        tvUserDOB = (TextView) viewObj.findViewById(R.id.tvAccount_fillUserDOB);

        etEmail = (EditText) viewObj.findViewById(R.id.etAccount_fillEmail);
        etPhone = (EditText) viewObj.findViewById(R.id.etAccount_fillPhone);
        etAddress = (EditText) viewObj.findViewById(R.id.etAccount_fillAddress);

        btnUpdateInfo = (Button) viewObj.findViewById(R.id.btnAccount_update);
        btnChangePassword = (Button) viewObj.findViewById(R.id.btnAccount_changePassword);

        serverAddress = this.getArguments().getString("serverAddress");

        initValues();
        addListeners();

        return viewObj;
    }

    private void initValues() {

        accountFetchTask = new AccountFetchTask(sUserName);
        accountFetchTask.execute();
    }

    private void addListeners() {
        etEmail.addTextChangedListener(twEmail);
        etAddress.addTextChangedListener(twAddress);
        etPhone.addTextChangedListener(twPhone);
        twPhone = new PhoneNumberFormattingTextWatcher();

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateInformation();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChangePassword();
            }
        });
    }

    private void onClickUpdateInformation() {
        Log.i(this.getClass().getSimpleName(), "Updating customer information.");
        AccountUpdateTask accountUpdateTask = new AccountUpdateTask(
                tvUserDOB.toString(),
                etEmail.toString(),
                etAddress.toString(),
                etPhone.toString());
        accountUpdateTask.execute();
    }

    // TextWatchers for editText fields

    private void onClickChangePassword() {
        Log.i(this.getClass().getSimpleName(), "Displaying change password dialog");
        changePassword();
        //return true;
    }

    private void changePassword() {

        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        View dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton("OK", null);
        alertDialogBuilder.setNegativeButton("Cancel", null);

        final EditText password1View;
        final EditText password2View;

        // EditText variables to fetch user inputs from the dialog
        password1View = (EditText) dialogView.findViewById(R.id.et_password_field1);
        password2View = (EditText) dialogView.findViewById(R.id.et_password_field2);


        final AlertDialog alertD = alertDialogBuilder.create();

        alertD.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertD.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password_field1 = password1View.getText().toString();
                        String password_field2 = password2View.getText().toString();
                        //ChangePasswordTask changePasswordTask;
                        boolean cancel = false;
                        View focusView = null;
                        Log.d("Password 1 ", password_field1);
                        Log.d("Password 2 ", password_field2);

                        if (TextUtils.isEmpty(password_field1)) {
                            password1View.setError(getString(R.string.error_field_required));
                            return;
                        }

                        if ((password_field1.length() < 3) || (password_field2.length() < 3)) {
                            password2View.setError("Minimum 3 characters length required");
                            password1View.setText("");
                            password2View.setText("");
                            return;
                        }

                        if (TextUtils.isEmpty(password_field2)) {
                            password2View.setError(getString(R.string.error_invalid_field));
                            return;
                        }

                        if (password_field1.equals(password_field2)) {
                            // changePasswordTask = new ChangePasswordTask("foo",password_field1);
                            // changePasswordTask.execute("foo", password_field1);
                            Toast.makeText(AccountFragment.this.getContext(), "Password match", Toast.LENGTH_LONG).show();

                        } else {
                            password1View.setText("");
                            password2View.setText("");
                            Toast.makeText(AccountFragment.this.getContext(), "Password mismatch", Toast.LENGTH_LONG).show();
                        }

                        alertD.dismiss();
                    }
                });
                alertD.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertD.dismiss();
                    }
                });
            }
        });

        alertD.show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class AccountFetchTask extends AsyncTask<String, String, CustomerVO> {

        private String userName;

        public AccountFetchTask(String user) {
            userName = user;
        }

        @Override
        protected CustomerVO doInBackground(String... params) {

            // Creating dbHelper with current context
            LoginDBHelper dbHelper = new LoginDBHelper(getContext());
            // Creating a new CustomerVO Object
            customerVOObj = new CustomerVO();
            String sendToServer = gson.toJson(customerVOObj);

            // contentValues to to store the parameter used to fetch the values
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", userName);

            // Establishing connection to the server
            Connectivity getInfoConnection = new Connectivity(getContext(), getString(R.string.cust_details_path), serverAddress, sendToServer);
            // Stroing server response
            String responseFromServer = getInfoConnection.get(contentValues);

            Log.i(this.getClass().getSimpleName(), "Account information retrieved successfully");

            // Storing server response in the customerVOObj
            customerVOObj = gson.fromJson(responseFromServer, CustomerVO.class);

            return customerVOObj;
        }

        @Override
        protected void onPostExecute(final CustomerVO customerVOObj) {
            Log.d(this.getClass().getSimpleName(), "In post execute. Updating view.");

            tvName.setText(customerVOObj.getCustName());
            tvAccountNumber.setText(Integer.toString(customerVOObj.getCustNo()));
            tvSSN.setText(Integer.toString(customerVOObj.getSsn()));
            tvUserDOB.setText(customerVOObj.getBirthDate().toString());
            etEmail.setText(customerVOObj.getEmail(), TextView.BufferType.EDITABLE);
            etAddress.setText(customerVOObj.getStreet() + ", " +
                    customerVOObj.getCity() + ", " + customerVOObj.getState() + "" +
                    ", " + Integer.toString(customerVOObj.getZipcode()), TextView.BufferType.EDITABLE);
            etPhone.setText(Integer.toString(customerVOObj.getPhoneNo()), TextView.BufferType.EDITABLE);
        }
    }

    private class AccountUpdateTask extends AsyncTask<String, String, CustomerVO> {
        private String userDOB;
        private String userEmail;
        private String userAddress;
        private String Phone;

        public AccountUpdateTask(String userDOB, String userEmail, String userAddress, String Phone) {
            this.userDOB = userDOB;
            this.userEmail = userEmail;
            this.userAddress = userAddress;


        }

        @Override
        protected CustomerVO doInBackground(String... params) {

            // Creating dbHelper with current context
            LoginDBHelper dbHelper = new LoginDBHelper(getContext());
            String sendToServer = gson.toJson(customerVOObj);
            // Establishing connection to the server
            Connectivity getInfoConnection = new Connectivity(getContext(), getString(R.string.cust_details_path), serverAddress, sendToServer);
            // Stroing server response
            String responseFromServer = getInfoConnection.post().trim();

            System.out.println(responseFromServer);

            return customerVOObj;
        }

        @Override
        protected void onPostExecute(final CustomerVO customerVOObj) {

        }
    }

    public class ChangePasswordTask extends AsyncTask<String, String, String> {

        private final String username;
        private final String password;

        ChangePasswordTask(String username, String password) {
            this.password = password;
            this.username = username;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.
            Log.d(this.getClass().getSimpleName(), "In background, validating user credentials");
            String password_changed = null;
            try {
                LoginDBHelper db = new LoginDBHelper(AccountFragment.this.getContext());
                //Parameters contain credentials which are capsuled to ChangePasswordVO objects
                ChangePasswordVO sendVo = new ChangePasswordVO(username, password);
                //sendToServer contains JSON object that has credentials
                String sendToServer = gson.toJson(sendVo);
                //Passing the context of LoginActivity to Connectivity
                Connectivity con_login = new Connectivity(AccountFragment.this.getContext(), getString(R.string.change_password_path), serverAddress, sendToServer);
                //Call post and since there are white spaces in the response, trim is called
                password_changed = con_login.post().trim();
                Log.d("Response from server", password_changed);
                Thread.sleep(2000);
                return password_changed;

            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Exception thrown in change password", e);
            }
            return password_changed;
        }
    }


}
