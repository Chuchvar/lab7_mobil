package com.example.lab7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "friends.db";
    public static final int DATABASE_VERSION = 1;

    private static AppDatabase instance = null;
    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance; // [cite: 130]
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + FriendsContract.TABLE_NAME + "(" +
                FriendsContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                FriendsContract.Columns.NAME + " TEXT NOT NULL, " +
                FriendsContract.Columns.EMAIL + " TEXT, " +
                FriendsContract.Columns.PHONE + " TEXT NOT NULL)";

        db.execSQL(sql);

        db.execSQL("INSERT INTO " + FriendsContract.TABLE_NAME + " ("
                + FriendsContract.Columns.NAME + ", "
                + FriendsContract.Columns.PHONE + ") VALUES ('Tom', '+12345678990');");

        db.execSQL("INSERT INTO " + FriendsContract.TABLE_NAME + " ("
                + FriendsContract.Columns.NAME + ", "
                + FriendsContract.Columns.EMAIL + ", "
                + FriendsContract.Columns.PHONE + ") VALUES ('Bob', 'bob@gmail.com', '+13456789102');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // [cite: 178]
    }
}