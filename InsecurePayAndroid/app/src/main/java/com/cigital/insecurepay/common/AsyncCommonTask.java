package com.cigital.insecurepay.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cigital.insecurepay.R;

import java.net.HttpURLConnection;


public abstract class AsyncCommonTask extends AsyncTask<Void, Void, ResponseWrapper> {

    protected Connectivity connectivityObj;
    private ProgressDialog progressDialogObj;
    private Context contextObj;
    private String serverAddress;
    private String path;


    public AsyncCommonTask(Context contextObj, String serverAddress, String path) {
        this.serverAddress = serverAddress;
        this.contextObj = contextObj;
        this.path = path;
        this.connectivityObj = new Connectivity(this.serverAddress);
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
    protected ResponseWrapper doInBackground(Void... params) {
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
            // TODO: do stuff here
            postSuccess(responseWrapperObj);
        } else {
            // TODO: Handle error here
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
            // TODO: Handle error here
            Log.d(this.getClass().getSimpleName(), "Network is off");
            Toast.makeText(contextObj, contextObj.getString(R.string.no_network), Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    protected void postSuccess(ResponseWrapper responseWrapperObj) {
        Log.i(this.getClass().getSimpleName(), "postSuccess: Successfully retrieved information");
    }

    protected void postFailure(ResponseWrapper responseWrapperObj) {
        Log.i(this.getClass().getSimpleName(), "postFailure: Failed to retrieve account information");
    }
}
