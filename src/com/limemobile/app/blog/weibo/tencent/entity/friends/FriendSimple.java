
package com.limemobile.app.blog.weibo.tencent.entity.friends;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FriendSimple extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 4284247923021115377L;
    public String timestamp; // 服务器时间戳
    public int hasnext; // 0-表示还有数据，1-表示下页没有数据
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -3091554324244879061L;
        public String name; // 帐户名
        public String nick; // 昵称
        public String head; // 头像url
        public int sex; // 用户性别，1-男，2-女，0-未填写
        public long fansnum; // 听众数
        public long idolnum; // 收听人数
        public int isidol; // 是否我收听的人
        public int isvip; // 是否名人用户
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
