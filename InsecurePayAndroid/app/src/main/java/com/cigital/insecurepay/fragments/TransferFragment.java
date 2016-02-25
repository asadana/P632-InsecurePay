package com.cigital.insecurepay.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.AccountVO;
import com.google.gson.Gson;


public class TransferFragment extends Fragment {

    private TransferFetchTask task = null;
    private Gson gson = new Gson();
    EditText etAccountNumber;
    EditText etTransferAmount;
    Button bTransfer;
    private OnFragmentInteractionListener mListener;

    public TransferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewObj = inflater.inflate(R.layout.fragment_home, container, false);
        etAccountNumber = (EditText)viewObj.findViewById(R.id.etAccountNumber);
        etTransferAmount = (EditText)viewObj.findViewById(R.id.etTransferAmount);
        bTransfer = (Button)viewObj.findViewById(R.id.bTransfer);
        onButtonPressed(bTransfer);
        return viewObj;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Button uri) {
        task = new TransferFetchTask();
        task.execute();
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
    class TransferFetchTask extends AsyncTask<String, String, AccountVO> {


        @Override
        protected AccountVO doInBackground(String... params) {
            return null;
        }
    }
}
