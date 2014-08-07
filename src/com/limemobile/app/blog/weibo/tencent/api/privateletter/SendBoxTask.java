
package com.limemobile.app.blog.weibo.tencent.api.privateletter;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.privateletter.SendBox;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.PrivateAPI;

/**
 * 私信相关/发件箱
 * 
 * @param pageflag Integer 分页标识（0：第一页，1：向下翻页，2向上翻页）
 * @param pagetime Integer
 *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
 * @param reqnum Integer 每次请求记录的条数（1-20条）
 * @param lastid Integer
 *            用于翻页，和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
 *            ）
 * @param contenttype Integer 内容过滤。0-所有类型，1-带文本，2-带链接，4-带图片，8-带视频，16-带音频，默认为0
 */
public class SendBoxTask extends TAsyncTask<SendBox> {
    public SendBoxTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public SendBoxTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 5);
        String pageflag = Integer.toString((Integer) params[0]);
        String pagetime = Integer.toString((Integer) params[1]);
        String reqnum = Integer.toString((Integer) params[2]);
        String lastid = Integer.toString((Integer) params[3]);
        String contenttype = Integer.toString((Integer) params[4]);

        PrivateAPI privateAPI = new PrivateAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = privateAPI.send(TencentWeibo.getInstance(), TencentWeibo.JSON, pageflag, pagetime,
                    reqnum, lastid, contenttype);
            ;
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
            data.data = new SendBox();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
