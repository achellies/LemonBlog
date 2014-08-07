
package com.tencent.weibo.api;

import org.apache.http.message.BasicNameValuePair;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

/**
 * 搜索相关API
 * 
 * @see <a href=
 *      "http://wiki.open.t.qq.com/index.php/%E6%90%9C%E7%B4%A2%E7%9B%B8%E5%85%B3"
 *      >腾讯微博开放平台上搜索相关的API文档<a>
 */

public class SearchAPI extends BasicAPI {
    // 搜索微博
    private String searchTUrl = apiBaseUrl + "/search/t";
    // 搜索用户
    private String searchUserUrl = apiBaseUrl + "/search/user";
    // 通过标签搜索用户
    private String searchUserByTagUrl = apiBaseUrl + "/search/userbytag";
    // 话题搜索
    private String searchHtUrl = apiBaseUrl + "/search/ht";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public SearchAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public SearchAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 搜索用户
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param keyword 搜索关键字（1-20字节）
     * @param pagesize 本次请求的记录条数（1-15个）
     * @param page 请求的页码，从1开始
     * @return
     * @throws Exception
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%90%9C%E7%B4%A2%E7%9B%B8%E5%85%B3/%E6%90%9C%E7%B4%A2%E7%94%A8%E6%88%B7">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String user(OAuth oAuth, String format, String keyword, String pagesize, String page)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("keyword", keyword));
        paramsList.add(new BasicNameValuePair("pagesize", pagesize));
        paramsList.add(new BasicNameValuePair("page", page));

        return requestAPI.getResource(searchUserUrl, paramsList,
                oAuth);
    }

    /**
     * 搜索微博
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param keyword 搜索关键字（1-128字节）
     * @param pagesize 本次请求的记录条数（1-30个）
     * @param page 请求的页码，从0开始 页码
     * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     * @param sorttype 排序方式 0-表示按默认方式排序(即时间排序(最新))
     * @param msgtype 消息的类型（按位使用） 0-所有，1-原创发表，2
     *            转载，8-回复(针对一个消息，进行对话)，0x10-空回(点击客人页，进行对话)
     * @param searchtype 搜索类型 <li>0-默认搜索类型（现在为模糊搜索） <li>
     *            1-模糊搜索：时间参数starttime和endtime间隔小于一小时
     *            ，时间参数会调整为starttime前endtime后的整点，即调整间隔为1小时 <li>
     *            8-实时搜索：选择实时搜索，只返回最近几分钟的微博，时间参数需要设置为最近的几分钟范围内才生效，并且不会调整参数间隔
     * @param starttime 开始时间，用UNIX时间表示（从1970年1月1日0时0分0秒起至现在的总秒数） endtime
     *            结束时间，与starttime一起使用（必须大于starttime）
     * @param endtime 结束时间，与starttime一起使用（必须大于starttime）
     * @param province 省编码（不填表示忽略地点搜索）
     * @param city 市编码（不填表示按省搜索）
     * @param longitue 经度，（实数）*1000000
     * @param latitude 纬度，（实数）*1000000
     * @param radius 半径（整数，单位米，不大于20000）
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%90%9C%E7%B4%A2%E7%9B%B8%E5%85%B3/%E6%90%9C%E7%B4%A2%E5%BE%AE%E5%8D%9A">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String t(OAuth oAuth, String format, String keyword,
            String pagesize, String page, String contenttype, String sorttype,
            String msgtype, String searchtype, String starttime,
            String endtime, String province, String city, String longitue,
            String latitude, String radius) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("keyword", keyword));
        paramsList.add(new BasicNameValuePair("pagesize", pagesize));
        paramsList.add(new BasicNameValuePair("page", page));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));
        paramsList.add(new BasicNameValuePair("sorttype", sorttype));
        paramsList.add(new BasicNameValuePair("msgtype", msgtype));
        paramsList.add(new BasicNameValuePair("searchtype", searchtype));
        paramsList.add(new BasicNameValuePair("starttime", starttime));
        paramsList.add(new BasicNameValuePair("endtime", endtime));
        paramsList.add(new BasicNameValuePair("province", province));
        paramsList.add(new BasicNameValuePair("city", city));
        paramsList.add(new BasicNameValuePair("longitue", longitue));
        paramsList.add(new BasicNameValuePair("latitude", latitude));
        paramsList.add(new BasicNameValuePair("radius", radius));

        return requestAPI.getResource(searchTUrl, paramsList,
                oAuth);
    }

    /**
     * 通过标签搜索用户
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param keyword 搜索关键字（1-64字节）
     * @param pagesize 每页大小（1-15个）
     * @param page 页码
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%90%9C%E7%B4%A2%E7%9B%B8%E5%85%B3/%E9%80%9A%E8%BF%87%E6%A0%87%E7%AD%BE%E6%90%9C%E7%B4%A2%E7%94%A8%E6%88%B7">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String userbytag(OAuth oAuth, String format, String keyword,
            String pagesize, String page) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("keyword", keyword));
        paramsList.add(new BasicNameValuePair("pagesize", pagesize));
        paramsList.add(new BasicNameValuePair("page", page));

        return requestAPI.getResource(searchUserByTagUrl, paramsList,
                oAuth);
    }

    /**
     * 话题搜索
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param keyword 搜索关键字（1-128字节）
     * @param pagesize 本次请求的记录条数（1-20个）
     * @param page 请求的页码，从1开始
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%90%9C%E7%B4%A2%E7%9B%B8%E5%85%B3/%E8%AF%9D%E9%A2%98%E6%90%9C%E7%B4%A2">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String ht(OAuth oAuth, String format, String keyword,
            String pagesize, String page) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("keyword", keyword));
        paramsList.add(new BasicNameValuePair("pagesize", pagesize));
        paramsList.add(new BasicNameValuePair("page", page));

        return requestAPI.getResource(searchHtUrl, paramsList,
                oAuth);
    }

    public void setAPIBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        searchTUrl = apiBaseUrl + "/search/t";
        searchUserUrl = apiBaseUrl + "/search/user";
        searchUserByTagUrl = apiBaseUrl + "/search/userbytag";
        searchHtUrl = apiBaseUrl + "/search/ht";
    }
}
