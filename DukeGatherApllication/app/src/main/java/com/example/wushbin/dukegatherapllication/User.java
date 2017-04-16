package com.example.wushbin.dukegatherapllication;

import android.net.Uri;

/**
 * Created by wushbin on 4/12/17.
 */

public class User {

    private String userName;
    private String email;
    public String photoUrl;
    public User(){ }
    public User(String userName){
        this.userName = userName;
    }

    public User(String userName, String email){
        this.userName = userName;
        this.email = email;
    }

    public User(String userName, String email, String photoUrl){
        this.userName = userName;
        this.email = email;
        this.photoUrl = photoUrl;
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
    public String getPhotoUrl(){
        return photoUrl;
    }
}
