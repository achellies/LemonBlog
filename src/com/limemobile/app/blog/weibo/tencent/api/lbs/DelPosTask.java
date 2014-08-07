
package com.limemobile.app.blog.weibo.tencent.api.lbs;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.common.Empty;
import com.tencent.weibo.api.LBSAPI;

/**
 * 删除最后更新位置
 * 
 * @param null
 */
public class DelPosTask extends TAsyncTask<Empty> {
    public DelPosTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public DelPosTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 0);

        LBSAPI lbsAPI = new LBSAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = lbsAPI.del_pos(TencentWeibo.getInstance(), TencentWeibo.JSON);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lbsAPI.shutdownConnection();
        }
        return response;
    }
}
