package com.cigital.insecurepay.common;

import android.content.ContentValues;
import android.content.Context;

import java.net.HttpURLConnection;

public abstract class GetAsyncCommonTask extends AsyncCommonTask {
    private ContentValues contentValuesObj;

    public GetAsyncCommonTask(Context contextObj, String serverAddress, String path, ContentValues contentValues) {
        super(contextObj, serverAddress, path);
        this.contentValuesObj = contentValues;
    }

    @Override
    protected ResponseWrapper doInBackground(Void... params) {
        super.doInBackground(params);
        if (checkConnection()) {
            return connectivityObj.get(contentValuesObj);
        } else {
            return new ResponseWrapper(HttpURLConnection.HTTP_CLIENT_TIMEOUT, null);
        }
    }


}
