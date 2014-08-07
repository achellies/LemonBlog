
package com.limemobile.app.blog.weibo.tencent.api.statuses;

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
import com.tencent.weibo.api.StatusesAPI;

/**
 * 获取二传手列表
 * 
 * @param reqnum 每次请求记录的条数（1-50条）
 * @param rootid 二传手转发或者回复查询节点的父亲结点id
 * @param type 1-转发，2-点评
 * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
 * @param pagetime 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
 * @param lastid 
 *            和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
 */
public class SubReListTask extends TAsyncTask<HotBroadcast> {
    public SubReListTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public SubReListTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 4);
        String reqnum = Integer.toString((Integer) params[0]);
        String rootid = Long.toString((Long) params[0]);
        String type = Integer.toString((Integer) params[0]);
        String pageflag = Integer.toString((Integer) params[1]);
        String pagetime = Integer.toString((Integer) params[2]);
        String lastid = Integer.toString((Integer) params[3]);

        StatusesAPI statusesAPI = new StatusesAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = statusesAPI.sub_re_list(TencentWeibo.getInstance(), TencentWeibo.JSON, reqnum,
                    rootid, type, pageflag, pagetime, lastid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            statusesAPI.shutdownConnection();
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
