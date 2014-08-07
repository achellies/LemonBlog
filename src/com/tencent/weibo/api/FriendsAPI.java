
package com.tencent.weibo.api;

import org.apache.http.message.BasicNameValuePair;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

/**
 * 关系链相关API
 * 
 * @see <a href=
 *      "http://wiki.open.t.qq.com/index.php/%E5%85%B3%E7%B3%BB%E9%93%BE%E7%9B%B8%E5%85%B3"
 *      >腾讯微博开放平台上关系链相关的API文档<a>
 */

public class FriendsAPI extends BasicAPI {

    // 我的听众列表
    private String friendsFansListUrl = apiBaseUrl + "/friends/fanslist";
    // 我收听的人列表
    private String friendsIdolListUrl = apiBaseUrl + "/friends/idollist";
    // 收听某个用户
    private String friendsAddUrl = apiBaseUrl + "/friends/add";
    // 取消收听某个用户
    private String friendsDelUrl = apiBaseUrl + "/friends/del";
    // 检测是否我的听众或收听的人
    private String friendsCheckUrl = apiBaseUrl + "/friends/check";
    // 其他帐户听众列表
    private String friendsUserFansListUrl = apiBaseUrl + "/friends/user_fanslist";
    // 其他帐户收听的人列表
    private String friendsUserIdolListUrl = apiBaseUrl + "/friends/user_idollist";
    // 其他帐户特别收听的人列表
    private String friendsUserSpecialListUrl = apiBaseUrl + "/friends/user_speciallist";
    // 我的听众列表，简单信息（200个）
    private String friendsFansListSUrl = apiBaseUrl + "/friends/fanslist_s";
    // 我的听众列表，只输出name
    private String friendsFansListNameUrl = apiBaseUrl + "/friends/fanslist_name";
    // 我收听的人列表，只输出name
    private String friendsIdolListNameUrl = apiBaseUrl + "/friends/idollist_name";
    // 黑名单列表
    private String friendsBlackListUrl = apiBaseUrl + "/friends/blacklist";
    // 特别收听列表
    private String friendsSpecialListUrl = apiBaseUrl + "/friends/speciallist";
    // 特别收听某个用户
    private String friendsAddSpecialUrl = apiBaseUrl + "/friends/addspecial";
    // 取消特别收听某个用户
    private String friendsDelSpecialUrl = apiBaseUrl + "/friends/delspecial";
    // 添加某个用户到黑名单
    private String friendsAddBlackListUrl = apiBaseUrl + "/friends/addblacklist";
    // 从黑名单中删除某个用户
    private String friendsDelBlackListUrl = apiBaseUrl + "/friends/delblacklist";
    // 我的收听列表，简单信息（200个）
    private String friendsIdolList_sUrl = apiBaseUrl + "/friends/idollist_s";
    // 互听关系链列表
    private String friendsMutual_listUrl = apiBaseUrl + "/friends/mutual_list";
    // 好友账号及昵称输入提示
    private String friendsMatch_nick_tipsUrl = apiBaseUrl + "/friends/match_nick_tips";
    // 获取最近联系人列表
    private String friendsGet_intimate_friendsUrl = apiBaseUrl + "/friends/get_intimate_friends";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public FriendsAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public FriendsAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 我的听众列表
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-30)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填：【reqnum*（page-1）】）
     * @param mode 获取模式，默认为0 <li>mode=0，旧模式，新粉丝在前，只能拉取1000个 <li>
     *            mode=1，新模式，拉取全量粉丝，老粉丝在前
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%88%91%E7%9A%84%E5%90%AC%E4%BC%97%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String fanslist(OAuth oAuth, String format, String reqnum,
            String startindex, String mode, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("mode", mode));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsFansListUrl,
                paramsList, oAuth);
    }

    /**
     * 我的听众列表，只输出name
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-30)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填：【reqnum*（page-1）】）
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%88%91%E7%9A%84%E5%90%AC%E4%BC%97%E5%88%97%E8%A1%A82">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String fanslist_name(OAuth oAuth, String format, String reqnum,
            String startindex, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsFansListNameUrl,
                paramsList, oAuth);
    }

    /**
     * 我收听的人列表，只输出name
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-200)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填：【reqnum*（page-1）】）
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%88%91%E6%94%B6%E5%90%AC%E7%9A%84%E4%BA%BA%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String idollist_name(OAuth oAuth, String format, String reqnum,
            String startindex, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsIdolListNameUrl,
                paramsList, oAuth);
    }

    /**
     * 我收听的人列表
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-30)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填：【reqnum*（page-1）】）
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%88%91%E6%94%B6%E5%90%AC%E7%9A%84%E4%BA%BA%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String idollist(OAuth oAuth, String format, String reqnum,
            String startindex, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsIdolListUrl,
                paramsList, oAuth);
    }

    /**
     * 特别收听列表
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-30)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填：【reqnum*（page-1）】）
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E7%89%B9%E5%88%AB%E6%94%B6%E5%90%AC%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String speciallist(OAuth oAuth, String format, String reqnum,
            String startindex, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsSpecialListUrl,
                paramsList, oAuth);
    }

    /**
     * 黑名单列表
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-30)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填：【reqnum*（page-1）】）
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E9%BB%91%E5%90%8D%E5%8D%95%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String blacklist(OAuth oAuth, String format, String reqnum,
            String startindex) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));

        return requestAPI.getResource(friendsBlackListUrl,
                paramsList, oAuth);
    }

    /**
     * 收听某个用户<br>
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param name 他人的帐户名列表，用","隔开
     * @param fopenids 你需要读取的用户openid列表，用下划线“_”隔开，例如：
     *            B624064BA065E01CB73F835017FE96FA_B624064BA065E01CB73F835017FE96FB
     *            （可选，最多30个） <br>
     *            name和fopenids至少选一个，若同时存在则以name值为主
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%94%B6%E5%90%AC%E6%9F%90%E4%B8%AA%E7%94%A8%E6%88%B7">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String add(OAuth oAuth, String format, String name, String fopenids) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenids", fopenids));

        return requestAPI.postContent(friendsAddUrl, paramsList,
                oAuth);
    }

    /**
     * 取消收听某个用户
     * 
     * @param oAuth 标准参数
     * @param format 回数据的格式 是（json或xml）
     * @param name 他人的帐户名
     * @param fopenid 他人的openid（可选） <br>
     *            name和fopenid至少选一个，若同时存在则以name值为主
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E5%8F%96%E6%B6%88%E6%94%B6%E5%90%AC%E6%9F%90%E4%B8%AA%E7%94%A8%E6%88%B7">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String del(OAuth oAuth, String format, String name, String fopenid) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));

        return requestAPI.postContent(friendsDelUrl, paramsList,
                oAuth);
    }

    /**
     * 特别收听某个用户
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param name 他人的帐户名列表，用","隔开
     * @param fopenids 你需要读取的用户openid列表，用下划线“_”隔开，例如：
     *            B624064BA065E01CB73F835017FE96FA_B624064BA065E01CB73F835017FE96FB
     *            （可选，最多30个） <br>
     *            name和fopenids至少选一个，若同时存在则以name值为主
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E7%89%B9%E5%88%AB%E6%94%B6%E5%90%AC%E6%9F%90%E4%B8%AA%E7%94%A8%E6%88%B7">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String addspecial(OAuth oAuth, String format, String name, String fopenids)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenids", fopenids));

        return requestAPI.postContent(friendsAddSpecialUrl, paramsList,
                oAuth);
    }

    /**
     * 取消特别收听某个用户
     * 
     * @param oAuth 标准参数
     * @param format 回数据的格式 是（json或xml）
     * @param name 他人的帐户名
     * @param fopenid 他人的openid（可选） <br>
     *            name和fopenid至少选一个，若同时存在则以name值为主
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E5%8F%96%E6%B6%88%E7%89%B9%E5%88%AB%E6%94%B6%E5%90%AC%E6%9F%90%E4%B8%AA%E7%94%A8%E6%88%B7">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String delspecial(OAuth oAuth, String format, String name, String fopenid)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));

        return requestAPI.postContent(friendsDelSpecialUrl, paramsList,
                oAuth);
    }

    /**
     * 添加某个用户到黑名单
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param name 他人的帐户名列表，用","隔开
     * @param fopenid 你需要读取的用户openid列表，用下划线“_”隔开，例如：
     *            B624064BA065E01CB73F835017FE96FA_B624064BA065E01CB73F835017FE96FB
     *            （可选，最多30个） <br>
     *            name和fopenids至少选一个，若同时存在则以name值为主
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%B7%BB%E5%8A%A0%E6%9F%90%E4%B8%AA%E7%94%A8%E6%88%B7%E5%88%B0%E9%BB%91%E5%90%8D%E5%8D%95">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String addblacklist(OAuth oAuth, String format, String name, String fopenid)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));

        return requestAPI.postContent(friendsAddBlackListUrl, paramsList,
                oAuth);
    }

    /**
     * 从黑名单中删除某个用户
     * 
     * @param oAuth 标准参数
     * @param format 回数据的格式 是（json或xml）
     * @param name 他人的帐户名
     * @param fopenid 他人的openid（可选） <br>
     *            name和fopenid至少选一个，若同时存在则以name值为主
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E4%BB%8E%E9%BB%91%E5%90%8D%E5%8D%95%E4%B8%AD%E5%88%A0%E9%99%A4%E6%9F%90%E4%B8%AA%E7%94%A8%E6%88%B7">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String delblacklist(OAuth oAuth, String format, String name, String fopenid)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));

        return requestAPI.postContent(friendsDelBlackListUrl, paramsList,
                oAuth);
    }

    /**
     * 检测是否我的听众或我收听的人
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param names 其他人的帐户名列表，用逗号“,”分隔，如aaa,bbb（最多30个，可选）
     * @param fopenids 其他人的的用户openid列表，用“_”隔开，例如：
     *            B624064BA065E01CB73F835017FE96FA_B624064BA065E01CB73F835017FE96FB
     *            （可选，最多30个） <br>
     *            names和fopenids至少选一个，若同时存在则以names值为主
     * @param flag 0 检测听众，1检测收听的人 2 两种关系都检测
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%A3%80%E6%B5%8B%E6%98%AF%E5%90%A6%E6%88%91%E7%9A%84%E5%90%AC%E4%BC%97%E6%88%96%E6%94%B6%E5%90%AC%E7%9A%84%E4%BA%BA">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String check(OAuth oAuth, String format, String names, String fopenids, String flag)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("names", names));
        paramsList.add(new BasicNameValuePair("fopenids", fopenids));
        paramsList.add(new BasicNameValuePair("flag", flag));

        return requestAPI.getResource(friendsCheckUrl, paramsList,
                oAuth);
    }

    /**
     * 获取其他用户听众列表
     * 
     * @param oAuth 标准参数
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-30)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填【reqnum*（page-1）】）
     * @param name 用户帐户名
     * @param fopenid 他人的openid（可选） <br>
     *            name和fopenid至少选一个，若同时存在则以name值为主
     * @param mode 获取模式，默认为0 <li>mode=0，旧模式，新粉丝在前，只能拉取1000个 <li>
     *            mode=1，新模式，拉取全量粉丝，老粉丝在前
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E5%85%B6%E4%BB%96%E5%B8%90%E6%88%B7%E5%90%AC%E4%BC%97%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String userFanslist(OAuth oAuth, String format, String reqnum,
            String startindex, String name, String fopenid, String mode, String install)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));
        paramsList.add(new BasicNameValuePair("mode", mode));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsUserFansListUrl,
                paramsList, oAuth);
    }

    /**
     * 其他帐户收听的人列表
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-30)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填【reqnum*（page-1）】）
     * @param name 用户帐户名
     * @param fopenid 他人的openid（可选） <br>
     *            name和fopenid至少选一个，若同时存在则以name值为主
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E5%85%B6%E4%BB%96%E5%B8%90%E6%88%B7%E6%94%B6%E5%90%AC%E7%9A%84%E4%BA%BA%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String userIdollist(OAuth oAuth, String format, String reqnum,
            String startindex, String name, String fopenid, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsUserIdolListUrl,
                paramsList, oAuth);
    }

    /**
     * 其他帐户特别收听的人列表
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param reqnum 请求个数(1-30)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填【reqnum*（page-1）】）
     * @param name 用户帐户名（可选）
     * @param fopenid 他人的openid（可选） <br>
     *            name和fopenid至少选一个，若同时存在则以name值为主
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E5%85%B6%E4%BB%96%E5%B8%90%E6%88%B7%E7%89%B9%E5%88%AB%E6%94%B6%E5%90%AC%E7%9A%84%E4%BA%BA%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String userSpeciallist(OAuth oAuth, String format, String reqnum,
            String startindex, String name, String fopenid, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsUserSpecialListUrl,
                paramsList, oAuth);
    }

    /**
     * 我的粉丝列表，简单信息（200个)
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-200)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填：【reqnum*（page-1）】）
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%88%91%E7%9A%84%E5%90%AC%E4%BC%97%E5%88%97%E8%A1%A8%EF%BC%8C%E7%AE%80%E5%8D%95%E4%BF%A1%E6%81%AF%EF%BC%88200%E4%B8%AA%EF%BC%89">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String fanslistS(OAuth oAuth, String format, String reqnum,
            String startindex, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsFansListSUrl,
                paramsList, oAuth);
    }

    /**
     * 我的收听列表，简单信息（200个）
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数(1-200)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填：【reqnum*（page-1）】）
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%88%91%E7%9A%84%E6%94%B6%E5%90%AC%E5%88%97%E8%A1%A8%EF%BC%8C%E7%AE%80%E5%8D%95%E4%BF%A1%E6%81%AF%EF%BC%88200%E4%B8%AA%EF%BC%89">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String idollist_s(OAuth oAuth, String format, String reqnum,
            String startindex, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsIdolList_sUrl,
                paramsList, oAuth);
    }

    /**
     * 互听关系链列表
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param name 用户帐户名
     * @param fopenid 他人的openid（可选） <br>
     *            name和fopenid至少选一个，若同时存在则以name值为主
     * @param reqnum 请求个数(1-200)
     * @param startindex 起始位置（第一页填0，继续向下翻页：填：【reqnum*（page-1）】）
     * @param install 过滤安装应用好友（可选） <br>
     *            0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E4%BA%92%E5%90%AC%E5%85%B3%E7%B3%BB%E9%93%BE%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String mutual_list(OAuth oAuth, String format, String name, String fopenid,
            String startindex,
            String reqnum, String install) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("startindex", startindex));
        paramsList.add(new BasicNameValuePair("install", install));

        return requestAPI.getResource(friendsMutual_listUrl,
                paramsList, oAuth);
    }

    /**
     * 好友账号及昵称输入提示
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param match 匹配字符串（目前匹配范围为我的偶像，即我收听的人）
     *            采用拆分匹配的规则，如match="cw"，则匹配同时包含c和w的字符串，同时支持拼音首字母匹配
     * @param reqnum 请求个数（1-10）
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E5%A5%BD%E5%8F%8B%E8%B4%A6%E5%8F%B7%E5%8F%8A%E6%98%B5%E7%A7%B0%E8%BE%93%E5%85%A5%E6%8F%90%E7%A4%BA">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String match_nick_tips(OAuth oAuth, String format, String match, String reqnum)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("match", match));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));

        return requestAPI.getResource(friendsMatch_nick_tipsUrl,
                paramsList, oAuth);
    }

    /**
     * 获取最近联系人列表
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param reqnum 请求个数（1-20）
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%85%B3%E7%B3%BB%E9%93%BE%E7%9B%B8%E5%85%B3/%E8%8E%B7%E5%8F%96%E6%9C%80%E8%BF%91%E8%81%94%E7%B3%BB%E4%BA%BA%E5%88%97%E8%A1%A8">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String get_intimate_friends(OAuth oAuth, String format, String reqnum) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));

        return requestAPI.getResource(friendsGet_intimate_friendsUrl,
                paramsList, oAuth);
    }

    public void setAPIBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        friendsFansListUrl = apiBaseUrl + "/friends/fanslist";
        friendsIdolListUrl = apiBaseUrl + "/friends/idollist";
        friendsAddUrl = apiBaseUrl + "/friends/add";
        friendsDelUrl = apiBaseUrl + "/friends/del";
        friendsCheckUrl = apiBaseUrl + "/friends/check";
        friendsUserFansListUrl = apiBaseUrl + "/friends/user_fanslist";
        friendsUserIdolListUrl = apiBaseUrl + "/friends/user_idollist";
        friendsUserSpecialListUrl = apiBaseUrl + "/friends/user_speciallist";
        friendsFansListSUrl = apiBaseUrl + "/friends/fanslist_s";
        friendsFansListNameUrl = apiBaseUrl + "/friends/fanslist_name";
        friendsIdolListNameUrl = apiBaseUrl + "/friends/idollist_name";
        friendsBlackListUrl = apiBaseUrl + "/friends/blacklist";
        friendsSpecialListUrl = apiBaseUrl + "/friends/speciallist";
        friendsAddSpecialUrl = apiBaseUrl + "/friends/addspecial";
        friendsDelSpecialUrl = apiBaseUrl + "/friends/delspecial";
        friendsAddBlackListUrl = apiBaseUrl + "/friends/addblacklist";
        friendsDelBlackListUrl = apiBaseUrl + "/friends/delblacklist";
        friendsIdolList_sUrl = apiBaseUrl + "/friends/idollist_s";
        friendsMutual_listUrl = apiBaseUrl + "/friends/mutual_list";
        friendsMatch_nick_tipsUrl = apiBaseUrl + "/friends/match_nick_tips";
        friendsGet_intimate_friendsUrl = apiBaseUrl + "/friends/get_intimate_friends";
    }
}
