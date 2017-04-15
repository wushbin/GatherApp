package com.example.wushbin.dukegatherapllication;

/**
 * Created by wushbin on 4/12/17.
 */

public class User {

    private String userName;
    private String email;
    public User(){ }
    public User(String userName){
        this.userName = userName;
    }

    public User(String userName, String email){
        this.userName = userName;
        this.email = email;
    }

    public String getUserName(){
        return userName;
    }
    public String getEmail(){
        if(email == null){
            return "N/A";
        }
        return email;
    }
}
