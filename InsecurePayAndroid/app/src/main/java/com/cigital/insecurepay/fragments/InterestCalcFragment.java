package com.cigital.insecurepay.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.math.BigDecimal;
import java.util.Random;

/**
 * InterestCalcFragment extends Fragment and is used for calculating interest based on credit score
 */
public class InterestCalcFragment extends Fragment {


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
    //Credit Score Rank
    private static final int RANK_ONE = 1;
    private static final int RANK_TWO = 2;
    private static final int RANK_THREE = 3;
    private static final int RANK_FOUR = 4;
    //Balance Range
    private static final double RANGE_BASE = 0;
    private static final double RANGE_LEVEL1 = 1000;
    private static final double RANGE_LEVEL2 = 4000;
    private static final double RANGE_LEVEL3 = 10000;
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
    private Spinner dateType;
    private TextView tvInterest;
    private TextView tvRateOfInterest;
    private TextView tvFillInterest;
    private TextView tvFillRateOfInterest;
    private CommonVO commonVO;
    private Integer creditScore;
    private Double interest;
    private HomeFragment.OnFragmentInteractionListener mListener;

    public InterestCalcFragment() {
        // Required empty public constructor
    }

    // onCreateView is called when the class's view is being generated
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

    // generateCreditScore is called to determine the credit score
    private int generateCreditScore() {
        Log.i(this.getClass().getSimpleName(), "Generating credit Score ... ");
        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        double account_bal = commonVO.getAccountVO().getAccountBalance();
        //Determine credit score range depending on Account Balance
        int creditScoreRank = RANK_ONE;
        if (account_bal >= RANGE_BASE && account_bal <= RANGE_LEVEL1) {
            creditScoreRank = RANK_FOUR;
        } else if (account_bal > RANGE_LEVEL1 && account_bal <= RANGE_LEVEL2) {
            creditScoreRank = RANK_THREE;
        } else if (account_bal > RANGE_LEVEL2 && account_bal <= RANGE_LEVEL3) {
            creditScoreRank = RANK_TWO;
        }

        Random r = new Random((long) account_bal);
        int creditScore = 0;
        switch (creditScoreRank) {
            //Excellent
            case RANK_ONE: {
                creditScore = r.nextInt(MAXSCORE - EXCELLENT) + EXCELLENT;
                break;
            }
            //Good
            case RANK_TWO: {
                creditScore = r.nextInt(EXCELLENT - GOOD) + GOOD;
                break;
            }
            //Fair
            case RANK_THREE: {
                creditScore = r.nextInt(GOOD - FAIR) + FAIR;
                break;
            }
            //Bad
            case RANK_FOUR: {
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
                String inputTime = String.valueOf(etDate.getText());
                String inputPrincipal = String.valueOf(etPrincipal.getText());

                if (etPrincipal.equals("")) {
                    etPrincipal.setError("Enter Valid Amount");
                    etPrincipal.requestFocus();
                    return;
                }
                double time = Double.parseDouble(inputTime);
                double principal = Double.parseDouble(inputPrincipal);
                calcInterest(time, principal);
            }
        });
    }

    // Initializes all the variables
    private void initValues(View viewObj) {
        Log.i(this.getClass().getSimpleName(), "Initializing Values..");
        tvBalance = (TextView) viewObj.findViewById(R.id.tvIntCalc_AccountBalance);
        tvDisplayBalance = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillAccountBalance);
        tvDisplayBalance.setText(String.valueOf(commonVO.getAccountVO().getAccountBalance()));
        tvPrincipal = (TextView) viewObj.findViewById(R.id.tvIntCalc_PrincipalAmount);
        etPrincipal = (EditText) viewObj.findViewById(R.id.etIntCalc_FillPrincipalAmount);
        etDate = (EditText) viewObj.findViewById(R.id.etIntCalc_Date);
        tvPeriod = (TextView) viewObj.findViewById(R.id.tvIntCalc_Period);
        tvCreditScore = (TextView) viewObj.findViewById(R.id.tvIntCalc_CreditScore);
        tvDisplayCreditScore = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillCreditScore);
        tvInterest = (TextView) viewObj.findViewById(R.id.tvIntCalc_Interest);
        tvRateOfInterest = (TextView) viewObj.findViewById(R.id.tvIntCalc_RateOfInterest);
        tvFillInterest = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillInterest);
        tvFillRateOfInterest = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillRateOfInterest);
        btnCalculate = (Button) viewObj.findViewById(R.id.btnIntCalc_Calc);
        dateType = (Spinner) viewObj.findViewById(R.id.etIntCalc_Period);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.Date, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateType.setAdapter(adapter);

    }

    /**
     * calcInterest is called for calculating interest
     */
    private void calcInterest(double time, double principal) {
        Log.i(this.getClass().getSimpleName(), "Calculating Interest...");


        String type = dateType.getSelectedItem().toString();
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
        Double interest = (principal * rateOfInterest * time) / INTERESTDIVISOR;
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
