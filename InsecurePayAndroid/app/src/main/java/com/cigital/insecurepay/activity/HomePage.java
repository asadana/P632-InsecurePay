package com.cigital.insecurepay.activity;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.CustomerVO;
import com.cigital.insecurepay.common.Connectivity;
import com.cigital.insecurepay.fragments.AccountFragment;

public class HomePage extends AbstractBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AccountFragment.OnFragmentInteractionListener {
    protected Context contextHomePage = this;
    private CustDetailsRequestTask task = null;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        task = new CustDetailsRequestTask();
        task.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
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
        Fragment fragment = null;
        Class fragmentClass = null;

        Bundle bundleServerAddress = new Bundle();
        bundleServerAddress.putString("serverAddress", serverAddress);

        int id = item.getItemId();

        if (id == R.id.nav_account_manage) {
            fragmentClass = AccountFragment.class;
            Log.i(this.getClass().getSimpleName(), "Account Management selected");
            setTitle(R.string.nav_account_manage);
        } else if (id == R.id.nav_homepage) {

        } else if (id == R.id.nav_transfer_funds) {

        } else if (id == R.id.nav_interest_calc) {

        } else if (id == R.id.nav_support_chat) {

        } else if (id == R.id.nav_logout) {
            onLogOut();
            return true;
        }
        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
            }
            Log.i(this.getClass().getSimpleName(), "Creating fragment");
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.toString());
        }

        fragment.setArguments(bundleServerAddress);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContent, fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
     * Call login activity
     */
    public void onLogOut() {
        Intent intent = new Intent(HomePage.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class CustDetailsRequestTask extends AsyncTask<String, String, Boolean> {
        private CustomerVO customerDetails;

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d(this.getClass().getSimpleName(), "Background");
            try {
                //Converts customer details to CustomerVO
                Connectivity conn = new Connectivity(HomePage.this.getApplicationContext(), getString(R.string.cust_details_path), serverAddress);
                ContentValues contentValues = new ContentValues();
                contentValues.put(getString(R.string.username), "foo");
                customerDetails = gson.fromJson(conn.get(contentValues), CustomerVO.class);
                Log.d(this.getClass().getSimpleName(), "Customer details in HomePage" + customerDetails.getCity());
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Error while connecting: ", e);
            }
            return false;
        }

        protected void onPostExecute() {

        }

    }

}
