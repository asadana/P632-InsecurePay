package com.cigital.insecurepay.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.CommonVO;
import com.cigital.insecurepay.common.AsyncCommonTask;
import com.cigital.insecurepay.common.PostAsyncCommonTask;
import com.cigital.insecurepay.common.ResponseWrapper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ChatFragment extends Fragment and is used to handles file upload and sending customer feedback operations.
 * */
public class ChatFragment extends Fragment {

    //declare all mime types which user can upload
    String[] mimetypes = {"image/*", "audio/*", "text/*", "video/*", "application/*"};
    //declare webView reference
    private WebView mWebView;
    //A callback interface used to provide values asynchronously.
    private ValueCallback<Uri[]> mFilePathCallback;
    //declare VO
    private CommonVO commonVO;

    //Following are the constants and references needed for file upload operations
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    private int megaByteSize = 1024 * 1024;
    private int maxFileSize = 5 * megaByteSize;
    String fileName;

    //Tag to be used for logging
    private static final String TAG = ChatFragment.class.getSimpleName();

    public ChatFragment() {
        // Required empty public constructor
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        // Get reference of WebView from layout/activity_main.xml
        mWebView = (WebView) rootView.findViewById(R.id.chat_webview);
        //Enable Javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        //Inject WebAppInterface methods into Web page by having Interface name 'Android'
        mWebView.addJavascriptInterface(new WebAppInterface(getContext()), "Android");
        setUpWebViewDefaults(mWebView);

        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore the previous URL and history stack
            mWebView.restoreState(savedInstanceState);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser");
                chooserIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                return true;
            }

        });

        // Load the local index.html file
        if (mWebView.getUrl() == null) {
            mWebView.loadUrl("file:///android_asset/LiveChat.html");
        }

        return rootView;
    }


    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();
        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // We set the WebViewClient to ensure links are consumed by the WebView rather
        // than passed to a browser if it can
        mWebView.setWebViewClient(new WebViewClient());
    }

    public void getFileName(Uri fileUri) {
        String uriString = fileUri.toString();
        File myFile = new File(uriString);
        String path = myFile.getAbsolutePath();

        //get name of the selected file
        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(fileUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = "custNo-" + String.valueOf(commonVO.getCustomerNumber()) +
                            '-' + cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    fileName = fileName.replaceAll("\\s", "");
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (uriString.startsWith("file://")) {
            fileName = "custNo-" + String.valueOf(commonVO.getCustomerNumber()) +
                    '-' + myFile.getName();
            fileName = fileName.replaceAll("\\s", "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;

        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {

                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }

                Uri uri = data.getData();
                getFileName(uri);
            }
        }

        int bytesAvailable;
        try {
            InputStream inputStream = null;
            if (results != null) {
                inputStream = getContext().getContentResolver().openInputStream(results[0]);
            }
            if (inputStream != null) {
                // create a buffer of  maximum size
                bytesAvailable = inputStream.available();
                Log.d(TAG, "onActivityResult: Size of the file: " + bytesAvailable);

                if (bytesAvailable <= maxFileSize) {
                    UploadFileTask task = null;
                    task = new UploadFileTask(getContext(), commonVO.getServerAddress(),
                            getString(R.string.chatService), results[0]);
                    task.execute();
                } else {
                    int allowedSize = maxFileSize / megaByteSize;
                    int allowedSizeRemainder = maxFileSize % megaByteSize;
                    int currentFileSize = bytesAvailable / megaByteSize;
                    int currentFileSizeRemainder = bytesAvailable % megaByteSize;

                    Log.d(TAG, "onActivityResult: Allowed file size: " +
                            String.valueOf(allowedSize) + "." +
                            String.valueOf(allowedSizeRemainder));
                    Toast.makeText(getContext(), "File size cannot be bigger than " +
                                    String.valueOf(allowedSize) + "." +
                                    String.valueOf(allowedSizeRemainder) + "MB.\nCurrent file size: " +
                                    String.valueOf(currentFileSize) + "." +
                                    String.valueOf(currentFileSizeRemainder) + "MB.",
                            Toast.LENGTH_LONG).show();
                }
            }

        } catch (IOException e) {
            Log.e(TAG, "onActivityResult: ", e);
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
    }

    class UploadFileTask extends AsyncCommonTask {

        private Uri sourceFileUri;
        private String serverAddress;
        private String path;

        public UploadFileTask(Context contextObj, String serverAddress,
                              String path, Uri sourceFileUri) {
            super(contextObj, serverAddress, path);
            this.sourceFileUri = sourceFileUri;
            this.serverAddress = serverAddress;
            this.path = path;
        }

        @Override
        protected ResponseWrapper doInBackground(Object... params) {
            super.doInBackground(params);
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(sourceFileUri);
                URL url = new URL(serverAddress + path);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setChunkedStreamingMode(1024);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                connectivityObj.setHttpURLConnectionObj(conn);
                connectivityObj.addCookiesToRequest();

                if (checkConnection()) {
                    // After this line it returns null exception
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + fileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = inputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = inputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = inputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = inputStream.read(buffer, 0, bufferSize);
                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                    ResponseWrapper responseWrapperObj = new ResponseWrapper(conn.getResponseCode(),
                            connectivityObj.readIt(conn.getInputStream()), conn.getResponseMessage());
                    return responseWrapperObj;

                } else {
                    return new ResponseWrapper(HttpURLConnection.HTTP_CLIENT_TIMEOUT, null,
                            "HTTP Client Timeout");
                }
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: ", e);
                return new ResponseWrapper(HttpURLConnection.HTTP_INTERNAL_ERROR, null,
                        "HTTP Internal Error");
            }
        }

        @Override
        protected void postSuccess(String resultObj) {
            super.postSuccess(resultObj);
            Log.d(TAG, "postSuccess: Server response: " + resultObj);
            Toast.makeText(getContext(), getString(R.string.chatUploadSuccess), Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void postFailure(ResponseWrapper responseWrapperObj) {
            if (responseWrapperObj.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                shouldLogout = false;
            }
            Toast.makeText(getContext(), getString(R.string.chatUploadFailure), Toast.LENGTH_LONG).show();
            super.postFailure(responseWrapperObj);

        }
    }
    private void onSubmit(String subject) {
        Log.i(this.getClass().getSimpleName(), "Sending subject");
        ChatSubjectTask sendsubject = new ChatSubjectTask(getContext(), commonVO.getServerAddress(),getString(R.string.chatService),
                subject);
        sendsubject.execute();
    }

    //Inner class to send subject
    private class ChatSubjectTask extends PostAsyncCommonTask<String> {
        public ChatSubjectTask(Context contextObj, String serverAddress, String path, String subject){
                super(contextObj, serverAddress, path, subject, String.class);
        }

        @Override
        protected void postSuccess(String resultObj) {
            Log.d(this.getClass().getSimpleName(), "postSuccess: " + resultObj);
            // Converting the string back to right encoding
            resultObj = new String(resultObj.toCharArray());
            run(resultObj);
        }
    }

    public void run(final String scriptSrc) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getSimpleName(), "Subject: "+scriptSrc);
                mWebView.loadUrl("javascript:" + "displaySubject(\'"+scriptSrc+"\')");
            }
        });
    }




    class WebAppInterface {

        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void showDialog( String subject) {
            onSubmit(subject);
        }

    }
}


