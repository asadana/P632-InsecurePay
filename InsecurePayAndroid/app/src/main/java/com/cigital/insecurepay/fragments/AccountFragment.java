/**
 * AccountFragment Class retrieves and displays the customer details.
 */

package com.cigital.insecurepay.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import com.cigital.insecurepay.common.GetAsyncCommonTask;
import com.cigital.insecurepay.common.JsonFileHandler;
import com.cigital.insecurepay.common.PostAsyncCommonTask;
import com.cigital.insecurepay.common.ResponseWrapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private Gson gsonObj = new Gson();
    private CommonVO commonVO;
    private JsonFileHandler jsonFileHandlerObj;

    // Objects to handle Date format conversion
    private Calendar calenderObj = Calendar.getInstance();
    private SimpleDateFormat dateFormatObj = new SimpleDateFormat("yyyy-MM-dd");

    // TextWatcher objects to handle on edit events for EditText fields
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
    private TextWatcher twAddressStreet = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Address street value changed.");
        }
    };
    private TextWatcher twAddressCity = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Address city value changed.");
        }
    };
    private TextWatcher twAddressState = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Address state value changed.");
        }
    };
    private TextWatcher twAddressZip = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (etAddressZip.getText().toString().trim().length() < 5) {
                etAddressZip.setError(getString(R.string.accountZipError));
            } else {
                etAddressZip.setError(null);
            }
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Address zip value changed.");
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
            if (etPhone.getText().toString().trim().length() < 10) {
                etPhone.setError(getString(R.string.accountPhoneError));
            } else {
                etPhone.setError(null);
            }
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Phone number value changed.");
        }
    };
    private TextWatcher twDOB = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            btnUpdateInfo.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Date of Birth value changed.");
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
        ContentValues contentValues = new ContentValues();
        contentValues.put(getString(R.string.cust_no), commonVO.getCustNo());

        GetCustomerDetailsTask getCustomerDetailsTask = new GetCustomerDetailsTask(getContext(), commonVO.getServerAddress(),
                getString(R.string.cust_details_path), contentValues);
        getCustomerDetailsTask.execute();
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
        tvUserDOB.addTextChangedListener(twDOB);

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
                DateOfBirthPickerFragment dateDialogFragment = new DateOfBirthPickerFragment(AccountFragment.this.getContext());
                // Calling show with FragmentManager and a unique tag name
                dateDialogFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });
    }

    // Handles tasks to be done when Update button is clicked
    private void onClickUpdateInformation() {
        if (etAddressZip.getError() != null || etPhone.getError() != null) {
            Toast.makeText(getContext(), getString(R.string.accountFixBeforeUpdate), Toast.LENGTH_SHORT).show();
        } else {
            Log.i(this.getClass().getSimpleName(), "Updating customer information.");

            customerVOObj.setEmail(etEmail.getText().toString());
            customerVOObj.setStreet(etAddressStreet.getText().toString());
            customerVOObj.setCity(etAddressCity.getText().toString());
            customerVOObj.setState(etAddressState.getText().toString());

            customerVOObj.setBirthDate(tvUserDOB.getText().toString());
            customerVOObj.setZipcode(Integer.parseInt(etAddressZip.getText().toString()));
            customerVOObj.setPhoneNo(Integer.parseInt(etPhone.getText().toString()));

            jsonFileHandlerObj.writeToFile(gsonObj.toJson(customerVOObj));

            try {
                customerVOObj = gsonObj.fromJson(jsonFileHandlerObj.readFromFile(), CustomerVO.class);
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Overridden Constructor: " + e.toString());
            }

            UpdateCustomerDetailsTask updateCustomerDetailsTask = new UpdateCustomerDetailsTask(getContext(), commonVO.getServerAddress(),
                    getString(R.string.cust_details_path), customerVOObj);
            updateCustomerDetailsTask.execute();
        }
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

                        //To enforce minimum 3 character length password
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

                        //Checks to see if both entries of new password match
                        if (newPassword.equals(confirmPassword)) {
                            changePasswordTask = new ChangePasswordTask(getContext(), commonVO.getServerAddress(),
                                    getString(R.string.change_password_path), new ChangePasswordVO(commonVO.getUsername(), newPassword));
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

    // Inner class to update the password on the server
    private class ChangePasswordTask extends PostAsyncCommonTask<ChangePasswordVO> {
        private String newPassword;

        public ChangePasswordTask(Context contextObj, String serverAddress, String path,
                                  ChangePasswordVO changePasswordVO) {
            super(contextObj, serverAddress, path, changePasswordVO, ChangePasswordVO.class);
            newPassword = changePasswordVO.getPassword();
        }

        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            Log.d(this.getClass().getSimpleName(), "postSuccess: " + resultObj);
            // Checking the server response
            switch (resultObj) {
                case "false":
                    Toast.makeText(getContext(), getString(R.string.password_not_changed), Toast.LENGTH_LONG).show();
                    break;
                case "true":
                    Toast.makeText(getContext(), getString(R.string.password_changed) + " '" + newPassword + "'", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Log.e(this.getClass().getSimpleName(), "Invalid response on password change");
                    break;
            }
        }
    }

    // Inner class to fetch customer details
    private class GetCustomerDetailsTask extends GetAsyncCommonTask<CustomerVO> {

        public GetCustomerDetailsTask(Context contextObj, String serverAddress, String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, CustomerVO.class);
        }

        @Override
        public void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            Log.d(this.getClass().getSimpleName(), "postSuccess: Updating view.");

            customerVOObj = objReceived;
            // Writing to the local JSON file
            jsonFileHandlerObj.writeToFile(gsonObj.toJson(customerVOObj, CustomerVO.class));

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
            btnUpdateInfo.setEnabled(false);
        }

    }

    // Inner class to update customer details
    private class UpdateCustomerDetailsTask extends PostAsyncCommonTask<CustomerVO> {

        public UpdateCustomerDetailsTask(Context contextObj, String serverAddress, String path,
                                         CustomerVO customerVO) {
            super(contextObj, serverAddress, path, customerVO, CustomerVO.class);
        }

        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            Log.d(this.getClass().getSimpleName(), "postSuccess: Updated account information successfully.");
            Toast.makeText(getContext(), getString(R.string.account_update_successful), Toast.LENGTH_SHORT).show();
            btnUpdateInfo.setEnabled(false);
        }

        @Override
        protected void postFailure(ResponseWrapper responseWrapperObj) {
            Toast.makeText(getContext(), getString(R.string.account_update_failed), Toast.LENGTH_SHORT).show();
            ArrayList<String> arrayListObj = new ArrayList<String>();
            arrayListObj.add("Customer Name: " + customerVOObj.getCustName());
            arrayListObj.add("Customer Number: " + customerVOObj.getCustNo());
            arrayListObj.add("Customer Account Number: " + Integer.toString(commonVO.getAccountVO().getAccNo()));
            arrayListObj.add("Customer SSN: " + customerVOObj.getDecodedSsn());
            String currentString = responseWrapperObj.getResponseString();
            responseWrapperObj.setResponseString(arrayListObj.toString() + "\n\n" + currentString);
            if (responseWrapperObj.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                shouldLogout = false;
            }
            super.postFailure(responseWrapperObj);
        }
    }

    // Inner class for DialogFragment
    @SuppressLint("ValidFragment")
    private class DateOfBirthPickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private Context context;

        public DateOfBirthPickerFragment(Context context) {
            this.context = context;
            try {
                calenderObj.setTime(dateFormatObj.parse(customerVOObj.getBirthDate()));
            } catch (ParseException e) {
                Log.e(this.getClass().getSimpleName(), "DateOfBirthPickerFragment: ", e);
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle bundleObj) {
            int year = calenderObj.get(Calendar.YEAR);
            int month = calenderObj.get(Calendar.MONTH);
            int day = calenderObj.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(context, this, year, month, day);
        }

        // Function handles what to once new date is selected from dialog
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calenderObj.set(year, monthOfYear, dayOfMonth);
            tvUserDOB.setText(dateFormatObj.format(calenderObj.getTime()));
        }
    }

}
