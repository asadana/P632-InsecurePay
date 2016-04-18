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
import com.cigital.insecurepay.fragments.ChatFragment;
import com.cigital.insecurepay.fragments.HomeFragment;
import com.cigital.insecurepay.fragments.InterestCalcFragment;
import com.cigital.insecurepay.fragments.TransferFragment;

/**
 * HomePageActivity is an activity that is the entry point after the user logs in.
 * This activity also acts as a host to Fragments and Navigation Drawer.
 */
public class HomePageActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener {

    // Drawer component
    private DrawerLayout drawer;

    // For handling fragments
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fragment = null;
    private Class fragmentClass = null;

    private CommonVO commonVO;

    /**
     * onCreate is the first method called when the Activity is being created.
     * It populates and initializes the text views.
     *
     * @param savedInstanceState Object that may be  used to pass data to this activity while
     *                           creating it.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(this.getClass().getSimpleName(), "onCreate: Initializing HomePageActivity");
        setContentView(R.layout.activity_home_page);

        // To allow Screenshots
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

        // Fragment loaded as default
        fragmentClass = HomeFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            if (fragment != null) {
                fragment.setArguments(getIntent().getExtras());
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContent, fragment).commit();
        } catch (InstantiationException e) {
            Log.e(this.getClass().getSimpleName(), "onCreate: ", e);
        } catch (IllegalAccessException e) {
            Log.e(this.getClass().getSimpleName(), "onCreate: ", e);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Adding username in the navigation drawer
        TextView tvUsername = (TextView) findViewById(R.id.tvNavHeaderUsername);
        tvUsername.setText(((CommonVO) getIntent()
                .getSerializableExtra(getString(R.string.common_VO)))
                .getUsername());
    }

    /**
     * onBackPressed is called when the user presses the hardware back button.
     * This function closes the navigation drawer if it is open when back is pressed.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * onCreateOptionsMenu function is called after onCreate and is used to create
     * an options menu in the current activity.
     *
     * @param menu Contains the options overflow menu.
     * @return boolean Returns true since we are enabling the Options Menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    /**
     * onOptionsItemSelected function is called when one of the items in the options
     * overflow menu is selected.
     *
     * @param item Contains the item that the user selected.
     */
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

    /**
     * onNavigationItemSelected is a function that is called when the user selects
     * one of the items from the navigation drawer.
     *
     * @param item Contains the item selected from the navigation drawer.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Get the id of the item selected
        int id = item.getItemId();

        // If condition checks which item was selected from te navigation drawer
        if (id == R.id.nav_account_manage) {

            fragmentClass = AccountFragment.class;
            Log.i(this.getClass().getSimpleName(),
                    "onNavigationItemSelected: Account Management selected");
            setTitle(R.string.nav_account_manage);

        } else if (id == R.id.nav_homepage) {

            fragmentClass = HomeFragment.class;
            Log.i(this.getClass().getSimpleName(),
                    "onNavigationItemSelected: Home Fragment selected");
            setTitle(R.string.nav_homepage);

        } else if (id == R.id.nav_transfer_funds) {

            fragmentClass = TransferFragment.class;
            Log.i(this.getClass().getSimpleName(),
                    "onNavigationItemSelected: Transfer Fragment selected");
            setTitle(R.string.nav_transfer_funds);

        } else if (id == R.id.nav_activity_history) {

            fragmentClass = ActivityHistoryFragment.class;
            Log.i(this.getClass().getSimpleName(),
                    "onNavigationItemSelected: Activity History Fragment selected");
            setTitle(R.string.nav_activity_history);

        } else if (id == R.id.nav_interest_calc) {

            fragmentClass = InterestCalcFragment.class;
            Log.i(this.getClass().getSimpleName(),
                    "onNavigationItemSelected: Interest Calculator Fragment selected");
            setTitle(R.string.nav_interest_calc);

        } else if (id == R.id.nav_support_chat) {

            fragmentClass = ChatFragment.class;
            Log.i(this.getClass().getSimpleName(),
                    "onNavigationItemSelected: Chat Fragment selected");
            setTitle(R.string.nav_support_chat);

        } else if (id == R.id.nav_logout) {
            Log.i(this.getClass().getSimpleName(),
                    "onNavigationItemSelected: Logging out.");
            onLogOut();
            return true;
        }
        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();

                if (fragment != null) {
                    fragment.setArguments(getIntent().getExtras());
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContent, fragment).commit();
            }
        } catch (InstantiationException e) {
            Log.e(this.getClass().getSimpleName(), "onNavigationItemSelected: ", e);
        } catch (IllegalAccessException e) {
            Log.e(this.getClass().getSimpleName(), "onNavigationItemSelected: ", e);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * onLogOut is a function that is called to log the user out.
     * This function calls the LogoutTask instance to communicate with the server.
     */
    public void onLogOut() {
        CommonVO commonVO = (CommonVO) getIntent().getSerializableExtra(getString(R.string.common_VO));
        LogoutTask logout = new LogoutTask(this, commonVO.getServerAddress(), getString(R.string.logout_path));
        logout.execute();
    }

    /**
     * setAccDetails is a function that is called to take account details retrieved
     * and pass them as intent to child fragments.
     *
     * @param accountVO Contains the account details for the user.
     */
    @Override
    public void setAccDetails(AccountVO accountVO) {
        CommonVO commonVO = (CommonVO) getIntent().getSerializableExtra(getString(R.string.common_VO));
        commonVO.setAccountVO(accountVO);
        getIntent().putExtra(getString(R.string.common_VO), commonVO);
    }

    /**
     * LogoutTask extends PostAsyncCommonTask to asynchronously communicate with the
     * server and perform logout for the user.
     */
    private class LogoutTask extends PostAsyncCommonTask<String> {

        /**
         * ForgotPasswordTask is the parametrized constructor of ForgotPasswordTask
         *
         * @param contextObj    Contains the context of the parent activity.
         * @param serverAddress Contains the server url/address .
         * @param path          Contains the sub-path to the service that needs to be used.
         */
        public LogoutTask(Context contextObj, String serverAddress, String path) {
            super(contextObj, serverAddress, path, null, String.class);
        }

        /**
         * postSuccess is called when the server responds with a non-error code response.
         * This function performs all the tasks to be done in postExecute when server response
         * is not an error.
         *
         * @param resultObj Contains the string sent from the server as part of the response.
         */
        @Override
        protected void postSuccess(String resultObj) {
            Log.d(this.getClass().getSimpleName(),
                    "postSuccess: Response from server: " + resultObj);

            // Deleting cookies from the device while logging out
            connectivityObj.deleteCookies();
            // Passing the control to LoginActivity
            Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
