package com.cigital.insecurepay.common;

import android.content.Context;

import java.net.HttpURLConnection;

/**
 * PostAsyncCommonTask is used to handle all the POST requests Async tasks
 */
public abstract class PostAsyncCommonTask<T> extends AsyncCommonTask {
    protected T objToBeSent;
    private Class<T> classObj;

    /**
     * PostAsyncCommonTask is the parameterized constructor
     *
     * @param contextObj    Contains the context of the parent activity.
     * @param serverAddress Contains the server url/address .
     * @param path          Contains the sub-path to the service that needs to be used.
     * @param objToBeSent Data or Object of the VO class being sent to the server
     */
    public PostAsyncCommonTask(Context contextObj, String serverAddress, String path,
                               T objToBeSent, Class<T> classObj) {
        super(contextObj, serverAddress, path);
        this.objToBeSent = objToBeSent;
        this.classObj = classObj;
    }

    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        super.doInBackground(params);
        if (checkConnection()) {
            connectivityObj.setSendToServer(gsonObj.toJson(objToBeSent, classObj));
            return connectivityObj.post();
        } else {
            return new ResponseWrapper(HttpURLConnection.HTTP_CLIENT_TIMEOUT, null, null);
        }
    }
}
