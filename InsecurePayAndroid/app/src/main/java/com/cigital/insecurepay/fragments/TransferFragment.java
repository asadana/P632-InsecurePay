package com.cigital.insecurepay.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.AccountVO;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.TransferFundsVO;
import com.cigital.insecurepay.activity.TransferActivity;
import com.cigital.insecurepay.common.GetAsyncCommonTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TransferFragment extends {@link Fragment}.
 * This class is used transferring funds from one user to another.
 */
public class TransferFragment extends Fragment {

    private static final Pattern stringPattern = Pattern.compile("^-?[0-9]\\d*(\\.\\d+)?$");
    //View objects
    private EditText etTransfer_Details;
    private EditText etTransfer_CustomerUsername;
    private EditText etTransfer_Amount;
    private Button btnTransfer;
    private TransferFundsVO transferFundsVO;
    private TransferValidationTask transfervalidationtask;
    private CommonVO commonVO;
    private String enterCorrectAmount = "Enter valid amount";
    private String enterDifferentUser = "Enter different username";
    private String enterUser = "Enter username";

    /**
     * TransferFragment is the default empty constructor of this class.
     */
    public TransferFragment() {
    }

    /**
     * onCreateView is an overridden function that is called while the fragment is
     * being created.
     *
     * @param layoutInflater     Contains the {@link LayoutInflater} object that defines
     *                           the layout of the fragment
     * @param viewGroup          Contains the {@link ViewGroup} object to which this fragment
     *                           belongs to.
     * @param savedInstanceState Object of {@link Bundle} that is used to pass data to this
     *                           activity while creating it.
     * @return View         Return the {@link View} object created.
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewObj = layoutInflater.inflate(R.layout.fragment_transfer, viewGroup, false);
        initValues(viewObj);
        addListeners();
        return viewObj;
    }

    /**
     * initValues is a function that is used to initialize the UI components
     * and other variables.
     *
     * @param viewObj   Contains the {@link View} of the fragment.
     */
    private void initValues(View viewObj) {
        Log.d(this.getClass().getSimpleName(), "initValues: Initializing values.");

        etTransfer_Amount = (EditText) viewObj.findViewById(R.id.ettransferAmount);
        etTransfer_Details = (EditText) viewObj.findViewById(R.id.ettransferDetails);
        etTransfer_CustomerUsername = (EditText) viewObj.findViewById(R.id.etCust_username);
        btnTransfer = (Button) viewObj.findViewById(R.id.btn_transfer);

        // Initializing commonVO and transferFundsVO object
        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));

        transferFundsVO = new TransferFundsVO();
        transferFundsVO.setFromAccount(commonVO.getAccountVO());
    }

    /**
     * addListeners is a function that is called to attach listeners
     * to various components.
     */
    private void addListeners() {
        Log.d(this.getClass().getSimpleName(), "addListeners: Adding Listeners");

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerUsername;
                String transferDetails;
                float transferAmount;

                String amount = String.valueOf(etTransfer_Amount.getText());
                Matcher m = stringPattern.matcher(amount);
                transferDetails = etTransfer_Details.getText().toString();
                customerUsername = etTransfer_CustomerUsername.getText().toString();

                if (!m.matches()) {
                    etTransfer_Amount.setError(enterCorrectAmount);
                    etTransfer_Amount.requestFocus();
                    return;
                }
                //Ensures recipient of funds is filled
                if (customerUsername.equals("")) {
                    etTransfer_CustomerUsername.setError(enterUser);
                    etTransfer_CustomerUsername.requestFocus();
                    return;
                }

                //Ensures funds are not transferred to oneself
                if (commonVO.getUsername().equals(customerUsername)) {
                    etTransfer_CustomerUsername.setError(enterDifferentUser);
                    etTransfer_CustomerUsername.requestFocus();
                    return;
                }
                transferAmount = Float.parseFloat(amount);
                transferFundsVO.setTransferAmount(transferAmount);
                transferFundsVO.setTransferDetails(transferDetails);
                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.username), customerUsername);

                // Prepares for validation of receiver of funds
                transfervalidationtask = new TransferValidationTask(getContext(), commonVO.getServerAddress(),
                        getString(R.string.transfer_validation_path), contentValues);
                transfervalidationtask.execute();
            }
        });
    }

    /**
     * TransferValidationTask is used to ensure that the receiver of the funds is valid.
     * CustomerAccountFetchTask extends {@link GetAsyncCommonTask} and produces
     * object of {@link String} in postExecute.
     */
    private class TransferValidationTask extends GetAsyncCommonTask<String> {

        /**
         * TransferValidationTask is the parametrized constructor of ForgotPasswordTask
         *
         * @param contextObj    Contains the context of the parent activity.
         * @param serverAddress Contains the server url/address .
         * @param path          Contains the sub-path to the service that needs to be used.
         * @param contentValues Contains the values to be sent to the server.
         */
        public TransferValidationTask(Context contextObj, String serverAddress,
                                      String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, String.class);
        }

        /**
         * postSuccess is called when the server responds with a non-error code response.
         * This function performs all the tasks to be done in postExecute when server response
         * is not an error.
         *
         * @param resultObj Contains the string sent from the server as part of the response.
         *                  Server sends the Customer No in response.
         */
        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            Log.d(this.getClass().getSimpleName(), "postSuccess: " + resultObj);
            if (resultObj != null) {
                //Server sends the Customer Number in response
                int customerNumber = Integer.parseInt(resultObj);

                //If Customer is invalid, Server sends -1 as Customer Number
                if (customerNumber == -1) {
                    etTransfer_CustomerUsername.setError("Invalid User");
                    etTransfer_CustomerUsername.requestFocus();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(getString(R.string.cust_no), customerNumber);

                    //If valid customer is entered, call customer account fetch task
                    CustomerAccountFetchTask customerAccountFetchTask =
                            new CustomerAccountFetchTask(getContext(),
                                    commonVO.getServerAddress(),
                                    getString(R.string.account_details_path),
                                    contentValues);
                    customerAccountFetchTask.execute();
                }
            }
        }
    }

    /**
     * CustomerAccountFetchTask is used to get the Account information of the customer.
     * CustomerAccountFetchTask extends {@link GetAsyncCommonTask} and produces
     * object of {@link AccountVO} in postExecute.
     */
    private class CustomerAccountFetchTask extends GetAsyncCommonTask<AccountVO> {

        /**
         * CustomerAccountFetchTask is the parametrized constructor of ForgotPasswordTask
         *
         * @param contextObj    Contains the context of the parent activity.
         * @param serverAddress Contains the server url/address .
         * @param path          Contains the sub-path to the service that needs to be used.
         * @param contentValues Contains the values to be sent to the server.
         */
        public CustomerAccountFetchTask(Context contextObj, String serverAddress,
                                        String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, AccountVO.class);
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

            AccountVO accountVOObj = objReceived;

            Log.d(this.getClass().getSimpleName(), "Customer Balance: "
                    + accountVOObj.getAccountBalance());

            transferFundsVO.setToAccount(accountVOObj);
            // Prepares for passing data to Transfer Activity
            Intent intent = new Intent(getContext(), TransferActivity.class);
            intent.putExtra(getString(R.string.transferFunds_VO), transferFundsVO);
            intent.putExtra(getString(R.string.common_VO), commonVO);
            startActivity(intent);
        }
    }
}

