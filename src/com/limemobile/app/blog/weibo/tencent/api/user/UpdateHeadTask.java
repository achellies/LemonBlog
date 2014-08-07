
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
 * 更新用户头像信息
 * 
 * @param picpath String 本地头像图片（最大不超过4M）。文件域表单名，本字段不能放入到签名参数中，不然会出现签名错误
 */
public class UpdateHeadTask extends TAsyncTask<Info> {

    public UpdateHeadTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public UpdateHeadTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 1);
        String picpath = (String) params[0];
        UserAPI userAPI = new UserAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = userAPI.updateHead(TencentWeibo.getInstance(), TencentWeibo.JSON, picpath);
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
