
package com.limemobile.app.blog.weibo.tencent.entity.other;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class KnowPerson extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -3083287914898369460L;
    public int hasnext; // 0-表示还有数据，1-表示下页没有数据
    public ArrayList<Data> data;
    
    public KnowPerson() {
        hasnext = 1;
    }

    public class Data extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -3857074817756946696L;
        public String name; // 帐户名
        public String openid; // 用户唯一id，与name相对应
        public String nick; // 昵称
        public String head; // 头像url
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
