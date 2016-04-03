package com.cigital.insecurepay.common;

import android.content.ContentValues;
import android.content.Context;

import java.net.HttpURLConnection;

public abstract class GetAsyncCommonTask<T> extends AsyncCommonTask {
    protected T objReceived;
    private ContentValues contentValuesObj;
    private Class<T> classObj;

    public GetAsyncCommonTask(Context contextObj, String serverAddress, String path,
                              ContentValues contentValues, Class<T> classObj) {
        super(contextObj, serverAddress, path);
        this.contentValuesObj = contentValues;
        this.classObj = classObj;
    }

    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        super.doInBackground(params);
        if (checkConnection()) {
            return connectivityObj.get(contentValuesObj);
        } else {
            return new ResponseWrapper(HttpURLConnection.HTTP_CLIENT_TIMEOUT, null, null);
        }
    }

    @Override
    protected void postSuccess(String resultObj) {
        super.postSuccess(resultObj);
        objReceived = gsonObj.fromJson(resultObj, classObj);
    }
}
