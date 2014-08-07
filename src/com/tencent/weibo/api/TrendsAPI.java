
package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

import org.apache.http.message.BasicNameValuePair;

/**
 * 热度，趋势API
 * 
 * @see <a href=
 *      "http://wiki.open.t.qq.com/index.php/%E5%BE%AE%E5%8D%9A%E7%9B%B8%E5%85%B3"
 *      >腾讯微博开放平台上微博相关的API文档<a>
 */
public class TrendsAPI extends BasicAPI {
    private String trendsHtUrl = apiBaseUrl + "/trends/ht";
    private String treadsTUrl = apiBaseUrl + "/trends/t";
    private String treadsFamousListUrl = apiBaseUrl + "/trends/famouslist";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public TrendsAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public TrendsAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 话题热榜
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数（最多20）
     * @param pos 请求位置，第一次请求时填0，继续填上次返回的pos
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E7%83%AD%E5%BA%A6%EF%BC%8C%E8%B6%8B%E5%8A%BF/%E8%AF%9D%E9%A2%98%E7%83%AD%E6%A6%9C">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String ht(OAuth oAuth, String format, String reqnum, String pos) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("pos", pos));

        return requestAPI.getResource(trendsHtUrl, paramsList, oAuth);
    }

    /**
     * 转播热榜
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 每次请求记录的条数（1-100条）
     * @param pos 翻页标识，第一次请求时填0，继续填上次返回的pos
     * @param type 微博消息类型 0x1-带文本 0x2-带链接 0x4-带图片 0x8-带视频
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，此时type=3即可，填零表示拉取所有类型
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E7%83%AD%E5%BA%A6%EF%BC%8C%E8%B6%8B%E5%8A%BF/%E8%BD%AC%E6%92%AD%E7%83%AD%E6%A6%9C">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String t(OAuth oAuth, String format, String reqnum, String pos, String type)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("pos", pos));
        paramsList.add(new BasicNameValuePair("type", type));

        return requestAPI.getResource(treadsTUrl, paramsList, oAuth);
    }

    /**
     * 推荐名人列表
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param classid 请求的名人类别id（如：娱乐明星类别id:101）
     * @param subclassid
     *            请求的名人类别的子类别id（如媒体机构类别下子类别广播id：subclass_959，若为空，则默认一个子类别，没有子类别的
     *            ，此处为空）
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E7%83%AD%E5%BA%A6%EF%BC%8C%E8%B6%8B%E5%8A%BF/%E6%8E%A8%E8%8D%90%E5%90%8D%E4%BA%BA%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String famouslist(OAuth oAuth, String format, String classid, String subclassid)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("classid", classid));
        paramsList.add(new BasicNameValuePair("subclassid", subclassid));

        return requestAPI.getResource(treadsFamousListUrl, paramsList, oAuth);
    }

    @Override
    public void setAPIBaseUrl(String apiBaseUrl) {
        trendsHtUrl = apiBaseUrl + "/trends/ht";
        treadsTUrl = apiBaseUrl + "/trends/t";
        treadsFamousListUrl = apiBaseUrl + "/trends/famouslist";
    }
}
