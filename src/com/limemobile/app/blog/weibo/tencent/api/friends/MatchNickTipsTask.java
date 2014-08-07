
package com.limemobile.app.blog.weibo.tencent.api.friends;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.friends.MatchNick;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.FriendsAPI;

/**
 * 好友账号及昵称输入提示
 * 
 * @param match String 匹配字符串（目前匹配范围为我的偶像，即我收听的人）
 *            采用拆分匹配的规则，如match="cw"，则匹配同时包含c和w的字符串，同时支持拼音首字母匹配
 * @param reqnum Integer 请求个数（1-10）
 */
public class MatchNickTipsTask extends TAsyncTask<MatchNick> {

    public MatchNickTipsTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public MatchNickTipsTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String match = (String) params[0];
        String reqnum = Integer.toString((Integer) params[1]);
        FriendsAPI friendsAPI = new FriendsAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = friendsAPI.match_nick_tips(TencentWeibo.getInstance(), TencentWeibo.JSON, match,
                    reqnum);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            friendsAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new MatchNick();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }

}
