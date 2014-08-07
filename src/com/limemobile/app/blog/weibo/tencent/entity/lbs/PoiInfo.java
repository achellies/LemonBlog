
package com.limemobile.app.blog.weibo.tencent.entity.lbs;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PoiInfo extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1700036347519671929L;
    public int lastposition; // 本次请求返回的最后一个POI的位置，用于下次请求的翻页，即poition参数
    public ArrayList<Info> poiinfo;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -617298351686866815L;
        public String addr; // POI点的地址
        public long distance; // 离请求点的距离（单位：米）
        public long districtcode; // 行政区号编码，与身份证号前6位意义相同
        public long hot; // 热度值
        public long latitude; // 纬度
        public long longitude; // 经度
        public long poiid; // POI的ID
        public String poiname; // POI名称　
        public long poitype; // POI类型
        public String poitypename; // POI类型名称（如:公司，医院，学校等）
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
