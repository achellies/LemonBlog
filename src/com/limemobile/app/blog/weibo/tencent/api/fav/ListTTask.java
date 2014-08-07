
package com.limemobile.app.blog.weibo.tencent.api.fav;

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
import com.tencent.weibo.api.FavoriteAPI;

/**
 * 收藏的微博列表
 * 
 * @param pageflag Integer 分页标识（0：第一页，1：向下翻页，2：向上翻页）
 * @param pagetime Integer
 *            翻页用，第一页时：填0；向上翻页：填上一次请求返回的prevtime字段值；向下翻页：填上一次请求返回的nexttime字段值
 * @param reqnum Integer 每次请求记录的条数（1-100条）
 * @param lastid Integer
 *            翻页用，第一页时：填0；继续向上翻页：填上一次请求返回的第一条记录id；继续向下翻页：填上一次请求返回的最后一条记录id
 */
public class ListTTask extends TAsyncTask<HotBroadcast> {
    public ListTTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public ListTTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 4);
        String pageflag = Integer.toString((Integer) params[0]);
        String pagetime = (String) params[1];
        String reqnum = Integer.toString((Integer) params[2]);
        String lastid = (String) params[3];

        FavoriteAPI favoriteAPI = new FavoriteAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = favoriteAPI.list_t(TencentWeibo.getInstance(), TencentWeibo.JSON, pageflag,
                    pagetime, reqnum, lastid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            favoriteAPI.shutdownConnection();
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
