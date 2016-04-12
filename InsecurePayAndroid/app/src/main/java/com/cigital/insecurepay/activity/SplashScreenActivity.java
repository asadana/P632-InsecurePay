package com.cigital.insecurepay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * SplashScreenActivity is an empty activity class with no layout file.
 * This class is used to display a splash screen to the user
 * before passing the control to LoginActivity.
 */
public class SplashScreenActivity extends AppCompatActivity {

    /**
     * onCreate is the first method called when the Activity is being created.
     * It puts the activity UI thread to sleep for 1500 milliseconds while the
     * splash screen is displayed. Then passes the control to LoginActivity class.
     *
     * @param savedInstanceState object that may be used to pass data to while
     *                           creating this Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Log.e(this.getClass().getSimpleName(), "onCreate: ", e);
        }
        Intent intentObj = new Intent(this, LoginActivity.class);
        startActivity(intentObj);
        finish();
    }
}
