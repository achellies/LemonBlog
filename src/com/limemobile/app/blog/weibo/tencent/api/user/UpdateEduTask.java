
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
 * 更新用户教育信息
 * 
 * @param feildid String
 *            教育信息记录id（大于0，添加：feildid=1；修改和删除：fieldid通过调用user/info接口获取
 *            ，删除：下面四个参数均为空） 必填项
 * @param year Integer 入学年限（1900至今）
 * @param schoolid Integer 学校id，请参考http://open.t.qq.com/download/edu.zip
 * @param departmentid Integer 院系id，请参考http://open.t.qq.com/download/edu.zip
 * @param level Integer 学历，请参考http://open.t.qq.com/download/edu.zip
 */
public class UpdateEduTask extends TAsyncTask<Info> {

    public UpdateEduTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public UpdateEduTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 5);
        String feildid = (String) params[0];
        String year = Integer.toString((Integer) params[1]);
        String schoolid = Integer.toString((Integer) params[2]);
        String departmentid = Integer.toString((Integer) params[3]);
        String level = Integer.toString((Integer) params[4]);
        UserAPI userAPI = new UserAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = userAPI.updateEdu(TencentWeibo.getInstance(), TencentWeibo.JSON, feildid, year,
                    schoolid, departmentid, level);
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
