
package com.limemobile.app.blog.weibo.tencent.api.friends;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.friends.Friend;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.FriendsAPI;

/**
 * 其他帐户收听的人列表
 * 
 * @param reqnum Integer 请求个数(1-30)
 * @param startindex Integer 起始位置（第一页填0，继续向下翻页：填【reqnum*（page-1）】）
 * @param name String 用户帐户名
 * @param fopenid String 他人的openid（可选） <br>
 *            name和fopenid至少选一个，若同时存在则以name值为主
 * @param install Integer 过滤安装应用好友（可选） <br>
 *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
 */
public class UserIdolListTask extends TAsyncTask<Friend> {
    public UserIdolListTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public UserIdolListTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 5);
        String reqnum = Integer.toString((Integer) params[0]);
        String startindex = Integer.toString((Integer) params[1]);
        String name = (String) params[2];
        String fopenid = (String) params[3];
        String install = Integer.toString((Integer) params[4]);
        FriendsAPI friendsAPI = new FriendsAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = friendsAPI.userIdollist(TencentWeibo.getInstance(), TencentWeibo.JSON, reqnum,
                    startindex, name, fopenid, install);
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
            data.data = new Friend();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
