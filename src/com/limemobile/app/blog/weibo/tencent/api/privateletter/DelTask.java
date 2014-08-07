
package com.limemobile.app.blog.weibo.tencent.api.privateletter;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.privateletter.LetterResult;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.PrivateAPI;

/**
 * 删除一条私信
 * 
 * @param id String 要删除的私信id
 */
public class DelTask extends TAsyncTask<LetterResult> {
    public DelTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public DelTask(ViewGroup container, ILoadListener loadListener, IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 1);
        String id = (String) params[0];

        PrivateAPI privateAPI = new PrivateAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = privateAPI.del(TencentWeibo.getInstance(), TencentWeibo.JSON, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new LetterResult();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
