package com.cigital.insecurepay.common;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Connectivity implements Serializable {

    final static String COOKIES_HEADER = "Set-Cookie";
    // Stores and handles cookies
    private static CookieStore mCookieStore;
    private static CookieManager mCookieManager = new CookieManager(mCookieStore, null);
    private Context context;
    private String path;
    private String sendToServer;
    private URL url;
    private HttpURLConnection httpURLConnectionObj;

    private ResponseWrapper responseWrapperObj;
    private InputStream is;
    private String serverAddress;

    public Connectivity(String serverAddress) {
        this.serverAddress = serverAddress;
        mCookieStore = mCookieManager.getCookieStore();
    }

    /*
     * Sends data to server in JSON format and receives response in JSON as well
     */
    public ResponseWrapper post() {
        Log.d(this.getClass().getSimpleName(), "In Post()");
        try {
            url = new URL(serverAddress + path);
            Log.d(this.getClass().getSimpleName(), "URL set now opening connections " + url.toString());
            httpURLConnectionObj = (HttpURLConnection) url.openConnection();
            httpURLConnectionObj.setDoInput(true);
            httpURLConnectionObj.setDoOutput(true);
            httpURLConnectionObj.setChunkedStreamingMode(0);
            httpURLConnectionObj.setRequestMethod("POST");
            httpURLConnectionObj.setRequestProperty("Content-Type", "application/json");
            if (mCookieStore.getCookies().size() > 0) {
                //To join cookies in the request
                httpURLConnectionObj.setRequestProperty("Cookie",
                        TextUtils.join(";", mCookieStore.getCookies()));
                Log.d(this.getClass().getSimpleName(), "post: " + mCookieStore.getCookies().toString());
            }
            writeIt();
            try {
                is = httpURLConnectionObj.getInputStream();
                Log.d(this.getClass().getSimpleName(), "post: Getting InputStream");
            } catch (IOException e) {
                is = httpURLConnectionObj.getErrorStream();
                Log.d(this.getClass().getSimpleName(), "post: Getting ErrorStream");
            }
            if (is != null) {
                responseWrapperObj = new ResponseWrapper(httpURLConnectionObj.getResponseCode(),
                        readIt(is));
                Log.d(this.getClass().getSimpleName(), "post: InputStream is not null");
            } else {
                responseWrapperObj = new ResponseWrapper(HttpURLConnection.HTTP_NOT_FOUND,
                        "Unable to connect to the server");
                Log.e(this.getClass().getSimpleName(), "post: InputStream is null");
            }

            if (mCookieStore.getCookies().size() <= 0 && is != null) {
                Map<String, List<String>> headerFields = httpURLConnectionObj.getHeaderFields();
                List<String> cookieHeaderList = headerFields.get(COOKIES_HEADER);
                if (cookieHeaderList != null) {
                    for (String cookie : cookieHeaderList) {
                        mCookieStore.add(null, HttpCookie.parse(cookie).get(0));
                    }

                }

            }
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Post error", e);
        } finally {

            try {
                if (httpURLConnectionObj != null)
                    httpURLConnectionObj.disconnect();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Post error", e);
            }
        }
        return responseWrapperObj;
    }

    public ResponseWrapper get(ContentValues contentValues) {
        Log.d(this.getClass().getSimpleName(), "In Get()");
        String params = null;
        if (contentValues != null)
            params = setParameters(contentValues);
        try {
            url = new URL(serverAddress + path + params);
            Log.d(this.getClass().getSimpleName(), "URL set now opening connections " + url);
            httpURLConnectionObj = (HttpURLConnection) url.openConnection();
            httpURLConnectionObj.setReadTimeout(10000);
            httpURLConnectionObj.setConnectTimeout(15000);
            httpURLConnectionObj.setRequestMethod("GET");
            httpURLConnectionObj.setDoInput(true);

            if (mCookieStore.getCookies().size() > 0) {
                //To join cookies in the request
                httpURLConnectionObj.setRequestProperty("Cookie", TextUtils.join(";", mCookieStore.getCookies()));
                Log.d("IN GET METHOD", mCookieStore.getCookies().toString());
            }

            try {
                is = httpURLConnectionObj.getInputStream();
                Log.d(this.getClass().getSimpleName(), "get: Getting InputStream");
            } catch (IOException e) {
                is = httpURLConnectionObj.getErrorStream();
                Log.d(this.getClass().getSimpleName(), "get: Getting ErrorStream");
            }
            if (is != null) {
                responseWrapperObj = new ResponseWrapper(httpURLConnectionObj.getResponseCode(),
                        readIt(is));
                Log.d(this.getClass().getSimpleName(), "get: InputStream is not null");
            } else {
                responseWrapperObj = new ResponseWrapper(HttpURLConnection.HTTP_NOT_FOUND,
                        "Unable to connect to the server");
                Log.e(this.getClass().getSimpleName(), "get: InputStream is null");
            }

        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Get error", e);
        } finally {
            try {
                if (httpURLConnectionObj != null)
                    httpURLConnectionObj.disconnect();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Get error", e);
            }
        }

        return responseWrapperObj;
    }

    private String setParameters(ContentValues contentValues) {
        String key;
        String value;
        Uri.Builder builder = new Uri.Builder();
        for (Map.Entry<String, Object> entry : contentValues.valueSet()) {
            key = entry.getKey();
            value = entry.getValue().toString();
            builder.appendQueryParameter(key, value);
        }
        return builder.build().toString();
    }

    //To read the response from server
    private String readIt(InputStream stream) {
        Log.d(this.getClass().getSimpleName(), "Reading response");
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Reading response error", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Reading response error", e);
            }
        }

        return sb.toString();
    }

    //To send request to server
    private void writeIt() {
        Log.d(this.getClass().getSimpleName(), "Sending data to server");
        OutputStream out;
        OutputStreamWriter outWriter = null;
        try {
            out = new BufferedOutputStream(httpURLConnectionObj.getOutputStream());
            outWriter = new OutputStreamWriter(out, "UTF-8");
            outWriter.write(sendToServer);
            outWriter.flush();
            Log.d(this.getClass().getSimpleName(), "Sent");
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Error sending data to server", e);
        } finally {
            try {
                if (outWriter != null) {
                    outWriter.close();
                }
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Sending data to server", e);
            }
        }


    }

    public void setConnectionParameters(String path) {
        this.path = path;
    }


    public ResponseWrapper getResponseWrapperObj() {
        return responseWrapperObj;
    }

    public void setResponseWrapperObj(ResponseWrapper responseWrapperObj) {
        this.responseWrapperObj = responseWrapperObj;
    }

    public void deleteCookies() {
        mCookieStore.removeAll();
        Log.d(this.getClass().getSimpleName(), "deleteCookies: Cookies deleted");
    }

    public String getSendToServer() {
        return sendToServer;
    }

    public void setSendToServer(String sendToServer) {
        this.sendToServer = sendToServer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}