
package com.limemobile.app.blog.weibo.tencent.entity;

import com.limemobile.app.blog.constant.Constant;
import com.loopj.android.http.JSONObjectProxy;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TObject implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6316678189169178117L;

    public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
            throws IllegalArgumentException, IllegalAccessException {
    }

    public void parse(JSONObjectProxy data) {
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            try {
                Class<?> typeClass = field.getType();
                String typeString = typeClass.toString();
                String fieldName = field.getName();
                if (typeString.equals(Constant.CLASS_TYPE_BOOLEAN)) {
                    field.setBoolean(this, data.getBooleanOrNull(fieldName));
                } else if (typeString.equals(Constant.CLASS_TYPE_INT)) {
                    field.setInt(this, data.getIntOrNull(fieldName));
                } else if (typeString.equals(Constant.CLASS_TYPE_LONG)) {
                    field.setLong(this, data.getLongOrNull(fieldName));
                } else if (typeString.equals(Constant.CLASS_TYPE_STRING)) {
                    if (data.getStringOrNull(fieldName) != null)
                        field.set(this, data.getStringOrNull(fieldName));
                    else if (data.getLongOrNull(fieldName) != null)
                        field.set(this, data.getLongOrNull(fieldName).toString());
                } else {
                    parse(field, typeString, data, fieldName);
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (NullPointerException e) {
            }
        }
    }

    public String dump() {
        StringBuilder sb = new StringBuilder();
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            try {
                Class<?> typeClass = field.getType();
                String typeString = typeClass.toString();
                String fieldName = field.getName();
                if (typeString.equals(Constant.CLASS_TYPE_BOOLEAN)) {
                    sb.append(fieldName + " = " + field.getBoolean(this));
                    sb.append("\r\n");
                } else if (typeString.equals(Constant.CLASS_TYPE_INT)) {
                    sb.append(fieldName + " = " + field.getInt(this));
                    sb.append("\r\n");
                } else if (typeString.equals(Constant.CLASS_TYPE_LONG)) {
                    sb.append(fieldName + " = " + field.getLong(this));
                    sb.append("\r\n");
                } else if (typeString.equals(Constant.CLASS_TYPE_STRING)) {
                    sb.append(fieldName + " = " + field.get(this));
                    sb.append("\r\n");
                } else {
                    Object object = field.get(this);
                    if (typeString.equals(Constant.CLASS_TYPE_ARRAY_LIST)) {
                        @SuppressWarnings("unchecked")
                        ArrayList<? extends Object> list = (ArrayList<? extends Object>) object;
                        int index = 0;
                        for (Object tmp : list) {
                            if (tmp instanceof TObject) {
                                sb.append(((TObject) tmp).dump());
                            } else {
                                sb.append("index = " + index + "   " + fieldName + " = "
                                        + tmp.toString());
                                sb.append("\r\n");
                            }
                            ++index;
                        }
                    } else {
                        sb.append(((TObject) object).dump());
                    }
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (NullPointerException e) {
            }
        }

        return sb.toString();
    }
}
