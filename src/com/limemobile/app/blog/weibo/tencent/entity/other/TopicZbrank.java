
package com.limemobile.app.blog.weibo.tencent.entity.other;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TopicZbrank extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -7342965368705280240L;
    public int checktype; // 值为1时表示查询“全时段”排行榜，2表示查询“单周内“排行榜，3表示查询”单月内“排行榜
    public long topicid; // 需要进行转播排行查询的话题id
    public ArrayList<Info> info; // 包含了返回的排行数据

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -3365434208436820450L;
        public long tweetid; // 该话题下被转播次数靠前的原创消息id
        public String name; // 该原创消息的作者微博账号
        public String openid; // 用户唯一id，与name相对应
        public long num; // 该消息被转播的次数
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
