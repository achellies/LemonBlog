
package com.limemobile.app.blog.weibo.tencent.api.lbs;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.lbs.AroundPeople;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.LBSAPI;

/**
 * 获取身边的人
 * 
 * @param longitude String 经度，例如：22.541321
 * @param latitude String 纬度，例如：13.935558
 * @param pageinfo String 翻页参数，由上次请求返回（第一次请求时请填空）
 * @param pagesize Integer 请求的每页个数（1-25个）
 * @param gender Integer 性别，0-全部，1-男，2-女
 */
public class GetAroundPeopleTask extends TAsyncTask<AroundPeople> {
    public GetAroundPeopleTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public GetAroundPeopleTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 5);
        String longitude = (String) params[0];
        String latitude = (String) params[1];
        String pageinfo = (String) params[2];
        String pagesize = Integer.toString((Integer) params[3]);
        String gender = Integer.toString((Integer) params[4]);

        LBSAPI lbsAPI = new LBSAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = lbsAPI.get_around_people(TencentWeibo.getInstance(), TencentWeibo.JSON, longitude,
                    latitude, pageinfo, pagesize, gender);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lbsAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new AroundPeople();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
