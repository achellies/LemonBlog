
package com.limemobile.app.blog.activity.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class UserListTable implements IDatabase {
    public static final String SQL_TABLE_NAME = "userlist_table";
    public static final String SQL_CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS "
            + SQL_TABLE_NAME
            + " (active INTEGER, name TEXT, nick TEXT, openid TEXT, openkey TEXT, accessToken TEXT, expiresIn TEXT, msg TEXT, date TEXT);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserListTable.SQL_CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
