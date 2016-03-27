package com.cigital.insecurepay.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.VOs.TransferFundsVO;
import com.google.gson.Gson;

import org.w3c.dom.Text;


public class InterestCalcFragment extends Fragment {


    // View objects
    private TextView tvBalance;
    private TextView tvDisplayBalance;
    private TextView tvPrincipal;
    private EditText etPrincipal;
    private TextView tvPeriod;
    private TextView tvCreditScore;
    private TextView tvDisplayCreditScore;
    private Button btnCalculate;
    private CommonVO commonVO;

    private HomeFragment.OnFragmentInteractionListener mListener;

    // TextWatcher objects to handle on edit events for EditText fields
    private TextWatcher etDateType = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            btnCalculate.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Amount entered");
        }
    };



    public InterestCalcFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewObj = inflater.inflate(R.layout.fragment_interest_calc, container, false);
        initValues(viewObj);

        return viewObj;
    }
    private void initValues(View viewObj) {


        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        tvBalance = (TextView) viewObj.findViewById(R.id.tvIntCalc_AccountBalance);
        tvDisplayBalance= (TextView) viewObj.findViewById(R.id.tvIntCalc_FillAccountBalance);
        tvDisplayBalance.setText(String.valueOf(commonVO.getAccountVO().getAccountBalance()));
        tvPrincipal = (TextView) viewObj.findViewById(R.id.tvIntCalc_PrincipalAmount);
        etPrincipal = (EditText) viewObj.findViewById(R.id.etIntCalc_FillPrincipalAmount);
        etPrincipal.setText(String.valueOf(commonVO.getAccountVO().getAccountBalance()));
        tvPeriod = (TextView) viewObj.findViewById(R.id.tvIntCalc_Period);
        tvCreditScore =  (TextView) viewObj.findViewById(R.id.tvIntCalc_CreditScore);
        tvDisplayCreditScore = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillCreditScore);
        btnCalculate=(Button) viewObj.findViewById(R.id.btnIntCalc_Calc);
        Spinner spinner = (Spinner) viewObj.findViewById(R.id.etIntCalc_Period);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.Date, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

}
