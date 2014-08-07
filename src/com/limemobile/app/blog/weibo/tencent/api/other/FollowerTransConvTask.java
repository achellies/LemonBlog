
package com.limemobile.app.blog.weibo.tencent.api.other;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.trends.HotBroadcast;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.OtherAPI;

/**
 * 拉取我收听的用户的转播消息接口
 * 
 * @param rootid Integer 转发或者回复的微博根结点ID
 * @param pageflag Integer 翻页标识，0：第一页，1：向下翻页，2：向上翻页
 * @param pagetime Integer 本页起始时间，和pageflag一起使用，精确定位翻页点，若不需要精确定位，只需给出pageTime
 *            第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间
 * @param tweetid Integer 与pageflag和pagetime一起使用，用于翻页
 *            第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
 * @param reqnum Integer 用户请求的微博消息数目（最多50个）
 */
public class FollowerTransConvTask extends TAsyncTask<HotBroadcast> {

    public FollowerTransConvTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public FollowerTransConvTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 5);
        String rootid = Long.toString((Long) params[0]);
        String pageflag = Integer.toString((Integer) params[1]);
        String pagetime = Integer.toString((Integer) params[2]);
        String tweetid = Integer.toString((Integer) params[3]);
        String reqnum = Integer.toString((Integer) params[4]);

        OtherAPI otherAPI = new OtherAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = otherAPI.follower_trans_conv(TencentWeibo.getInstance(), TencentWeibo.JSON, rootid,
                    pageflag, pagetime, tweetid, reqnum);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            otherAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new HotBroadcast();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
