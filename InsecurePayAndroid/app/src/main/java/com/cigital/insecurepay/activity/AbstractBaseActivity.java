package com.cigital.insecurepay.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cigital.insecurepay.R;
import com.google.gson.Gson;

public abstract class AbstractBaseActivity extends AppCompatActivity {
    protected String userAddress;
    protected String userPath;
    protected String serverAddress;
    protected Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAddress = getString(R.string.defaultAddress);
        userPath = getString(R.string.defaultPath);
        serverAddress = userAddress + userPath;
    }
}
