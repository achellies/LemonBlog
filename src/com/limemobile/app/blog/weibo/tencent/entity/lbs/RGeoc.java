
package com.limemobile.app.blog.weibo.tencent.entity.lbs;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RGeoc extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -1926177648033700333L;
    public Info info; // 包含了服务器应答的基本信息
    public Detail detail; // 应答详细信息

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 1337383330589220039L;
        public int type; // 应答的类型，对于逆地理编码系统，统一为46
        public int error; // 应答错误码，定义如下：
                          // 0：查询正常
                          // -1：系统异常
                          // -2：查询无结果
                          // -3：访问超过次数限制
        public long time; // 响应时间，从发送查询至从服务器取得完整的查询结果的时间，单位是毫秒
    }

    public class Results extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 7602343125330114973L;
        public String name; // 名称
        public String dtype; // 元素的类型，共有如下几种：
                             // AD：精确到区一级的行政区划
                             // ST_NO：门址
                             // ST_NO_A：插值估算的门址
                             // POI：POI
                             // ST：街道
                             // TRAFFIC：公交、地铁站点
                             // WATER：水系
                             // MOUNTAIN：山
                             // TOWN：乡镇
                             // VILLAGE：村庄
                             // RD_CROSS: 道路交叉口
        public String pointx; // 经度
        public String pointy; // 纬度
        public String dist; // 查询点距离该几何对象的距离，单位是米。
        public String adcode; // 六位行政区划码(这个只有在AD类型的数据里才有)。
    }

    public class Poilist extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 736578946037268147L;
        public String name; // 名称
        public String addr; // 地址
        public String dtype; // 系统内部对POI的一个类别编码，可忽略
        public String pointx; // 经度
        public String pointy; // 纬度
        public String dist; // 距离查询点的距离
        public String catalog; // POI的类型
        public String uid; // POI对应的检索POI的id
    }

    /*
     * 如果结尾非0，里面的内容为一个"errmsg"字段，说明了错误的原因，如下例：
     * 对查询：http://api.map.qq.com/rgeoc/?lnglat=1116.306205,39.982094，返回结果如下： {
     * info:{ type:46, error:-1, time:0 }, detail:{
     * errmsg:"input coordinates are illegal." } }
     */
    public class Detail extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = 4713767227428847732L;
        public String errmsg; // 如果结尾非0，里面的内容为一个"errmsg"字段，说明了错误的原因，
        public ArrayList<Results> results; // 里面的内容为"results"字段，对应的是一个json数组，包含一系列查询得到的结果。
        public ArrayList<Poilist> poilist; // 包含了取回来的POI对象，是综合考虑了POI本身权重和距离查询点的距离得到的一个最终排序的结果

        @Override
        public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
                throws IllegalArgumentException, IllegalAccessException {
            if (typeString.equals(Constant.CLASS_TYPE_ARRAY_LIST)) {
                if (fieldName.equals("results")) {
                    JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
                    if (jsonArray != null) {
                        ArrayList<Results> array = new ArrayList<Results>();
                        for (int index = 0; index < jsonArray.length(); ++index) {
                            try {
                                JSONObjectProxy jsonObject = jsonArray.getJSONObject(index);
                                if (jsonObject != null) {
                                    Results results = new Results();
                                    results.parse(jsonObject);
                                    array.add(results);
                                }
                            } catch (JSONException e) {
                            }
                        }
                        field.set(this, array);
                    }
                } else if (fieldName.equals("poilist")) {
                    JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
                    if (jsonArray != null) {
                        ArrayList<Poilist> array = new ArrayList<Poilist>();
                        for (int index = 0; index < jsonArray.length(); ++index) {
                            try {
                                JSONObjectProxy jsonObject = jsonArray.getJSONObject(index);
                                if (jsonObject != null) {
                                    Poilist poilist = new Poilist();
                                    poilist.parse(jsonObject);
                                    array.add(poilist);
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

    @Override
    public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
            throws IllegalArgumentException, IllegalAccessException {
        if (typeString.equals("class com.limemobile.app.blog.weibo.tencent.entity.lbs.RGeoc$Info")) {
            try {
                JSONObjectProxy jsonObject = data.getJSONObject(fieldName);
                if (jsonObject != null) {
                    Info info = new Info();
                    info.parse(jsonObject);
                    field.set(this, info);
                }
            } catch (JSONException e) {
            }
        } else if (typeString
                .equals("class com.limemobile.app.blog.weibo.tencent.entity.lbs.RGeoc$Detail")) {
            try {
                JSONObjectProxy jsonObject = data.getJSONObject(fieldName);
                if (jsonObject != null) {
                    Detail detail = new Detail();
                    detail.parse(jsonObject);
                    field.set(this, detail);
                }
            } catch (JSONException e) {
            }
        }
    }
}
