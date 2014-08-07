
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
 * 回复一条微博（即对话）
 * 
 * @param content 是 微博内容（若在此处@好友，需正确填写好友的微博账号，而非昵称），不超过140字
 * @param clientip 是 用户ip（必须正确填写用户侧真实ip，不能为内网ip及以127或255开头的ip，以分析用户所在地）
 * @param longitude 否 经度，为实数，如113.421234（最多支持10位有效数字，可以填空）
 * @param latitude 否 纬度，为实数，如22.354231（最多支持10位有效数字，可以填空）
 * @param reid 是 回复的父节点微博id
 * @param syncflag 否 微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0），目前仅支持oauth1.0鉴权方式
 */
public class ReplayTask extends TAsyncTask<TResult> {
    public ReplayTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public ReplayTask(ViewGroup container, ILoadListener loadListener,
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
        String reid = Long.toString((Long) params[4]);
        String syncflag = Integer.toString((Integer) params[5]);

        TAPI tAPI = new TAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = tAPI.reply(TencentWeibo.getInstance(), TencentWeibo.JSON, content, clientip,
                    longitude, latitude, reid, syncflag);
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
