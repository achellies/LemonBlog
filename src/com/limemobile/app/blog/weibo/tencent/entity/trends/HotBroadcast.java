
package com.limemobile.app.blog.weibo.tencent.entity.trends;

import android.text.TextUtils;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tweet;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

// 转播热榜
public class HotBroadcast extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1604371632474420068L;
    public long pos; // 本次拉取达到的最后一个位置，用于下次请求时的起始位置
    public String timestamp; // 服务器时间戳，不能用于翻页
    public int hasnext; // 0-表示还有微博可拉取，1-已拉取完毕
    public String pageinfo; // 用于 (LBS相关/获取身边最新的微博 用于翻页参数)，即输入参数字段的pageinfo,
                            // 例如："msgid=90140103228163,time=1325316332",
    public long totalnum; // 所有记录的总数 用于（其他/拉取精华转播消息接口）
    public ArrayList<Tweet> info;
    public ArrayList<User> user;

    public class User extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 107538502397924791L;
        // 当页数据涉及到的用户的帐号与昵称映射
        public String name; // 帐户名
        public String nick; // 昵称
    }

    public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
            throws IllegalArgumentException, IllegalAccessException {
        if (typeString.equals(Constant.CLASS_TYPE_ARRAY_LIST)) {
            if (fieldName.equals("info")) {
                JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
                if (jsonArray != null) {
                    ArrayList<Tweet> array = new ArrayList<Tweet>();
                    for (int index = 0; index < jsonArray.length(); ++index) {
                        try {
                            JSONObjectProxy jsonObject = jsonArray.getJSONObject(index);
                            if (jsonObject != null) {
                                Tweet info = new Tweet();
                                info.parse(jsonObject);
                                array.add(info);
                            }
                        } catch (JSONException e) {
                        }
                    }
                    field.set(this, array);
                }
            } else if (fieldName.equals("user")) {
                JSONObjectProxy userObject = data.getJSONObjectOrNull(fieldName);
                if (userObject != null) {
                    String userString = userObject.toString();
                    if (userString.startsWith("{"))
                        userString = userString.substring(1);
                    if (userString.endsWith("}"))
                        userString = userString.substring(0, userString.length() - 1);
                    if (!TextUtils.isEmpty(userString)) {
                        String[] array = userString.split(",");
                        ArrayList<User> userList = new ArrayList<User>();
                        for (String object : array) {
                            String[] tmp = object.split(":");
                            User user = new User();
                            String tmp2 = tmp[0];
                            if (tmp2.startsWith("\""))
                                tmp2 = tmp2.substring(1);
                            if (tmp2.endsWith("\""))
                                tmp2 = tmp2.substring(0, tmp2.length() - 1);
                            user.name = tmp2;

                            tmp2 = tmp[1];
                            if (tmp2.startsWith("\""))
                                tmp2 = tmp2.substring(1);
                            if (tmp2.endsWith("\""))
                                tmp2 = tmp2.substring(0, tmp2.length() - 1);
                            user.nick = tmp2;
                            userList.add(user);
                        }
                        field.set(this, userList);
                    }
                }
            }
        }
    }
}
