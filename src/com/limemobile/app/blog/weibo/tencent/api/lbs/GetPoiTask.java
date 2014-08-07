
package com.limemobile.app.blog.weibo.tencent.api.lbs;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.lbs.PoiInfo;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.LBSAPI;

/**
 * 获取POI(Point of Interest)
 * 
 * @param longitude String 经度，例如：22.541321
 * @param latitude String 纬度，例如：13.935558
 * @param reqnum Integer 每次请求记录的条数（1-25条）
 * @param radius Integer POI的半径（单位为米），取值范围为100-1000，为达到比较好的搜索结果，建议设置为200
 * @param position Integer 上次查询返回的位置，用于翻页（第一次请求时填0）
 */
public class GetPoiTask extends TAsyncTask<PoiInfo> {
    public GetPoiTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public GetPoiTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 5);
        String longitude = (String) params[0];
        String latitude = (String) params[1];
        String reqnum = Integer.toString((Integer) params[2]);
        String radius = Integer.toString((Integer) params[3]);
        String position = Integer.toString((Integer) params[4]);

        LBSAPI lbsAPI = new LBSAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = lbsAPI.get_poi(TencentWeibo.getInstance(), TencentWeibo.JSON, longitude, latitude,
                    reqnum, radius, position);
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
            data.data = new PoiInfo();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
