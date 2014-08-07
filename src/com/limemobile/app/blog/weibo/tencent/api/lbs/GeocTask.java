
package com.limemobile.app.blog.weibo.tencent.api.lbs;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.lbs.Geoc;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.LBSAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 通过地理位置获取经纬度
 * 
 * @param addr String 地理位置
 * @param reqsrc String 请求来源，请填写"wb”
 * @see http
 *      ://ugc.map.soso.com/geoc/?addr=%E5%8C%97%E4%BA%AC%E5%B8%82%E6%B5%B7%E6
 *      %B7%80%E5%8C%BA%E6%B5%B7%E6%B7%80%E5%A4%A7%E8%A1%9738%E5%8F%B7&reqsrc=wb
 */
public class GeocTask extends TAsyncTask<Geoc> {

    public GeocTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public GeocTask(ViewGroup container, ILoadListener loadListener, IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String addr = (String) params[0];
        String reqsrc = (String) params[1];

        LBSAPI lbsAPI = new LBSAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = lbsAPI.geoc(TencentWeibo.getInstance(), TencentWeibo.JSON, addr, reqsrc);
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
                data.data = new Geoc();
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
