package com.cigital.insecurepay.common;

/**
 * ResponseWrapper is a wrapper class that stores the response details from the server.
 */
public class ResponseWrapper {

    private int responseCode;
    private String responseString;
    private String responseMessage;

    /**
     * ResponseWrapper parametrized constructor.
     *
     * @param responseCode      Contains the response code from the server.
     * @param responseString    Contains the response string from the server.
     * @param responseMessage   Contains the response message from the server.
     */
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
