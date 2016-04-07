package com.cigital.insecurepay.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChatFragment extends Fragment {

    public static final int INPUT_FILE_REQUEST_CODE = 1;
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    private static final String TAG = ChatFragment.class.getSimpleName();
    //WebAppInterface webAppInterface;
    private WebView mWebView;
    private ValueCallback<Uri[]> mFilePathCallback;
    String fileName;
    WebAppInterface webAppInterface;
    String[] mimetypes = {"image/*", "audio/*", "text/*", "video/*", "application/*"};
    private CommonVO commonVO;
    private int custNo;

    public ChatFragment() {

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
        mWebView.addJavascriptInterface(new WebAppInterface(getContext()),"Android");
        setUpWebViewDefaults(mWebView);

        commonVO = ((CommonVO) this.getArguments().getSerializable(getString(R.string.common_VO)));
        custNo = commonVO.getCustNo();
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
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
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

    public void getFileName(Uri fileuri) {
        String uriString = fileuri.toString();
        File myFile = new File(uriString);
        String path = myFile.getAbsolutePath();

        //get name of the selected file
        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(fileuri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = "CustNo_" + String.valueOf(custNo) + '_' + cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uriString.startsWith("file://")) {
            fileName = "CustNo_" + String.valueOf(custNo) + '_' + myFile.getName();
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

        UploadFileTask task = new UploadFileTask(results[0]);
        task.execute();

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;
    }

    class UploadFileTask extends AsyncTask<String, Void, String> {

        Uri sourceFileUri;

        UploadFileTask(Uri sourceFileUri) {
            this.sourceFileUri = sourceFileUri;
        }

        protected String doInBackground(String... urls) {

            Log.d("source uri", sourceFileUri.toString());
            String serverResponseMessage = null;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int max = 5*1024*1024;
            int maxBufferSize = 1024 * 1024;
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(sourceFileUri);
                URL url = new URL(getString(R.string.default_address) + "/rest/upload");

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

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseMessage);
                dos.flush();
                dos.close();

            } catch (
                    MalformedURLException ex
                    )

            {
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (
                    Exception e
                    )

            {
                e.printStackTrace();
                Log.e(this.getClass().getSimpleName(), "Exception : "
                        + e.getMessage(), e);
            }
            return serverResponseMessage;
        }


        protected void onPostExecute(final String serverResponseMessage) {

            if (serverResponseMessage.equals("OK")) {
                Toast.makeText(getContext(), getString(R.string.chat_upload_successful), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public class WebAppInterface {

        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void showDialog(String dialogMsg) {
            new AlertDialog.Builder(mContext)
                    .setMessage(dialogMsg)
                    .setPositiveButton("OK", null)
                    .create().show();

        }
    }
}


