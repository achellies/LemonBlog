
package com.limemobile.app.blog.weibo.tencent.api.friends;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.friends.Check;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.FriendsAPI;

import org.json.JSONException;

/**
 * 检测是否我的听众或收听的人
 * 
 * @param name String 其他人的帐户名列表，用逗号“,”分隔
 * @param fopenids String 其他人的的用户openid列表，用“_”隔开
 * @param flag Integer 0 检测听众，1检测收听的人 2 两种关系都检测
 */
public class CheckTask extends TAsyncTask<Check> {
    public String[] nameList;

    public CheckTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public CheckTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 2);
        String name = (String) params[0];
        String fopenids = (String) params[1];
        String flag = Integer.toString(2); // 0 检测听众，1检测收听的人 2 两种关系都检测

        if (name != null && !name.isEmpty()) {
            nameList = name.split(",");
        } else if (fopenids != null && !fopenids.isEmpty()) {
            nameList = fopenids.split("_");
        }
        FriendsAPI friendsAPI = new FriendsAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = friendsAPI
                    .check(TencentWeibo.getInstance(), TencentWeibo.JSON, name, fopenids, flag);
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
            data.data = new Check();

            if (nameList != null) {
                for (String name : nameList) {
                    JSONObjectProxy jsonObject = dataObject.getJSONObjectOrNull(name);
                    if (jsonObject != null) {
                        Check.Info info = new Check.Info();
                        info.name = name;
                        try {
                            info.isfans = jsonObject.getBoolean("isfans");
                            info.isidol = jsonObject.getBoolean("isidol");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return true;
        }
        return false;
    }
}
