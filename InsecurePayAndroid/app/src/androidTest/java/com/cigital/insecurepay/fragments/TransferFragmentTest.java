package com.cigital.insecurepay.fragments;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.activity.LoginActivity;
import com.cigital.insecurepay.activity.TransferActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by janakbhalla on 11/03/16.
 */
@RunWith(AndroidJUnit4.class)
public class TransferFragmentTest {
    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    //public final ActivityTestRule<LoginActivity> TransferActivityTestRule = new ActivityTestRule<>(TransferActivity.class);

    public static final String senderPassword = "abc";
    public static final String senderUsername = "foo";
    public static final String receiverUsername = "amish";
    public static final String receiverPassword = "amish1502";
    public static final String transferAmount = "10";
    public static final String negativeTransferAmount = "-10";
    public static final String transferMessage = "Thank you so much!";
    public static final String transferNotification = "Transaction successful";

    @Test
    public void checkHomePage() {
        init();
        onView(withId(R.id.username)).
                perform(typeText(senderUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(senderPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.sign_in_button))
                .perform(click());

        // Open Drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Transfer Funds
        onView(withText(R.string.nav_transfer_funds))
                .perform(click());

        //Enter customer username
        onView(withId(R.id.etCust_username)).
                perform(typeText(receiverUsername), closeSoftKeyboard());

        //Enter amount
        onView(withId(R.id.ettransferAmount)).
                perform(typeText(transferAmount), closeSoftKeyboard());

        //Enter message
        onView(withId(R.id.ettransferDetails)).
                perform(typeText(transferMessage), closeSoftKeyboard());


        //Click on Transfer
        onView(withId(R.id.btn_transfer))
                .perform(click());
        intended(hasComponent(TransferActivity.class.getName()));
        onView(withText(R.string.btnConfirm))
                .perform(click());

        onView(withText(transferNotification))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));


        //Lets check negative funds transfer
        // Open Drawer
        /*onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Transfer Funds
        onView(withText(R.string.nav_transfer_funds))
                .perform(click());

        //Enter customer username
        onView(withId(R.id.etCust_username)).
                perform(typeText(receiverUsername), closeSoftKeyboard());

        //Enter amount
        onView(withId(R.id.ettransferAmount)).
                perform(typeText(negativeTransferAmount), closeSoftKeyboard());

        //Enter message
        onView(withId(R.id.ettransferDetails)).
                perform(typeText(transferMessage), closeSoftKeyboard());

        onView(withId(R.id.btn_transfer))
                .perform(click());



        intended(hasComponent(TransferActivity.class.getName()));
        onView(withText(R.string.btnConfirm))
                .perform(click());
                */
/*onView(withText(R.string.))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
        */

        //Lets cancel transfer
        // Open Drawer
        /*onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Click on Transfer Funds
        onView(withText(R.string.nav_transfer_funds))
                .perform(click());

        //Enter customer username
        onView(withId(R.id.etCust_username)).
                perform(typeText(receiverUsername), closeSoftKeyboard());

        //Enter amount
        onView(withId(R.id.ettransferAmount)).
                perform(typeText(negativeTransferAmount), closeSoftKeyboard());

        //Enter message
        onView(withId(R.id.ettransferDetails)).
                perform(typeText(transferMessage), closeSoftKeyboard());

        onView(withId(R.id.btnCancel))
                .perform(click());
*/
        release();
    }
}