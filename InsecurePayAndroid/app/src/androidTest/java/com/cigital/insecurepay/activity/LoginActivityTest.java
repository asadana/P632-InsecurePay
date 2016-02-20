package com.cigital.insecurepay.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cigital.insecurepay.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

/**
 * Created by ankit-arch on 2/19/16.
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    public static final String typeUsername = "asadana";
    public static final String typePassword = "abcdef";

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    // This test will fail if the account is already locked
    @Test
    public void loginFailTest() {
        onView(withId(R.id.username)).
                perform(typeText(typeUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(typePassword), closeSoftKeyboard());
        // First attempt
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withText(R.string.login_failed))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

   /* @Test
    public void accountLockedTest() {
        onView(withId(R.id.username)).
                perform(typeText(typeUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(typePassword), closeSoftKeyboard());
        // Second attempt
        onView(withId(R.id.sign_in_button))
                .perform(click());
        // Third attempt
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withText(R.string.login_failed_account_locked))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }*/
}
