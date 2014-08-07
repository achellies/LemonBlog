
package com.limemobile.app.blog.weibo.tencent.api.fav;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.fav.FavResult;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.FavoriteAPI;

/**
 * 取消订阅话题
 * 
 * @param id String 话题id
 */
public class DelHTTask extends TAsyncTask<FavResult> {
    public DelHTTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public DelHTTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 1);
        String id = (String) params[0];

        FavoriteAPI favoriteAPI = new FavoriteAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = favoriteAPI.delht(TencentWeibo.getInstance(), TencentWeibo.JSON, id);
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
            data.data = new FavResult();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
