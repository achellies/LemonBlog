
package com.limemobile.app.blog.weibo.tencent.api.trends;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.trends.HotBroadcast;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.TrendsAPI;

/**
 * 转播热榜
 * 
 * @param reqnum Integer 每次请求记录的条数（1-100条）
 * @param pos Integer 翻页标识，第一次请求时填0，继续填上次返回的pos
 * @param type Integer 微博消息类型 0x1-带文本 0x2-带链接 0x4-带图片 0x8-带视频
 *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，此时type=3即可，填零表示拉取所有类型
 */
public class HotBroadcastTask extends TAsyncTask<HotBroadcast> {

    public HotBroadcastTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public HotBroadcastTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 3);
        String reqnum = Integer.toString((Integer) params[0]);
        String pos = Long.toString((Long) params[1]);
        String type = Integer.toString((Integer) params[2]);

        TrendsAPI trendsAPI = new TrendsAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = trendsAPI.t(TencentWeibo.getInstance(), TencentWeibo.JSON, reqnum, pos, type);
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
            data.data = new HotBroadcast();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
