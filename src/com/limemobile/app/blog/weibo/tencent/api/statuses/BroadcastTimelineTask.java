
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
 * 我发表时间线
 * 
 * @param pageflag 分页标识（0：第一页，1：向下翻页，2向上翻页）
 * @param pagetime 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
 * @param reqnum 每次请求记录的条数（1-200条）
 * @param lastid 
 *            和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id）
 * @param type 拉取类型, 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评
 *            如需拉取多个类型请|上(0x1|0x2) 得到3，type=3即可,填零表示拉取所有类型
 * @param contenttype 内容过滤 填零表示所有类型 1-带文本 2-带链接 4图片 8-带视频 0x10-带音频
 */
public class BroadcastTimelineTask extends TAsyncTask<HotBroadcast> {
    public BroadcastTimelineTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public BroadcastTimelineTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 6);
        String pageflag = Integer.toString((Integer) params[0]);
        String pagetime = (String) params[1];
        String reqnum = Integer.toString((Integer) params[2]);
        String lastid = (String) params[3];
        String type = Integer.toString((Integer) params[4]);
        String contenttype = Integer.toString((Integer) params[5]);

        StatusesAPI statusesAPI = new StatusesAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = statusesAPI.broadcastTimeline(TencentWeibo.getInstance(), TencentWeibo.JSON,
                    pageflag, pagetime, reqnum, lastid, type, contenttype);
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
