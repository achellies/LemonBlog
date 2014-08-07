
package com.limemobile.app.blog.weibo.tencent.api.friends;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.common.Empty;
import com.tencent.weibo.api.FriendsAPI;

/**
 * 从黑名单中删除某个用户
 * 
 * @param name String 他人的帐户名
 * @param fopenids String 他人的openid（可选）
 */
public class DelBlackListTask extends TAsyncTask<Empty> {

    public DelBlackListTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public DelBlackListTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String name = (String) params[0];
        String fopenids = (String) params[1];
        FriendsAPI friendsAPI = new FriendsAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = friendsAPI.delblacklist(TencentWeibo.getInstance(), TencentWeibo.JSON, name,
                    fopenids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            friendsAPI.shutdownConnection();
        }
        return response;
    }
}
