package com.example.android.haqdarshak_akshay_android_test.database;

import android.provider.BaseColumns;

/**
 * Created by Akshay on 30-09-2017.
 */

public class databaseContract {
    //constructor
    private databaseContract(){}

    public static final class usersEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME = "userName";
        public static final String COLUMN_SEX = "userSex";
        public static final String COLUMN_EMAIL = "emailId";
        public static final String COLUMN_MOBILE = "mobileNum";
        public static final String COLUMN_AGE = "userAge";
        public static final String USER_PASSWORD = "password";
    }
}
