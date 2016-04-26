package com.cigital.insecurepay.fragments;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.activity.LoginActivity;
import com.cigital.insecurepay.common.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class InterestCalcFragmentTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void calculateInterestTest() {

        Calendar calendarObj = Calendar.getInstance();
        Constants.principal = calendarObj.get(Calendar.MILLISECOND)*calendarObj.get(Calendar.MILLISECOND);
        Constants.period  = calendarObj.get(Calendar.HOUR_OF_DAY);

        // Logging in
        Constants.login();

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Interest Calculator
        onView(withText(R.string.nav_interest_calc))
                .perform(click());


        // Enter some principal amount
        onView(withId(R.id.etIntCalc_FillPrincipalAmount)).
                perform(replaceText(String.valueOf(Constants.principal)), closeSoftKeyboard());


        // Enter some period for days/months/year
        onView(withId(R.id.etIntCalc_Date)).
                perform(typeText(String.valueOf(Constants.period)), closeSoftKeyboard());

        // Check that ROI and Interest are empty before Calculate button is clicked
        onView(withId(R.id.tvIntCalc_FillRateOfInterest))
                .check(matches((withText(""))));
        onView(withId(R.id.tvIntCalc_FillInterest))
                .check(matches((withText(""))));

        // Press Calculate Button
        onView(withId(R.id.btnIntCalc_Calc))
                .perform(click());

        // After clicking 'Calculate' Check that ROI and Interest are  displayed and is not empty
        onView(withId(R.id.tvIntCalc_FillRateOfInterest))
                .check(matches(isDisplayed())).check(matches(not(withText(""))));
        onView(withId(R.id.tvIntCalc_FillInterest))
                .check(matches(isDisplayed())).check(matches(not(withText(""))));

        Constants.logout();
    }
}