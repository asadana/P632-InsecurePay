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
 * InterestCalcFragment extends {@link Fragment}.
 * This class is used for calculating interest based on generated credit score.
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
    private Button btnCalculate;
    private EditText etPrincipal;
    private EditText etDate;
    private Spinner dateType;
    private TextView tvDisplayBalance;
    private TextView tvDisplayCreditScore;
    private TextView tvInterest;
    private TextView tvRateOfInterest;
    private TextView tvFillInterest;
    private TextView tvFillRateOfInterest;

    private CommonVO commonVO;
    private Integer creditScore;

    private String enterValidAmount = "Enter Valid Amount";

    /**
     * InterestCalcFragment is the default constructor for this class.
     */
    public InterestCalcFragment() {
    }


    /**
     * onCreateView is an overridden function that is called while the fragment is
     * being created.
     *
     * @param layoutInflater     Contains the {@link LayoutInflater} object that defines
     *                           the layout of the fragment
     * @param viewGroup          Contains the {@link ViewGroup} object to which this fragment
     *                           belongs to.
     * @param savedInstanceState Object of {@link Bundle} that is used to pass data to this
     *                           activity while creating it.
     * @return View         Return the {@link View} object created.
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        Log.i(this.getClass().getSimpleName(), "onCreateView: Initializing view.");

        View viewObj = layoutInflater.inflate(R.layout.fragment_interest_calc, viewGroup, false);

        creditScore = generateCreditScore();
        initValues(viewObj);

        //Credit score is init seperately
        tvDisplayCreditScore.setText((creditScore).toString());
        addListener();

        return viewObj;
    }

    /**
     * initValues is a function that is used to initialize the UI components
     * and other variables.
     *
     * @param viewObj Contains the {@link View} of the fragment.
     */
    private void initValues(View viewObj) {
        Log.d(this.getClass().getSimpleName(), "initValues: Initializing values.");

        tvDisplayBalance = (TextView) viewObj.findViewById(R.id.tvIntCalc_FillAccountBalance);
        tvDisplayBalance.setText(String.valueOf(commonVO.getAccountVO().getAccountBalance()));
        etPrincipal = (EditText) viewObj.findViewById(R.id.etIntCalc_FillPrincipalAmount);
        etDate = (EditText) viewObj.findViewById(R.id.etIntCalc_Date);
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
     * addListeners is a function that is called to attach listeners
     * to various components.
     */
    private void addListener() {
        Log.d(this.getClass().getSimpleName(), "addListener: Adding listeners.");

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String inputTime = String.valueOf(etDate.getText());
                String inputPrincipal = String.valueOf(etPrincipal.getText());

                if (etPrincipal.equals("")) {
                    etPrincipal.setError(enterValidAmount);
                    etPrincipal.requestFocus();
                    return;
                }
                double time = Double.parseDouble(inputTime);
                double principal = Double.parseDouble(inputPrincipal);
                calcInterest(time, principal);
            }
        });
    }

    /**
     * generateCreditScore is a function that is called to determine the credit score
     * given the current balance.
     *
     * @return int  Contains the credit score generated.
     */
    private int generateCreditScore() {
        Log.i(this.getClass().getSimpleName(), "generateCreditScore: Generating credit score.");

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

    /**
     * calcInterest is a function that is called ti calculate interest.
     *
     * @param time          Contains the time duration entered by the user.
     * @param principal     Contains the principal amount entered by the user.
     */
    private void calcInterest(double time, double principal) {
        Log.d(this.getClass().getSimpleName(), "calcInterest: Calculating interest.");


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

    /**
     * roundOff is a function that is called to round off to 2 decimal places after decimal.
     *
     * @param value Contains the value to be rounded off.
     * @return Double   Contains the double value returned after rounding off.
     */
    private Double roundOff(double value) {
        Log.d(this.getClass().getSimpleName(), "roundOff: Rounding off.");
        return new BigDecimal(value)
                .setScale(ROUNDOFFVALUE, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}
