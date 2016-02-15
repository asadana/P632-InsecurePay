package com.cigital.insecurepay.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import java.net.HttpURLConnection;
import java.net.URL;

public class Connectivity {
    Context context;
    String path;
    String sendToServer;
    URL url;
    HttpURLConnection conn;
    String response;
    InputStream is;
    String serverAddress;

    //Constructor called if connection is to be established for get
    public Connectivity(Context context, String path, String serverAddress) {
        this.context = context;
        this.path = path;
        this.serverAddress = serverAddress;
    }

    //Constructor called if connection is to be established for post
    public Connectivity(Context context, String path, String serverAddress, String sendToServer) {
        this(context, path, serverAddress);
        this.sendToServer = sendToServer;
    }

    /*
    Sends data to server in JSON format and receives response in JSON as well
     */
    public String post() throws IOException {
        Log.d(this.getClass().getSimpleName(), "In Post()");
        if (checkConnection()) {
            url = new URL(serverAddress + path);
            Log.d(this.getClass().getSimpleName(), "URL set now opening connections " + url.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            writeIt();
            is = conn.getInputStream();
            response = readIt(is);
            is.close();
        }
        return response;
    }

    //Right now get is hardcoded to get Customer Details of username = foo
    public String get() throws IOException {
        Log.d(this.getClass().getSimpleName(), "In Get()");
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("username", "foo");
        String uricustService = builder.build().toString();
        url = new URL(serverAddress + path + uricustService);
        Log.d(this.getClass().getSimpleName(), "URL set now opening connections " + url);
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000); /* milliseconds */
        conn.setConnectTimeout(15000); /* milliseconds */
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        is = conn.getInputStream();
        conn.connect();
        response = readIt(is);
        return response;
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

            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, context.getString(R.string.no_network), duration);
            toast.show();
        }
        return false;
    }

    //To read the response from server
    private String readIt(InputStream stream) throws IOException {
        Log.d(this.getClass().getSimpleName(), "Reading response");
        if (stream == null) {
            Log.d(this.getClass().getSimpleName(), "Stream is null");
        }
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    //To send request to server
    private void writeIt() throws IOException {
        Log.d(this.getClass().getSimpleName(), "Sending data to server");
        OutputStream out = new BufferedOutputStream(conn.getOutputStream());
        OutputStreamWriter outwriter = new OutputStreamWriter(out, "UTF-8");
        outwriter.write(sendToServer);
        Log.d(this.getClass().getSimpleName(), "Sent");
        outwriter.flush();
        outwriter.close();
    }
}
/*
Following will be the declaration for the class variables of cookie

CookieStore mCookieStore;
CookieManager mCookieManager=new CookieManager(mCookieStore, null);
Addition in Constructor
mCookieStore = mCookieManager.getCookieStore();
Above needed for both get and post with cookies
 */

/*public String cookie_post() throws IOException {
         url = new URL(serverAddress + "http://10.0.0.10:8090/InsecurePayService/rest/" + "login");
        Log.d("Response", "URL set now opening connections" + url.toString());
        conn = (HttpURLConnection) url.openConnection();
        Log.d("Response", "URL Connection opened successfully");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setChunkedStreamingMode(0);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        writeIt();
        final String COOKIES_HEADER = "Set-Cookie";

        Map<String, List<String>> headerFields = conn.getHeaderFields();
        List<String> cookieHeader = headerFields.get(COOKIES_HEADER);
        if (cookieHeader != null) {
            for (String cookie : cookieHeader) {

                mCookieStore.add(null, HttpCookie.parse(cookie).get(0));
            }
        }
        Log.d("Response", "Cookie " + mCookieStore.getCookies().toString());
        is = conn.getInputStream();
        conn.connect();
        response = readIt(is);
        cookie_get();
        return response;
    }
*/
/*
    public String cookie_get() throws IOException {
        url = new URL(serverAddress + "http://10.0.0.10:8090/InsecurePayService/rest/" + "cookie");
        Log.d("Response", "URL set now opening connections" + url.toString());
        conn = (HttpURLConnection) url.openConnection();
        Log.d("Response", "URL Connection opened successfully");
        conn.setDoInput(true);
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        if(mCookieStore.getCookies().size()>0){
            //TO join cookies in the request
            conn.setRequestProperty("Cookie", TextUtils.join(";",mCookieStore.getCookies()));
        }
        is = conn.getInputStream();
        conn.connect();
        response = readIt(is);
        return response;
    }
*/