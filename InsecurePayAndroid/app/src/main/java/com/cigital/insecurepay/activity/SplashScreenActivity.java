package com.cigital.insecurepay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashScreenActivity extends AppCompatActivity {

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
