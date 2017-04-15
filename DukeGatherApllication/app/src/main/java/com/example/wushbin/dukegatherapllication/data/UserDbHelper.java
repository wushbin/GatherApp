package com.example.wushbin.dukegatherapllication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wushbin on 3/8/17.
 */

import com.example.wushbin.dukegatherapllication.data.UserContract.UserEntry;

public class UserDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = UserDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "users_info.db";
    private static final int DATABASE_VERSION = 1;
    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USERS_TABLE =  "CREATE TABLE " + UserEntry.TABLE_NAME + " ("
                + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserEntry.COLUMN_USER_FIRST_NAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USER_LAST_NAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USER_GENDER + " INTEGER NOT NULL, "
                + UserEntry.COLUMN_USER_EMAIL + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}