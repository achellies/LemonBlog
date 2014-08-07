
package com.limemobile.app.blog.weibo.tencent.api.ht;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.ht.HuaTi;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.HuaTiAPI;

/**
 * 根据话题id获取话题相关信息
 * 
 * @param ids String 话题id列表，用“,”分隔，如12345,12345（最多30个）
 */
public class HuaTiInfoTask extends TAsyncTask<HuaTi> {
    public HuaTiInfoTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public HuaTiInfoTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 1);
        String ids = (String) params[0];

        HuaTiAPI htAPI = new HuaTiAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = htAPI.info(TencentWeibo.getInstance(), TencentWeibo.JSON, ids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            htAPI.shutdownConnection();
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
