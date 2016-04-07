package com.cigital.insecurepay.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cigital.insecurepay.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.DialogInterface.OnClickListener;

public class ChatFragment extends Fragment {

    public static final int INPUT_FILE_REQUEST_CODE = 1;
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    private static final String TAG = ChatFragment.class.getSimpleName();
    //WebAppInterface webAppInterface;
    private WebView mWebView;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    WebAppInterface webAppInterface;

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
                Log.d(this.getClass().getSimpleName(), "before takepicture internt");

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                Log.d(this.getClass().getSimpleName(), "before startActivity");
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                Log.d(this.getClass().getSimpleName(), "after startActivity");
                return true;
            }

            /*@Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                new AlertDialog.Builder(ChatFragment.this)
                        .setMessage(message)
                        .setPositiveButton("OK",null)
                        .create().show();
                return true;
            }*/
        });

        // Load the local index.html file
        if (mWebView.getUrl() == null) {
            mWebView.loadUrl("file:///android_asset/LiveChat.html");
        }

        return rootView;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();
        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // We set the WebViewClient to ensure links are consumed by the WebView rather
        // than passed to a browser if it can
        mWebView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(this.getClass().getSimpleName(), "inside  onActivity");
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Log.d(this.getClass().getSimpleName(), "inside  onActivity2");
        Uri[] results = null;

        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                // If there is not data, then we may have taken a photo
                if (mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }

        UploadFileTask task = new UploadFileTask(results[0]);
        task.execute();

        mFilePathCallback.onReceiveValue(results);
        Log.d(this.getClass().getSimpleName(), "after  onReceiveValue");
        mFilePathCallback = null;
        return;
    }

    class UploadFileTask extends AsyncTask<String, Void, String> {

        Uri sourceFileUri;

        UploadFileTask(Uri sourceFileUri) {
            this.sourceFileUri = sourceFileUri;
        }

        protected String doInBackground(String... urls) {
            //String fileName = "abc.jpeg";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "img_" + timeStamp  + ".jpeg";
            Log.d("upload file name",fileName);
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
               /* if(bytesRead<max)
                {
                    Toast.makeText(getContext(), "File size too big", Toast.LENGTH_SHORT).show();

                }*/

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
                Toast.makeText(getContext(), "File Upload successful", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public class WebAppInterface {

        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }



       /* @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(ChatFragment.this, toast, Toast.LENGTH_SHORT).show();
        }
       // public WebAppInterface(ChatFragment chatFragment) {

        //}*/


        /**
         * Show Dialog
         *
         * @param dialogMsg
         */
        @JavascriptInterface
        public void showDialog(String dialogMsg) {
            new AlertDialog.Builder(mContext)
                    .setMessage(dialogMsg)
                    .setPositiveButton("OK", null)
                    .create().show();

        }
    }
}


