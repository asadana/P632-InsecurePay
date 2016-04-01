package com.cigital.insecurepay.fragments;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.activity.LoginActivity;
import com.cigital.insecurepay.activity.TransferActivity;
import com.cigital.insecurepay.common.Constants;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TransferFragmentTest {
    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);


    @Test
    public void fundsTransferTest() {
        init();

        Calendar calendarObj = Calendar.getInstance();
        Constants.transferAmount = calendarObj.get(Calendar.DATE);

        onView(withId(R.id.username)).
                perform(typeText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(Constants.correctPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.btnSignIn))
                .perform(click());

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Transfer Funds
        onView(withText(R.string.nav_transfer_funds))
                .perform(click());

        //Enter customer username
        onView(withId(R.id.etCust_username)).
                perform(typeText(Constants.receiverUserName), closeSoftKeyboard());

        //Enter amount
        onView(withId(R.id.ettransferAmount)).
                perform(typeText(String.valueOf(-Constants.transferAmount)), closeSoftKeyboard());

        //Enter message
        onView(withId(R.id.ettransferDetails)).
                perform(typeText(calendarObj.getTime().toString()), closeSoftKeyboard());


        //Click on Transfer
        onView(withId(R.id.btn_transfer))
                .perform(click());
        intended(hasComponent(TransferActivity.class.getName()));
        onView(withText(R.string.btnConfirm))
                .perform(click());


        //Lets cancel transfer
        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Transfer Funds
        onView(withText(R.string.nav_transfer_funds))
                .perform(click());

        //Enter customer username
        onView(withId(R.id.etCust_username)).
                perform(typeText(Constants.receiverUserName), closeSoftKeyboard());

        //Enter amount
        onView(withId(R.id.ettransferAmount)).
                perform(typeText(String.valueOf(Constants.transferAmount)), closeSoftKeyboard());

        //Enter message
        onView(withId(R.id.ettransferDetails)).
                perform(typeText(calendarObj.getTime().toString()), closeSoftKeyboard());
        //Click on Transfer
        onView(withId(R.id.btn_transfer))
                .perform(click());


        onView(withId(R.id.btnCancel))
                .perform(click());

        //Check Amount
        onView(withId(R.id.etCust_username)).check(matches(inTextEdit(Constants.receiverUserName)));
        onView(withId(R.id.ettransferAmount)).check(matches(inTextEdit(String.valueOf(Constants.transferAmount))));

        release();
    }

    //Matcher to match string in TextEdit
    public static Matcher<View> inTextEdit(final String input) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                EditText editTextObj = (EditText) view;
                String text = editTextObj.getText().toString();
                return input.equals(text);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
    
}