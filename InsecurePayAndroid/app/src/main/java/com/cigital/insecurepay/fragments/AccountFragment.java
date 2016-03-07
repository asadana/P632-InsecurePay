/**
 * AccountFragment Class retrieves and displays the customer details.
 */

package com.cigital.insecurepay.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.ChangePasswordVO;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.CustomerVO;
import com.cigital.insecurepay.activity.LoginActivity;
import com.cigital.insecurepay.common.JsonFileHandler;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * AccountFragment extends Fragment and is used to display and handle Account Management
 * related view operations.
 */
public class AccountFragment extends Fragment {

    // View objects
    private TextView tvName;
    private TextView tvAccountNumber;
    private TextView tvSSN;
    private TextView tvUserDOB;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddressStreet;
    private EditText etAddressCity;
    private EditText etAddressState;
    private EditText etAddressZip;
    private Button btnUpdateInfo;
    private Button btnChangePassword;

    private CustomerVO customerVOObj;

    // To handle connections
    private Gson gson = new Gson();
    private CommonVO commonVO;
    private JsonFileHandler jsonFileHandlerObj;

    // Objects to handle Date format conversion
    private Calendar calenderObj = Calendar.getInstance();
    private SimpleDateFormat dateFormatObj = new SimpleDateFormat("yyyy-MM-dd");

