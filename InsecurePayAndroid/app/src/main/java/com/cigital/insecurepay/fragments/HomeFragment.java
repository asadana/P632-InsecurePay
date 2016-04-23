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

/**
 * HomeFragment extends {@link Fragment}.
 * This class displays the Home Screen containing Account number and current balance
 * of the logged in user.
 */
public class HomeFragment extends Fragment {

    // UI Components
    private TextView tvBalance;
    private TextView tvAccountNumber;

    private OnFragmentInteractionListener mListener;

    /**
     * HomeFragment is the default constructor of this class.
     */
    public HomeFragment() {
    }

    /**
     * onCreate is the first method called when the Activity is being created.
     * It populates and initializes the text views.
     *
     * @param savedInstanceState Object that is used to pass data to this activity while
     *                           creating it.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(this.getClass().getSimpleName(), "onCreateView: Initializing the fragment.");
        // Inflate the layout for this fragment
        View viewObj = inflater.inflate(R.layout.fragment_home, container, false);
        tvBalance = (TextView)viewObj.findViewById(R.id.tvHome_fillBalance);
        tvAccountNumber = (TextView)viewObj.findViewById(R.id.tvHome_fillAccountNumber);
        CommonVO commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        ContentValues contentValues = new ContentValues();
        contentValues.put(getString(R.string.cust_no), commonVO.getCustomerNumber());
        CustomerAccountFetchTask task = new CustomerAccountFetchTask(getContext(), commonVO.getServerAddress(),
                getString(R.string.account_details_path), contentValues);
        task.execute();
        return viewObj;
    }

    /**
     * onAttach is an overridden function that is called to associate the
     * particular fragment with or from the activity.
     *
     * @param context   Contains the the {@link Context} of the parent activity.
     */
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

    /**
     * onDetach is an overridden function that is called to detach the
     * particular fragment with or from the activity.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * onFragmentInteractionListener is an interface that contains
     * an abstract method used to interact with parent activity.
     */
    public interface OnFragmentInteractionListener {

        /**
         * setAccountDetails is a function that allows the parent activity to populate
         * accountVO object.
         *
         * @param accountVO Contains the basic account information from parent activity
         *                  in form of {@link AccountVO} object.
         */
        void setAccountDetails(AccountVO accountVO);
    }

    /**
     * CustomerAccountFetchTask is used to get the account details of customer.
     * CustomerAccountFetchTask extends {@link GetAsyncCommonTask} and produces
     * {@link AccountVO} in postExecute.
     */
    private class CustomerAccountFetchTask extends GetAsyncCommonTask<AccountVO> {
        private AccountVO accountVOObj;

        /**
         * CustomerAccountFetchTask is the parametrized constructor of this class.
         *
         * @param contextObj    Contains the context of the parent activity.
         * @param serverAddress Contains the server url/address .
         * @param path          Contains the sub-path to the service that needs to be used.
         * @param contentValues Contains data to be sent to the server
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
         *                  Server sends AccountVO object as response
         */
        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);

            accountVOObj = objReceived;
            Log.d(this.getClass().getSimpleName(),
                    "postSuccess: Account NO: " + accountVOObj.getAccountNumber());
            Log.d(this.getClass().getSimpleName(),
                    "postSuccess: Customer Balance: " + accountVOObj.getAccountBalance());

            // Display Account number and current balance on home screen
            tvBalance.setText(Float.toString(accountVOObj.getAccountBalance()));
            tvAccountNumber.setText(Integer.toString(accountVOObj.getAccountNumber()));
            mListener.setAccountDetails(accountVOObj);
        }
    }

}
