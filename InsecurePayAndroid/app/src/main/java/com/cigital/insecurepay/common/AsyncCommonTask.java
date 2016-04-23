package com.cigital.insecurepay.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.activity.LoginActivity;
import com.google.gson.Gson;

import java.net.HttpURLConnection;

/**
 * AsyncCommonTask extends {@link AsyncTask}, that returns {@link ResponseWrapper}.
 * This is a parent class that is extended by {@link GetAsyncCommonTask} and
 * {@link PostAsyncCommonTask}.
 * This class is used to communicate with the server using {@link Connectivity} class
 * asynchronously with the UI thread, and displays a ProgressDialog while
 * the application communicates with the server.
 */
public class AsyncCommonTask extends AsyncTask<Object, Void, ResponseWrapper> {

    protected Connectivity connectivityObj;
    protected Gson gsonObj;
    // shouldLogout is used as a flag to let the program know whether the user should be
    // logged out on error.
    protected boolean shouldLogout;
    private ProgressDialog progressDialogObj;
    private Context contextObj;
    private String path;

    /**
     * AsyncCommonTask is the parametrized constructor for this class.
     *
     * @param contextObj    Contains the context of the parent activity.
     * @param serverAddress Contains the server address used to connect to the server.
     * @param path          Contains the sub-path of the url to connect.
     */
    public AsyncCommonTask(Context contextObj, String serverAddress, String path) {
        this.contextObj = contextObj;
        this.path = path;
        this.connectivityObj = new Connectivity(serverAddress);
        gsonObj = new Gson();
        shouldLogout = true;
    }

    /**
     * onPreExecute is a function that is called before the establishing the connection.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialogObj = new ProgressDialog(contextObj);
        progressDialogObj.setMessage(contextObj.getString(R.string.progress_dialog_message));
        progressDialogObj.setCancelable(false);
        progressDialogObj.show();
    }

    /**
     * doInBackground is the function that establishes a connection with the server
     * in a background thread.
     *
     * @param params    Contains the parameters that may be passed while calling doInBackground.
     *
     * @return ResponseWrapper  Return an object of {@link ResponseWrapper}.
     */
    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e(this.getClass().getSimpleName(), "doInBackground: " + e);
        }
        connectivityObj.setConnectionParameters(path);
        return null;
    }

    /**
     * onPostExecute is a function that is called after doInBackground ends.
     *
     * @param responseWrapperObj Contains the {@link ResponseWrapper} object with server
     *                           response.
     */
    @Override
    protected void onPostExecute(ResponseWrapper responseWrapperObj) {
        super.onPostExecute(responseWrapperObj);

        // Checking if the response is in 2xx range
        if (responseWrapperObj.getResponseCode() >= HttpURLConnection.HTTP_OK
                && responseWrapperObj.getResponseCode() < HttpURLConnection.HTTP_MULT_CHOICE) {
            postSuccess(responseWrapperObj.getResponseString());
        } else {
            postFailure(responseWrapperObj);
        }

        // Dismisses the loading progress dialog
        progressDialogObj.dismiss();
    }

    /**
     * checkConnection is a function that is called to see if the device has access to
     * internet to be able to communicate with the server.
     *
     * @return boolean  Return a boolean value depending on if the network connection is
     *                  available.
     */
    protected boolean checkConnection() {
        Log.d(this.getClass().getSimpleName(), "checkConnection: Checking network connections.");

        ConnectivityManager connMgr = (ConnectivityManager) contextObj
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(this.getClass().getSimpleName(), "checkConnection: Network is on.");
            return true;
        } else {
            Log.e(this.getClass().getSimpleName(), "checkConnection: Network is off.");
            Toast.makeText(contextObj, contextObj.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * postSuccess is called when the server responds with a non-error code response.
     * This function performs all the tasks to be done in postExecute when server response
     * is not an error.
     *
     * @param resultObj Contains the string sent from the server as part of the response.
     *                  Server sends the Customer No in response.
     */
    protected void postSuccess(String resultObj) {
        Log.d(this.getClass().getSimpleName(), "postSuccess: Successfully retrieved information");
    }

    /**
     * postFailure is called when the server responds with an error code response.
     * This function performs all the tasks to be done in postExecute when server response
     * is an error.
     *
     * @param responseWrapperObj    Contains the response from the server in form of
     *                              {@link ResponseWrapper} object.
     */
    protected void postFailure(ResponseWrapper responseWrapperObj) {
        Log.d(this.getClass().getSimpleName(), "postFailure: Failed to retrieve information");

        final AlertDialog alertDialog = new AlertDialog.Builder(contextObj).create();

        alertDialog.setTitle("Alert: " + responseWrapperObj.getResponseMessage());

        // Check if shouldLogout is true
        if (shouldLogout) {
            // Deleting cookies to keep the app clean on error
            connectivityObj.deleteCookies();

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(contextObj, LoginActivity.class);
                            // Clear back stack
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            // Start a new activity
                            contextObj.startActivity(intent);
                        }
                    });
        } else {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
        }

        alertDialog.setMessage(responseWrapperObj.getResponseString());
        alertDialog.show();

        // Custom text size for the message in alert dialog
        TextView textViewObj = (TextView) alertDialog.findViewById(android.R.id.message);
        textViewObj.setTextSize(13);
    }
}
