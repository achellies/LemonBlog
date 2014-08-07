
package com.limemobile.app.blog.weibo.tencent.api.statuses;

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
import com.tencent.weibo.api.StatusesAPI;

/**
 * 话题时间线(新)
 * 
 * @param reqnum 请求数量（1-100）
 * @param tweetid 微博帖子ID（详细用法见pageflag）
 * @param time 微博帖子生成时间（详细用法见pageflag）
 * @param pageflag 翻页标识(第一次务必填零) pageflag=1表示向下翻页：tweetid和time是上一页的最后一个帖子ID和时间；
 *            pageflag=2表示向上翻页：tweetid和time是下一页的第一个帖子ID和时间；
 * @param flag 是否拉取认证用户，用作筛选。0-拉取所有用户，0x01-拉取认证用户
 * @param httext 话题内容，长度有限制。
 * @param htid 话题ID（可以通过ht/ids获取指定话题的ID）
 *            htid和httext这两个参数不能同时填写，如果都填写只拉取htid指定的微博 ，如果要拉取指定话题的微博，请务必让htid为0
 * @param type 1-原创发表，2-转载
 * @param contenttype 正文类型（按位使用）。1-带文本(这一位一定有)，2-带链接，4-带图片，8-带视频，0x10-带音频
 *            建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80
 */
public class HTTimelineTask extends TAsyncTask<HotBroadcast> {
    public HTTimelineTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public HTTimelineTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 9);
        String reqnum = Integer.toString((Integer) params[0]);
        String tweetid = (String) params[1];
        String time = (String) params[2];
        String pageflag = Integer.toString((Integer) params[3]);
        String flag = Integer.toString((Integer) params[4]);
        String httext = (String) params[5];
        String htid = (String) params[6];
        String type = Integer.toString((Integer) params[7]);
        String contenttype = Integer.toString((Integer) params[8]);

        StatusesAPI statusesAPI = new StatusesAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = statusesAPI.htTimeline(TencentWeibo.getInstance(), TencentWeibo.JSON,
                    reqnum, tweetid, time, pageflag,
                    flag, httext, htid, type, contenttype);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            statusesAPI.shutdownConnection();
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
