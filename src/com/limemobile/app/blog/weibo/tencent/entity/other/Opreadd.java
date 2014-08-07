
package com.limemobile.app.blog.weibo.tencent.entity.other;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Opreadd extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 7348078611048523827L;
    public ArrayList<Data> data;

    public class Data extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 2764991756984860944L;
        public String tid; // 该地区被一键转播次数最多的微博消息id
        public long count; // 微博消息被一键转播的次数
    }

    @Override
    public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
            throws IllegalArgumentException, IllegalAccessException {
        if (typeString.equals(Constant.CLASS_TYPE_ARRAY_LIST)) {
            JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
            if (jsonArray != null) {
                ArrayList<Data> array = new ArrayList<Data>();
                for (int index = 0; index < jsonArray.length(); ++index) {
                    try {
                        JSONObjectProxy jsonObject = jsonArray.getJSONObject(index);
                        if (jsonObject != null) {
                            Data data2 = new Data();
                            data2.parse(jsonObject);
                            array.add(data2);
                        }
                    } catch (JSONException e) {
                    }
                }
                field.set(this, array);
            }
        }
    }
}
