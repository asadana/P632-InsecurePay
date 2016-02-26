package com.cigital.insecurepay.activity;


import android.support.test.espresso.contrib.DrawerActions;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class HomePageTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    public static final String correctPassword = "12345";
    public static final String correctUsername = "foo";
    public static final String correctAccountNumber = "2005";
    public static final String correctBalance = "591.0";

    @Test
    public void checkHomePage() {
        onView(withId(R.id.username)).
                perform(typeText(correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(correctPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.sign_in_button))
                .perform(click());

        onView(withId(R.id.tvHome_fillAccountNumber))
                .check(matches(withText(correctAccountNumber)));

        onView(withId(R.id.tvHome_fillBalance))
                .check(matches(withText(correctBalance)));

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Account Management
        onView(withText(R.string.nav_account_manage))
                .perform(click());

        //Enter
    }

}