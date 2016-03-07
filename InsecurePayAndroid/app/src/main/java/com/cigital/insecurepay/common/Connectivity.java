package com.cigital.insecurepay.common;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cigital.insecurepay.R;

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
    CookieStore mCookieStore;
    CookieManager mCookieManager = new CookieManager(mCookieStore, null);
    private Context context;
    private String path;
    private String sendToServer;
    private URL url;
    private HttpURLConnection conn;
    private String response;
    private InputStream is;
    private String serverAddress;

    public Connectivity(String serverAddress) {
        this.serverAddress = serverAddress;
        mCookieStore = mCookieManager.getCookieStore();
    }

    /*
     * Sends data to server in JSON format and receives response in JSON as well
     */
    public String post() {
        Log.d(this.getClass().getSimpleName(), "In Post()");
        if (checkConnection()) {
            try {
                url = new URL(serverAddress + path);
                Log.d(this.getClass().getSimpleName(), "URL set now opening connections " + url.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                if (mCookieStore.getCookies().size() > 0) {
                    //TO join cookies in the request
                    conn.setRequestProperty("Cookie", TextUtils.join(";", mCookieStore.getCookies()));
                    Log.d("IN POST METHOD", mCookieStore.getCookies().toString());
                }
                writeIt();
                is = conn.getInputStream();
                response = readIt(is);
                if (mCookieStore.getCookies().size() <= 0) {
                    Map<String, List<String>> headerFields = conn.getHeaderFields();
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
                    conn.disconnect();
                    is.close();
                } catch (IOException e) {
                    Log.e(this.getClass().getSimpleName(), "Post error", e);
                }
            }

        }
        return response;
    }

    //Right now get is hardcoded to get Customer Details of username = foo
    public String get(ContentValues contentValues) {
        Log.d(this.getClass().getSimpleName(), "In Get()");
        String params = null;
        if (contentValues != null)
            params = setParameters(contentValues);
        try {
            url = new URL(serverAddress + path + params);
            Log.d(this.getClass().getSimpleName(), "URL set now opening connections " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if (mCookieStore.getCookies().size() > 0) {
                //TO join cookies in the request
                conn.setRequestProperty("Cookie", TextUtils.join(";", mCookieStore.getCookies()));
                Log.d("IN GET METHOD", mCookieStore.getCookies().toString());
            }

            is = conn.getInputStream();
            conn.connect();
            response = readIt(is);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Get error", e);
        } finally {
            try {
                conn.disconnect();
                is.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Get error", e);
            }
        }

        return response;
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

    //Checks whether network is on
    private boolean checkConnection() {
        Log.d(this.getClass().getSimpleName(), "Checking network connections");
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(this.getClass().getSimpleName(), "Network is on");
            return true;
        } else {
            Log.d(this.getClass().getSimpleName(), "Network is off");
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();

        }
        return false;
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
            out = new BufferedOutputStream(conn.getOutputStream());
            outWriter = new OutputStreamWriter(out, "UTF-8");
            outWriter.write(sendToServer);
            outWriter.flush();
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Sending data to server", e);
        } finally {
            try {
                if (outWriter != null) {
                    outWriter.close();
                }
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Sending data to server", e);
            }
        }
        Log.d(this.getClass().getSimpleName(), "Sent");

    }

    public void setConnectionParameters(Context contextObj, String path) {
        this.context = contextObj;
        this.path = path;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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