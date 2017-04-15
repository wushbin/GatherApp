package com.example.wushbin.dukegatherapllication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by wushbin on 3/8/17.
 */

public class MessageDbHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = MessageDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "message.db";
    private static final int DATABASE_VERSION = 1;

    public MessageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USERS_TABLE =  "CREATE TABLE " + MessageContract.MessageEntry.TABLE_NAME + " ("
                + MessageContract.MessageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MessageContract.MessageEntry.COLUMN_USER_FIRST_NAME + " TEXT NOT NULL, "
                + MessageContract.MessageEntry.COLUMN_USER_LAST_NAME + " TEXT NOT NULL, "
                + MessageContract.MessageEntry.COLUMN_MESSAGE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
