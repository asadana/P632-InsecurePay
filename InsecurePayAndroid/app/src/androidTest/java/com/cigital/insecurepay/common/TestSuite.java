package com.cigital.insecurepay.common;

import com.cigital.insecurepay.activity.ForgotPasswordTest;
import com.cigital.insecurepay.activity.HomePageTest;
import com.cigital.insecurepay.activity.LoginTest;
import com.cigital.insecurepay.fragments.AccountFragment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ForgotPasswordTest.class,
        //LoginActivityTest.class,
        LoginTest.class,
        HomePageTest.class,
        AccountFragment.class//,
       // TransferFragmentTest.class,
        //ActivityHistoryFragmentTest.class
})
public class TestSuite {}
