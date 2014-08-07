
package com.limemobile.app.blog.weibo.tencent.api.info;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.info.Update;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.InfoAPI;

/**
 * 查看数据更新条数
 * 
 * @param op Integer 请求类型： 0-仅查询，1-查询完毕后将相应计数清0
 * @param type Integer 5-首页未读消息计数，6-@页未读消息计数，7-私信页消息计数，8-新增听众数，9-首页广播数（原创的）<br>
 *            op=0时，type默认为0，此时返回所有类型计数；op=1时，需带上某种类型的type，除该type类型的计数，并返回所有类型计数
 */
public class UpdateTask extends TAsyncTask<Update> {
    public UpdateTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public UpdateTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String op = Integer.toString((Integer) params[0]);
        String type = Integer.toString((Integer) params[1]);

        InfoAPI infoAPI = new InfoAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = infoAPI.update(TencentWeibo.getInstance(), TencentWeibo.JSON, op, type);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            infoAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new Update();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
