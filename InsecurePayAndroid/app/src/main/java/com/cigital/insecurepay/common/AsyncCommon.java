package com.cigital.insecurepay.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.net.HttpURLConnection;


public class AsyncCommon extends AsyncTask<Void, Void, ResponseWrapper> {

    private ProgressDialog progressDialogObj;
    private Context contextObj;

    public AsyncCommon(Context contextObj) {
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
        return null;
    }

    @Override
    protected void onPostExecute(ResponseWrapper responseWrapperObj) {
        super.onPostExecute(responseWrapperObj);

        // Checking if the response is in 2xx range
        if (responseWrapperObj.getResponseCode() >= HttpURLConnection.HTTP_OK
                && responseWrapperObj.getResponseCode() < HttpURLConnection.HTTP_MULT_CHOICE) {
            // TODO: do stuff here
        } else {
            // TODO: Handle error here
        }

        // Dismisses the loading progress dialog
        progressDialogObj.dismiss();
    }
}
