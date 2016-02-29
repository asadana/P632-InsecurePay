package com.cigital.insecurepay.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cigital.insecurepay.DBHelper.LoginDBHelper;
import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.TransferFundsVO;
import com.cigital.insecurepay.VOs.TransferValidationVO;
import com.cigital.insecurepay.common.Connectivity;
import com.google.gson.Gson;

import static com.cigital.insecurepay.R.string.account_lockout_duration;
import static com.cigital.insecurepay.R.string.transfer_validation_path;
//import static com.cigital.insecurepay.R.string.transfervalidation_VO;


public class TransferFragment extends Fragment {

    EditText etTransferDetails;
    EditText etCust_username;
    EditText etTransferAmount;
    int fromCustNo;
    int fromAccountNo = 2004;
    int toCustNo;
    String custUsername;
    Button btnTransfer;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private SharedPreferences sharedpreferences;
    String transferDetails;
    private float transferAmount;


    // To handle connections
    private Gson gson = new Gson();
    private CommonVO commonVO;
    private TransferValidationVO transferValidationVO;

    private TransferTask transferTask = null;
    private TransferValidationTask transfervalidationtask = null;

    public TransferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewObj = inflater.inflate(R.layout.fragment_transfer, container, false);

        initValues(viewObj);
        addListeners();
        return viewObj;
    }

    private void initValues(View viewObj) {
        Log.i(this.getClass().getSimpleName(), "Initializing values.");





        // Initializing all objects from fragment_transfer
        etTransferAmount = (EditText) viewObj.findViewById(R.id.ettransferAmount);
        etTransferDetails = (EditText) viewObj.findViewById(R.id.ettransferDetails);
        etCust_username = (EditText) viewObj.findViewById(R.id.etCust_username);
        btnTransfer = (Button) viewObj.findViewById(R.id.btn_transfer);

        //Implementing SharedPreferences



        // Initializing commonVO and transferfundsVO object
        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        transferValidationVO = ((TransferValidationVO) this.getArguments().getSerializable(getString(transfer_validation_path)));

        Log.d(this.getClass().getSimpleName(), "current Account No" + Integer.toString(commonVO.getAccountNo()));
        Log.d(this.getClass().getSimpleName(), Integer.toString(commonVO.getCustNo()));



    }



    // Initializing listeners where needed
    private void addListeners() {
        Log.i(this.getClass().getSimpleName(), "Adding Listeners");


        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("in on click", "clicked");
                 transferDetails = etTransferDetails.getText().toString();
                 transferAmount = etTransferAmount.getAlpha();
                custUsername = etCust_username.getText().toString();
                Log.d("transferDetails", "" + transferDetails);
                Log.d("transferAmount", "" + transferAmount);

              /*  sharedpreferences = this.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(PREF_USERNAME, etCust_username.getText().toString());
                editor.commit();*/

                //custUsername = sharedpreferences.getString(PREF_USERNAME,"");
                // etCust_username.setText(custUsername);
                if (transferAmount == 0 || custUsername == null) {
                    etTransferAmount.setError("Enter Amount");
                    etCust_username.setError("Enter Username");
                }
                Log.d("custUsername", "" + custUsername);

                transfervalidationtask = new TransferValidationTask(custUsername);
                transfervalidationtask.execute();

            }
        });
    }
    private void callingtransfertask() {
        if(transferValidationVO.isUsernameExists()) {
            toCustNo = transferValidationVO.getCustNo();
            transferTask = new TransferTask(fromAccountNo, commonVO.getCustNo(), toCustNo, transferAmount, transferDetails);
            transferTask.execute();
        }
    }

    class TransferValidationTask extends AsyncTask<String, String, TransferValidationVO> {

        private int custno;

        public TransferValidationTask(String custUsername) {
        }

        @Override
        protected TransferValidationVO doInBackground(String... params) {
            Log.d(this.getClass().getSimpleName(), "Background");
            try {
                //Connection to get Account Details
                Connectivity conn = new Connectivity(TransferFragment.this.getContext(), getString(R.string.transfer_validation_path), commonVO.getServerAddress());
                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.username),custUsername);
                //Converts customer details to CustomerVO
                transferValidationVO = gson.fromJson(conn.get(contentValues), TransferValidationVO.class);
                Log.d(this.getClass().getSimpleName(), "Customer number: " + transferValidationVO.getCustNo());
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Error while connecting: ", e);
            }
            return transferValidationVO;
        }

        protected void onPostExecute(TransferValidationVO accountDetails) {
            callingtransfertask();


        }



    }

    private class TransferTask extends AsyncTask<String, String, String> {

        private int fromCustNo;
        private int fromAccountNo;
        private int toCustNo;
        private float transferAmount;
        private String transferDetails;



        TransferTask(int fromAccountNo, int fromCustNo, int toCustNo, float transferAmount, String transferDetails) {
            this.fromCustNo = fromCustNo;
            this.fromAccountNo = fromAccountNo;
            this.toCustNo = toCustNo;
            this.transferAmount = transferAmount;
            this.transferDetails = transferDetails;
        }

        @Override
        protected String doInBackground(String... params) {

            Log.d(this.getClass().getSimpleName(), "In background, sending transfer details");
            String amount_transferred = null;
            try {
                LoginDBHelper db = new LoginDBHelper(TransferFragment.this.getContext());
                //Parameters contain credentials which are capsuled to ChangePasswordVO objects
                TransferFundsVO sendVo = new TransferFundsVO(fromAccountNo, fromCustNo, toCustNo, transferAmount, transferDetails);
                //sendToServer contains JSON object that has credentials
                String sendToServer = gson.toJson(sendVo);
                //Passing the context of LoginActivity to Connectivity
                Connectivity con_login = new Connectivity(TransferFragment.this.getContext(), getString(R.string.transfer_funds_path), commonVO.getServerAddress(), sendToServer);
                //Call post and since there are white spaces in the response, trim is called
                amount_transferred = con_login.post().trim();
                Log.d("Response from server", amount_transferred);
                Thread.sleep(2000);

            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Exception thrown in transfer funds", e);
            }

            return amount_transferred;
        }

        @Override
        protected void onPostExecute(final String amount_transferred) {

            if (amount_transferred.equals("false")) {
                Toast.makeText(TransferFragment.this.getContext(), "Amount was not transferred", Toast.LENGTH_LONG).show();
            } else if (amount_transferred.equals("true")) {
                Toast.makeText(TransferFragment.this.getContext(), "Transaction successful", Toast.LENGTH_LONG).show();
            } else {
                Log.e(this.getClass().getSimpleName(), "Invalid response on transfer funds");
            }
        }
    }
}
