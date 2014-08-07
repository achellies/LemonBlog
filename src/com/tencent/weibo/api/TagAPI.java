
package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

import org.apache.http.message.BasicNameValuePair;

/**
 * 标签相关相关API
 * 
 * @see <a href=
 *      "http://wiki.open.t.qq.com/index.php/%E5%BE%AE%E5%8D%9A%E7%9B%B8%E5%85%B3"
 *      >腾讯微博开放平台上微博相关的API文档<a>
 */
public class TagAPI extends BasicAPI {
    private String tagAddUrl = apiBaseUrl + "/tag/add";
    private String tagDelUrl = apiBaseUrl + "/tag/del";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public TagAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public TagAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 添加标签
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param tag 标签内容
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%A0%87%E7%AD%BE%E7%9B%B8%E5%85%B3/%E6%B7%BB%E5%8A%A0%E6%A0%87%E7%AD%BE">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String add(OAuth oAuth, String format, String tag) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("tag", tag));

        return requestAPI.postContent(tagAddUrl, paramsList, oAuth);
    }

    /**
     * 删除标签
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param tagid 标签id
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%A0%87%E7%AD%BE%E7%9B%B8%E5%85%B3/%E5%88%A0%E9%99%A4%E6%A0%87%E7%AD%BE">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String del(OAuth oAuth, String format, String tagid) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("tagid", tagid));

        return requestAPI.postContent(tagDelUrl, paramsList, oAuth);
    }

    @Override
    public void setAPIBaseUrl(String apiBaseUrl) {
        tagAddUrl = apiBaseUrl + "/tag/add";
        tagDelUrl = apiBaseUrl + "/tag/del";
    }

}
