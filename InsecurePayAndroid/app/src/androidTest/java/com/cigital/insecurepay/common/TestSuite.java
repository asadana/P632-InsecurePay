package com.cigital.insecurepay.common;

import com.cigital.insecurepay.activity.ForgotPasswordActivityTest;
import com.cigital.insecurepay.activity.HomePageActivityTest;
import com.cigital.insecurepay.activity.LoginActivityTest;
import com.cigital.insecurepay.activity.LoginTest;
import com.cigital.insecurepay.fragments.AccountFragmentTest;
import com.cigital.insecurepay.fragments.ActivityHistoryFragmentTest;
import com.cigital.insecurepay.fragments.ChatFragmentTest;
import com.cigital.insecurepay.fragments.InterestCalcFragmentTest;
import com.cigital.insecurepay.fragments.TransferFragmentTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ForgotPasswordActivityTest.class,
        LoginActivityTest.class,
        LoginTest.class,
        HomePageActivityTest.class,
        AccountFragmentTest.class,
        TransferFragmentTest.class,
        ActivityHistoryFragmentTest.class,
        InterestCalcFragmentTest.class,
        ChatFragmentTest.class
})
public class TestSuite {}
