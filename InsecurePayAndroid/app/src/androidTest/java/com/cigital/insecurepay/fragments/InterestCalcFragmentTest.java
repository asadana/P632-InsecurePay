package com.cigital.insecurepay.fragments;

import android.app.Activity;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.ViewMatchers;
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
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class InterestCalcFragmentTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    private Activity activityObj;

    @Test
    public void calcInterest() {

        activityObj = loginActivityActivityTestRule.getActivity();

        Calendar calendarObj = Calendar.getInstance();
        Constants.principal = calendarObj.get(Calendar.MILLISECOND)*calendarObj.get(Calendar.MILLISECOND);
        Constants.period  = calendarObj.get(Calendar.HOUR_OF_DAY);

        onView(withId(R.id.username)).
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(Constants.defaultPassword), closeSoftKeyboard());

        // Enter correct username and password
        onView(withId(R.id.btnSignIn))
                .perform(click());

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Interest Calculator
        onView(withText(R.string.nav_interest_calc))
                .perform(click());


        //Enter some principal amount
        onView(withId(R.id.etIntCalc_FillPrincipalAmount)).
                perform(replaceText(String.valueOf(Constants.principal)), closeSoftKeyboard());


        //Enter some period for days/months/year
        onView(withId(R.id.etIntCalc_Date)).
                perform(typeText(String.valueOf(Constants.period)), closeSoftKeyboard());
        //Check that ROI and Interest are empty
        onView(withId(R.id.tvIntCalc_FillRateOfInterest))
                .check(matches((withText(""))));

        onView(withId(R.id.tvIntCalc_FillInterest))
                .check(matches((withText(""))));
        //Hit Calculate
        onView(withId(R.id.btnIntCalc_Calc))
                .perform(click());

        //After clicking 'Calculate' Check that ROI and Interest are  displayed
        onView(withId(R.id.tvIntCalc_FillRateOfInterest)).check(matches(isDisplayed()));
        onView(withId(R.id.tvIntCalc_FillInterest)).check(matches(isDisplayed()));


        Constants.logout();
    }


}