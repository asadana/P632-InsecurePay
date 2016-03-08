package com.cigital.insecurepay.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;


public class AsyncCommon extends AsyncTask<Void, Void, ResponseWrapper> {

    private boolean isPost;
    private ProgressDialog progressDialogObj;
    private Context contextObj;

    public AsyncCommon(Context contextObj, boolean isPost) {
        this.isPost = isPost;
        this.contextObj = contextObj;
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
        ResponseWrapper responseWrapperObj;
        if (isPost) {
            responseWrapperObj = doPost();
        } else {
            responseWrapperObj = doGet();
        }
        return responseWrapperObj;
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

    protected void postFailure(ResponseWrapper responseWrapperObj) {
        Log.i(this.getClass().getSimpleName(), "postFailure: Failed to retrieve account information");
    }

    protected void postSuccess(ResponseWrapper responseWrapperObj) {
        Log.i(this.getClass().getSimpleName(), "postSuccess: Account information retrieved successfully");
    }

    protected ResponseWrapper doPost() {
        Log.i(this.getClass().getSimpleName(), "doPost: Executing post");
        return null;
    }

    protected ResponseWrapper doGet() {
        Log.i(this.getClass().getSimpleName(), "doGet: Executing get");
        return null;
    }
}
