
package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

import org.apache.http.message.BasicNameValuePair;

/**
 * 数据收藏相关API
 * 
 * @see <a href=
 *      "http://wiki.open.t.qq.com/index.php/%E5%BE%AE%E5%8D%9A%E7%9B%B8%E5%85%B3"
 *      >腾讯微博开放平台上微博相关的API文档<a>
 */
public class FavoriteAPI extends BasicAPI {
    private String favAddtUrl = apiBaseUrl + "/fav/addt";
    private String favDeltUrl = apiBaseUrl + "/fav/delt";
    private String favList_tUrl = apiBaseUrl + "/fav/list_t";
    private String favAddhtUrl = apiBaseUrl + "/fav/addht";
    private String favDelhtUrl = apiBaseUrl + "/fav/delht";
    private String favList_htUrl = apiBaseUrl + "/fav/list_ht";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public FavoriteAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public FavoriteAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 收藏一条微博
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param id 需要收藏的微博id
     * @return
     * @throws Exception
     */
    public String addt(OAuth oAuth, String format, String id) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("id", id));

        return requestAPI.postContent(favAddtUrl, paramsList, oAuth);
    }

    /**
     * 取消收藏一条微博
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param id 微博id
     * @return
     * @throws Exception
     */
    public String delt(OAuth oAuth, String format, String id) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("id", id));

        return requestAPI.postContent(favDeltUrl, paramsList, oAuth);
    }

    /**
     * 收藏的微博列表
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime 
     *            翻页用，第一页时：填0；向上翻页：填上一次请求返回的prevtime字段值；向下翻页：填上一次请求返回的nexttime字段值
     * @param reqnum 每次请求记录的条数（1-100条）
     * @param lastid
     *            翻页用，第一页时：填0；继续向上翻页：填上一次请求返回的第一条记录id；继续向下翻页：填上一次请求返回的最后一条记录id
     * @return
     * @throws Exception
     */
    public String list_t(OAuth oAuth, String format, String pageflag, String pagetime,
            String reqnum, String lastid) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));

        return requestAPI.getResource(favList_tUrl, paramsList, oAuth);
    }

    /**
     * 订阅话题
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param id 需要收藏的话题id
     * @return
     * @throws Exception
     */
    public String addht(OAuth oAuth, String format, String id) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("id", id));

        return requestAPI.postContent(favAddhtUrl, paramsList, oAuth);
    }

    /**
     * 取消订阅话题
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param id 话题id
     * @return
     * @throws Exception
     */
    public String delht(OAuth oAuth, String format, String id) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("id", id));

        return requestAPI.postContent(favDelhtUrl, paramsList, oAuth);
    }

    /**
     * 获取已订阅话题列表
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param reqnum 请求数（最多15）
     * @param pageflag 翻页标识（0：首页 1：向下翻页 2：向上翻页）
     * @param pagetime 翻页用，第一页时：填0；向上翻页：填上一次请求返回的第一条记录时间；向下翻页：填上一次请求返回的最后一条记录时间
     * @param lastid
     *            翻页用，第一页时：填0；继续向上翻页：填上一次请求返回的第一条记录id；继续向下翻页：填上一次请求返回的最后一条记录id
     * @return
     * @throws Exception
     */
    public String list_ht(OAuth oAuth, String format, String reqnum, String pageflag,
            String pagetime, String lastid) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        ;

        return requestAPI.getResource(favList_htUrl, paramsList, oAuth);
    }

    @Override
    public void setAPIBaseUrl(String apiBaseUrl) {
        favAddtUrl = apiBaseUrl + "/fav/addt";
        favDeltUrl = apiBaseUrl + "/fav/delt";
        favList_tUrl = apiBaseUrl + "/fav/list_t";
        favAddhtUrl = apiBaseUrl + "/fav/addht";
        favDelhtUrl = apiBaseUrl + "/fav/delht";
        favList_htUrl = apiBaseUrl + "fav/list_ht";
    }
}
