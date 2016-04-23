package com.cigital.insecurepay.common;

import android.content.Context;

import java.net.HttpURLConnection;

/**
 * PostAsyncCommonTask extends {@link AsyncCommonTask}.
 * This class is a parent class that is extended by any Activity/Fragment that
 * wants to perform a Post request to the server.
 */
public abstract class PostAsyncCommonTask<T> extends AsyncCommonTask {
    protected T objToBeSent;
    private Class<T> classObj;

    /**
     * PostAsyncCommonTask is the parametrized constructor
     *
     * @param contextObj    Contains the context of the parent activity.
     * @param serverAddress Contains the server url/address.
     * @param path          Contains the sub-path to the service that needs to be used.
     * @param objToBeSent   Data or Object of the class being sent to the server.
     */
    public PostAsyncCommonTask(Context contextObj, String serverAddress, String path,
                               T objToBeSent, Class<T> classObj) {
        super(contextObj, serverAddress, path);
        this.objToBeSent = objToBeSent;
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
            connectivityObj.setSendToServer(gsonObj.toJson(objToBeSent, classObj));
            return connectivityObj.post();
        } else {
            return new ResponseWrapper(HttpURLConnection.HTTP_CLIENT_TIMEOUT, null, null);
        }
    }
}
