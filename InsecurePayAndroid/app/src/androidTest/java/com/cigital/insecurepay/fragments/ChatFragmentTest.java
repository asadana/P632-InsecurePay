package com.cigital.insecurepay.fragments;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.web.assertion.WebAssertion;
import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.activity.LoginActivity;
import com.cigital.insecurepay.common.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.clearElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;
import static android.support.v4.content.res.TypedArrayUtils.getResourceId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);



    @Test
    public void submitMessage()throws Exception {
        Calendar calendarObj = Calendar.getInstance();
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

        // Click on Chat
        onView(withText(R.string.nav_support_chat))
                .perform(click());
        //For safe side enable JS, without we Espresso can't test webviews
        onWebView().forceJavascriptEnabled();
        //Enter data in textarea
        onWebView().withElement(findElement(Locator.ID, "text")).perform(clearElement()).perform(DriverAtoms.webKeys(String.valueOf(calendarObj.DATE)));
        //Click on send Button

        onWebView().withElement(findElement(Locator.ID, "sendButton")).perform(webClick());
        //Check for thank you message
        //onView(withText(R.string.chatThankyou)).check(matches(isDisplayed()));
        //onView(withId(android.R.id.button1)).perform(click());
        //Constants.logout();
    }




}