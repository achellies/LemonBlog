
package com.limemobile.app.blog.weibo.tencent.api.search;

import android.view.ViewGroup;

import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.trends.HotBroadcast;
import com.loopj.android.http.JSONObjectProxy;
import com.tencent.weibo.api.SearchAPI;

/**
 * 搜索相关/搜索微博
 * 
 * @param keyword String 搜索关键字
 * @param pagesize Integer 本次请求的记录条数（1-30个）
 * @param page Integer 请求的页码，从0开始 页码
 * @param contenttype Integer 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
 * @param sorttype Integer 排序方式 0-表示按默认方式排序(即时间排序(最新))
 * @param msgtype Integer 消息的类型（按位使用） 0-所有，1-原创发表，2
 *            转载，8-回复(针对一个消息，进行对话)，0x10-空回(点击客人页，进行对话)
 * @param searchtype Integer 搜索类型 <li>0-默认搜索类型（现在为模糊搜索） <li>
 *            1-模糊搜索：时间参数starttime和endtime间隔小于一小时
 *            ，时间参数会调整为starttime前endtime后的整点，即调整间隔为1小时 <li>
 *            8-实时搜索：选择实时搜索，只返回最近几分钟的微博，时间参数需要设置为最近的几分钟范围内才生效，并且不会调整参数间隔
 * @param starttime String 开始时间，用UNIX时间表示（从1970年1月1日0时0分0秒起至现在的总秒数） endtime
 *            结束时间，与starttime一起使用（必须大于starttime）
 * @param endtime String 结束时间，与starttime一起使用（必须大于starttime）
 * @param province Integer 省编码（不填表示忽略地点搜索）
 * @param city Integer 市编码（不填表示按省搜索）
 * @param longitue Integer 经度，（实数）*1000000
 * @param latitude Integer 纬度，（实数）*1000000
 * @param radius Integer 半径（整数，单位米，不大于20000）
 */
public class SearchTask extends TAsyncTask<HotBroadcast> {
    public SearchTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public SearchTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        super(container, loadListener, successListener);
    }

    @Override
    public String callAPI(Object... params) {
        assert (params.length == 14);
        String keyword = (String) params[0];
        String pagesize = Integer.toString((Integer) params[1]);
        String page = Integer.toString((Integer) params[2]);
        String contenttype = Integer.toString((Integer) params[3]);
        String sorttype = Integer.toString((Integer) params[4]);
        String msgtype = Integer.toString((Integer) params[5]);
        String searchtype = Integer.toString((Integer) params[6]);
        String starttime = (String) params[7];
        String endtime = (String) params[8];
        String province = Integer.toString((Integer) params[9]);
        String city = Integer.toString((Integer) params[10]);
        String longitue = Integer.toString((Integer) params[11]);
        String latitude = Integer.toString((Integer) params[12]);
        String radius = Integer.toString((Integer) params[13]);

        SearchAPI searchAPI = new SearchAPI(TencentWeibo.getInstance().getOauthVersion());
        String response = null;
        try {
            response = searchAPI.t(TencentWeibo.getInstance(), TencentWeibo.JSON, keyword, pagesize, page,
                    contenttype, sorttype, msgtype, searchtype,
                    starttime, endtime, province, city, longitue, latitude, radius);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            searchAPI.shutdownConnection();
        }
        return response;
    }

    @Override
    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
        if (dataObject != null) {
            data.data = new HotBroadcast();
            data.data.parse(dataObject);

            return true;
        }
        return false;
    }
}
