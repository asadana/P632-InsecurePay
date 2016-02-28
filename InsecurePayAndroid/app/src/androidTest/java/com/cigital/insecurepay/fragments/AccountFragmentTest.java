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
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AccountFragmentTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    public static final String correctPassword = "12345";
    public static final String correctUsername = "foo";
    public static final String newPassword = "abc";
    public static final String changePasswordToast = "Password changed to " + newPassword;

    public static final String updateEmail = "foofan@gmail.com";
    public static final String updatePhoneNo = "123456";
    public static final String updateState = "New York";
    public static final String updateCity = "New York City";
    public static final String updateStreetAddress = "Manhattan";
    public static final String updateZip = "1234";
    public static final String updateSuccessfulToast = "Update successful";
    /*
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
        onView(withId(R.id.etChangePassword_newPassword))
                .perform(typeText(newPassword), closeSoftKeyboard());

        //Confirm new Password
        onView(withId(R.id.etChangePassword_confirmPassword))
                .perform(typeText(newPassword), closeSoftKeyboard());

        onView(withId(android.R.id.button1)).perform(click());

        onView(withText(changePasswordToast))
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
    */

    @Test
    public void checkAccountUpdate() {
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


        //Enter details
        onView(withId(R.id.etAccount_fillEmail))
                .perform(clearText(), typeText(updateEmail));

        onView(withId(R.id.etAccount_fillPhone))
                .perform(clearText(), typeText(updatePhoneNo), closeSoftKeyboard());

        onView(withId(R.id.etAccount_fillAddressStreet))
                .perform(clearText(), typeText(updateStreetAddress), closeSoftKeyboard());

        onView(withId(R.id.etAccount_fillAddressCity))
                .perform(clearText(), typeText(updateCity), closeSoftKeyboard());

        onView(withId(R.id.etAccount_fillAddressState))
                .perform(clearText(), typeText(updateState), closeSoftKeyboard());

        onView(withId(R.id.etAccount_fillAddressZip))
                .perform(clearText(), typeText(updateZip), closeSoftKeyboard());

        //Click on Update
        onView(withId(R.id.btnAccount_update))
                .perform(click());

        //Check toast
        onView(withText(updateSuccessfulToast))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));


        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        onView(withText(R.string.nav_homepage))
                .perform(click());


        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on  Account Management
        onView(withText(R.string.nav_account_manage))
                .perform(click());

        //Check updated details
        onView(withId(R.id.etAccount_fillEmail))
                .check(matches(withText(updateEmail)));


        onView(withId(R.id.etAccount_fillPhone))
                .check(matches(withText(updatePhoneNo)));

        onView(withId(R.id.etAccount_fillAddressStreet))
                .check(matches(withText(updateStreetAddress)));


        onView(withId(R.id.etAccount_fillAddressCity))
                .check(matches(withText(updateCity)));

        onView(withId(R.id.etAccount_fillAddressState))
                .check(matches(withText(updateState)));

        onView(withId(R.id.etAccount_fillAddressZip))
                .check(matches(withText(updateZip)));


    }
}