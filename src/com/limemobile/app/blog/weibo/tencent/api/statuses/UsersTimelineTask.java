
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
 * 多用户发表时间线
 * 
 * @param pageflag 分页标识（0：第一页，1：向下翻页，2向上翻页）
 * @param pagetime 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
 * @param reqnum 每次请求记录的条数（1-100条）
 * @param lastid 第一页 时填0,继续向下翻页，填上一次请求返回的最后一条记录ID，翻页用
 * @param names 你需要读取用户列表用“,”隔开，例如：abc,bcde,effg（可选，最多30个）
 * @param fopenids 你需要读取的用户openid列表，用下划线“_”隔开，例如：
 *            B624064BA065E01CB73F835017FE96FA_B624064BA065E01CB73F835017FE96FB
 *            （可选，最多30个） <br>
 *            names和fopenids至少选一个，若同时存在则以names值为主
 * @param type 拉取类型, 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评
 * @param contenttype 内容过滤 填零表示所有类型 1-带文本 2-带链接 4图片 8-带视频 0x10-带音频
 */
public class UsersTimelineTask extends TAsyncTask<HotBroadcast> {
    public UsersTimelineTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public UsersTimelineTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 8);
        String pageflag = Integer.toString((Integer) params[0]);
        String pagetime = Integer.toString((Integer) params[1]);
        String reqnum = Integer.toString((Integer) params[2]);
        String lastid = Integer.toString((Integer) params[3]);
        String names = (String) params[4];
        String fopenids = (String) params[5];
        String type = Integer.toString((Integer) params[6]);
        String contenttype = Integer.toString((Integer) params[7]);

        StatusesAPI statusesAPI = new StatusesAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = statusesAPI.usersTimeline(TencentWeibo.getInstance(), TencentWeibo.JSON, pageflag,
                    pagetime, reqnum, lastid, names, fopenids, type, contenttype);
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
