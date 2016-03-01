package com.cigital.insecurepay.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.AccountVO;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.activity.LoginActivity;
import com.google.gson.Gson;


public class HomeFragment extends Fragment {

    private Gson gson = new Gson();
    private TextView tvBalance;
    private CommonVO commonVO;
    private TextView tvAccountNumber;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewObj = inflater.inflate(R.layout.fragment_home, container, false);
        tvBalance = (TextView)viewObj.findViewById(R.id.tvHome_fillBalance);
        tvAccountNumber = (TextView)viewObj.findViewById(R.id.tvHome_fillAccountNumber);
        commonVO = ((CommonVO)this.getArguments().getSerializable(getString(R.string.common_VO)));
        custAccountFetchTask task = new custAccountFetchTask();
        task.execute();


        return viewObj;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void setAccNo(int accNo);
    }

    private class custAccountFetchTask extends AsyncTask<String, String, AccountVO> {
        private AccountVO accountDetails;

        @Override
        protected AccountVO doInBackground(String... params) {
            Log.d(this.getClass().getSimpleName(), "Background");
            try {
/*                // Fetching the connectivity object and setting context and path
                commonVO.getConnectivityObj().setConnectionParameters(getContext(), getString(R.string.account_details_path));
                */
                // Fetching the connectivity object and setting context and path
                LoginActivity.connectivityObj.setConnectionParameters(getContext(), getString(R.string.account_details_path));

                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.cust_no), commonVO.getCustNo());

/*                //Converts customer details to CustomerVO
                accountDetails = gson.fromJson(commonVO.getConnectivityObj().get(contentValues), AccountVO.class);
                */
                //Converts customer details to CustomerVO
                accountDetails = gson.fromJson(LoginActivity.connectivityObj.get(contentValues), AccountVO.class);

                Log.d(this.getClass().getSimpleName(), "Customer Balance: " + accountDetails.getAccountBalance());
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Error while connecting: ", e);
            }
            return accountDetails;
        }

        protected void onPostExecute(AccountVO accountDetails) {

            tvBalance.setText(Float.toString(accountDetails.getAccountBalance()));
            tvAccountNumber.setText(Integer.toString(accountDetails.getAccNo()));
            mListener.setAccNo(accountDetails.getAccNo());

        }

    }

}
