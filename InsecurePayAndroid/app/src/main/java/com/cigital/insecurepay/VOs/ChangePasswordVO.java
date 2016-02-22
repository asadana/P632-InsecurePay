package com.cigital.insecurepay.VOs;

/**
 * Created by Jaini on 2/20/2016.
 */
public class ChangePasswordVO {

    private String username;
    private String password;

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
