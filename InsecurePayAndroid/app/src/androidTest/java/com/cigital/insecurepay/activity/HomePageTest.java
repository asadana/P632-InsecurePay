package com.cigital.insecurepay.activity;


import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.common.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class HomePageTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void homePageTest() {

        onView(withId(R.id.username)).
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(Constants.defaultPassword), closeSoftKeyboard());

        // First attempt with correct username and password
        onView(withId(R.id.btnSignIn))
                .perform(click());

        onView(withId(R.id.tvHome_fillAccountNumber))
                .check(matches(not(withText(""))));

        onView(withId(R.id.tvHome_fillBalance))
                .check(matches(not(withText(""))));

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
    }
}