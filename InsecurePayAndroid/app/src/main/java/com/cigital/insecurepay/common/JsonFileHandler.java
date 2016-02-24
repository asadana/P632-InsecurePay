/**
 * JsonFileHandler handles reading and writing JSON objects to a local file
 */
package com.cigital.insecurepay.common;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonFileHandler {
    private final Context contextObj;
    private String fileName;
    private String contentToStore;
    private Gson gsonObj = new Gson();

    public JsonFileHandler(Context contextObj, String fileName, String contentToStore) {
        this.contextObj = contextObj;
        this.fileName = fileName;
        this.contentToStore = contentToStore;
    }

    private boolean writeToFile() {
        FileOutputStream outputStreamObj;

        try {
            outputStreamObj = contextObj.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStreamObj.write(contentToStore.getBytes());
            outputStreamObj.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e(this.getClass().getSimpleName(), e.toString());
            return false;
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.toString());
            return false;
        }
    }

    private String readFromFile() throws IOException {
        FileInputStream fileInputStreamObj = contextObj.openFileInput(fileName);
        InputStreamReader inputStreamReaderObj = new InputStreamReader(fileInputStreamObj);
        BufferedReader bufferedReaderObj = new BufferedReader(inputStreamReaderObj);
        StringBuilder stringBuilderObj = new StringBuilder();
        String readLine;

        while ((readLine = bufferedReaderObj.readLine()) != null) {
            stringBuilderObj.append(readLine);
        }

        contentToStore = stringBuilderObj.toString();

        fileInputStreamObj.close();

        return contentToStore;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentToStore() {
        return contentToStore;
    }

    public void setContentToStore(String contentToStore) {
        this.contentToStore = contentToStore;
    }
}
