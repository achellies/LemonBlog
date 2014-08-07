
package com.limemobile.app.blog.weibo.tencent.entity.friends;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

// 互听关系链列表
public class Mutual extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1480584481347965225L;
    public int hasnext; // 0-表示还有数据，1-表示下页没有数据了
    public int nextstartpos; // 下次请求开始位置
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -688876109383056993L;
        public String name; // 帐户名
        public String openid; // 用户唯一id，与name相对应
        public String nick; // 昵称
        public String headurl; // 头像url
        public long fansnum; // 粉丝数
        public long idolnum; // 偶像数
        public int isvip; // 是否微博认证用户
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
