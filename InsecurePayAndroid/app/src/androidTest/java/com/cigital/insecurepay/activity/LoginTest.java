package com.cigital.insecurepay.activity;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.cigital.insecurepay.DBHelper.LoginDBHelper;
import com.cigital.insecurepay.R;
import com.cigital.insecurepay.common.Constants;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class LoginTest {

    public static Activity activityObj;

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginPassTest() {

        activityObj = loginActivityActivityTestRule.getActivity();
        // Getting database and deleting it
        activityObj = loginActivityActivityTestRule.getActivity();
        activityObj.deleteDatabase(LoginDBHelper.TABLE_NAME_LOGIN);
        activityObj.finish();
        activityObj.startActivity(activityObj.getIntent());

        // Logging in
        Constants.login();

        Constants.sleepWait();

        onView(withText(R.string.login_successful))
                .inRoot(withDecorView(not(activityObj.getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        // Logging out
        Constants.logout();
    }
}
