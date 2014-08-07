
package com.limemobile.app.blog.weibo.tencent.entity.other;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Emotions extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1023323667125441138L;
    public ArrayList<Emotion> emotions;

    public class Emotion extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 1321647214042832280L;
        public String name; // 表情名称
        public String url; // 表情的url
    }

    @Override
    public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
            throws IllegalArgumentException, IllegalAccessException {
        if (typeString.equals(Constant.CLASS_TYPE_ARRAY_LIST)) {
            JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
            if (jsonArray != null) {
                ArrayList<Emotion> array = new ArrayList<Emotion>();
                for (int index = 0; index < jsonArray.length(); ++index) {
                    try {
                        JSONObjectProxy jsonObject = jsonArray.getJSONObject(index);
                        if (jsonObject != null) {
                            Emotion emotion = new Emotion();
                            emotion.parse(jsonObject);
                            array.add(emotion);
                        }
                    } catch (JSONException e) {
                    }
                }
                field.set(this, array);
            }
        }
    }
}
