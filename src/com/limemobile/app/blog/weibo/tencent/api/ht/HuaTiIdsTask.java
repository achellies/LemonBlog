
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
 * 根据话题名称查询话题id
 * 
 * @param httexts String 话题名字列表，用“,”分隔，如abc,efg（最多30个）
 */
public class HuaTiIdsTask extends TAsyncTask<HuaTi> {
    public HuaTiIdsTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public HuaTiIdsTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 1);
        String httexts = (String) params[0];

        HuaTiAPI htAPI = new HuaTiAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = htAPI.ids(TencentWeibo.getInstance(), TencentWeibo.JSON, httexts);
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
