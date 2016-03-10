package com.cigital.insecurepay.common;

import android.content.ContentValues;
import android.content.Context;

import java.net.HttpURLConnection;

public abstract class GetAsyncCommonTask<T> extends AsyncCommonTask<T> {
    private ContentValues contentValuesObj;

    public GetAsyncCommonTask(Context contextObj, String serverAddress, String path,
                              ContentValues contentValues, Class<T> classObj) {
        super(contextObj, serverAddress, path, classObj);
        this.contentValuesObj = contentValues;
    }

    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        super.doInBackground(params);
        if (checkConnection()) {
            return connectivityObj.get(contentValuesObj);
        } else {
            return new ResponseWrapper(HttpURLConnection.HTTP_CLIENT_TIMEOUT, null);
        }
    }


}
