
package com.limemobile.app.blog.weibo.tencent.api.t;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.t.VideoInfo;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.TAPI;

public class GetVideoInfoTask extends TAsyncTask<VideoInfo> {
    public GetVideoInfoTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public GetVideoInfoTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 1);
        String video_url = (String) params[0];

        TAPI tAPI = new TAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = tAPI.getVideoInfo(TencentWeibo.getInstance(), TencentWeibo.JSON, video_url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new VideoInfo();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
