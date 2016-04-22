package com.cigital.insecurepay.fragments;

import android.content.ContentValues;
import android.content.Context;
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
import com.cigital.insecurepay.common.GetAsyncCommonTask;
import com.google.gson.Gson;


public class HomeFragment extends Fragment {

    private Gson gson = new Gson();
    private TextView tvBalance;
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
        CommonVO commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        ContentValues contentValues = new ContentValues();
        contentValues.put(getString(R.string.cust_no), commonVO.getCustomerNumber());
        CustAccountFetchTask task = new CustAccountFetchTask(getContext(), commonVO.getServerAddress(),
                getString(R.string.account_details_path), contentValues);
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
        void setAccDetails(AccountVO accountVO);
    }

    private class CustAccountFetchTask extends GetAsyncCommonTask<AccountVO> {
        private AccountVO accountVOObj;

        public CustAccountFetchTask(Context contextObj, String serverAddress, String path, ContentValues contentValues) {
            super(contextObj, serverAddress, path, contentValues, AccountVO.class);
        }

        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            accountVOObj = objReceived;
            Log.d(this.getClass().getSimpleName(), "postSuccess: Account NO: " + accountVOObj.getAccountNumber());
            Log.d(this.getClass().getSimpleName(), "postSuccess: Customer Balance: " + accountVOObj.getAccountBalance());

            tvBalance.setText(Float.toString(accountVOObj.getAccountBalance()));
            tvAccountNumber.setText(Integer.toString(accountVOObj.getAccountNumber()));
            mListener.setAccDetails(accountVOObj);
        }
    }

}
