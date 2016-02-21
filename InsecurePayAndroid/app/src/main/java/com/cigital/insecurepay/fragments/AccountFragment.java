package com.cigital.insecurepay.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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

import com.cigital.insecurepay.R;

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

    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

        initValues();
        addListeners();

        return viewObj;
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
    }

    private void onClickChangePassword() {
        Log.i(this.getClass().getSimpleName(), "Displaying change password dialog");
        changePassword();
        //return true;
    }

    private void initValues() {
        // TODO: Update with call to server
        etPhone.setText("0000000000", TextView.BufferType.EDITABLE);
        etAddress.setText("I dont live here", TextView.BufferType.EDITABLE);
        etEmail.setText("something@gmail.com", TextView.BufferType.EDITABLE);


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

}
