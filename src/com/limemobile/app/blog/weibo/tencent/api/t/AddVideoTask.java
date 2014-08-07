
package com.limemobile.app.blog.weibo.tencent.api.t;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.t.TResult;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.TAPI;

/**
 * 发表视频微博
 * 
 * @param content 微博内容
 * @param clientip 用户IP(以分析用户所在地) 用户IP（必填）用户浏览器IP,
 * @param longitude 经度（可以填空） 经度（可以填空）
 * @param latitude 纬度（可以填空）
 * @param url 视频地址，后台自动分析视频信息，支持youku,tudou,ku6
 * @param syncflag 微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0）
 */
public class AddVideoTask extends TAsyncTask<TResult> {
    public AddVideoTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public AddVideoTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 6);
        String content = (String) params[0];
        String clientip = (String) params[1];
        String longitude = (String) params[2];
        String latitude = (String) params[3];
        String url = (String) params[4];
        String syncflag = Integer.toString((Integer) params[5]);

        TAPI tAPI = new TAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = tAPI.addVideo(TencentWeibo.getInstance(), TencentWeibo.JSON, content, clientip,
                    longitude, latitude, url, syncflag);
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
            data.data = new TResult();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
