
package com.limemobile.app.blog.weibo.tencent.api.other;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.other.KnowPerson;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.OtherAPI;

/**
 * 我可能认识的人
 * 
 * @param reqnum Integer 请求个数（1-200），默认30
 * @param startindex Integer 起始位置（第一页填0，继续向下翻页：填【reqnum*（page-1）】）
 */
public class KnowPersonTask extends TAsyncTask<KnowPerson> {

    public KnowPersonTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public KnowPersonTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String reqnum = Integer.toString((Integer) params[0]);
        String startindex = Integer.toString((Integer) params[1]);

        OtherAPI otherAPI = new OtherAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = otherAPI.kownperson(TencentWeibo.getInstance(), TencentWeibo.JSON, reqnum,
                    startindex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            otherAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        data.data = new KnowPerson();
        data.data.parse(object);
        return true;
    }
}
