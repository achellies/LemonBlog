
package com.limemobile.app.blog.weibo.tencent.api.search;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.search.HuaTi;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.SearchAPI;

/**
 * 话题搜索
 * 
 * @param keyword String 搜索关键字（1-128字节）
 * @param pagesize Integer 本次请求的记录条数（1-20个）
 * @param page Integer 请求的页码，从1开始
 */
public class HTSearchTask extends TAsyncTask<HuaTi> {
    public HTSearchTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public HTSearchTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 3);
        String keyword = (String) params[0];
        String pagesize = Integer.toString((Integer) params[1]);
        String page = Integer.toString((Integer) params[2]);

        SearchAPI searchAPI = new SearchAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = searchAPI.ht(TencentWeibo.getInstance(), TencentWeibo.JSON, keyword, pagesize, page);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            searchAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new HuaTi();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
