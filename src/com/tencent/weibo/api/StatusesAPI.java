
package com.tencent.weibo.api;

import org.apache.http.message.BasicNameValuePair;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

/**
 * 时间线相关API
 * 
 * @see <a
 *      href="http://wiki.open.t.qq.com/index.php/%E6%97%B6%E9%97%B4%E7%BA%BF"
 *      >腾讯微博开放平台上时间线相关的API文档<a>
 */

public class StatusesAPI extends BasicAPI {

    private String statusesHomeTimelineUrl = apiBaseUrl + "/statuses/home_timeline";
    private String statusesUSERTimelineUrl = apiBaseUrl + "/statuses/user_timeline";
    private String statusesMentionsTimelineUrl = apiBaseUrl + "/statuses/mentions_timeline";
    private String statusesBroadcastTimelineUrl = apiBaseUrl + "/statuses/broadcast_timeline";
    private String statusesUserTimelineIdsUrl = apiBaseUrl + "/statuses/user_timeline_ids";
    private String statusesUsersTimelineUrl = apiBaseUrl + "/statuses/users_timeline";
    private String statusesPublicTimelineUrl = apiBaseUrl + "/statuses/public_timeline";
    private String statusesHTTimelineUrl = apiBaseUrl + "/statuses/ht_timeline_ext";
    private String statusesSpecialTimelineUrl = apiBaseUrl + "/statuses/special_timeline";
    private String statusesAreaTimelineUrl = apiBaseUrl + "/statuses/area_timeline";
    private String statusesHomeTimelineVIPUrl = apiBaseUrl + "/status/home_timeline_vip";
    private String statusesSubResListUrl = apiBaseUrl + "/statuses/sub_re_list";
    private String statusesUsersTimelineIdsUrl = apiBaseUrl + "/statuses/users_timeline_ids";
    private String statusesMetionsTimelineIdsUrl = apiBaseUrl + "/statuses/mentions_timeline_ids";
    private String statusesBroadcastTimelineIdsUrl = apiBaseUrl
            + "/statuses/broadcast_timeline_ids";
    private String statusesHomeTimelineIdsUrl = apiBaseUrl + "/statuses/home_timeline_ids";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public StatusesAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public StatusesAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 主页时间线
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-70条）
     * @param type 拉取类型 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评 <br>
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，此时type=3即可，填零表示拉取所有类型
     * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%97%B6%E9%97%B4%E7%BA%BF/%E4%B8%BB%E9%A1%B5%E6%97%B6%E9%97%B4%E7%BA%BF">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String homeTimeline(OAuth oAuth, String format, String pageflag,
            String pagetime, String reqnum, String type, String contenttype) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(statusesHomeTimelineUrl,
                paramsList, oAuth);
    }

    /**
     * 广播大厅时间线
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param pos 记录的起始位置（第一次请求时填0，继续请求时填上次请求返回的pos）
     * @param reqnum 每次请求记录的条数（1-100条）
     * @return
     * @throws Exception
     */
    public String publicTimeline(OAuth oAuth, String format, String pos, String reqnum)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pos", pos));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));

        return requestAPI.getResource(statusesPublicTimelineUrl,
                paramsList, oAuth);
    }

    /**
     * 其他用户发表时间线
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-70条）
     * @param lastid 
     *            用于翻页，和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     *            ）
     * @param name 你需要读取的用户的用户名
     * @param fopenid 你需要读取的用户的openid（可选） <br>
     *            name和fopenid至少选一个，若同时存在则以name值为主
     * @param type 拉取类型 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评 <br>
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，此时type=3即可，填零表示拉取所有类型
     * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%97%B6%E9%97%B4%E7%BA%BF/%E5%85%B6%E4%BB%96%E7%94%A8%E6%88%B7%E5%8F%91%E8%A1%A8%E6%97%B6%E9%97%B4%E7%BA%BF">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String userTimeline(OAuth oAuth, String format, String pageflag,
            String pagetime, String reqnum, String lastid, String name, String fopenid,
            String type, String contenttype) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(statusesUSERTimelineUrl,
                paramsList, oAuth);
    }

    /**
     * 用户提及时间线
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-100条）
     * @param lastid 
     *            和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     *            ）
     * @param type 拉取类型 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评 <br>
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，此时type=3即可，填零表示拉取所有类型
     * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%97%B6%E9%97%B4%E7%BA%BF/%E7%94%A8%E6%88%B7%E6%8F%90%E5%8F%8A%E6%97%B6%E9%97%B4%E7%BA%BF">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String mentionsTimeline(OAuth oAuth, String format,
            String pageflag, String pagetime, String reqnum, String lastid,
            String type, String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesMentionsTimelineUrl,
                paramsList, oAuth);
    }

    /**
     * 话题时间线(新)
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param reqnum 请求数量（1-100）
     * @param tweetid 微博帖子ID（详细用法见pageflag）
     * @param time 微博帖子生成时间（详细用法见pageflag）
     * @param pageflag 翻页标识(第一次务必填零)
     *            pageflag=1表示向下翻页：tweetid和time是上一页的最后一个帖子ID和时间；
     *            pageflag=2表示向上翻页：tweetid和time是下一页的第一个帖子ID和时间；
     * @param flag 是否拉取认证用户，用作筛选。0-拉取所有用户，0x01-拉取认证用户
     * @param httext 话题内容，长度有限制。
     * @param htid 话题ID（可以通过ht/ids获取指定话题的ID）
     *            htid和httext这两个参数不能同时填写，如果都填写只拉取htid指定的微博
     *            ，如果要拉取指定话题的微博，请务必让htid为0
     * @param type 1-原创发表，2-转载
     * @param contenttype 正文类型（按位使用）。1-带文本(这一位一定有)，2-带链接，4-带图片，8-带视频，0x10-带音频
     *            建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80
     * @return
     * @throws Exception
     */
    public String htTimeline(OAuth oAuth, String format,
            String reqnum, String tweetid, String time, String pageflag,
            String flag, String httext, String htid, String type, String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("tweetid", tweetid));
        paramsList.add(new BasicNameValuePair("time", time));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("flag", flag));
        paramsList.add(new BasicNameValuePair("httext", httext));
        paramsList.add(new BasicNameValuePair("htid", htid));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesHTTimelineUrl,
                paramsList, oAuth);
    }

    /**
     * 我发表时间线
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml） 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-200条）
     * @param lastid 
     *            和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     *            ）
     * @param type 拉取类型, 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评
     *            如需拉取多个类型请|上(0x1|0x2) 得到3，type=3即可,填零表示拉取所有类型
     * @param contenttype 内容过滤 填零表示所有类型 1-带文本 2-带链接 4图片 8-带视频 0x10-带音频
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%97%B6%E9%97%B4%E7%BA%BF/%E6%88%91%E5%8F%91%E8%A1%A8%E6%97%B6%E9%97%B4%E7%BA%BF">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String broadcastTimeline(OAuth oAuth, String format,
            String pageflag, String pagetime, String reqnum, String lastid,
            String type, String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesBroadcastTimelineUrl,
                paramsList, oAuth);
    }

    /**
     * 特别收听的人发表时间线
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-70条）
     * @param lastid 
     *            用于翻页，和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     *            ）
     * @param type 拉取类型 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型
     * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     *            建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80
     * @return
     * @throws Exception
     */
    public String specialTimeline(OAuth oAuth, String format,
            String pageflag, String pagetime, String reqnum, String lastid,
            String type, String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesSpecialTimelineUrl,
                paramsList, oAuth);
    }

    /**
     * 地区发表时间线
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param pos 记录的起始位置（第一次请求时填0，继续请求时填上次请求返回的pos）
     * @param reqnum 每次请求记录的条数（1-100条）
     * @param country 国家码
     * @param province 省份码
     * @param city 城市码
     * @return
     * @throws Exception
     */
    public String areaTimeline(OAuth oAuth, String format,
            String pos, String reqnum, String country, String province,
            String city)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pos", pos));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("country", country));
        paramsList.add(new BasicNameValuePair("province", province));
        paramsList.add(new BasicNameValuePair("city", city));

        return requestAPI.getResource(
                statusesAreaTimelineUrl,
                paramsList, oAuth);
    }

    /**
     * 主页时间线索引
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-300条）
     * @param type 拉取类型（需填写十进制数字） 0x1 原创发表 0x2 转载
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型
     * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     *            建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80
     * @return
     * @throws Exception
     */
    public String homeTimelineIds(OAuth oAuth, String format,
            String pageflag, String pagetime, String reqnum, String type, String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesHomeTimelineIdsUrl,
                paramsList, oAuth);
    }

    /**
     * 我发表时间线索引
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-300条）
     * @param lastid 
     *            和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     *            ）
     * @param type 拉取类型 0x1 原创发表 0x2 转载
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型
     * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     *            建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80
     * @return
     * @throws Exception
     */
    public String broadcastTimelineIds(OAuth oAuth, String format,
            String pageflag, String pagetime, String reqnum, String lastid, String type,
            String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesBroadcastTimelineIdsUrl,
                paramsList, oAuth);
    }

    /**
     * 用户提及时间线索引
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-300条）
     * @param lastid 
     *            和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     *            ）
     * @param type 拉取类型 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型
     * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     *            建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80
     * @return
     * @throws Exception
     */
    public String mentionsTimelineIds(OAuth oAuth, String format,
            String pageflag, String pagetime, String reqnum, String lastid, String type,
            String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesMetionsTimelineIdsUrl,
                paramsList, oAuth);
    }

    /**
     * 其他用户发表时间线索引
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-300条）
     * @param lastid 
     *            和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     *            ）
     * @param name 你需要读取的用户的用户名（可选）
     * @param fopenid 你需要读取的用户的openid（可选） name和fopenid至少选一个，若同时存在则以name值为主
     * @param type 拉取类型, 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评 拉取类型,
     *            0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评
     *            如需拉取多个类型请|上(0x1|0x2) 得到3，type=3即可,填零表示拉取所有类型
     * @param contenttype 内容过滤 填零表示所有类型 1-带文本 2-带链接 4图片 8-带视频 0x10-带音频
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%97%B6%E9%97%B4%E7%BA%BF/%E5%85%B6%E4%BB%96%E7%94%A8%E6%88%B7%E5%8F%91%E8%A1%A8%E6%97%B6%E9%97%B4%E7%BA%BF%E7%B4%A2%E5%BC%95">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String userTimelineIds(OAuth oAuth, String format,
            String pageflag, String pagetime, String reqnum, String lastid,
            String name, String fopenid, String type, String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesUserTimelineIdsUrl,
                paramsList, oAuth);
    }

    /**
     * 多用户发表时间线索引
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-300条）
     * @param lastid 
     *            用于翻页，和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     *            ）
     * @param names 你需要读取用户列表用“,”隔开，例如：abc,bcde,effg（可选，最多30个）
     * @param fopenids 你需要读取的用户openid列表，用“_”隔开，例如：
     *            B624064BA065E01CB73F835017FE96FA_B624064BA065E01CB73F835017FE96FB
     *            （可选，最多30个） names和fopenids至少选一个，若同时存在则以names值为主
     * @param type 拉取类型 0x1 原创发表 0x2 转载
     *            如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型
     * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
     *            建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80
     * @return
     * @throws Exception
     */
    public String usersTimelineIds(OAuth oAuth, String format,
            String pageflag, String pagetime, String reqnum, String lastid,
            String names, String fopenids, String type, String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        paramsList.add(new BasicNameValuePair("names", names));
        paramsList.add(new BasicNameValuePair("fopenids", fopenids));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesUsersTimelineIdsUrl,
                paramsList, oAuth);
    }

    /**
     * 多用户发表时间线
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-100条）
     * @param lastid 第一页 时填0,继续向下翻页，填上一次请求返回的最后一条记录ID，翻页用
     * @param names 你需要读取用户列表用“,”隔开，例如：abc,bcde,effg（可选，最多30个）
     * @param fopenids 你需要读取的用户openid列表，用下划线“_”隔开，例如：
     *            B624064BA065E01CB73F835017FE96FA_B624064BA065E01CB73F835017FE96FB
     *            （可选，最多30个） <br>
     *            names和fopenids至少选一个，若同时存在则以names值为主
     * @param type 拉取类型, 0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评
     * @param contenttype 内容过滤 填零表示所有类型 1-带文本 2-带链接 4图片 8-带视频 0x10-带音频
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E6%97%B6%E9%97%B4%E7%BA%BF/%E5%A4%9A%E7%94%A8%E6%88%B7%E5%8F%91%E8%A1%A8%E6%97%B6%E9%97%B4%E7%BA%BF">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String usersTimeline(OAuth oAuth, String format, String pageflag,
            String pagetime, String reqnum, String lastid, String names,
            String fopenids, String type, String contenttype)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));
        paramsList.add(new BasicNameValuePair("names", names));
        paramsList.add(new BasicNameValuePair("fopenids", fopenids));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("contenttype", contenttype));

        return requestAPI.getResource(
                statusesUsersTimelineUrl,
                paramsList, oAuth);
    }

    /**
     * 拉取vip用户发表微博消息接口
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param reqnum 每次请求记录的条数（1-70条）
     * @param lastid 
     *            用于翻页，和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     *            ） 如果用户不指定该参数，默认为0
     * @return
     * @throws Exception
     */
    public String homeTimelineVIP(OAuth oAuth, String format, String pageflag,
            String pagetime, String reqnum, String lastid) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("lastid", lastid));

        return requestAPI.getResource(statusesHomeTimelineVIPUrl,
                paramsList, oAuth);
    }

    /**
     * 获取二传手列表
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param reqnum 每次请求记录的条数（1-50条）
     * @param rootid 二传手转发或者回复查询节点的父亲结点id
     * @param type 1-转发，2-点评
     * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页）
     * @param pagetime
     *            本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
     * @param lastid 
     *            和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id
     * @return
     * @throws Exception
     */
    public String sub_re_list(OAuth oAuth, String format, String rootid,
            String reqnum, String type, String pageflag, String pagetime, String lastid)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("rootid", rootid));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("type", type));
        paramsList.add(new BasicNameValuePair("pageflag", pageflag));
        paramsList.add(new BasicNameValuePair("pagetime", pagetime));
        paramsList.add(new BasicNameValuePair("lastid", lastid));

        return requestAPI.getResource(statusesSubResListUrl,
                paramsList, oAuth);
    }

    public void setAPIBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        statusesHomeTimelineUrl = apiBaseUrl + "/statuses/home_timeline";
        statusesUSERTimelineUrl = apiBaseUrl + "/statuses/user_timeline";
        statusesMentionsTimelineUrl = apiBaseUrl + "/statuses/mentions_timeline";
        statusesBroadcastTimelineUrl = apiBaseUrl + "/statuses/broadcast_timeline";
        statusesUserTimelineIdsUrl = apiBaseUrl + "/statuses/user_timeline_ids";
        statusesUsersTimelineUrl = apiBaseUrl + "/statuses/users_timeline";
        statusesPublicTimelineUrl = apiBaseUrl + "/statuses/public_timeline";
        statusesHTTimelineUrl = apiBaseUrl + "/statuses/ht_timeline_ext";
        statusesSpecialTimelineUrl = apiBaseUrl + "/statuses/special_timeline";
        statusesAreaTimelineUrl = apiBaseUrl + "/statuses/area_timeline";
        statusesHomeTimelineVIPUrl = apiBaseUrl + "/status/home_timeline_vip";
        statusesSubResListUrl = apiBaseUrl + "/statuses/sub_re_list";
        statusesUsersTimelineIdsUrl = apiBaseUrl + "/statuses/users_timeline_ids";
        statusesMetionsTimelineIdsUrl = apiBaseUrl + "/statuses/mentions_timeline_ids";
        statusesBroadcastTimelineIdsUrl = apiBaseUrl + "/statuses/broadcast_timeline_ids";
        statusesHomeTimelineIdsUrl = apiBaseUrl + "/statuses/home_timeline_ids";
    }
}
