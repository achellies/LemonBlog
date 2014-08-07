
package com.limemobile.app.blog.weibo.tencent.entity.privateletter;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SendBox extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 6798592338780420843L;
    public String timestamp; // 服务器时间戳
    public int hasnext; // 0-表示还有私信可拉取，1-已拉取完毕
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 5578592278713687793L;
        public String text; // 私信内容
        public String origtext; // 原始内容
        public String from; // 来源
        public long id; // 私信唯一id
        public String image; // 图片url列表
        public String name; // 发表人帐户名
        public String openid; // 用户唯一id，与name相对应
        public String nick; // 发表人昵称
        public int self; // 是否自已发的的私信，0-不是,1-是
        public String timestamp; // 发表时间
        public int type; // 微博类型，1-原创发表，2-转载，3-私信，4-回复，5-空回，6-提及，7-评论
        public String head; // 发表者头像url
        public String toname; // 收信人帐户名
        public String tonick; // 收信人昵称
        public String tohead; // 收信人头像
        public int toisvip; // 收信人是否是微博认证用户
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
