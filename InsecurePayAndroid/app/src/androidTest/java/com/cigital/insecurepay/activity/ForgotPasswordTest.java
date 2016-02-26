package com.cigital.insecurepay.activity;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.cigital.insecurepay.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class ForgotPasswordTest {

    public static final String correctSSN = "222222";
    public static final String wrongSSN = "1234234";
    public static final String correctAccountNo = "2000";
    public static final String defaultPassword = "12345";
    public static final String correctPassword = "123";
    public static final String correctUsername = "foo";

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void passwordRestPass() {
        onView(withId(R.id.btn_forgot_password)).
                perform(click());
        // Enter account number
        onView(withId(R.id.etForgotPassword_AccountNo)).
                perform(typeText(correctAccountNo), closeSoftKeyboard());
        // Enter SSN
        onView(withId(R.id.etForgotPassword_SSNNo)).
                perform(typeText(correctSSN), closeSoftKeyboard());
        // Enter new password
        onView(withId(R.id.etForgotPassword_username)).
                perform(typeText(correctUsername), closeSoftKeyboard());

        onView(withId(R.id.btn_send)).
                perform(click());

        onView(withText(R.string.default_password_linksent))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        Espresso.pressBack();

        init();
        onView(withId(R.id.username)).
                perform(typeText(correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(defaultPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.sign_in_button))
                .perform(click());
        intended(hasComponent(HomePage.class.getName()));
        release();

    }

    @Test
    public void passwordRestFail() {
        onView(withId(R.id.btn_forgot_password)).
                perform(click());
        // Enter account number
        onView(withId(R.id.etForgotPassword_AccountNo)).
                perform(typeText(correctAccountNo), closeSoftKeyboard());
        // Enter SSN
        onView(withId(R.id.etForgotPassword_SSNNo)).
                perform(typeText(wrongSSN), closeSoftKeyboard());
        // Enter new password
        onView(withId(R.id.etForgotPassword_username)).
                perform(typeText(correctUsername), closeSoftKeyboard());

        onView(withId(R.id.btn_send)).
                perform(click());

        onView(withText(R.string.information_mismatch))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

    }

}