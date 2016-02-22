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
import com.cigital.insecurepay.VOs.CustomerVO;
import com.cigital.insecurepay.common.Connectivity;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CustAccountFetchTask task = null;
    private Gson gson = new Gson();
    private CustAccountFetchTask accountFetchTask = null;
    private TextView tvBalance;
    // TODO: Rename and change types of parameters
    private String serverAddress;
    private OnFragmentInteractionListener mListener;

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */return fragment;
    }
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        task = new CustDetailsRequestTask();
        task.execute();

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewObj = inflater.inflate(R.layout.fragment_home, container, false);
        tvBalance = (TextView)viewObj.findViewById(R.id.tvBalance);
        serverAddress = this.getArguments().getString("serverAddress");
        task = new CustAccountFetchTask();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    class CustAccountFetchTask extends AsyncTask<String, String, AccountVO> {
        private CustomerVO customerDetails;
        private AccountVO accountDetails;

        @Override
        protected AccountVO doInBackground(String... params) {
            Log.d(this.getClass().getSimpleName(), "Background");
            try {
                //Connection to get Account Details
                Connectivity conn = new Connectivity(HomeFragment.this.getContext(), getString(R.string.Account_details_path), serverAddress);
                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.customerno), 4);
                //Converts customer details to CustomerVO
                accountDetails = gson.fromJson(conn.get(contentValues), AccountVO.class);
                Log.d(this.getClass().getSimpleName(), "Customer Balance: " + accountDetails.getAccountBalance());
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Error while connecting: ", e);
            }
            return accountDetails;
        }

        protected void onPostExecute(AccountVO accountDetails) {

            tvBalance.setText(accountDetails.getAccountBalance()+"");

        }

    }

}
