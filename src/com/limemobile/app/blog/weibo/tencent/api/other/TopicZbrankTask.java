
package com.limemobile.app.blog.weibo.tencent.api.other;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.other.TopicZbrank;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.OtherAPI;

/**
 * 同话题热门转播
 * 
 * @param format 返回数据的格式（json或xml）
 * @param checktype 值为1时表示查询“全时段”排行榜，2表示查询“单周内“排行榜，3表示查询”单月内“排行榜
 * @param topictype 用户通过何种方式指定要查询的话题
 *            0-表示按照ID查询，通过topicid参数指定要查询的话题id；1-表示按照话题标题进行查询
 *            ，topicname参数指定要查询的话题的标题
 *            要使用该接口，需先向微博开放平台提供要拉取的话题id和话题名称，申请对该话题进行配置，
 *            联系企业QQ：800013811或直接联系BD。
 * @param reqnum 用户请求的微博消息数目（最多100个）
 */
public class TopicZbrankTask extends TAsyncTask<TopicZbrank> {

    public TopicZbrankTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public TopicZbrankTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 4);
        String checktype = Integer.toString((Integer) params[0]);
        String topictype = Integer.toString((Integer) params[1]);
        String topicid = Integer.toString((Integer) params[2]);
        String reqnum = Integer.toString((Integer) params[3]);

        OtherAPI otherAPI = new OtherAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = otherAPI.gettopiczbrank(TencentWeibo.getInstance(), TencentWeibo.JSON, checktype,
                    topictype, topicid, reqnum);
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
            data.data = new TopicZbrank();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
