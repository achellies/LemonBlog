
package com.limemobile.app.blog.weibo.tencent.entity.user;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

// 帐户相关/获取一批人的简单资料 https://open.t.qq.com/api/user/infos
public class SimpleInfo extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1097401871678781368L;
    public String timestamp; // 服务器时间戳
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -615902656684179245L;
        public String name; // 用户帐户名
        public String openid; // 用户唯一id，与name相对应
        public String nick; // 用户昵称
        public String head; // 头像url
        public int isvip; // 是否认证用户，0-不是，1-是
        public long fansnum; // 听众数
        public long idolnum; // 收听的人数
        public boolean isidol; // 是否我收听的人，false-不是，true-是
        public int isrealname; // 是否实名认证，0-未实名认证，1-已实名认证
        public int sex; // 用户性别，1-男，2-女，0-未填写
        public long exp; // 经验值
        public int level; // 微博等级
    }

    @Override
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
