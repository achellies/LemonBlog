
package com.limemobile.app.blog.weibo.tencent.api.tag;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.tag.Tag;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.TagAPI;

/**
 * 删除标签
 * 
 * @param tagid String 标签id
 */
public class DelTagTask extends TAsyncTask<Tag> {

    public DelTagTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public DelTagTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 1);
        String tagid = (String) params[0];

        TagAPI tagAPI = new TagAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = tagAPI.del(TencentWeibo.getInstance(), TencentWeibo.JSON, tagid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tagAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new Tag();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
