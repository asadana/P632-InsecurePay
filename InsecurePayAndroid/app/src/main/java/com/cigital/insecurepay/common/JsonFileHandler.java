/**
 * JsonFileHandler handles reading and writing JSON objects to a local file
 */
package com.cigital.insecurepay.common;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * JsonFileHandler is a class that is used to handle read and write data to JSON file.
 */
public class JsonFileHandler {

    private final Context contextObj;
    private String fileName;
    private String contentToStore;

    /**
     * JsonFileHandler parametrized constructor
     *
     * @param contextObj    Contains the context of the parent activity.
     * @param fileName      Contains the filename .
     */
    public JsonFileHandler(Context contextObj, String fileName) {
        this.contextObj = contextObj;
        this.fileName = fileName;
    }

    /**
     * writeToFile is a function that is called to write data to the file in local storage.
     *
     * @param contentToStore Contains the content that needs to be stored into a file.
     *
     * @return boolean      Return a boolean value depending on if the write to file was a
     *                      success.
     */
    public boolean writeToFile(String contentToStore) {

        this.contentToStore = contentToStore;
        FileOutputStream outputStreamObj;

        try {
            outputStreamObj = contextObj.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStreamObj.write(contentToStore.getBytes());
            outputStreamObj.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e(this.getClass().getSimpleName(), "writeToFile: ", e);
            return false;
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "writeToFile: ", e);
            return false;
        }
    }

    /**
     * readFromFile is a function that is called to read data from the file stored in local storage.
     *
     * @return String   Return a string containing the contents of the file.
     */
    public String readFromFile() throws IOException {
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
