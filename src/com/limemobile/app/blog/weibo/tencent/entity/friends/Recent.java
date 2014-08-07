
package com.limemobile.app.blog.weibo.tencent.entity.friends;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

// 获取最近联系人列表
public class Recent extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -7923987662209805673L;
    public int curnum; // 本次请求返回记录条数
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -5719796021521043899L;
        public String name; // 帐户名
        public String openid; // 用户唯一id，与name相对应
        public String nick; // 昵称
        public String head; // 头像url
        public int sex; // 用户性别，1-男，2-女，0-未填写
        public long fansnum; // 听众数
        public long idolnum; // 收听人数
        public boolean isfans; // 是否我的粉丝，false-不是，true-是
        public boolean isidol; // 是否我收听的人，false-不是，true-是
        public int isrealname; // 是否实名认证，0-老用户，1-已实名认证，2-已注册但未实名认证
        public int isvip; // 是否微博认证用户，0-不是，1-是
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
