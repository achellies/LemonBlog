
package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

import org.apache.http.message.BasicNameValuePair;

/**
 * 帐户相关API
 * 
 * @see <a href=
 *      "http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3"
 *      >腾讯微博开放平台上帐户相关的API文档<a>
 */

public class UserAPI extends BasicAPI {

    private String userInfoUrl = apiBaseUrl + "/user/info";
    private String userOtherInfoUrl = apiBaseUrl + "/user/other_info";
    private String userInfosUrl = apiBaseUrl + "/user/infos";
    private String userUpdateUrl = apiBaseUrl + "/user/update";
    private String userUpdateHeadUrl = apiBaseUrl + "/user/update_head";
    private String userUpdateEduUrl = apiBaseUrl + "/user/update_edu";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public UserAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public UserAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 获取自己的资料
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E8%8E%B7%E5%8F%96%E8%87%AA%E5%B7%B1%E7%9A%84%E8%AF%A6%E7%BB%86%E8%B5%84%E6%96%99">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String info(OAuth oAuth, String format) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));

        return requestAPI.getResource(userInfoUrl, paramsList,
                oAuth);
    }

    /**
     * 获取其他用户个人资料
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param name 他人的帐户名（可选）
     * @param fopenid 他人的openid（可选） name和fopenid至少选一个，若同时存在则以name值为主
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E8%8E%B7%E5%8F%96%E5%85%B6%E4%BB%96%E4%BA%BA%E8%B5%84%E6%96%99">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String otherInfo(OAuth oAuth, String format, String name, String fopenid)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("name", name));
        paramsList.add(new BasicNameValuePair("fopenid", fopenid));

        return requestAPI.getResource(userOtherInfoUrl,
                paramsList, oAuth);
    }

    /**
     * 获取一批人的简单资料
     * 
     * @param oAuth
     * @param format 返回数据的格式 是（json或xml）
     * @param names 用户ID列表 比如 abc,edf,xxxx
     * @param fopenid 他人的openid（可选） name和fopenid至少选一个，若同时存在则以name值为主
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E8%8E%B7%E5%8F%96%E4%B8%80%E6%89%B9%E4%BA%BA%E7%9A%84%E7%AE%80%E5%8D%95%E8%B5%84%E6%96%99">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String infos(OAuth oAuth, String format, String names, String fopenids
            )
                    throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("names", names));
        paramsList.add(new BasicNameValuePair("fopenids", fopenids));

        return requestAPI.getResource(userInfosUrl, paramsList, oAuth);
    }

    /**
     * 更新用户信息, 本接口用于更新用户基本信息，包括出生日期、地区码及个人介绍等。
     * 
     * @param oAuth 必填项
     * @param format 返回数据的格式 是（json或xml） 必填项
     * @param nick 用户昵称（1-12个中文、字母、数字、下划线或减号） 必填项
     * @param sex 性别（0：未填，1：男，2：女）
     * @param year 出生年（1900-2010）
     * @param month 出生月（1-12）
     * @param day 出生日（1-31）
     * @param countrycode 
     *            国家码（不超过4字节），请参考http://open.t.qq.com//download/addresslist.zip
     * @param provincecode 
     *            地区码（不超过4字节），请参考http://open.t.qq.com//download/addresslist.zip
     * @param citycode 
     *            城市码（不超过4字节），请参考http://open.t.qq.com//download/addresslist.zip
     * @param introduction 个人介绍，不超过140字
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%9B%B4%E6%96%B0%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String update(OAuth oAuth, String format, String nick, String sex, String year,
            String month, String day,
            String countrycode, String provincecode, String citycode, String introduction)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("nick", nick));
        paramsList.add(new BasicNameValuePair("sex", sex));
        paramsList.add(new BasicNameValuePair("year", year));
        paramsList.add(new BasicNameValuePair("month", month));
        paramsList.add(new BasicNameValuePair("day", day));
        paramsList.add(new BasicNameValuePair("countrycode", countrycode));
        paramsList.add(new BasicNameValuePair("provincecode", provincecode));
        paramsList.add(new BasicNameValuePair("citycode", citycode));
        paramsList.add(new BasicNameValuePair("introduction", introduction));

        return requestAPI.postContent(userUpdateUrl, paramsList, oAuth);
    }

    /**
     * 更新用户教育信息, 本接口用于添加、修改或删除用户的教育信息，包括入学年份、学校id等基本信息。
     * 
     * @param oAuth 必填项
     * @param format 返回数据的格式（json或xml） 必填项
     * @param feildid
     *            教育信息记录id（大于0，添加：feildid=1；修改和删除：fieldid通过调用user/info接口获取，删除：
     *            下面四个参数均为空） 必填项
     * @param year 入学年限（1900至今）
     * @param schoolid 学校id，请参考http://open.t.qq.com/download/edu.zip
     * @param departmentid 院系id，请参考http://open.t.qq.com/download/edu.zip
     * @param level 学历，请参考http://open.t.qq.com/download/edu.zip
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%9B%B4%E6%96%B0%E7%94%A8%E6%88%B7%E6%95%99%E8%82%B2%E4%BF%A1%E6%81%AF">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String updateEdu(OAuth oAuth, String format, String feildid, String year,
            String schoolid,
            String departmentid, String level) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("feildid", feildid));
        paramsList.add(new BasicNameValuePair("year", year));
        paramsList.add(new BasicNameValuePair("schoolid", schoolid));
        paramsList.add(new BasicNameValuePair("departmentid", departmentid));
        paramsList.add(new BasicNameValuePair("level", level));

        return requestAPI.postContent(userUpdateEduUrl, paramsList, oAuth);
    }

    /**
     * 本接口用于更新用户的头像信息，从而在微博主站显示不同的头像。
     * 
     * @param oAuth 必填项
     * @param format 返回数据的格式（json或xml） 必填项
     * @param picpath 本地头像图片（最大不超过4M）。文件域表单名，本字段不能放入到签名参数中，不然会出现签名错误
     * @return
     * @throws Exception
     * @see <a
     *      href="http://wiki.open.t.qq.com/index.php/%E5%B8%90%E6%88%B7%E7%9B%B8%E5%85%B3/%E6%9B%B4%E6%96%B0%E7%94%A8%E6%88%B7%E5%A4%B4%E5%83%8F%E4%BF%A1%E6%81%AF">腾讯微博开放平台上关于此条API的文档</a>
     */
    public String updateHead(OAuth oAuth, String format, String picpath) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("pic", picpath));

        QArrayList pic = new QArrayList();
        pic.add(new BasicNameValuePair("pic", picpath));
        return requestAPI.postFile(userUpdateHeadUrl, paramsList, pic, oAuth);
    }

    public void setAPIBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        userInfoUrl = apiBaseUrl + "/user/info";
        userOtherInfoUrl = apiBaseUrl + "/user/other_info";
        userInfosUrl = apiBaseUrl + "/user/infos";
        userUpdateUrl = apiBaseUrl + "/user/update";
        userUpdateHeadUrl = apiBaseUrl + "/user/update_head";
        userUpdateEduUrl = apiBaseUrl + "/user/update_edu";
    }

}
