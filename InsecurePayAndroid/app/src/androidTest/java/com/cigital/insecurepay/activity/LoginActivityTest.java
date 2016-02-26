package com.cigital.insecurepay.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import com.cigital.insecurepay.R;

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
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    public static final String correctUsername = "foo";
    public static final String wrongPassword = "abcdef";
    public static final String correctPassword = "12345";
    public static final String wrongUsername = "abc";
    public static final String wrongAccount = "1234234";


    public static final String URL = "http://10.0.0.3:8090/";
    public static final String path = "InsecurePayServiceServer/rest/";



    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);


    @Test
    public void changeServerAdress() {

        // Open the overflow menu OR open the options menu,
        // depending on if the device has a hardware or software overflow menu button.
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText(R.string.menu_change_url))
                .perform(click());
        // Enter URL
        onView(withId(R.id.etUrlAddress))
                .perform(typeText(URL), closeSoftKeyboard());

        //Enter Path
        onView(withId(R.id.etUrlPath))
                .perform(typeText(path), closeSoftKeyboard());

        onView(withId(android.R.id.button1)).perform(click());

        //Login Again

        onView(withId(R.id.username))
                .perform(typeText(correctUsername), closeSoftKeyboard());

        onView(withId(R.id.password)).
                perform(typeText(wrongPassword), closeSoftKeyboard());

        // First attempt
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withText(R.string.login_failed))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginFailTest() {
        onView(withId(R.id.username)).
                perform(typeText(correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(wrongPassword), closeSoftKeyboard());
        // First attempt
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withText(R.string.login_failed))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginPassTest() {
        init();
        onView(withId(R.id.username)).
                perform(typeText(correctUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(correctPassword), closeSoftKeyboard());
        // First attempt with correct username and password
        onView(withId(R.id.sign_in_button))
                .perform(click());
        intended(hasComponent(HomePage.class.getName()));
        release();
    }


   /* @Test
    public void loginUsernameExistsTest() {
        onView(withId(R.id.username)).
                perform(typeText(wrongUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(correctPassword), closeSoftKeyboard());
        // First attempt with wrong username
        onView(withId(R.id.username)).check(matches(withError(
                loginActivityActivityTestRule.getActivity().getString(R.string.username_does_not_exist))));
    }*/

    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
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
   /*@Test
    public void accountLockedTest() {
        onView(withId(R.id.username)).
                perform(typeText(typeUsername), closeSoftKeyboard());
        onView(withId(R.id.password)).
                perform(typeText(typePassword), closeSoftKeyboard());
        // Second attempt
        onView(withId(R.id.sign_in_button))
                .perform(click());
        // Third attempt
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withText(R.string.login_failed_account_locked))
                .inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }*/
}
