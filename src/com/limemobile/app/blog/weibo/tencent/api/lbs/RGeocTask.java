
package com.limemobile.app.blog.weibo.tencent.api.lbs;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.lbs.RGeoc;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.LBSAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 通过经纬度获取地理位置
 * 
 * @param lnglat String lnglat 经纬度值，采用经度在前，纬度在后，经纬度值之间用“,”隔开的方式
 *            如：113.94830703735352,22.54046422124227
 * @param reqsrc String 请求来源，请填写"wb”
 * @see http
 *      ://ugc.map.soso.com/rgeoc/?lnglat=113.94830703735352,22.54046422124227
 *      &reqsrc=wb
 */
public class RGeocTask extends TAsyncTask<RGeoc> {

    public RGeocTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public RGeocTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String lnglat = (String) params[0];
        String reqsrc = (String) params[1];

        LBSAPI lbsAPI = new LBSAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = lbsAPI.rgeoc(TencentWeibo.getInstance(), TencentWeibo.JSON, lnglat, reqsrc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lbsAPI.shutdownConnection();
        }
        return response;
    }
    
    @Override
    protected Boolean doInBackground(Object... params) {
        Boolean isOk = Boolean.FALSE;
        String result = callAPI(params);
        Entity<TObject> data = new Entity<TObject>();
        if (!TextUtils.isEmpty(result)) {
            try {
                JSONObjectProxy object = new JSONObjectProxy(new JSONObject(result));
                data.data = new RGeoc();
                data.data.parse(object);
                isOk = Boolean.TRUE;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (resultListener != null) {
            if (isOk)
                resultListener.onSuccess(data);
            else
                resultListener.onFail(data);
        }
        return isOk;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (loadListener != null)
            loadListener.onEnd();
    }
}
