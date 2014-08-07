
package com.limemobile.app.blog.weibo.tencent.entity.info;

import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

public class Update extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -8784682701088588551L;
    public long home; // 首页未读消息计数
    public long private_; // 私信页未读消息计数
    public long fans; // 新增听众数
    public long mentions; // 提及我的未读消息计数
    public long create; // 首页广播（原创）更新数

    @Override
    public void parse(JSONObjectProxy data) {
        super.parse(data);
        try {
            private_ = data.getLong("private");
        } catch (JSONException e) {
        }
    }
}
