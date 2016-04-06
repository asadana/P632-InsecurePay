package com.cigital.insecurepay.common;

/**
 * ResponseWrapper stores the response code, the response from the server
 */
public class ResponseWrapper {
    private int responseCode;
    private String responseString;
    private String responseMessage;

    public ResponseWrapper(int responseCode, String responseString, String responseMessage) {
        this.responseCode = responseCode;
        this.responseString = responseString;
        this.responseMessage = responseMessage;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
