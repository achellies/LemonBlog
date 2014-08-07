
package com.limemobile.app.blog.weibo.tencent.entity.search;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class HuaTi extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 9170733377810414913L;
    public int totalnum; // 搜索结果总条数（估算值）
    public int hasnext; // 1-可以往下翻，2-可以往上翻 3-两边都可以翻 0-不能向上或向下翻页
    public String timestamp; // 服务器时间戳
    public ArrayList<Info> info;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 5336769246439977067L;
        public String id; // 话题id
        public String text; // 话题名字
        public int favnum; // 收藏数
        public int tweetnum; // 话题下微博数
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
