package com.cigital.insecurepay.common;

import android.util.Log;

import com.cigital.insecurepay.R;

import java.text.SimpleDateFormat;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;

public class Constants {

    // For ForgotPasswordTest
    public static final String correctSSN = "132137";
    public static final String wrongSSN = "1234234";
    public static final String correctAccountNo = "2008";

    // For LoginActivityTest
    public static final String defaultPassword = "12345";
    public static final String correctUsername = "testUser";
    public static final String wrongInput = "testUserWrong";

    // For TransferFragmentTest
    public static final String receiverUserName = "foo";

    // For AccountFragmentTest
    public static SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat("SSSSS");
    public static String displayedText = "";
    public static int transferAmount = 0;

    // For InterestCalc
    public static int period = 0;
    public static double principal = 0;

    // Common login for all tests
    public static void login() {
        onView(withId(R.id.username)).
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(Constants.defaultPassword), closeSoftKeyboard());
        onView(withId(R.id.btnSignIn))
                .perform(click());
    }


    // Common logout for all tests
    public static void logout() {
        sleepWait();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.action_logout))
                .perform(click());
    }

    // Common sleep for all tests where needed
    public static void sleepWait() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            Log.e("Constants: ", "sleepWait: ", e);
        }
    }
}
