
package com.limemobile.app.blog.weibo.tencent.entity.friends;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tag;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tweet;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

// 我的听众列表
// 我收听的人列表
// 特别收听列表
public class Friend extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -1402488333172861128L;
    public String timestamp; // 服务器时间戳
    public int hasnext; // 0-表示还有数据，1-表示下页没有数据
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 5335601827719045368L;
        public String name; // 帐户名
        public String openid; // 用户唯一id，与name相对应
        public String nick; // 昵称
        public String head; // 头像url
        public int sex; // 用户性别，1-男，2-女，0-未填写
        public String location; // 用户发表微博时的所在地
        public int country_code; // 国家码
        public int province_code; // 省份码
        public int city_code; // 城市码
        public ArrayList<Tweet> tweet; // 用户最近发的一条微博
        public long fansnum; // 听众数
        public long idolnum; // 偶像数
        public boolean isfans; // 是否我的听众，false-不是，1-是
        public boolean isidol; // 是否我收听的人
        public int isvip; // 是否微博认证用户，0-不是，1-是
        public int isrealname; // 是否实名认证，0-老用户，1-已实名认证，2-已注册但未实名认证
        public ArrayList<Tag> tag; // 用户标签

        public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
                throws IllegalArgumentException, IllegalAccessException {
            if (typeString.equals(Constant.CLASS_TYPE_ARRAY_LIST)) {
                if (fieldName.equals("tweet")) {
                    JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
                    if (jsonArray != null) {
                        ArrayList<Tweet> array = new ArrayList<Tweet>();
                        for (int index = 0; index < jsonArray.length(); ++index) {
                            try {
                                JSONObjectProxy jsonObject = jsonArray.getJSONObject(index);
                                if (jsonObject != null) {
                                    Tweet tag = new Tweet();
                                    tag.parse(jsonObject);
                                    array.add(tag);
                                }
                            } catch (JSONException e) {
                            }
                        }
                        field.set(this, array);
                    }
                } else if (fieldName.equals("tag")) {
                    JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
                    if (jsonArray != null) {
                        ArrayList<Tag> array = new ArrayList<Tag>();
                        for (int index = 0; index < jsonArray.length(); ++index) {
                            try {
                                JSONObjectProxy jsonObject = jsonArray.getJSONObject(index);
                                if (jsonObject != null) {
                                    Tag tag = new Tag();
                                    tag.parse(jsonObject);
                                    array.add(tag);
                                }
                            } catch (JSONException e) {
                            }
                        }
                        field.set(this, array);
                    }
                }
            } else if (typeString
                    .equals("com.limemobile.app.blog.weibo.tencent.entity.common.Tweet")) {
                try {
                    JSONObjectProxy tweetObject = data.getJSONObject(fieldName);
                    if (tweetObject != null) {
                        Tweet tweet = new Tweet();
                        tweet.parse(tweetObject);
                        field.set(this, tweet);
                    }
                } catch (JSONException e) {
                }
            }
        }
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
