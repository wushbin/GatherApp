package com.example.wushbin.dukegatherapllication.data;


import android.provider.BaseColumns;

/**
 * Created by wushbin on 3/8/17.
 */

public class MessageContract {
    private MessageContract(){}

    public static final class MessageEntry implements BaseColumns {

        public final static String TABLE_NAME = "Message";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USER_FIRST_NAME = "first_name";
        public final static String COLUMN_USER_LAST_NAME = "last_name";
        public final static String COLUMN_MESSAGE = "message";

    }
}
