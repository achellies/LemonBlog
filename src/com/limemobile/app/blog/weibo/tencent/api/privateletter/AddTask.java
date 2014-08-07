
package com.limemobile.app.blog.weibo.tencent.api.privateletter;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.privateletter.LetterResult;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.PrivateAPI;

/**
 * 发私信
 * 
 * @param content String 微博内容
 * @param clientip String 用户ip(以分析用户所在地)
 * @param name String 对方用户名（可选）
 * @param fopenid String 对方openid（可选） name和fopenid至少选一个，若同时存在则以name值为主
 * @param picpatch String 文件域表单名。本字段不要放在签名的参数中，不然请求时会出现签名错误，图片大小限制在2M。
 * @param contentflag Integer 私信类型标识，1-普通私信，2-带图私信
 */
public class AddTask extends TAsyncTask<LetterResult> {

    public AddTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public AddTask(ViewGroup container, ILoadListener loadListener, IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 6);
        String content = (String) params[0];
        String clientip = (String) params[1];
        String name = (String) params[2];
        String fopenid = (String) params[3];
        String picpath = (String) params[4];
        String contentflag = Integer.toString((Integer) params[5]);

        PrivateAPI privateAPI = new PrivateAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = privateAPI.add(TencentWeibo.getInstance(), TencentWeibo.JSON, content, clientip,
                    name, fopenid, picpath, contentflag);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new LetterResult();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
