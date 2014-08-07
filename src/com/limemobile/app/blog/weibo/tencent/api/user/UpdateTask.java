
package com.limemobile.app.blog.weibo.tencent.api.user;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.user.Info;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.UserAPI;

/**
 * 更新用户信息
 * 
 * @param nick String 用户昵称（1-12个中文、字母、数字、下划线或减号） 必填项
 * @param sex Integer 性别（0：未填，1：男，2：女）
 * @param year Integer 出生年（1900-2010）
 * @param month Integer 出生月（1-12）
 * @parma day Integer 出生日（1-31）
 * @param countrycode Integer
 *            国家码（不超过4字节），请参考http://open.t.qq.com//download/addresslist.zip
 * @param provincecode Integer
 *            地区码（不超过4字节），请参考http://open.t.qq.com//download/addresslist.zip
 * @param citycode Integer
 *            城市码（不超过4字节），请参考http://open.t.qq.com//download/addresslist.zip
 * @param introduction String 个人介绍，不超过140字
 */
public class UpdateTask extends TAsyncTask<Info> {

    public UpdateTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public UpdateTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 9);
        String nick = (String) params[0];
        String sex = Integer.toString((Integer) params[1]);
        String year = Integer.toString((Integer) params[2]);
        String month = Integer.toString((Integer) params[3]);
        String day = Integer.toString((Integer) params[4]);
        String countrycode = Integer.toString((Integer) params[5]);
        String provincecode = Integer.toString((Integer) params[6]);
        String citycode = Integer.toString((Integer) params[7]);
        String introduction = (String) params[8];
        UserAPI userAPI = new UserAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = userAPI.update(TencentWeibo.getInstance(), TencentWeibo.JSON, nick, sex, year,
                    month, day, countrycode, provincecode, citycode, introduction);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new Info();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }

}
