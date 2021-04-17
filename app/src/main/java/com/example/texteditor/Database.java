package com.example.texteditor;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Database.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "users";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_HEAD = "head";
    static final String COLUMN_TEXT = "text";
    static final String COLUMN_TIME = "time";

    Database(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE users (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_HEAD
                + " TEXT, " + COLUMN_TEXT + " TEXT, " + COLUMN_TIME + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}