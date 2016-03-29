package com.cigital.insecurepay.fragments;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.activity.LoginActivity;
import com.cigital.insecurepay.activity.LoginTest;
import com.cigital.insecurepay.common.Constants;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AccountFragmentTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void checkHomePage() {

        onView(withId(R.id.username)).
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(Constants.defaultPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.btnSignIn))
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
                .perform(typeText(Constants.correctPassword), closeSoftKeyboard());

        //Confirm new Password
        onView(withId(R.id.etChangePassword_confirmPassword))
                .perform(typeText(Constants.defaultPassword), closeSoftKeyboard());

        onView(withId(android.R.id.button1)).perform(click());


        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on  Logout
        onView(withText(R.string.nav_logout))
                .perform(click());

        //Check login again with new Password
        onView(withId(R.id.username)).
                perform(typeText(Constants.correctUsername), closeSoftKeyboard());

        onView(withId(R.id.password)).
                perform(typeText(Constants.correctPassword), closeSoftKeyboard());


        onView(withId(R.id.btnSignIn))
                .perform(click());


        onView(withText(R.string.login_successful))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkAccountUpdate() {

        onView(withId(R.id.username)).
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(Constants.defaultPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.btnSignIn))
                .perform(click());

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Account Management
        onView(withText(R.string.nav_account_manage))
                .perform(click());

        onView(withId(R.id.btnAccount_update)).check(matches(not(isEnabled())));

        onView(withId(R.id.tvAccount_fillUserDOB))
                .perform(click());

        Calendar calendarObj = Calendar.getInstance();
        Date dateObj = calendarObj.getTime();

        //Set date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(
                        calendarObj.get(Calendar.YEAR),
                        calendarObj.get(Calendar.MONTH),
                        calendarObj.get(Calendar.DATE)));
        onView(withId(android.R.id.button1)).perform(click());


        //Click on Update
        onView(withId(R.id.btnAccount_update))
                .perform(click());

        //Check toast
        onView(withText(LoginTest.activityObj.getString(R.string.account_update_successful)))
                .inRoot(withDecorView(not(LoginTest.activityObj.getWindow().getDecorView())))
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

        onView(withId(R.id.tvAccount_fillUserDOB))
                .check(matches(withText(calendarObj.get(Calendar.YEAR) +
                        "-" + calendarObj.get(Calendar.MONTH) +
                        "-" + calendarObj.get(Calendar.DATE))));
    }
}