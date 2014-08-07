
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
 * 帐户相关/获取其他人资料
 * 
 * @param name String 他人的帐户名（可选）
 * @param fopenid String 他人的openid（可选） name和fopenid至少选一个，若同时存在则以name值为主
 */
public class OtherInfoTask extends TAsyncTask<Info> {

    public OtherInfoTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public OtherInfoTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String name = (String) params[0];
        String fopenid = (String) params[1];
        UserAPI userAPI = new UserAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = userAPI.otherInfo(TencentWeibo.getInstance(), TencentWeibo.JSON, name, fopenid);
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
