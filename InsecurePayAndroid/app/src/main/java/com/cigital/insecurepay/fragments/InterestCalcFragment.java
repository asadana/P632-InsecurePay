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
    private CommonVO commonVO;


    //Set constants for credit score range
    private static final int MAXSCORE = 1000;
    private static final int EXCELLENT = 720;
    private static final int GOOD = 690;
    private static final int FAIR = 630;
    private static final int BAD = 300;
    //Base interest rate
    private static final int INTERESTBASE = 10;
    // Number of Months
    private static final int MONTHS =12;
    //Number of Days
    private static final int DAYS =365;

    private Integer creditScore;
    private Double interest;
    private HomeFragment.OnFragmentInteractionListener mListener;

    // TextWatcher objects to handle on edit events for EditText fields
    private TextWatcher twDateEntry = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            btnCalculate.setEnabled(true);
            Log.i(this.getClass().getSimpleName(), "Date entered");
        }
    };



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
        //Log.d(this.getClass().getSimpleName(), "The interest is " + );
        return viewObj;
    }
    private int generateCreditScore() {
        Log.i(this.getClass().getSimpleName(),"Generating credit Score ... ");
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

        Random r = new Random((long)account_bal);
        int creditScore = 0;
        Log.d(this.getClass().getSimpleName(), "Credit Score Rank" + creditScoreRank);
        switch(creditScoreRank)
        {
            //Excellent
            case 1:
            {
                creditScore = r.nextInt(MAXSCORE-EXCELLENT) + EXCELLENT;
                break;
            }
            //Good
            case 2:
            {
                creditScore = r.nextInt(EXCELLENT-GOOD)+GOOD;
                break;
            }
            //Fair
            case 3:
            {
                creditScore = r.nextInt(GOOD-FAIR)+FAIR;
                break;
            }
            //Bad
            case 4:
            {
                creditScore = r.nextInt(FAIR-BAD)+BAD;
                break;
            }
        }
        Log.d(this.getClass().getSimpleName(), "Credit Score" + creditScore);
        return creditScore;
    }

    // Initializing listeners where needed
    private void addListener(){
        Log.i(this.getClass().getSimpleName(), "Adding Listeners");
        etDate.addTextChangedListener(twDateEntry);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcInterest();
            }
        });
    }
    private void initValues(View viewObj) {
        Log.i(this.getClass().getSimpleName(), "Initializing Values..");

        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        tvBalance = (TextView) viewObj.findViewById(R.id.tvIntCalc_AccountBalance);
        tvDisplayBalance= (TextView) viewObj.findViewById(R.id.tvIntCalc_FillAccountBalance);
        tvDisplayBalance.setText(String.valueOf(commonVO.getAccountVO().getAccountBalance()));
        tvPrincipal = (TextView) viewObj.findViewById(R.id.tvIntCalc_PrincipalAmount);
        etPrincipal = (EditText) viewObj.findViewById(R.id.etIntCalc_FillPrincipalAmount);
        etPrincipal.setText(String.valueOf(commonVO.getAccountVO().getAccountBalance()));
        etDate = (EditText) viewObj.findViewById(R.id.etIntCalc_Date);
        DateType=(Spinner) viewObj.findViewById(R.id.etIntCalc_Period);
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
    private void calcInterest() {
        Log.i(this.getClass().getSimpleName(), "Calculating Interest...");
        double principal = Double.parseDouble(etPrincipal.getText().toString());
        double time = Double.parseDouble(etDate.getText().toString());

        String type = DateType.getSelectedItem().toString();
        //If month or days convert to year
        switch(type){
            case "Months":
                time /=MONTHS;
                break;
            case "Days":
                time /=DAYS;
                break;
        }

        //Calc rate of interest
        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        double amount = commonVO.getAccountVO().getAccountBalance();
        int creditScore = Integer.parseInt(tvDisplayCreditScore.getText().toString());
        Log.d(this.getClass().getSimpleName(), "Credit Score" + creditScore);
        //Adding (1-creditscore/1000) to the base interest rate
        Double rateOfInterest= INTERESTBASE + (1-(creditScore/1000.0));

        //Round off to 2 decimal places after point

        rateOfInterest = roundOff(rateOfInterest);
        interest = (rateOfInterest * principal * time) /100.0;
        interest = roundOff(interest);
    }

    private Double roundOff(double value){
        Double temp = new BigDecimal(value)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        return temp;
    }
}
