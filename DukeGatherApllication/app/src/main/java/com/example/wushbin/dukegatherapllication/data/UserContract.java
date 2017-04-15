package com.example.wushbin.dukegatherapllication.data;

import android.provider.BaseColumns;

/**
 * Created by wushbin on 3/8/17.
 */


public final class UserContract  {
    private UserContract() {}

    public static final class UserEntry implements BaseColumns {
        public final static String TABLE_NAME = "User";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USER_FIRST_NAME = "first_name";
        public final static String COLUMN_USER_LAST_NAME = "last_name";
        public final static String COLUMN_USER_GENDER = "gender";
        public final static String COLUMN_USER_EMAIL = "email";
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }
}