
package com.limemobile.app.blog.weibo.tencent.api.user;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.user.Info;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.UserAPI;

/**
 * 帐户相关/获取自己的详细资料
 * 
 * @param null
 */
public class InfoTask extends TAsyncTask<Info> {
    public InfoTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public InfoTask(ViewGroup container, ILoadListener loadListener, IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        UserAPI userAPI = new UserAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = userAPI.info(TencentWeibo.getInstance(), TencentWeibo.JSON);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new Info();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }

}
