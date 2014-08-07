
package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

import org.apache.http.message.BasicNameValuePair;

/**
 * 话题相关API
 * 
 * @see <a href=
 *      "http://wiki.open.t.qq.com/index.php/%E5%BE%AE%E5%8D%9A%E7%9B%B8%E5%85%B3"
 *      >腾讯微博开放平台上微博相关的API文档<a>
 */
public class HuaTiAPI extends BasicAPI {
    private String htInfoUrl = apiBaseUrl + "/ht/ids";
    private String htIdsUrl = apiBaseUrl + "/api/ht/ids";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public HuaTiAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public HuaTiAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 根据话题id获取话题相关信息
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param ids 话题id列表，用“,”分隔，如12345,12345（最多30个）
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E8%AF%9D%E9%A2%98%E7%9B%B8%E5%85%B3/%E6%A0%B9%E6%8D%AE%E8%AF%9D%E9%A2%98id%E8%8E%B7%E5%8F%96%E8%AF%9D%E9%A2%98%E7%9B%B8%E5%85%B3%E4%BF%A1%E6%81%AF">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String info(OAuth oAuth, String format, String ids) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("ids", ids));

        return requestAPI.getResource(htInfoUrl, paramsList, oAuth);
    }

    /**
     * 根据话题名称查询话题id
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param httexts 话题名字列表，用“,”分隔，如abc,efg（最多30个）
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E8%AF%9D%E9%A2%98%E7%9B%B8%E5%85%B3/%E6%A0%B9%E6%8D%AE%E8%AF%9D%E9%A2%98%E5%90%8D%E7%A7%B0%E6%9F%A5%E8%AF%A2%E8%AF%9D%E9%A2%98id">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String ids(OAuth oAuth, String format, String httexts) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("httexts", httexts));

        return requestAPI.getResource(htIdsUrl, paramsList, oAuth);
    }

    @Override
    public void setAPIBaseUrl(String apiBaseUrl) {
        htInfoUrl = apiBaseUrl + "/ht/ids";
        htIdsUrl = apiBaseUrl + "/api/ht/ids";
    }

}
