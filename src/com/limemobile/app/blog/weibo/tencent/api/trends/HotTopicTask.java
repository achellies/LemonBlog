
package com.limemobile.app.blog.weibo.tencent.api.trends;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.trends.HotTopic;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.TrendsAPI;

/**
 * 话题热榜
 * 
 * @param reqnum Integer 请求个数（最多20）
 * @param pos Integer 请求位置，第一次请求时填0，继续填上次返回的pos
 */
public class HotTopicTask extends TAsyncTask<HotTopic> {

    public HotTopicTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public HotTopicTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String reqnum = Integer.toString((Integer) params[0]);
        String pos = Long.toString((Long) params[1]);

        TrendsAPI trendsAPI = new TrendsAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = trendsAPI.ht(TencentWeibo.getInstance(), TencentWeibo.JSON, reqnum, pos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            trendsAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new HotTopic();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
