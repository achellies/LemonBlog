
package com.limemobile.app.blog.weibo.tencent.entity.search;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tag;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tweet;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class User extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -2078519024092604609L;
    public String timestamp; // 服务器时间戳
    public long totalnum; // 所有记录总数
    public int hasnext; // 翻页标志，0-第一页且只有一页，1-多页第一页，还可向下翻页，2-还可向上翻页，3-可向上或向下翻页

    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 6712432842932562738L;
        public String name; // 帐户名
        public String openid; // 用户唯一id，与name相对应
        public String nick; // 昵称
        public String head; // 头像url
        public int isvip; // 是否微博认证用户，0-不是，1-是
        public int isrealname; // 是否实名认证，0-老用户，1-已实名认证，2-已注册但未实名认证
        public String location; // 用户所在地
        public int country_code; // 国家码（与地区发表时间线一样）
        public int province_code; // 省份码（与地区发表时间线一样）
        public int city_code; // 城市码（与地区发表时间线一样）
        public ArrayList<Tweet> tweet; // 用户最近发的一条微博
        public long fansnum; // 听众数
        public long idolnum; // 收听人数
        public int isidol; // 是否我收听的人，0-不是，1-是
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
                                    Tweet info = new Tweet();
                                    info.parse(jsonObject);
                                    array.add(info);
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
                                    Tag info = new Tag();
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
