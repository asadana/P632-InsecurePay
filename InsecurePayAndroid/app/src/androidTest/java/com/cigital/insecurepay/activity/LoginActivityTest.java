package com.cigital.insecurepay.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.common.Constants;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private String wrongUsername = "abc";
    private String wrongPassword = "notyourpassword";
    private String wrongAccount = "1234234";
    private Activity activityObj;

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginUsernameExistsTest() {
        onView(withId(R.id.username)).
                perform(replaceText(wrongUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(Constants.defaultPassword), closeSoftKeyboard());
        // First attempt with wrong username
        onView(withId(R.id.username)).check(matches(withError(
                loginActivityActivityTestRule.getActivity().getString(R.string.error_username_does_not_exist))));
    }

    @Test
    public void accountLockedTest() throws InterruptedException {

        // Getting database and deleting it
        activityObj = loginActivityActivityTestRule.getActivity();
        activityObj.deleteDatabase(activityObj.getString(R.string.tableLoginTrials));
        activityObj.finish();
        activityObj.startActivity(activityObj.getIntent());

        onView(withId(R.id.username)).
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(wrongPassword), closeSoftKeyboard());

        // First attempt
        onView(withId(R.id.btnSignIn))
                .perform(click());
        onView(withText(R.string.login_failed))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        // Second attempt
        onView(withId(R.id.btnSignIn))
                .perform(click());
        SystemClock.sleep(Toast.LENGTH_SHORT);
        onView(withText(R.string.login_failed))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        // Third attempt
        onView(withId(R.id.btnSignIn))
                .perform(click());//.wait(Toast.LENGTH_SHORT);
        SystemClock.sleep(Toast.LENGTH_SHORT);
        onView(withText(R.string.login_failed))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnSignIn))
                .perform(click());//.wait(Toast.LENGTH_SHORT);
        SystemClock.sleep(Toast.LENGTH_SHORT);
        onView(withText(R.string.login_failed_account_locked))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void rememberMeTest() {
/*

        // Getting the shared preference and clearing it
        SharedPreferences loginPreferences = activityObj.getSharedPreferences(activityObj.getString(R.string.sharedPreferenceLogin),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editorObj = loginPreferences.edit();
        editorObj.clear();
        editorObj.commit();
*/

        // check the checkbox
        onView(withId(R.id.saveLoginCheckBox))
                .check(matches(not(isChecked())))
                .perform(click())
                .check(matches(isChecked()));

        onView(withId(R.id.username)).
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(Constants.defaultPassword), closeSoftKeyboard());

        onView(withId(R.id.btnSignIn))
                .perform(click());

        // Getting the activity, closing it and restarting it
        activityObj.finish();
        activityObj.startActivity(activityObj.getIntent());

        onView(withId(R.id.username)).check(matches(inTextEdit(Constants.correctUsername)));
        onView(withId(R.id.password)).check(matches(inTextEdit(Constants.defaultPassword)));

        onView(withId(R.id.saveLoginCheckBox))
                .check(matches(isChecked()))
                .perform(click());
    }


    @Test
    public void loginFailTest() {
        onView(withId(R.id.username)).
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(wrongPassword), closeSoftKeyboard());
        // First attempt
        onView(withId(R.id.btnSignIn))
                .perform(click());
        onView(withText(R.string.login_failed))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    // A matcher to match the credentials after coming back to the app
    public static Matcher<View> inTextEdit(final String credentials) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }

                String text = ((EditText) view).getText().toString();

                return credentials.equals(text);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    // A Matcher to match the error message displayed on EditText
    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) throws NullPointerException {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return editText.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}
