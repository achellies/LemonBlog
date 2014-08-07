package com.limemobile.app.blog.bean;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

import com.limemobile.app.blog.constant.Constant;

public class DObject {
	public void constructFromCursor(Cursor cursor) {
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            try {
                Class<?> typeClass = field.getType();
                String typeString = typeClass.toString();
                String fieldName = field.getName();
                int columnIndex = cursor.getColumnIndex(fieldName);
                if (typeString.equals(Constant.CLASS_TYPE_BOOLEAN)) {
                    field.setBoolean(this, cursor.getInt(columnIndex) == 1 ? true : false);
                } else if (typeString.equals(Constant.CLASS_TYPE_INT)) {
                    field.setInt(this, cursor.getInt(columnIndex));
                } else if (typeString.equals(Constant.CLASS_TYPE_LONG)) {
                    field.setLong(this, cursor.getLong(columnIndex));
                } else if (typeString.equals(Constant.CLASS_TYPE_STRING)) {
                	field.set(this, cursor.getString(columnIndex));
                } else {
                	constructFromCursor(field, typeString, cursor, fieldName);
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (NullPointerException e) {
            }
        }
	}
	
    public void constructFromCursor(Field field, String typeString, Cursor cursor, String fieldName) {
    }
    
    public ContentValues constructContentValues() {
    	ContentValues values = new ContentValues();
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            try {
                Class<?> typeClass = field.getType();
                String typeString = typeClass.toString();
                String fieldName = field.getName();
                if (typeString.equals(Constant.CLASS_TYPE_BOOLEAN)) {
                	values.put(fieldName, field.getBoolean(this));
                } else if (typeString.equals(Constant.CLASS_TYPE_INT)) {
                	values.put(fieldName, field.getInt(this));
                } else if (typeString.equals(Constant.CLASS_TYPE_LONG)) {
                	values.put(fieldName, field.getLong(this));
                } else if (typeString.equals(Constant.CLASS_TYPE_STRING)) {
                	values.put(fieldName, (String)field.get(this));
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (NullPointerException e) {
            }
        }
    	return values;
    }
}
