package com.cigital.insecurepay.common;

import android.content.Context;

import java.net.HttpURLConnection;

public abstract class PostAsyncCommonTask<T> extends AsyncCommonTask {
    protected T objToBeSent;
    private Class<T> classObj;

    public PostAsyncCommonTask(Context contextObj, String serverAddress, String path,
                               T objToBeSent, Class<T> classObj) {
        super(contextObj, serverAddress, path);
        this.objToBeSent = objToBeSent;
        this.classObj = classObj;
    }

    public PostAsyncCommonTask(Context contextObj, String serverAddress, String path, Class<T> classObj) {
        super(contextObj, serverAddress, path);
        this.classObj = classObj;
    }

    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        super.doInBackground(params);
        if (checkConnection()) {
            connectivityObj.setSendToServer(gsonObj.toJson(objToBeSent, classObj));
            return connectivityObj.post();
        } else {
            return new ResponseWrapper(HttpURLConnection.HTTP_CLIENT_TIMEOUT, null);
        }
    }
}
