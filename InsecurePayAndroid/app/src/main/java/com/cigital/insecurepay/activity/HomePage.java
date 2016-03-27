package com.cigital.insecurepay.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.AccountVO;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.common.PostAsyncCommonTask;
import com.cigital.insecurepay.fragments.AccountFragment;
import com.cigital.insecurepay.fragments.ActivityHistoryFragment;
import com.cigital.insecurepay.fragments.HomeFragment;
import com.cigital.insecurepay.fragments.InterestCalcFragment;
import com.cigital.insecurepay.fragments.TransferFragment;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener {
    private DrawerLayout drawer;

    // For handling fragments
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fragment = null;
    private Class fragmentClass = null;
    private CommonVO commonVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // To allow Screenshots
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

        fragmentClass = HomeFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            if (fragment != null) {
                fragment.setArguments(getIntent().getExtras());
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContent, fragment).commit();
        } catch (InstantiationException e) {
            Log.e(this.getClass().getSimpleName(), e.toString());
        } catch (IllegalAccessException e) {
            Log.e(this.getClass().getSimpleName(), e.toString());
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView tvCustUserName = (TextView) findViewById(R.id.tvNavHeaderUsername);
        tvCustUserName.setText(((CommonVO) getIntent().getSerializableExtra(getString(R.string.common_VO))).getUsername());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            //Calls action to be taken when 'Log out' button is pressed
            onLogOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_account_manage) {
            fragmentClass = AccountFragment.class;
            Log.i(this.getClass().getSimpleName(), "Account Management selected");
            setTitle(R.string.nav_account_manage);
        } else if (id == R.id.nav_homepage) {
            fragmentClass = HomeFragment.class;
            Log.i(this.getClass().getSimpleName(), "Home Fragment selected");
            setTitle(R.string.nav_homepage);
        } else if (id == R.id.nav_transfer_funds) {
            fragmentClass = TransferFragment.class;
            Log.i(this.getClass().getSimpleName(), "Transfer Fragment selected");
            setTitle(R.string.nav_transfer_funds);
        } else if (id == R.id.nav_activity_history) {
            fragmentClass = ActivityHistoryFragment.class;
            Log.i(this.getClass().getSimpleName(), "Activity History Selected");
            setTitle(R.string.nav_activity_history);
        } else if (id == R.id.nav_interest_calc) {
            fragmentClass = InterestCalcFragment.class;
            Log.i(this.getClass().getSimpleName(), "Interest Calculator Selected");
            setTitle(R.string.nav_interest_calc);
        } else if (id == R.id.nav_support_chat) {

        } else if (id == R.id.nav_logout) {
            onLogOut();
            return true;
        }
        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.toString());
        }

        if (fragment != null) {
            fragment.setArguments(getIntent().getExtras());
        }

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContent, fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
     * Call login activity
     */
    public void onLogOut() {
        CommonVO commonVO = (CommonVO) getIntent().getSerializableExtra(getString(R.string.common_VO));
        LogoutTask logout = new LogoutTask(this, commonVO.getServerAddress(), getString(R.string.logout_path));
        logout.execute();

    }


    //Set Account Details in CommonVO
    @Override
    public void setAccDetails(AccountVO accountVO) {
        CommonVO commonVO = (CommonVO) getIntent().getSerializableExtra(getString(R.string.common_VO));
        commonVO.setAccountVO(accountVO);
        getIntent().putExtra(getString(R.string.common_VO), commonVO);
    }

    private class LogoutTask extends PostAsyncCommonTask<String> {

        public LogoutTask(Context contextObj, String serverAddress, String path) {
            super(contextObj, serverAddress, path, null, String.class);
        }


        @Override
        protected void postSuccess(String resultObj) {
            connectivityObj.deleteCookies();
            Intent intent = new Intent(HomePage.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
