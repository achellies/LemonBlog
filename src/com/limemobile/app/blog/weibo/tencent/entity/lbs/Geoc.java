
package com.limemobile.app.blog.weibo.tencent.entity.lbs;

import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;

public class Geoc extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -6036211977019889919L;
    public Info info; // 包含了服务器应答的基本信息
    public Detail detail; // 应答详细信息

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -5004768711063521709L;
        public int type; // 应答的类型，对于geocoding系统，统一为45
        public int error; // 应答错误码，定义如下：
                          // 0：查询正常
                          // -1：系统异常
                          // -2：查询无结果
                          // -3：访问超过次数限制
        public long time; // 响应时间，从发送查询至从服务器取得完整的查询结果的时间，单位是毫秒
    }

    public class Detail extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -1030823569365301185L;
        public String city; // 所在的城市
        public String district; // 所在的区
        public String gps_type; // 数据类型，值域定义为
                                // 1：行政区划中心点
                                // 2：道路中心点
                                // 3：道路交叉口
                                // 4：估算的门址数据
                                // 5：POI（如银科大厦、第三极书局这种类型的）
                                // 6：门址
        public String name; // 结果名称
        public String pcd_conflict_flag; // 结果与查询的行政区划冲突，值域定义为
                                         // 4： 查询中无行政区划信息
                                         // 3：省不一致
                                         // 2：市不一致
                                         // 1：区不一致
                                         // 0：全部一致
        public String pointx; // 经度
        public String pointy; // 纬度
        public String province; // 所在的省份
        public String query_status; // 查询状态，0代表成功，1代表失败
        public String server_retcode; // 定义了一些系统内部的状态代码，和用户关系不大，只知道0值代表成功就可以了
        public String similarity; // 查询字符串与查询结果的相似度
    }

    @Override
    public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
            throws IllegalArgumentException, IllegalAccessException {
        if (typeString.equals("class com.limemobile.app.blog.weibo.tencent.entity.lbs.Geoc$Info")) {
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
                .equals("class com.limemobile.app.blog.weibo.tencent.entity.lbs.Geoc$Detail")) {
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
