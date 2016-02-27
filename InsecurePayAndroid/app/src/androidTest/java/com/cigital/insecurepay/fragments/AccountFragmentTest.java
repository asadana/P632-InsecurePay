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
 * Created by janakbhalla on 26/02/16.
 */
@RunWith(AndroidJUnit4.class)
public class AccountFragmentTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    public static final String correctPassword = "12345";
    public static final String correctUsername = "foo";
    public static final String newPassword = "abc";
    public static final String changePassowrdToast = "Password Changed to " + newPassword;

    @Test
    public void checkHomePage() {
        onView(withId(R.id.username)).
                perform(typeText(correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(correctPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.sign_in_button))
                .perform(click());

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Account Management
        onView(withText(R.string.nav_account_manage))
                .perform(click());

        //Click on Change Password
        onView(withId(R.id.btnAccount_changePassword))
                .perform(click());

        //Enter a new Password
        onView(withId(R.id.etxtChangePassword_newPassword))
                .perform(typeText(newPassword), closeSoftKeyboard());

        //Confirm new Password
        onView(withId(R.id.etxtChangePassword_confirmPassword))
                .perform(typeText(newPassword), closeSoftKeyboard());

        onView(withId(android.R.id.button1)).perform(click());

        onView(withText(changePassowrdToast))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on  Logout
        onView(withText(R.string.nav_logout))
                .perform(click());

        //Check login again with new Password
        onView(withId(R.id.username)).
                perform(typeText(correctUsername), closeSoftKeyboard());

        onView(withId(R.id.password)).
                perform(typeText(newPassword), closeSoftKeyboard());


        onView(withId(R.id.sign_in_button))
                .perform(click());


    }

}