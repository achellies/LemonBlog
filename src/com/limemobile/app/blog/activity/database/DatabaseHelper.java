
package com.limemobile.app.blog.activity.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public UserListTable userListTable;

    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        userListTable = new UserListTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        userListTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        userListTable.onUpgrade(db, oldVersion, newVersion);
    }

}
