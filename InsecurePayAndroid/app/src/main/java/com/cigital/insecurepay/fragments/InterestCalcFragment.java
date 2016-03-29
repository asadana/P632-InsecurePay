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

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Random;


public class InterestCalcFragment extends Fragment {


    // View objects
    private TextView tvBalance;
    private TextView tvDisplayBalance;
    private TextView tvPrincipal;
    private EditText etPrincipal;
    private TextView tvPeriod;
    private EditText etDate;
    private TextView tvCreditScore;
    private TextView tvDisplayCreditScore;
    private Button btnCalculate;
    private Spinner DateType;
    private TextView tvInterest;
    private TextView tvRateOfInterest;
    private TextView tvFillInterest;
    private TextView tvFillRateOfInterest;
    private CommonVO commonVO;


    //Set constants for credit score range
    private static final int MAXSCORE = 1000;
    private static final int EXCELLENT = 720;
    private static final int GOOD = 690;
    private static final int FAIR = 630;
    private static final int BAD = 300;
    //Base interest rate
    private static final int INTERESTBASE = 4;
    private static final int MONTHS = 12;
    private static final int DAYS = 365;
    private static final int ROUNDOFFVALUE = 2;
    private static final double CREDITSCOREDIVISOR = 1000.0;
    private static final double INTERESTDIVISOR = 100.0;

    private Integer creditScore;
    private Double interest;
    private HomeFragment.OnFragmentInteractionListener mListener;


    public InterestCalcFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewObj = inflater.inflate(R.layout.fragment_interest_calc, container, false);
        Log.i(this.getClass().getSimpleName(), "Generating View ...");
        creditScore = generateCreditScore();
        initValues(viewObj);
        //Credit score is init seperately
        tvDisplayCreditScore.setText((creditScore).toString());
        addListener();
        return viewObj;
    }

    private int generateCreditScore() {
        Log.i(this.getClass().getSimpleName(), "Generating credit Score ... ");
        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        double account_bal = commonVO.getAccountVO().getAccountBalance();
        int creditScoreRank = 1;
        if (account_bal >= 0 && account_bal < 1000) {
            creditScoreRank = 4;
        } else if (account_bal >= 1001 && account_bal < 4000) {
            creditScoreRank = 3;
        } else if (account_bal >= 4001 && account_bal <= 10000) {
            creditScoreRank = 2;
        }

        Random r = new Random((long) account_bal);
        int creditScore = 0;
        switch (creditScoreRank) {
            //Excellent
            case 1: {
                creditScore = r.nextInt(MAXSCORE - EXCELLENT) + EXCELLENT;
                break;
            }
            //Good
            case 2: {
                creditScore = r.nextInt(EXCELLENT - GOOD) + GOOD;
                break;
            }
            //Fair
            case 3: {
                creditScore = r.nextInt(GOOD - FAIR) + FAIR;
                break;
            }
            //Bad
            case 4: {
                creditScore = r.nextInt(FAIR - BAD) + BAD;
                break;
            }
        }

        return creditScore;
    }

    // Initializing listeners where needed
    private void addListener() {
        Log.i(this.getClass().getSimpleName(), "Adding Listeners");

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcInterest();
            }
        });
    }

    private void initValues(View viewObj) {
        Log.i(this.getClass().getSimpleName(), "Initializing Values..");
        tvBalance = (TextView) viewObj.findViewById(R.id.tvIntCalc_AccountBalance);
        tvDisplayBalance = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillAccountBalance);
        tvDisplayBalance.setText(String.valueOf(commonVO.getAccountVO().getAccountBalance()));
        tvPrincipal = (TextView) viewObj.findViewById(R.id.tvIntCalc_PrincipalAmount);
        etPrincipal = (EditText) viewObj.findViewById(R.id.etIntCalc_FillPrincipalAmount);
        etPrincipal.setText(String.valueOf(commonVO.getAccountVO().getAccountBalance()));
        etDate = (EditText) viewObj.findViewById(R.id.etIntCalc_Date);
        DateType = (Spinner) viewObj.findViewById(R.id.etIntCalc_Period);
        tvPeriod = (TextView) viewObj.findViewById(R.id.tvIntCalc_Period);
        tvCreditScore = (TextView) viewObj.findViewById(R.id.tvIntCalc_CreditScore);
        tvDisplayCreditScore = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillCreditScore);
        tvInterest = (TextView) viewObj.findViewById(R.id.tvIntCalc_Interest);
        tvRateOfInterest = (TextView) viewObj.findViewById(R.id.tvIntCalc_RateOfInterest);
        tvFillInterest = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillInterest);
        tvFillRateOfInterest = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillRateOfInterest);
        btnCalculate = (Button) viewObj.findViewById(R.id.btnIntCalc_Calc);
        Spinner spinner = (Spinner) viewObj.findViewById(R.id.etIntCalc_Period);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.Date, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private void calcInterest() {
        Log.i(this.getClass().getSimpleName(), "Calculating Interest...");
        double principal = Double.parseDouble(etPrincipal.getText().toString());
        double time = Double.parseDouble(etDate.getText().toString());

        String type = DateType.getSelectedItem().toString();
        //If month or days convert to year
        switch (type) {
            case "Months":
                time /= MONTHS;
                break;
            case "Days":
                time /= DAYS;
                break;
        }

        //Calc rate of interest
        double amount = commonVO.getAccountVO().getAccountBalance();
        int creditScore = Integer.parseInt(tvDisplayCreditScore.getText().toString());
        //Adding (1-creditscore/1000) to the base interest rate
        Double rateOfInterest = INTERESTBASE + (1 - (creditScore / CREDITSCOREDIVISOR));
        rateOfInterest = roundOff(rateOfInterest);
        tvRateOfInterest.setVisibility(View.VISIBLE);
        tvFillRateOfInterest.setText(rateOfInterest.toString() + getString(R.string.tvIntCalc_Percent));
        Double interest =( principal * rateOfInterest * time )/INTERESTDIVISOR;
        interest = roundOff(interest);
        tvInterest.setVisibility(View.VISIBLE);
        tvFillInterest.setText(interest.toString());

    }

    //Round off to 2 decimal places after point
    private Double roundOff(double value) {
        Log.i(this.getClass().getSimpleName(), "Rounding Off...");
        return new BigDecimal(value)
                .setScale(ROUNDOFFVALUE, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

    }
}
