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


public abstract class AsyncCommonTask extends AsyncTask<Object, Void, ResponseWrapper> {

    protected Connectivity connectivityObj;
    protected Gson gsonObj;
    private ProgressDialog progressDialogObj;
    private Context contextObj;
    private String path;
    protected boolean shouldLogout;


    public AsyncCommonTask(Context contextObj, String serverAddress, String path) {
        this.contextObj = contextObj;
        this.path = path;
        this.connectivityObj = new Connectivity(serverAddress);
        gsonObj = new Gson();
        shouldLogout = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialogObj = new ProgressDialog(contextObj);
        progressDialogObj.setMessage("Loading. Please wait..");
        progressDialogObj.setCancelable(false);
        progressDialogObj.show();
    }


    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e(this.getClass().getSimpleName(), "doInBackground: " + e.toString());
        }
        connectivityObj.setConnectionParameters(path);
        return null;
    }

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


    protected boolean checkConnection() {
        Log.d(this.getClass().getSimpleName(), "Checking network connections");
        ConnectivityManager connMgr = (ConnectivityManager) contextObj
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(this.getClass().getSimpleName(), "Network is on");
            return true;
        } else {
            Log.e(this.getClass().getSimpleName(), "Network is off");
            Toast.makeText(contextObj, contextObj.getString(R.string.no_network), Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    protected void postSuccess(String resultObj) {
        Log.i(this.getClass().getSimpleName(), "postSuccess: Successfully retrieved information");
    }

    protected void postFailure(ResponseWrapper responseWrapperObj) {
        Log.i(this.getClass().getSimpleName(), "postFailure: Failed to retrieve account information");

        final AlertDialog alertDialog = new AlertDialog.Builder(contextObj).create();

        if (responseWrapperObj.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            alertDialog.setTitle("Alert: Session Expired");
        } else {
            alertDialog.setTitle("Alert: " + responseWrapperObj.getResponseMessage());
        }

        // Check if shouldLogout is true
        if (shouldLogout) {
            // Deleting cookies to keep the app clean on error
            connectivityObj.deleteCookies();

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(contextObj, LoginActivity.class);
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
        TextView textViewObj = (TextView) alertDialog.findViewById(android.R.id.message);
        textViewObj.setTextSize(13);
    }
}
