
package com.limemobile.app.blog.weibo.tencent.api.trends;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.trends.RecommendCelebrity;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.TrendsAPI;

/**
 * 推荐名人列表
 * 
 * @param classid Integer 请求的名人类别id（如：娱乐明星类别id:101）
 * @param subclassid Integer
 *            请求的名人类别的子类别id（如媒体机构类别下子类别广播id：subclass_959，若为空，则默认一个子类别
 *            ，没有子类别的，此处为空）
 */
public class RecommendCelebrityTask extends TAsyncTask<RecommendCelebrity> {

    public RecommendCelebrityTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public RecommendCelebrityTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String classid = Integer.toString((Integer) params[0]);
        String subclassid = Integer.toString((Integer) params[1]);

        TrendsAPI trendsAPI = new TrendsAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = trendsAPI.famouslist(TencentWeibo.getInstance(), TencentWeibo.JSON, classid,
                    subclassid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            trendsAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new RecommendCelebrity();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
