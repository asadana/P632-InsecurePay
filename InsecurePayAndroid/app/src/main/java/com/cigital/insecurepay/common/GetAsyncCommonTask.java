package com.cigital.insecurepay.common;

import android.content.ContentValues;
import android.content.Context;

import java.net.HttpURLConnection;

/**
 * GetAsyncCommonTask extends {@link AsyncCommonTask}.
 * This class is a parent class that is extended by any Activity/Fragment that
 * wants to perform a Get request to the server.
 */
public abstract class GetAsyncCommonTask<T> extends AsyncCommonTask {
    protected T objReceived;
    private ContentValues contentValuesObj;
    private Class<T> classObj;

    /**
     * GetAsyncCommonTask is the parametrized constructor of this class.
     *
     * @param contextObj    Contains the context of the parent activity.
     * @param serverAddress Contains the server url/address.
     * @param path          Contains the sub-path to the service that needs to be used.
     * @param contentValues Data or Object of the VO class being sent to the server.
     * @param classObj      Object of the class sent by server in response.
     */
    public GetAsyncCommonTask(Context contextObj, String serverAddress, String path,
                              ContentValues contentValues, Class<T> classObj) {
        super(contextObj, serverAddress, path);
        this.contentValuesObj = contentValues;
        this.classObj = classObj;
    }

    /**
     * doInBackground is the function that establishes a connection with the server
     * in a background thread.
     *
     * @param params Contains the parameters that may be passed while calling doInBackground.
     * @return ResponseWrapper  Return an object of {@link ResponseWrapper}.
     */
    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        super.doInBackground(params);

        // If condition checks if the network is on.
        if (checkConnection()) {
            return connectivityObj.get(contentValuesObj);
        } else {
            return new ResponseWrapper(HttpURLConnection.HTTP_CLIENT_TIMEOUT, null, null);
        }
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
        super.postSuccess(resultObj);
        objReceived = gsonObj.fromJson(resultObj, classObj);
    }
}
