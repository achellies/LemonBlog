
package com.limemobile.app.blog.weibo.tencent.api.t;

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
import com.tencent.weibo.api.TAPI;

/**
 * 获取单条微博的转播理由/点评列表
 * 
 * @param flag 标识。0－转播列表 1－点评列表 2－点评与转播列表
 * @param rootid 转发或回复的微博根结点id（源微博id）
 * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
 * @param pagetime 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
 * @param reqnum 每次请求记录的条数（1-100条）
 * @param twitterid 翻页用，第1-100条填0，继续向下翻页，填上一次请求返回的最后一条记录id
 */
public class ReListTask extends TAsyncTask<HotBroadcast> {
    public ReListTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public ReListTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 6);
        String flag = Integer.toString((Integer) params[0]);
        String rootid = Long.toString((Long) params[1]);
        String pageflag = Integer.toString((Integer) params[2]);
        String pagetime = Integer.toString((Integer) params[3]);
        String reqnum = Integer.toString((Integer) params[4]);
        String twitterid = Long.toString((Long) params[5]);

        TAPI tAPI = new TAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = tAPI.reList(TencentWeibo.getInstance(), TencentWeibo.JSON, flag, rootid, pageflag,
                    pagetime, reqnum, twitterid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tAPI.shutdownConnection();
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
