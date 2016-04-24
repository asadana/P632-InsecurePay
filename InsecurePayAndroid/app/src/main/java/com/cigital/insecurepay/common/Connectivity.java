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
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Connectivity is a class that implements {@link Serializable} and is used to handle most
 * connection related properties including cookies.
 */
public class Connectivity implements Serializable {

    private final static String COOKIES_HEADER = "Set-Cookie";
    // Stores and handles cookies
    private static CookieStore mCookieStore;
    private static CookieManager mCookieManager = new CookieManager(mCookieStore, null);

    private Context context;
    private String path;
    private String serverAddress;
    private URL url;
    private HttpURLConnection httpURLConnectionObj;

    private String sendToServer;
    private ResponseWrapper responseWrapperObj;
    private InputStream inputStream;

    /**
     * Connectivity is the parametrized constructor of this class.
     *
     * @param serverAddress Contains the server address for the connection.
     */
    public Connectivity(String serverAddress) {
        this.serverAddress = serverAddress;
        mCookieStore = mCookieManager.getCookieStore();
    }

    /**
     * post is a function that is used to do a post onto the server.
     *
     * @return ResponseWrapper  Returns a {@link ResponseWrapper} object.
     */
    public ResponseWrapper post() {
        Log.d(this.getClass().getSimpleName(), "post: Creating a post request.");
        try {
            url = new URL(serverAddress + path);

            Log.d(this.getClass().getSimpleName(),
                    "post: Now opening connection to " + url.toString());

            httpURLConnectionObj = (HttpURLConnection) url.openConnection();
            httpURLConnectionObj.setDoInput(true);
            httpURLConnectionObj.setDoOutput(true);
            httpURLConnectionObj.setChunkedStreamingMode(0);
            httpURLConnectionObj.setRequestMethod("POST");
            httpURLConnectionObj.setRequestProperty("Content-Type", "application/json");

            addCookiesToRequest();

            writeIt();

            try {
                inputStream = httpURLConnectionObj.getInputStream();
                Log.d(this.getClass().getSimpleName(), "post: Getting InputStream");

                if (inputStream != null) {
                    responseWrapperObj = new ResponseWrapper(httpURLConnectionObj.getResponseCode(),
                            readIt(inputStream), httpURLConnectionObj.getResponseMessage());
                    Log.d(this.getClass().getSimpleName(), "post: InputStream is not null");
                }
            } catch (IOException e) {
                inputStream = httpURLConnectionObj.getErrorStream();

                if (inputStream != null) {
                    // Dumping stacktrace into message
                    responseWrapperObj = new ResponseWrapper(httpURLConnectionObj.getResponseCode(),
                            errorToString(e), httpURLConnectionObj.getResponseMessage());

                    Log.d(this.getClass().getSimpleName(), "post: InputStream is not null");
                }
                Log.d(this.getClass().getSimpleName(), "post: Getting ErrorStream");
            }

            if (inputStream == null) {
                responseWrapperObj = new ResponseWrapper(HttpURLConnection.HTTP_NOT_FOUND,
                        "Unable to connect to the server", "HTTP not found");
                Log.e(this.getClass().getSimpleName(), "post: InputStream is null");
            }

            // Check to see if no cookies exist for this application yet
            if (mCookieStore.getCookies().size() <= 0 && inputStream != null) {
                Map<String, List<String>> headerFields = httpURLConnectionObj.getHeaderFields();
                List<String> cookieHeaderList = headerFields.get(COOKIES_HEADER);
                if (cookieHeaderList != null) {
                    for (String cookie : cookieHeaderList) {
                        mCookieStore.add(null, HttpCookie.parse(cookie).get(0));
                    }

                }

            }
        } catch (MalformedURLException e) {
            Log.e(this.getClass().getSimpleName(), "post: ", e);
            return new ResponseWrapper(HttpURLConnection.HTTP_BAD_REQUEST, errorToString(e),
                    "Bad Request");
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "post: ", e);
            return new ResponseWrapper(HttpURLConnection.HTTP_BAD_REQUEST, errorToString(e),
                    "Bad Request");
        } finally {

            try {
                if (httpURLConnectionObj != null)
                    httpURLConnectionObj.disconnect();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "post: Post error", e);
            }
        }
        return responseWrapperObj;
    }

    /**
     * addCookiesToRequest is a function that is called to add existing cookies to the request
     * being sent.
     */
    public void addCookiesToRequest() {
        if (mCookieStore.getCookies().size() > 0) {
            //To join cookies in the request
            httpURLConnectionObj.setRequestProperty("Cookie",
                    TextUtils.join(";", mCookieStore.getCookies()));
            Log.d(this.getClass().getSimpleName(), "addCookiesToRequest" +
                    mCookieStore.getCookies().toString());
        }
    }

    /**
     * get is a function that is called to send a get request to the server.
     *
     * @param contentValues Contains the values being sent to the server in the request.
     * @return ResponseWrapper  Return an object of {@link ResponseWrapper}.
     */
    public ResponseWrapper get(ContentValues contentValues) {
        Log.d(this.getClass().getSimpleName(), "get: Creating a get request.");

        String params = null;

        if (contentValues != null)
            params = setParameters(contentValues);

        try {
            url = new URL(serverAddress + path + params);

            Log.d(this.getClass().getSimpleName(),
                    "get: Now opening connection to " + url.toString());

            httpURLConnectionObj = (HttpURLConnection) url.openConnection();
            httpURLConnectionObj.setReadTimeout(10000);
            httpURLConnectionObj.setConnectTimeout(15000);
            httpURLConnectionObj.setRequestMethod("GET");
            httpURLConnectionObj.setDoInput(true);

            if (mCookieStore.getCookies().size() > 0) {
                //To join cookies in the request
                httpURLConnectionObj.setRequestProperty("Cookie",
                        TextUtils.join(";", mCookieStore.getCookies()));
                Log.d(this.getClass().getSimpleName(),
                        "get: " + mCookieStore.getCookies().toString());
            }

            try {
                inputStream = httpURLConnectionObj.getInputStream();
                Log.d(this.getClass().getSimpleName(), "get: Getting InputStream");
                Log.d(this.getClass().getSimpleName(), "get: "
                        + httpURLConnectionObj.getResponseMessage());

                if (inputStream != null) {
                    responseWrapperObj = new ResponseWrapper(httpURLConnectionObj.getResponseCode(),
                            readIt(inputStream), httpURLConnectionObj.getResponseMessage());
                    Log.d(this.getClass().getSimpleName(), "get: InputStream is not null");
                }
            } catch (IOException e) {
                inputStream = httpURLConnectionObj.getErrorStream();
                if (inputStream != null) {
                    // Dumping stacktrace into message
                    responseWrapperObj = new ResponseWrapper(httpURLConnectionObj.getResponseCode(),
                            errorToString(e), httpURLConnectionObj.getResponseMessage());
                    Log.d(this.getClass().getSimpleName(), "get: InputStream is not null");
                }
                Log.d(this.getClass().getSimpleName(), "get: Getting ErrorStream");
                Log.e(this.getClass().getSimpleName(), "get: ", e);
            }

            if (inputStream == null) {
                responseWrapperObj = new ResponseWrapper(HttpURLConnection.HTTP_NOT_FOUND,
                        "Unable to connect to the server", "HTTP not found");
                Log.e(this.getClass().getSimpleName(), "get: InputStream is null");
            }

        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "get: ", e);
        } finally {
            try {
                if (httpURLConnectionObj != null)
                    httpURLConnectionObj.disconnect();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "get: ", e);
            }
        }
        return responseWrapperObj;
    }

    /**
     * setParameters is a function that is called to set content to be sent to the server.
     *
     * @param contentValues Contains the content to be sent to the server.
     *
     * @return String   Return the string representation of content.
     */
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

    /**
     * readIt is a function that takes an InputStream and reads it into a string.
     *
     * @param stream Contains the InputStream received.
     *
     * @return String   Return the string representation of the input stream.
     */
    public String readIt(InputStream stream) {
        Log.d(this.getClass().getSimpleName(), "readIt: Reading response");

        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "readIt: Reading response error", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "readIt: Reading response error", e);
            }
        }
        return sb.toString();
    }

    /**
     * writeIt is a function that opens an OutputStream and send content to the server.
     */
    private void writeIt() {
        Log.d(this.getClass().getSimpleName(), "writeIt: Sending data to server.");

        OutputStream out;
        OutputStreamWriter outWriter = null;
        try {
            out = new BufferedOutputStream(httpURLConnectionObj.getOutputStream());
            outWriter = new OutputStreamWriter(out, "UTF-8");
            outWriter.write(sendToServer);
            outWriter.flush();
            Log.d(this.getClass().getSimpleName(), "writeIt: Sent");
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "writeIt: Error sending data to server", e);
        } finally {
            try {
                if (outWriter != null) {
                    outWriter.close();
                }
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "writeIt: ", e);
            }
        }
    }

    /**
     * errorToString is a function that is called to translate the {@link Throwable} from
     * an exception into a string.
     *
     * @param exception  Contains the the {@link Throwable} exception.
     *
     * @return String   Return the string representation of the exception.
     */
    public String errorToString(Exception exception) {
        StringWriter stringWriterObj = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriterObj));
        return stringWriterObj.toString();
    }

    /**
     * deleteCookies is a function that deletes all cookies of the app from the device.
     */
    public void deleteCookies() {
        mCookieStore.removeAll();
        Log.d(this.getClass().getSimpleName(), "deleteCookies: Cookies deleted.");
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

    public HttpURLConnection getHttpURLConnectionObj() {
        return httpURLConnectionObj;
    }

    public void setHttpURLConnectionObj(HttpURLConnection httpURLConnectionObj) {
        this.httpURLConnectionObj = httpURLConnectionObj;
    }
}