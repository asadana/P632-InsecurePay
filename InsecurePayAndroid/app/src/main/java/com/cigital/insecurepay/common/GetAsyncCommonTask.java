package com.cigital.insecurepay.common;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

public abstract class GetAsyncCommonTask<T> extends AsyncCommonTask<T> {
    private ContentValues contentValues;

    public GetAsyncCommonTask(Context contextObj, String serverAddress, String path, ContentValues contentValues, Class<T> cls) {
        super(contextObj, serverAddress, path, cls);
        this.contentValues = contentValues;

    }

    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e(this.getClass().getSimpleName(), "doInBackground: " + e.toString());
        }
        ResponseWrapper responseWrapperObj = null;
        connectivityObj.setConnectionParameters(path);
        responseWrapperObj = connectivityObj.get(contentValues);
        return responseWrapperObj;
    }


}
