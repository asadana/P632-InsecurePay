package com.cigital.insecurepay.fragments;

import android.app.Activity;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.activity.LoginActivity;
import com.cigital.insecurepay.common.Constants;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
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
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AccountFragmentTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    private Activity activityObj;

    @Test
    public void changepasswordTest() {

        activityObj = loginActivityActivityTestRule.getActivity();

        // Logging in
        Constants.login();

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
                .perform(typeText(Constants.defaultPassword), closeSoftKeyboard());

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
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());

        onView(withId(R.id.password)).
                perform(replaceText(Constants.defaultPassword), closeSoftKeyboard());


        onView(withId(R.id.btnSignIn))
                .perform(click());


        onView(withText(R.string.login_successful))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        Constants.logout();
    }

    @Test
    public void accountUpdateTest() {

        activityObj = loginActivityActivityTestRule.getActivity();

        // Logging in
        Constants.login();

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Account Management
        onView(withText(R.string.nav_account_manage))
                .perform(click());

        onView(withId(R.id.btnAccount_update)).check(matches(not(isEnabled())));

        Calendar calendarObj = Calendar.getInstance();
        Constants.displayedText = Constants.simpleDateFormatObj.format(calendarObj.getTime());

        onView(withId(R.id.etAccount_fillAddressZip)).perform(replaceText(Constants.displayedText));

        //Click on Update
        onView(withId(R.id.btnAccount_update)).check(matches(isEnabled()))
                .perform(click());

        Constants.sleepWait();

        //Check toast
        onView(withText(activityObj.getString(R.string.account_update_successful)))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
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


        // Checking if the current value of zipcode is same as what we entered
        onView(withId(R.id.etAccount_fillAddressZip))
                .check(matches(new TypeSafeMatcher<View>() {
                    @Override
                    protected boolean matchesSafely(View view) {
                        EditText editText = (EditText) view;
                        String displayed = editText.getText().toString();
                        return displayed.equals(Constants.displayedText);
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }));

        // Logging out
        Constants.logout();
    }
}