package com.cigital.insecurepay.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cigital.insecurepay.DBHelper.LoginDBHelper;
import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.CustomerVO;
import com.cigital.insecurepay.common.Connectivity;
import com.google.gson.Gson;

/**
 * AccountFragment extends Fragment and is used to display and handle Account Management
 * related view operations.
 */
public class AccountFragment extends Fragment {

    private TextView tvName;
    private TextView tvAccountNumber;
    private TextView tvSSN;
    private TextView tvUserDOB;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private Button btnUpdateInfo;
    private Button btnChangePassword;

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

        etPhone.setText("0000000000", TextView.BufferType.EDITABLE);
        etAddress.setText("I dont live here", TextView.BufferType.EDITABLE);
        etEmail.setText("something@gmail.com", TextView.BufferType.EDITABLE);

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

    // TextWatchers for editText fields

    private void onClickChangePassword() {
        Log.i(this.getClass().getSimpleName(), "Displaying change password dialog");
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class AccountFetchTask extends AsyncTask<String, String, CustomerVO> {

        private String userName;

        public AccountFetchTask(String user) {
            userName = user;
        }

        @Override
        protected CustomerVO doInBackground(String... params) {

            LoginDBHelper dbHelper = new LoginDBHelper(getContext());
            customerVOObj = new CustomerVO(userName);
            String sendToServer = gson.toJson(customerVOObj);
            Connectivity getInfoConnection = new Connectivity(getContext(), getString(R.string.cust_details_path), serverAddress, sendToServer);
            String responseFromServer = getInfoConnection.post().trim();

            return null;
        }
    }

}
