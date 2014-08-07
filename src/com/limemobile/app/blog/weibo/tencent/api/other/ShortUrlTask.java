
package com.limemobile.app.blog.weibo.tencent.api.other;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.other.ShortUrl;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.OtherAPI;

/**
 * 短url变长url
 * 
 * @param url String 短url后辍
 */
public class ShortUrlTask extends TAsyncTask<ShortUrl> {

    public ShortUrlTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public ShortUrlTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 1);
        String url = (String) params[0];

        OtherAPI otherAPI = new OtherAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = otherAPI.shorturl(TencentWeibo.getInstance(), TencentWeibo.JSON, url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            otherAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new ShortUrl();
            data.data.parse(dataObject);
            return true;
        }

        return false;
    }
}
