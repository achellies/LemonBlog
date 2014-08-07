
package com.limemobile.app.blog.weibo.tencent.entity.friends;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

// 黑名单列表
public class Blacklist extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -4260435122697237227L;
    public int hasnext; // 0-表示还有数据，1-表示下页没有数据
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -809310807957302217L;
        public String name; // 帐户名
        public String openid; // 用户唯一id，与name相对应
        public String nick; // 昵称
        public String head; // 头像url
    }

    public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
            throws IllegalArgumentException, IllegalAccessException {
        if (typeString.equals(Constant.CLASS_TYPE_ARRAY_LIST)) {
            JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
            if (jsonArray != null) {
                ArrayList<Info> array = new ArrayList<Info>();
                for (int index = 0; index < jsonArray.length(); ++index) {
                    try {
                        JSONObjectProxy jsonObject = jsonArray.getJSONObject(index);
                        if (jsonObject != null) {
                            Info info = new Info();
                            info.parse(jsonObject);
                            array.add(info);
                        }
                    } catch (JSONException e) {
                    }
                }
                field.set(this, array);
            }
        }
    }
}
