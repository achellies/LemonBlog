
package com.limemobile.app.blog.weibo.tencent.entity.lbs;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AroundPeople extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -2472224779530376642L;
    public int hasnext; // 是否还有下一页，0－没有，1-有
    public String pageinfo; // 用于下次请求的翻页参数
    public int total; // 总共的人数
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -666705419256257404L;
        public String head; // 头像url
        public String name; // 用户帐户名
        public String nick; // 昵称
        public String openid; // 用户唯一id,与name相对应
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