    // TextWatcher objects to handle on edit events for EditText fields
    private TextWatcher twEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Email Address value changed.");
        }
    };
    private TextWatcher twAddressStreet = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Address street value changed.");
        }
    };
    private TextWatcher twAddressCity = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Address city value changed.");
        }
    };
    private TextWatcher twAddressState = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Address state value changed.");
        }
    };
    private TextWatcher twAddressZip = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Address zip value changed.");
        }
    };

    private TextWatcher twPhone = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Phone number value changed.");
        }
    };

    public AccountFragment() {
        // Required empty public constructor
    }

    // onCreateView is called when the class's view is being generated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewObj = inflater.inflate(R.layout.fragment_account, container, false);

        initValues(viewObj);
        addListeners();

        return viewObj;
    }

    // Initializing all the variables
    private void initValues(View viewObj) {
        Log.i(this.getClass().getSimpleName(), "Initializing values.");

        // Initializing all objects from fragment_account
        tvName = (TextView) viewObj.findViewById(R.id.tvAccount_fillName);
        tvAccountNumber = (TextView) viewObj.findViewById(R.id.tvAccount_fillAccountNumber);
        tvSSN = (TextView) viewObj.findViewById(R.id.tvAccount_fillSSN);
        tvUserDOB = (TextView) viewObj.findViewById(R.id.tvAccount_fillUserDOB);

        etEmail = (EditText) viewObj.findViewById(R.id.etAccount_fillEmail);
        etPhone = (EditText) viewObj.findViewById(R.id.etAccount_fillPhone);
        etAddressStreet = (EditText) viewObj.findViewById(R.id.etAccount_fillAddressStreet);
        etAddressCity = (EditText) viewObj.findViewById(R.id.etAccount_fillAddressCity);
        etAddressState = (EditText) viewObj.findViewById(R.id.etAccount_fillAddressState);
        etAddressZip = (EditText) viewObj.findViewById(R.id.etAccount_fillAddressZip);

        btnUpdateInfo = (Button) viewObj.findViewById(R.id.btnAccount_update);
        btnChangePassword = (Button) viewObj.findViewById(R.id.btnAccount_changePassword);

        // Initializing commonVO object
        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));

        // Initializing JsonFileHandler
        jsonFileHandlerObj = new JsonFileHandler(getContext(), commonVO.getUsername());

        // Fetch details from the server
        AccountFetchTask accountFetchTask = new AccountFetchTask();
        accountFetchTask.execute();
    }

    // Initializing listeners where needed
    private void addListeners() {
        Log.i(this.getClass().getSimpleName(), "Adding Listeners");
        etEmail.addTextChangedListener(twEmail);
        etAddressStreet.addTextChangedListener(twAddressStreet);
        etAddressCity.addTextChangedListener(twAddressCity);
        etAddressState.addTextChangedListener(twAddressState);
        etAddressZip.addTextChangedListener(twAddressZip);
        etPhone.addTextChangedListener(twPhone);

        // TODO: Fix number formatting
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
        tvUserDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateOfBirthPickerFragment dateDialogFragment = new DateOfBirthPickerFragment();
                // Calling show with FragmentManager and a unique tag name
                dateDialogFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });
    }

    // Handles tasks to be done when Update button clicked
    private void onClickUpdateInformation() {
        Log.i(this.getClass().getSimpleName(), "Updating customer information.");

        customerVOObj.setEmail(etEmail.getText().toString());
        customerVOObj.setStreet(etAddressStreet.getText().toString());
        customerVOObj.setCity(etAddressCity.getText().toString());
        customerVOObj.setState(etAddressState.getText().toString());
        Log.i(this.getClass().getSimpleName(), tvUserDOB.getText().toString());

        customerVOObj.setBirthDate(tvUserDOB.getText().toString());
        customerVOObj.setZipcode(Integer.parseInt(etAddressZip.getText().toString()));
        customerVOObj.setPhoneNo(Integer.parseInt(etPhone.getText().toString()));

        jsonFileHandlerObj.writeToFile(gson.toJson(customerVOObj));

        // Object of inner class to post update to the server
        AccountUpdateTask accountUpdateTask = new AccountUpdateTask();
        accountUpdateTask.execute();
    }

    // Handles tasks to be done when Change Password is clicked
    private void onClickChangePassword() {
        Log.i(this.getClass().getSimpleName(), "Displaying change password dialog");


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton("OK", null);
        alertDialogBuilder.setNegativeButton("Cancel", null);

        final EditText etNewPassword;
        final EditText etConfirmPassword;

        // EditText variables to fetch user inputs from the dialog
        etNewPassword = (EditText) dialogView.findViewById(R.id.etChangePassword_newPassword);
        etConfirmPassword = (EditText) dialogView.findViewById(R.id.etChangePassword_confirmPassword);

        final AlertDialog alertD = alertDialogBuilder.create();

        alertD.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertD.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newPassword = etNewPassword.getText().toString();
                        String confirmPassword = etConfirmPassword.getText().toString();
                        ChangePasswordTask changePasswordTask;

                        if (TextUtils.isEmpty(newPassword)) {
                            etNewPassword.setError(getString(R.string.error_field_required));
                            return;
                        }

                        if ((newPassword.length() < 3) || (confirmPassword.length() < 3)) {
                            etConfirmPassword.setError("Minimum 3 characters length required");
                            etNewPassword.setText("");
                            etConfirmPassword.setText("");
                            return;
                        }

                        if (TextUtils.isEmpty(confirmPassword)) {
                            etConfirmPassword.setError(getString(R.string.error_invalid_field));
                            return;
                        }

                        if (newPassword.equals(confirmPassword)) {
                            changePasswordTask = new ChangePasswordTask(commonVO.getUsername(), newPassword);
                            changePasswordTask.execute();
                            alertD.dismiss();
                        } else {
                            etNewPassword.setText("");
                            etConfirmPassword.setText("");
                            Toast.makeText(AccountFragment.this.getContext(), "Password mismatch", Toast.LENGTH_LONG).show();
                        }

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

    // Inner-class for fetching information from server
    private class AccountFetchTask extends AsyncTask<String, String, CustomerVO> {

        @Override
        protected CustomerVO doInBackground(String... params) {

            // contentValues to to store the parameter used to fetch the values
            ContentValues contentValues = new ContentValues();
            contentValues.put(getString(R.string.cust_no), commonVO.getCustNo());
            // Fetching the connectivity object and setting context and path
            LoginActivity.connectivityObj.setConnectionParameters(getContext(), getString(R.string.cust_details_path));
            // Storing server response
            String responseFromServer = LoginActivity.connectivityObj.get(contentValues);

            Log.i(this.getClass().getSimpleName(), responseFromServer);

            Log.i(this.getClass().getSimpleName(), "Account information retrieved successfully");

            // Writing to the local JSON file
            jsonFileHandlerObj.writeToFile(responseFromServer);

            // Storing server response from JSON file in the customerVOObj
            try {
                customerVOObj = gson.fromJson(jsonFileHandlerObj.readFromFile(), CustomerVO.class);
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), e.toString());
            }

            return customerVOObj;
        }

        @Override
        protected void onPostExecute(final CustomerVO customerVOObj) {
            Log.d(this.getClass().getSimpleName(), "In post execute. Updating view.");

            // Populating customerVO with the information retrieved
            tvName.setText(customerVOObj.getCustName());
            tvAccountNumber.setText(Integer.toString(commonVO.getAccountVO().getAccNo()));
            tvSSN.setText(customerVOObj.getDecodedSsn());
            tvUserDOB.setText(customerVOObj.getBirthDate());
            etEmail.setText(customerVOObj.getEmail(), TextView.BufferType.EDITABLE);
            etAddressStreet.setText(customerVOObj.getStreet(), TextView.BufferType.EDITABLE);
            etAddressCity.setText(customerVOObj.getCity(), TextView.BufferType.EDITABLE);
            etAddressState.setText(customerVOObj.getState(), TextView.BufferType.EDITABLE);
            etAddressZip.setText(Integer.toString(customerVOObj.getZipcode()), TextView.BufferType.EDITABLE);
            etPhone.setText(Integer.toString(customerVOObj.getPhoneNo()), TextView.BufferType.EDITABLE);
        }
    }

    // Inner-class to update the server
    private class AccountUpdateTask extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... params) {

            String responseFromServer;
            // Getting JSON from customerVO object to be sent
            String sendToServer = null;
            try {
                sendToServer = jsonFileHandlerObj.readFromFile();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), e.toString());
            }
            // Fetching the connectivity object and setting context and path
            LoginActivity.connectivityObj.setConnectionParameters(getContext(), getString(R.string.cust_details_path));
            LoginActivity.connectivityObj.setSendToServer(sendToServer);
            // Storing server response
            responseFromServer = LoginActivity.connectivityObj.post();

            Log.d(this.getClass().getSimpleName(), "Server response in update: " + responseFromServer);

            return responseFromServer;
        }

        @Override
        protected void onPostExecute(final String responseFromServer) {
            switch (responseFromServer) {
                case "true":
                    Toast.makeText(getContext(), "Update successful", Toast.LENGTH_SHORT).show();
                    break;
                case "false":
                    Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.e(this.getClass().getSimpleName(), "Invalid response from the server on update credentials");
                    break;
            }

        }
    }

    // Inner class to update the password on the server
    public class ChangePasswordTask extends AsyncTask<String, String, String> {

        private final String username;
        private final String password;

        ChangePasswordTask(String username, String password) {
            this.password = password;
            this.username = username;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(this.getClass().getSimpleName(), "In background, validating user credentials");
            String password_changed = null;
            try {
                // Parameters contain credentials which are capsuled to ChangePasswordVO objects
                ChangePasswordVO sendVo = new ChangePasswordVO(username, password);
                // sendToServer contains JSON object that has credentials
                String sendToServer = gson.toJson(sendVo);

                // Fetching the connectivity object and setting context and path
                LoginActivity.connectivityObj.setConnectionParameters(getContext(), getString(R.string.change_password_path));
                LoginActivity.connectivityObj.setSendToServer(sendToServer);
                // Call post and since there are white spaces in the response, trim is called
                password_changed = LoginActivity.connectivityObj.post().trim();


                Log.d("Response from server", password_changed);
                Thread.sleep(2000);
                return password_changed;

            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Exception thrown in change password", e);
            }
            return password_changed;
        }

        @Override
        protected void onPostExecute(final String password_changed) {

            switch (password_changed) {
                case "false":
                    Toast.makeText(AccountFragment.this.getContext(), "Password was not changed", Toast.LENGTH_LONG).show();
                    break;
                case "true":
                    Toast.makeText(AccountFragment.this.getContext(), "Password Changed to " + password, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Log.e(this.getClass().getSimpleName(), "Invalid response on password change");
                    break;
            }
        }
    }

    // Inner class for DialogFragment
    private class DateOfBirthPickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle bundleObj) {
            int year = calenderObj.get(Calendar.YEAR);
            int month = calenderObj.get(Calendar.MONTH);
            int day = calenderObj.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getContext(), this, year, month, day);
        }

        // Function handles what to once new date is selected from dialog
        @Override
        public void onDateSet(DatePicker veiew, int year, int monthOfYear, int dayOfMonth) {
            calenderObj.set(year, monthOfYear, dayOfMonth);
            tvUserDOB.setText(dateFormatObj.format(calenderObj.getTime()));
        }
    }

}
