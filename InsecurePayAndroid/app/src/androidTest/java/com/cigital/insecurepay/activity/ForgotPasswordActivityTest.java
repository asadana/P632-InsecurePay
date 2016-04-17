package com.cigital.insecurepay.activity;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.common.Constants;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class ForgotPasswordActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    private Activity activityObj;

    @Test
    public void passwordResetFail() {

        activityObj = loginActivityActivityTestRule.getActivity();

        onView(withId(R.id.btnForgotPassword)).
                perform(click());

        // Enter account number
        onView(withId(R.id.etForgotPassword_AccountNo)).
                perform(typeText(Constants.correctAccountNo), closeSoftKeyboard());
        // Enter SSN
        onView(withId(R.id.etForgotPassword_SSNNo)).
                perform(typeText(Constants.wrongSSN), closeSoftKeyboard());
        // Enter new password
        onView(withId(R.id.etForgotPassword_username)).
                perform(typeText(Constants.correctUsername), closeSoftKeyboard());

        // Send the request to reset password
        onView(withId(R.id.btn_send)).
                perform(click());

        onView(withText(R.string.information_mismatch))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void passwordResetPass() {

        activityObj = loginActivityActivityTestRule.getActivity();

        onView(withId(R.id.btnForgotPassword)).
                perform(click());

        // Enter account number
        onView(withId(R.id.etForgotPassword_AccountNo)).
                perform(typeText(Constants.correctAccountNo), closeSoftKeyboard());
        // Enter SSN
        onView(withId(R.id.etForgotPassword_SSNNo)).
                perform(typeText(Constants.correctSSN), closeSoftKeyboard());
        // Enter new password
        onView(withId(R.id.etForgotPassword_username)).
                perform(typeText(Constants.correctUsername), closeSoftKeyboard());

        // Send the request to reset password
        onView(withId(R.id.btn_send)).
                perform(click());

        onView(withText(R.string.default_password_link_sent))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

}
