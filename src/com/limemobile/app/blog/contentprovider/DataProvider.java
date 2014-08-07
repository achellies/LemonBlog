
package com.limemobile.app.blog.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.limemobile.app.blog.activity.database.DatabaseHelper;
import com.limemobile.app.blog.activity.database.UserListTable;
import com.limemobile.app.blog.constant.Constant;

import java.io.File;

public class DataProvider extends ContentProvider {

    public static final String SHEME = "content://";
    public static final String AUTHORITY = "com.limemobile.app.blog.contentprovider.DataProvider";

    public static final String DATABASENAME = "tencent_weibo.db";
    private static final int dbVersion = 1;

    public static final Uri CONTENT_URI_USER_LIST = Uri.parse(SHEME + AUTHORITY + "/"
            + UserListTable.SQL_TABLE_NAME);
    private static final int DATABASE_USER_LIST = 1;

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, UserListTable.SQL_TABLE_NAME, DATABASE_USER_LIST);
    }

    private DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        String cacheName = DATABASENAME;
        File cacheBase = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + Constant.cacheDir + Constant.databaseFolder);
        else
            cacheBase = new File(getContext().getCacheDir() + File.separator + Constant.databaseFolder);
        File cacheFile = new File(cacheBase.getAbsolutePath() + File.separator + cacheName);
        if (!cacheFile.getParentFile().exists())
            cacheFile.getParentFile().mkdirs();
        dbHelper = new DatabaseHelper(getContext(), cacheFile.getAbsolutePath(), null, dbVersion);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
    	String groupBy = null;
    	String having = null;
        switch (sUriMatcher.match(uri)) {
            case DATABASE_USER_LIST:
            	return dbHelper.getReadableDatabase().query(UserListTable.SQL_TABLE_NAME, projection, selection, selectionArgs, groupBy, having, sortOrder);
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case DATABASE_USER_LIST:
            	return "vnd.android.cursor.dir/" + UserListTable.SQL_TABLE_NAME;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
    	String nullColumnHack = null;
        switch (sUriMatcher.match(uri)) {
            case DATABASE_USER_LIST:
            	return Uri.parse(CONTENT_URI_USER_LIST.toString() + "/" + dbHelper.getWritableDatabase().insert(UserListTable.SQL_TABLE_NAME, nullColumnHack, values));
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        switch (sUriMatcher.match(uri)) {
            case DATABASE_USER_LIST:
            	return dbHelper.getWritableDatabase().delete(UserListTable.SQL_TABLE_NAME, whereClause, whereArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause,
            String[] whereArgs) {
        switch (sUriMatcher.match(uri)) {
            case DATABASE_USER_LIST:
            	return dbHelper.getWritableDatabase().update(UserListTable.SQL_TABLE_NAME, values, whereClause, whereArgs);
        }
        return 0;
    }

}
