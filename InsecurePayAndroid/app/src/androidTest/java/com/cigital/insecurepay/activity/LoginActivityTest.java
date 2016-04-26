package com.cigital.insecurepay.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import com.cigital.insecurepay.DBHelper.LoginDBHelper;
import com.cigital.insecurepay.R;
import com.cigital.insecurepay.common.Constants;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    private Activity activityObj;

    // A matcher to match the credentials after coming back to the app
    private static Matcher<View> inTextEdit(final String credentials) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                EditText editTextObj = (EditText) view;
                String text = editTextObj.getText().toString();

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

                EditText editTextObj = (EditText) view;
                return editTextObj.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    @Test
    public void loginFailTest() {

        // Getting database and deleting it
        activityObj = loginActivityActivityTestRule.getActivity();
        activityObj.deleteDatabase(LoginDBHelper.TABLE_NAME_LOGIN);
        activityObj.finish();
        activityObj.startActivity(activityObj.getIntent());

        onView(withId(R.id.username)).
                perform(replaceText(Constants.correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(replaceText(Constants.wrongInput), closeSoftKeyboard());

        onView(withId(R.id.btnSignIn))
                .perform(click());

        onView(withId(R.id.password))
                .check(matches(withError(activityObj.getString(R.string.error_incorrect_password))));
        onView(withText(R.string.login_failed))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void rememberMeTest() {

        // Getting database and deleting it
        activityObj = loginActivityActivityTestRule.getActivity();
        activityObj.deleteDatabase(LoginDBHelper.TABLE_NAME_LOGIN);
        activityObj.finish();
        activityObj.startActivity(activityObj.getIntent());

        // Getting the shared preference and clearing it
        SharedPreferences loginPreferences = activityObj.getSharedPreferences(activityObj.getString(R.string.sharedPreferenceLogin),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editorObj = loginPreferences.edit();
        editorObj.clear();
        editorObj.commit();


        // check the checkbox
        onView(withId(R.id.saveLoginCheckBox))
                .check(matches(not(isChecked())))
                .perform(click())
                .check(matches(isChecked()));

        Constants.login();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText(R.string.action_logout))
                .perform(click());

        // Getting the activity, closing it and restarting it
        activityObj.finish();
        activityObj.startActivity(activityObj.getIntent());

        // Checking if the edit text fields retain their values
        onView(withId(R.id.username)).check(matches(inTextEdit(Constants.correctUsername)));
        onView(withId(R.id.password)).check(matches(inTextEdit(Constants.defaultPassword)));

        Constants.sleepWait();

        // Check if the checkbox is checked
        onView(withId(R.id.saveLoginCheckBox))
                .check(matches(isChecked()))
                .perform(click());

        onView(withId(R.id.btnSignIn))
                .perform(click());

        // Logging out
        Constants.logout();
    }
}
