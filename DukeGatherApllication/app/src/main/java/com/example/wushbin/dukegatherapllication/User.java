package com.example.wushbin.dukegatherapllication;

import android.net.Uri;

/**
 * Created by wushbin on 4/12/17.
 */

public class User {

    private String userName;
    private String email;
    public String photoUrl;
    public String uniqId;

    public User(){ }
    public User(String userName){
        this.userName = userName;
    }

    public User(String userName, String email){
        this.userName = userName;
        this.email = email;
    }

    public User(String userName, String email, String uniqId){
        this.userName = userName;
        this.email = email;
        this.uniqId = uniqId;
    }


    public User(String userName, String email, String photoUrl,String uniqId){
        this.userName = userName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.uniqId = uniqId;
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
    public void setuniId(String uniqId){
        this.uniqId = uniqId;
    }
    public String getUniqId(){
        return uniqId;
    }
}
