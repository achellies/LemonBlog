
package com.limemobile.app.blog.weibo.tencent.entity.ht;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

// 根据话题id获取话题相关信息
// 根据话题名称查询话题id
public class HuaTi extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 2007611905892396557L;
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 7303300329165127103L;
        public long id; // 话题id
        public String text; // 话题名字
        public long favnum; // 话题被收藏次数
        public long tweetnum; // 话题下微博数
        public String timestamp; // 收藏时间，主要用于 数据收藏
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
