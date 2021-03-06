package com.cigital.insecurepay.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
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
 * ChatFragment extends {@link Fragment}.
 * This class is used to handles file upload and sending customer feedback operations.
 */
public class ChatFragment extends Fragment {

    //Following are the constants and references needed for file upload operations
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    // Tag to be used for logging
    private static final String TAG = ChatFragment.class.getSimpleName();
    // Declare all mime types which user can upload
    private String[] mimeTypes = {"image/*", "audio/*", "text/*", "video/*", "application/*"};
    // Declare webView reference
    private WebView mWebView;
    // A callback interface used to provide values asynchronously.
    private ValueCallback<Uri[]> mFilePathCallback;
    // Declare VO
    private CommonVO commonVO;
    private int megaByteSize = 1024 * 1024;
    private int maxFileSize = 5 * megaByteSize;
    private String webAppInterface = "Android";
    private String httpTimeOutError = "HTTP Client Timeout";
    private String httpInternalError = "HTTP Internal Error";
    private String fileName;

    /**
     * ChatFragment is the default constructor of this class
     */
    public ChatFragment() {
    }

    @SuppressLint("JavascriptInterface")
    @Override
    /**
     * onCreateView is the first method called when the Fragment is being created.
     * It populates and initializes the necessary views.
     *
     * @param inflater The LayoutInflater object that is used to inflate fragment_chat.
     * @param container  Contains the {@link ViewGroup} object to which this fragment
     *                   belongs to.
     * @param savedInstanceState  If non-null, this fragment is being re-constructed from
     *                            a previous saved state.
     * */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        // Get reference of WebView from layout/activity_main.xml
        mWebView = (WebView) rootView.findViewById(R.id.chat_webview);
        //Enable Javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        //Inject WebAppInterface methods into Web page by having Interface name 'Android'
        mWebView.addJavascriptInterface(new WebAppInterface(getContext()), webAppInterface);
        setUpWebViewDefaults(mWebView);

        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore the previous URL and history stack
            mWebView.restoreState(savedInstanceState);
        }

        /**
         * This class is called when something that might impact a browser UI happens, for
         * instance, progress updates and JavaScript alerts are sent here.
         *
         * */
        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                /**
                 * ACTION_GET_CONTENT with MIME type in the given setType() and category
                 * CATEGORY_OPENABLE. Display all pickers for data that can be opened with
                 * ContentResolver.openInputStream(), allowing the user to pick one of them and
                 * then some data inside of it and returning the resulting URI to the caller.
                 * */
                String forAllTypes = "*/*";
                String fileChooserTitle = "File Chooser";
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType(forAllTypes);
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, fileChooserTitle);
                chooserIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                return true;
            }

        });

        // Load the local LiveChat.html file
        if (mWebView.getUrl() == null) {
            String pathToLiveChat = "file:///android_asset/LiveChat.html";
            mWebView.loadUrl(pathToLiveChat);
        }

        return rootView;
    }

    /**
     * setUpWebViewDefaults is a function that sets the basic webView settings.
     *
     * @param webView The webView Object
     */
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();
        // Enable Javascript to support JS in the web page to be rendered.
        settings.setJavaScriptEnabled(true);

        // We set the WebViewClient to ensure links are consumed by the WebView rather
        // than passed to a browser if it can
        mWebView.setWebViewClient(new WebViewClient());
    }

    /**
     * getFileName is a function that gets the name of the file to upload.
     *
     * @param fileUri The fileUri object
     */
    public void getFileName(Uri fileUri) {
        String uriStart = "content://";
        String uriString = fileUri.toString();
        File myFile = new File(uriString);
        String fileUriStart = "file://";
        String fileNameStart = "customerNo-";

        //get name of the selected file
        if (uriString.startsWith(uriStart)) {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(fileUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {

                    fileName = fileNameStart + String.valueOf(commonVO.getCustomerNumber())
                            + '-'
                            + cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    fileName = fileName.replaceAll("\\s", "");
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (uriString.startsWith(fileUriStart)) {
            fileName = fileNameStart + String.valueOf(commonVO.getCustomerNumber()) +
                    '-' + myFile.getName();
            fileName = fileName.replaceAll("\\s", "");
        }
    }


    /**
     * onActivityResult is an overridden function that is called when the user is done
     * with selecting file and returns.
     *
     * @param requestCode The request code you passed to startActivityForResult().
     * @param resultCode A result code specified by the second activity
     * @param data An Intent that carries the result data.
     */
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
                            getString(R.string.chat_path), results[0]);
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
                                    String.valueOf(allowedSizeRemainder) +
                                    "MB.\nCurrent file size: " +
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

    /**
     * onSubmit is a function that is invoked when send button is clicked which creates
     * sendSubject object for ChatSubjectTask
     *
     * @param subject The string given as input from the user
     */
    private void onSubmit(String subject) {
        Log.i(this.getClass().getSimpleName(), "onSubmit: Sending subject");
        ChatSubjectTask sendSubject = new ChatSubjectTask(getContext(),
                commonVO.getServerAddress(),
                getString(R.string.chat_path),
                subject);
        sendSubject.execute();
    }

    /**
     * run is a function that starts a new thread to render feedback message on the webView.
     * It is called from postSuccess in {@link ChatSubjectTask}.
     *
     * @param responseFeedback The subject to be displayed.
     */
    public void run(final String responseFeedback) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getSimpleName(), "run: Display subject to be rendered"
                        + responseFeedback);
                mWebView.loadUrl("javascript:" + "displaySubject(\'" + responseFeedback + "\')");
            }
        });
    }

    /**
     * UploadFileTask extends {@link AsyncCommonTask} to asynchronously communicate with the
     * server and upload file.
     */
    class UploadFileTask extends AsyncCommonTask {

        private Uri sourceFileUri;
        private String serverAddress;
        private String path;

        /**
         * UploadFileTask is the parametrized constructor for this class.
         *
         * @param contextObject Contains the {@link Context} object of the parent.
         * @param serverAddress Contains the server url/address .
         * @param path          Contains the sub-path to the service that needs to be used.
         * @param sourceFileUri URI of the source file
         */
        public UploadFileTask(Context contextObject, String serverAddress,
                              String path, Uri sourceFileUri) {
            super(contextObject, serverAddress, path);
            this.sourceFileUri = sourceFileUri;
            this.serverAddress = serverAddress;
            this.path = path;
        }


        /**
         * doInBackground is an overridden function that invokes a background thread to upload file.
         *
         * @param params  Pass objects that may be used to pass data to doInBackground.
         *
         * @return ResponseWrapper Returns the response wrapper received from server
         */
        @Override
        protected ResponseWrapper doInBackground(Object... params) {
            super.doInBackground(params);

            HttpURLConnection httpUrlConnection = null;
            DataOutputStream dataOutStream = null;

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = megaByteSize;

            try {
                InputStream inputStream = getContext().getContentResolver()
                        .openInputStream(sourceFileUri);

                Log.d(TAG, "doInBackground: Setting up connection.");
                URL url = new URL(serverAddress + path);
                Log.d(TAG, "doInBackground: Opening connection to : " + url.toString());
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setDoInput(true); // Allow Inputs
                httpUrlConnection.setDoOutput(true); // Allow Outputs
                httpUrlConnection.setUseCaches(false); // Don't use a Cached Copy
                httpUrlConnection.setChunkedStreamingMode(1024);
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                httpUrlConnection.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                httpUrlConnection.setRequestProperty("uploaded_file", fileName);

                connectivityObj.setHttpURLConnectionObj(httpUrlConnection);
                connectivityObj.addCookiesToRequest();

                if (checkConnection()) {
                    Log.d(TAG, "doInBackground: Network is on.");

                    // After this line it returns null exception
                    dataOutStream = new DataOutputStream(httpUrlConnection.getOutputStream());
                    dataOutStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutStream.writeBytes("Content-Disposition: form-data;" +
                            " name=\"file\";filename=\"" + fileName + "\"" + lineEnd);
                    dataOutStream.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = inputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = inputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dataOutStream.write(buffer, 0, bufferSize);
                        bytesAvailable = inputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = inputStream.read(buffer, 0, bufferSize);
                    }

                    // send multipart form data necessary after file data...
                    dataOutStream.writeBytes(lineEnd);
                    dataOutStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    Log.d(TAG, "doInBackground: Data sent to server.");

                    return new ResponseWrapper(
                            httpUrlConnection.getResponseCode(),
                            connectivityObj.readIt(httpUrlConnection.getInputStream()),
                            httpUrlConnection.getResponseMessage());

                } else {
                    return new ResponseWrapper(HttpURLConnection.HTTP_CLIENT_TIMEOUT, null,
                            httpTimeOutError);
                }
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: ", e);
                return new ResponseWrapper(HttpURLConnection.HTTP_INTERNAL_ERROR, null,
                        httpInternalError);
            }
        }

        /**
         * postSuccess is called when the server responds with a non-error code response.
         * This function performs all the tasks to be done in postExecute when server response
         * is not an error.
         *
         * @param resultObject Contains the string sent from the server as part of the response.
         */
        @Override
        protected void postSuccess(String resultObject) {
            super.postSuccess(resultObject);
            Log.d(TAG, "postSuccess: Server response: " + resultObject);
            Toast.makeText(getContext(), getString(R.string.chatUploadSuccess), Toast.LENGTH_SHORT).show();

        }


        /**
         * postFailure is called when the server responds with an error code response.
         * This function performs all the tasks to be done in postExecute when server response
         * is an error.
         *
         * @param ResponseWrapperObject Contains the response wrapper sent from the server
         */
        @Override
        protected void postFailure(ResponseWrapper ResponseWrapperObject) {
            if (ResponseWrapperObject.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                shouldLogout = false;
            }
            Toast.makeText(getContext(), getString(R.string.chatUploadFailure), Toast.LENGTH_LONG).show();
            super.postFailure(ResponseWrapperObject);
        }
    }

    /**
     * ChatSubjectTask extends {@link PostAsyncCommonTask} to asynchronously communicate with the
     * server and send subject and returns String object in postExecute.
     */
    private class ChatSubjectTask extends PostAsyncCommonTask<String> {
        /**
         * ChatSubjectTask is the parametrized constructor of this class.
         *
         * @param contextObject Contains the context of the parent activity.
         * @param serverAddress Contains the server url/address .
         * @param path          Contains the sub-path to the service that needs to be used.
         * @param subject       Contains subject to be sent
         */
        public ChatSubjectTask(Context contextObject, String serverAddress,
                               String path, String subject) {
            super(contextObject, serverAddress, path, subject, String.class);
        }

        /**
         * postSuccess is called when the server responds with a non-error code response.
         * This function performs all the tasks to be done in postExecute when server response
         * is not an error.
         *
         * @param resultObject Contains the string sent from the server as part of the response.
         */
        @Override
        protected void postSuccess(String resultObject) {
            Log.d(this.getClass().getSimpleName(), "postSuccess: " + resultObject);
            // Converting the string back to right encoding
            resultObject = new String(resultObject.toCharArray());
            run(resultObject);
        }
    }

    /**
     * WebInterface is class allows webPage to call showDialog().
     */
    class WebAppInterface {

        Context mContext;

        /**
         * WebAppInterface is a parametrized constructor to instantiate the interface
         * and set the context.
         *
         * @param context Contains the context object of the parent.
         */
        WebAppInterface(Context context) {
            mContext = context;
        }


        /**
         * showDialog is a function that is invoked from html page and calls onSubmit to send
         * subject to server.
         *
         * @param subject Contains the input subject.
         */
        @JavascriptInterface
        public void showDialog(String subject) {
            onSubmit(subject);
        }

    }
}


