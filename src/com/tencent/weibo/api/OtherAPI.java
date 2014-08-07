
package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

import org.apache.http.message.BasicNameValuePair;

/**
 * 其他相关API
 * 
 * @see <a href=
 *      "http://wiki.open.t.qq.com/index.php/%E5%BE%AE%E5%8D%9A%E7%9B%B8%E5%85%B3"
 *      >腾讯微博开放平台上微博相关的API文档<a>
 */
public class OtherAPI extends BasicAPI {
    private String otherKnowpersonUrl = apiBaseUrl + "/other/kownperson";
    private String otherShortUrl = apiBaseUrl + "/other/shorturl";
    private String otherVideokeyUrl = apiBaseUrl + "/other/videokey";
    private String otherGetEmotionsUrl = apiBaseUrl + "/other/get_emotions";
    private String otherGetOpreaddUrl = apiBaseUrl + "/other/gettopreadd";
    private String otherFollowerTransConvUrl = apiBaseUrl + "/other/follower_trans_conv";
    private String otherQualityTransConvUrl = apiBaseUrl + "/other/quality_trans_conv";
    private String otherVIPTransConvUrl = apiBaseUrl + "/other/vip_trans_conv";
    private String otherConvergeUrl = apiBaseUrl + "/other/url_converge";
    private String otherGetTopicZbrankUrl = apiBaseUrl + "/other/gettopiczbrank";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public OtherAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public OtherAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 我可能认识的人
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param reqnum 请求个数（1-200），默认30
     * @param startindex 起始位置（第一页填0，继续向下翻页：填【reqnum*（page-1）】）
     * @return
     * @throws Exception
     */
    public String kownperson(OAuth oAuth, String format, String reqnum, String startindex)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));

        return requestAPI.getResource(otherKnowpersonUrl, paramsList, oAuth);
    }

    /**
     * 短url变长url
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param url 短url后辍
     * @return
     * @throws Exception
     */
    public String shorturl(OAuth oAuth, String format, String url) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("url", url));

        return requestAPI.getResource(otherShortUrl, paramsList, oAuth);
    }

    /**
     * 获取视频上传的key
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @return
     * @throws Exception
     */
    public String videokey(OAuth oAuth, String format) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));

        return requestAPI.getResource(otherVideokeyUrl, paramsList, oAuth);
    }

    /**
     * 获取表情接口
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param type 支持的表情类型（0-17）
     * @return
     * @throws Exception
     */
    public String get_emotions(OAuth oAuth, String format, String type) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("type", type));

        return requestAPI.getResource(otherGetEmotionsUrl, paramsList, oAuth);
    }

    /**
     * 一键转播热门排行
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param reqnum 请求数（最大50个）
     * @param type 所请求的热门转播排行的类型，其值及其说明如下：
     *            type=5，表示该请求用于查询同一网站内的一键转播热门排行，此时get请求中需要提供待查询网站的sourceid的参数值
     *            ，该值需向微博开放平台申请，联系企业QQ：800013811。
     *            type=6，表示该请求用于查询同一地区内的一键转播热门排行，此时get请求中需要提供country,
     *            province和city的参数值。
     * @param country 国家id
     * @param province 地区id
     * @param city 城市id
     * @return
     * @throws Exception
     */
    public String gettopreadd(OAuth oAuth, String format, String reqnum, String type,
            String country, String province, String city) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("country", country));
        paramsList.add(new BasicNameValuePair("province", province));
        paramsList.add(new BasicNameValuePair("city", city));

        return requestAPI.getResource(otherGetOpreaddUrl, paramsList, oAuth);
    }

    /**
     * 拉取我收听的用户的转播消息接口
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param rootid 转发或者回复的微博根结点ID
     * @param pageflag 翻页标识，0：第一页，1：向下翻页，2：向上翻页
     * @param pagetime 本页起始时间，和pageflag一起使用，精确定位翻页点，若不需要精确定位，只需给出pageTime
     *            第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间
     * @param tweetid 与pageflag和pagetime一起使用，用于翻页
     *            第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     * @param reqnum 用户请求的微博消息数目（最多50个）
     * @return
     * @throws Exception
     */
    public String follower_trans_conv(OAuth oAuth, String format, String rootid, String pageflag,
            String pagetime, String tweetid, String reqnum) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("rootid", rootid));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("tweetid", tweetid));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));

        return requestAPI.getResource(otherFollowerTransConvUrl, paramsList, oAuth);
    }

    /**
     * 拉取精华转播消息接口
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param rootid 转发或者回复的微博的根结点ID
     * @param offset 起始偏移量，分页用
     * @param reqnum 用户请求的微博消息数目（最多50个）
     * @return
     * @throws Exception
     */
    public String quality_trans_conv(OAuth oAuth, String format, String rootid, String offset,
            String reqnum) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("rootid", rootid));
        paramsList.add(new BasicNameValuePair("offset", offset));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));

        return requestAPI.getResource(otherQualityTransConvUrl, paramsList, oAuth);
    }

    /**
     * 拉取vip用户的转播消息接口
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param rootid 转发或者回复的微博根结点ID
     * @param pageflag 翻页标识，0：第一页，1：向下翻页，2：向上翻页
     * @param pagetime 本页起始时间，和pageflag一起使用，精确定位翻页点，若不需要精确定位，只需给出pageTime
     *            第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间
     * @param tweetid 与pageflag和pagetime一起，用于翻页
     *            第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     * @param reqnum 用户请求的微博消息数目（最多50个）
     * @return
     * @throws Exception
     */
    public String vip_trans_conv(OAuth oAuth, String format, String rootid, String pageflag,
            String pagetime, String tweetid, String reqnum) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("rootid", rootid));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("tweetid", tweetid));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));

        return requestAPI.getResource(otherVIPTransConvUrl, paramsList, oAuth);
    }

    /**
     * 短url聚合
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param url 用户提供的需要进行聚合查询的url字符串，既可能是长url也可能是短url
     * @param pageflag 翻页标识，0：第一页，1：向下翻页，2：向上翻页
     * @param pageTime 本页起始时间，和pageflag一起使用，精确定位翻页点，若不需要精确定位，只需给出pageTime
     *            第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间
     * @param tweetid 与pageflag和pagetime一起，用于翻页
     *            第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     * @param type 查询条件，0x01-原创发表，0x02表示转载，0x40点评类型
     * @param words 字数限制，非0-返回微博内容字数大于或等于该值的微博信息，0-无需字数限制
     * @param flag 按位使用，0x01-VIP查询，0x02-非VIP查询
     * @param reqnum 请求的个数(最大30个)
     * @param detaillevel 详细程度(1、2、3)，不同级别返回的微博相关信息包括： 1. tweetinfo = ｛ tweetid
     *            + time ｝ 2. tweetinfo = ｛ tweetid + time + contenttype + type
     *            + name + nick + openid｝ 3. tweetinfo = ｛ tweetid + time +
     *            contenttype + type + name + nick + openid + rootid + parentid
     *            ｝ 如果用户不指定detaillevel的值，则服务器默认返回第1种类型的微博信息
     * @param referer 引用
     * @return
     * @throws Exception
     */
    public String url_converge(OAuth oAuth, String format, String url, String pageflag,
            String pagetime,
            String tweetid, String type, String words, String flag, String reqnum,
            String detaillevel, String referer) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("url", url));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("tweetid", tweetid));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("words", words));
        paramsList.add(new BasicNameValuePair("flag", flag));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("detaillevel", detaillevel));
        paramsList.add(new BasicNameValuePair("referer", referer));

        return requestAPI.getResource(otherConvergeUrl, paramsList, oAuth);
    }

    /**
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param checktype 值为1时表示查询“全时段”排行榜，2表示查询“单周内“排行榜，3表示查询”单月内“排行榜
     * @param topictype 用户通过何种方式指定要查询的话题
     *            0-表示按照ID查询，通过topicid参数指定要查询的话题id；1-表示按照话题标题进行查询
     *            ，topicname参数指定要查询的话题的标题
     *            要使用该接口，需先向微博开放平台提供要拉取的话题id和话题名称，申请对该话题进行配置
     *            ，联系企业QQ：800013811或直接联系BD。
     * @param reqnum 用户请求的微博消息数目（最多100个）
     */
    public String gettopiczbrank(OAuth oAuth, String format, String checktype, String topictype,
            String topicid, String reqnum) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("checktype", checktype));
        paramsList.add(new BasicNameValuePair("topictype", topictype));
        paramsList.add(new BasicNameValuePair("topicid", topicid));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));

        return requestAPI.getResource(otherGetTopicZbrankUrl, paramsList, oAuth);
    }

    @Override
    public void setAPIBaseUrl(String apiBaseUrl) {
        otherKnowpersonUrl = apiBaseUrl + "/other/kownperson";
        otherShortUrl = apiBaseUrl + "/other/shorturl";
        otherVideokeyUrl = apiBaseUrl + "/other/videokey";
        otherGetEmotionsUrl = apiBaseUrl + "/other/get_emotions";
        otherGetOpreaddUrl = apiBaseUrl + "/other/gettopreadd";
        otherFollowerTransConvUrl = apiBaseUrl + "/other/follower_trans_conv";
        otherQualityTransConvUrl = apiBaseUrl + "/other/quality_trans_conv";
        otherVIPTransConvUrl = apiBaseUrl + "/other/vip_trans_conv";
        otherConvergeUrl = apiBaseUrl + "/other/url_converge";
        otherGetTopicZbrankUrl = apiBaseUrl + "/other/gettopiczbrank";
    }
}
