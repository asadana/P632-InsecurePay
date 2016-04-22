package com.cigital.insecurepay.fragments;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.activity.LoginActivity;
import com.cigital.insecurepay.common.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.clearElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.getText;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void submitMessageTest() {

        // Logging in
        Constants.login();

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Chat
        onView(withText(R.string.nav_support_chat))
                .perform(click());
        // Without enable JS, Espresso can't test webviews
        onWebView().forceJavascriptEnabled();

        Calendar calendarObj = Calendar.getInstance();

        // Enter data in subject area
        onWebView().withElement(findElement(Locator.ID, "subject"))
                .perform(clearElement())
                .perform(DriverAtoms.webKeys(String.valueOf(calendarObj.DATE)));


        // Enter data in text area
        onWebView().withElement(findElement(Locator.ID, "text"))
                .perform(clearElement())
                .perform(DriverAtoms.webKeys(String.valueOf(calendarObj.DATE)));

        // Click on send Button and wait for response
        onWebView().withElement(findElement(Locator.ID, "sendButton")).perform(webClick());
        Constants.sleepWait();

        onWebView()
                .withElement(findElement(Locator.CLASS_NAME, "displaySubject"))
                .check(webMatches(getText(),
                        containsString("Thank you for your feedback on: "
                                + calendarObj.DATE)));
        // Logout
        Constants.logout();
    }


}