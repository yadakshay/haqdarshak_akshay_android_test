package com.example.android.haqdarshak_akshay_android_test.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Akshay on 30-09-2017.
 */

public class usedDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "usersData.db";
    private static final int DATABASE_VERSION = 2;

    public usedDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                databaseContract.usersEntry.TABLE_NAME + " (" +
                databaseContract.usersEntry.COLUMN_MOBILE + " INTEGER PRIMARY KEY, " +
                databaseContract.usersEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                databaseContract.usersEntry.COLUMN_SEX + " TEXT NOT NULL, " +
                databaseContract.usersEntry.COLUMN_AGE + " TEXT NOT NULL, " +
                databaseContract.usersEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                databaseContract.usersEntry.USER_PASSWORD + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + databaseContract.usersEntry.TABLE_NAME);
        onCreate(db);
    }
}