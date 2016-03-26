package com.cigital.insecurepay.fragments;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.activity.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ActivityHistoryFragmentTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    public static final String incorrectAccNumber = "2000";
    public static final String correctAccNumber = "2005";
    public static final String correctPassword = "abcde";
    public static final String correctUsername = "foo";

    @Test
    public void checkRetrieval() {
        onView(withId(R.id.username)).
                perform(typeText(correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(correctPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.btnSignIn))
                .perform(click());

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Account Management
        onView(withText(R.string.nav_activity_history))
                .perform(click());

        //Check for correct account number
        onView(withId(R.id.etActivityHistory_AccountNo))
                .check(matches(withText(correctAccNumber)));


        onView(withId(R.id.btnSubmit))
                .perform(click());

        //Check whether there is a list
        onView(withId(R.id.lvActivityHistory_transactionList))
                .perform(click())
                .check(matches(isDisplayed()));

    }

    @Test
    public void checkHPERetrieval() {
        onView(withId(R.id.username)).
                perform(typeText(correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(correctPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.btnSignIn))
                .perform(click());

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Account Management
        onView(withText(R.string.nav_activity_history))
                .perform(click());

        //Check for correct account number
        onView(withId(R.id.etActivityHistory_AccountNo))
                .check(matches(withText(correctAccNumber)));

        //Perform HPE
        onView(withId(R.id.etActivityHistory_AccountNo))
                .perform(clearText(), typeText(incorrectAccNumber), closeSoftKeyboard());

        onView(withId(R.id.btnSubmit))
                .perform(click());

        //Check whether there is a list
        onView(withId(R.id.lvActivityHistory_transactionList))
                .perform(click())
                .check(matches(isDisplayed()));

    }

}