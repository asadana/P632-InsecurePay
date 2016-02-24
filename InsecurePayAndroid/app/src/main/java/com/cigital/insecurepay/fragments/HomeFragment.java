package com.cigital.insecurepay.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
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
import com.cigital.insecurepay.common.Connectivity;
import com.google.gson.Gson;


public class HomeFragment extends Fragment {

    private custAccountFetchTask task = null;
    private Gson gson = new Gson();
    private TextView tvBalance;
    private OnFragmentInteractionListener mListener;
    private CommonVO commonVO;
    private TextView tvAccountNumber;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewObj = inflater.inflate(R.layout.fragment_home, container, false);
        tvBalance = (TextView)viewObj.findViewById(R.id.tvHome_fillBalance);
        tvAccountNumber = (TextView)viewObj.findViewById(R.id.tvHome_fillAccountNumber);
        commonVO = ((CommonVO)this.getArguments().getSerializable(getString(R.string.common_VO)));
        task = new custAccountFetchTask();
        task.execute();


        return viewObj;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    class custAccountFetchTask extends AsyncTask<String, String, AccountVO> {
        private AccountVO accountDetails;

        @Override
        protected AccountVO doInBackground(String... params) {
            Log.d(this.getClass().getSimpleName(), "Background");
            try {
                //Connection to get Account Details
                Connectivity conn = new Connectivity(HomeFragment.this.getContext(), getString(R.string.Account_details_path), commonVO.getServerAddress());
                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.cust_no), commonVO.getCustNo());
                //Converts customer details to CustomerVO
                accountDetails = gson.fromJson(conn.get(contentValues), AccountVO.class);
                Log.d(this.getClass().getSimpleName(), "Customer Balance: " + accountDetails.getAccountBalance());
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Error while connecting: ", e);
            }
            return accountDetails;
        }

        protected void onPostExecute(AccountVO accountDetails) {

            tvBalance.setText(Float.toString(accountDetails.getAccountBalance()));
            tvAccountNumber.setText(Integer.toString(accountDetails.getAccNo()));

        }

    }

}
