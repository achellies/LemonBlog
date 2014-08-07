
package com.limemobile.app.blog.weibo.tencent.api.lbs;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.common.Empty;
import com.tencent.weibo.api.LBSAPI;

/**
 * 更新地理位置
 * 
 * @param longitude String 经度，例如：22.541321
 * @param latitude String 纬度，例如：13.935558
 */
public class UpdatePosTask extends TAsyncTask<Empty> {
    public UpdatePosTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public UpdatePosTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String longitude = (String) params[0];
        String latitude = (String) params[1];

        LBSAPI lbsAPI = new LBSAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = lbsAPI
                    .update_pos(TencentWeibo.getInstance(), TencentWeibo.JSON, longitude, latitude);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lbsAPI.shutdownConnection();
        }
        return response;
    }
}
