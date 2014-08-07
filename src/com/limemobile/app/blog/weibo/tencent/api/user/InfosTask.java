
package com.limemobile.app.blog.weibo.tencent.api.user;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.user.SimpleInfo;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.UserAPI;

/**
 * 帐户相关/获取一批人的简单资料
 * 
 * @param names String 用户ID列表 比如 abc,edf,xxxx
 * @param fopenids String 他人的openid（可选） name和fopenid至少选一个，若同时存在则以name值为主
 */
public class InfosTask extends TAsyncTask<SimpleInfo> {

    public InfosTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public InfosTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String names = (String) params[0];
        String fopenids = (String) params[1];

        UserAPI userAPI = new UserAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = userAPI.infos(TencentWeibo.getInstance(), TencentWeibo.JSON, names, fopenids);
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
            data.data = new SimpleInfo();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }

}
