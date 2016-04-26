package com.cigital.insecurepay.VOs;

/**
 * ChangePasswordVO is a POJO class to store the username and password of the user.
 */
public class ChangePasswordVO {

    private String username;
    private String password;

    public ChangePasswordVO() {
    }

    /**
     * ChangePasswordVO parameterized constructor
     *
     * @param	username
     * @param	password
     */
    public ChangePasswordVO( String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
