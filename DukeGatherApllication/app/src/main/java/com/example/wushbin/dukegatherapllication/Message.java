package com.example.wushbin.dukegatherapllication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

/**
 * Created by wushbin on 3/8/17.
 */

public class Message {
    private String userName;
    private String messageContent;
    private String photoUrl;

    public Message(){
    }

    public Message(String userName, String messageContent, String photoUrl){
        this.userName = userName;
        this.messageContent = messageContent;
        this.photoUrl = photoUrl;
    }


    public String getUserName(){
        return  userName;
    }

    public String getMessageContent(){return messageContent;}

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setMessageContent(String text){ messageContent = text;}

    public void setUserName(String name){userName = name;}

    public void setPhotoUrl(String url){
        photoUrl = url;
    }


}
