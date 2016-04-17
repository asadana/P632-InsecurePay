package com.cigital.insecurepay.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.common.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
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

        // Logging in
        Constants.login();

        // Checking if the user's values are retrieved on load
        onView(withId(R.id.tvHome_fillAccountNumber))
                .check(matches(not(withText(""))));
        onView(withId(R.id.tvHome_fillBalance))
                .check(matches(not(withText(""))));

        // Logging out
        Constants.logout();
    }
}