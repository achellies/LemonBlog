package com.limemobile.app.blog.bean;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.limemobile.app.blog.activity.LunchActivity;
import com.limemobile.app.blog.contentprovider.DataProvider;

public class Account extends DObject {
	public boolean active;
	public String name;
	public String nick;
	public String openid;
	public String openkey;
	public String accessToken;
	public String expiresIn;
	public String msg;
	public String date;
	
	public Account() {
		active = false;
	}
	
	public static void getAccountList(Context context, ArrayList<Account> array) {
		array.clear();
		String sortOrder = "date ASC";
		Cursor cursor = context.getContentResolver().query(DataProvider.CONTENT_URI_USER_LIST, null, null, null, sortOrder);
		try {
			while (cursor != null && cursor.moveToNext()) {
				Account object = new Account();
				object.constructFromCursor(cursor);
				array.add(object);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return;
	}
	
	public boolean addAccount(Context context) {
		if (TextUtils.isEmpty(openid))
			throw new IllegalStateException("Account should be set value first.");
		boolean added = false;
		if (active) {
	        String where = null;
	        String[] selectionArgs = null;

	        ContentValues values = new ContentValues();
	        values.put("active", Integer.toString(0));
			context.getContentResolver().update(DataProvider.CONTENT_URI_USER_LIST, values, where, selectionArgs);
		}
		
		String[] projection = new String[] {"openid"};
        String selection = "openid=?";
        String[] selectionArgs = new String[] {openid};
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(DataProvider.CONTENT_URI_USER_LIST, projection, selection, selectionArgs, sortOrder);
        try {
            date = Long.toString(System.currentTimeMillis());
        	ContentValues values = constructContentValues();
	        if (cursor != null && cursor.moveToNext()) {
	        	context.getContentResolver().update(DataProvider.CONTENT_URI_USER_LIST, values, selection, selectionArgs);
	        } else {
	        	context.getContentResolver().insert(DataProvider.CONTENT_URI_USER_LIST, values);
	        }
	        added = true;
        } finally {
        	if (cursor != null)
        		cursor.close();
        }
        
        return added;
	}
	
	public Account deleteAccount(Context context) {
	    Account account = null;
		if (TextUtils.isEmpty(openid))
			throw new IllegalStateException("Account should be set value first.");
		
		String where = "openid=?";
		String[] selectionArgs = new String[] {openid};
		context.getContentResolver().delete(DataProvider.CONTENT_URI_USER_LIST, where, selectionArgs);
		
		Cursor cursor = context.getContentResolver().query(DataProvider.CONTENT_URI_USER_LIST, null, null, null, null);
		try {
			if (cursor != null && cursor.moveToNext()) {
				if (active) {
					Account object = new Account();
					object.constructFromCursor(cursor);
					object.active = active;
					object.addAccount(context);
					
					account = object;
				}
			} else {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(LunchActivity.ACCESS_TOKEN, "");
                editor.putString(LunchActivity.EXPIRES_IN, "");
                editor.putString(LunchActivity.OPEN_ID, "");
                editor.putString(LunchActivity.OPEN_KEY, "");
                editor.putString(LunchActivity.MSG, "");
                editor.commit();
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return account;
	}
}
