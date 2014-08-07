
package com.limemobile.app.blog.weibo.tencent.api.other;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.other.Opreadd;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.OtherAPI;

/**
 * 一键转播热门排行
 * 
 * @param reqnum Integer 请求数（最大50个）
 * @param type Integer 所请求的热门转播排行的类型，其值及其说明如下：
 *            type=5，表示该请求用于查询同一网站内的一键转播热门排行，此时get请求中需要提供待查询网站的sourceid的参数值
 *            ，该值需向微博开放平台申请，联系企业QQ：800013811。
 *            type=6，表示该请求用于查询同一地区内的一键转播热门排行，此时get请求中需要提供country,
 *            province和city的参数值。
 * @param country Integer 国家id
 * @param province Integer 地区id
 * @param city Integer 城市id
 */
public class OpreaddTask extends TAsyncTask<Opreadd> {

    public OpreaddTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public OpreaddTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 5);
        String reqnum = Integer.toString((Integer) params[0]);
        String type = Integer.toString((Integer) params[1]);
        String country = Integer.toString((Integer) params[2]);
        String province = Integer.toString((Integer) params[3]);
        String city = Integer.toString((Integer) params[4]);

        OtherAPI otherAPI = new OtherAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = otherAPI.gettopreadd(TencentWeibo.getInstance(), TencentWeibo.JSON, reqnum, type,
                    country, province, city);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            otherAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        data.data = new Opreadd();
        data.data.parse(object);
        return true;
    }

}
