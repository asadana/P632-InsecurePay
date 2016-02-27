package com.cigital.insecurepay.fragments;

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


public class TransferFragment extends Fragment {

    EditText etTransferDetails;
    EditText etCust_username;
    EditText etTransferAmount;
    int fromCustNo;
    int fromAccountNo = 2004;
    int toCustNo;
    Button btnTransfer;

    // To handle connections
    private Gson gson = new Gson();
    private CommonVO commonVO;
    private TransferValidationVO transferValidationVO;

    private TransferTask transferTask = null;

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

        // Initializing commonVO object
        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        transferValidationVO = ((TransferValidationVO) this.getArguments().getSerializable(getString(R.string.common_VO)));

        Log.d(this.getClass().getSimpleName(), "current Account No" + Integer.toString(commonVO.getAccountNo()));
        Log.d(this.getClass().getSimpleName(), Integer.toString(commonVO.getCustNo()));
    }

    // Initializing listeners where needed
    protected void addListeners() {
        Log.i(this.getClass().getSimpleName(), "Adding Listeners");


        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("in on click", "clicked");
                String transferDetails = etTransferDetails.getText().toString();
                String transferAmount = etTransferAmount.getText().toString();
                String custUsername = etCust_username.getText().toString();
                Log.d("transferDetails", "" + transferDetails);
                Log.d("transferAmount", "" + transferAmount);
                Log.d("custUsername", "" + custUsername);
                if(transferValidationVO.isUsernameExists())
                {
                    toCustNo = transferValidationVO.getCustNo();
                }

                TransferTask transferTask = new TransferTask(fromAccountNo, commonVO.getCustNo(), toCustNo, Float.parseFloat(transferAmount), transferDetails);
                transferTask.execute();
            }
        });
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
