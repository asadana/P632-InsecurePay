package com.cigital.insecurepay.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cigital.insecurepay.R;
import com.google.gson.Gson;

import java.net.HttpURLConnection;


public abstract class AsyncCommonTask<T> extends AsyncTask<Object, Void, ResponseWrapper> {

    private ProgressDialog progressDialogObj;
    protected Context contextObj;
    protected Connectivity connectivityObj;
    private String serverAddress;
    protected String path;
    private Gson gson = new Gson();
    protected Class<T> cls;


    public AsyncCommonTask(Context contextObj, String serverAddress, String path, Class<T> cls) {
        this.serverAddress = serverAddress;
        this.contextObj = contextObj;
        this.path = path;
        this.connectivityObj = new Connectivity(this.serverAddress);
        this.cls = cls;

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
    protected void onPostExecute(ResponseWrapper responseWrapperObj) {
        super.onPostExecute(responseWrapperObj);

        // Checking if the response is in 2xx range
        if (responseWrapperObj.getResponseCode() >= HttpURLConnection.HTTP_OK
                && responseWrapperObj.getResponseCode() < HttpURLConnection.HTTP_MULT_CHOICE) {
            // TODO: do stuff here
            postSuccess(gson.fromJson(responseWrapperObj.getResponseString(), cls));
        } else {
            // TODO: Handle error here
            postFailure(responseWrapperObj);
        }

        // Dismisses the loading progress dialog
        progressDialogObj.dismiss();
    }

    protected void postFailure(ResponseWrapper responseWrapperObj) {
        Log.i(this.getClass().getSimpleName(), "postFailure: Failed to retrieve account information");
    }

    private boolean checkConnection() {
        Log.d(this.getClass().getSimpleName(), "Checking network connections");
        ConnectivityManager connMgr = (ConnectivityManager) contextObj
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(this.getClass().getSimpleName(), "Network is on");
            return true;
        } else {
            Log.d(this.getClass().getSimpleName(), "Network is off");
            Toast.makeText(contextObj, contextObj.getString(R.string.no_network), Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    public abstract void postSuccess(T result);
}
