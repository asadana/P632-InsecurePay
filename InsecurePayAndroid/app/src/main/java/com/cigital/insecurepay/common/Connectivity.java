package com.cigital.insecurepay.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    public Connectivity(Context context, String path, String serverAddress) {
        this.context = context;
        this.path = path;
        this.serverAddress = serverAddress;
    }
    public Connectivity(Context context, String path, String serverAddress, String sendToServer) {
        this(context, path, serverAddress);
        this.sendToServer = sendToServer;
    }

    public String post()     throws IOException {
        Log.d("Response","Checking for connection");
        if (checkConnection()) {
            url = new URL(serverAddress + path);

            Log.d("Response", "URL set now opening connections " + url.toString());
            conn = (HttpURLConnection) url.openConnection();
            Log.d("Response","URL Connection opened succesfully");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            Log.d("Response", "Sending to server");
            writeIt();

            Log.d("Response", "Getting results");

            is = conn.getInputStream();
            Log.d("Response", "Converting to string");
            response = readIt(is);
        }
        return response;
    }

    public String get() throws IOException {
        url = new URL(serverAddress + path);
        Log.d("Response","URL set now opening connections");
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

    private boolean checkConnection() {
        Log.d("Response","Checking network connections");
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("Response","Network is on");
            return true;
        } else {

            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, context.getString(R.string.no_network), duration);
            toast.show();
        }
        return false;
    }

    private String readIt(InputStream stream) throws IOException
             {
        if(stream==null){
            Log.d("Response","Stream is null");
        }
        Log.d("Response","Reading response");

        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        while ((line = reader.readLine()) != null) {
            Log.d("Response","Reading lines");
            sb.append(line);
        }
        return sb.toString();
    }

    private void writeIt() throws IOException {
        OutputStream out = new BufferedOutputStream(conn.getOutputStream());
        OutputStreamWriter outwriter = new OutputStreamWriter(out,"UTF-8");
        outwriter.write(sendToServer);
        Log.d("Response", "Sent");
        outwriter.flush();
        outwriter.close();

    }
}
