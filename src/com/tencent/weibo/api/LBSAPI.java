
package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;

import org.apache.http.message.BasicNameValuePair;

/**
 * LBS相关相关API
 * 
 * @see <a href=
 *      "http://wiki.open.t.qq.com/index.php/%E5%BE%AE%E5%8D%9A%E7%9B%B8%E5%85%B3"
 *      >腾讯微博开放平台上微博相关的API文档<a>
 */
public class LBSAPI extends BasicAPI {
    private String lbsUpdate_posUrl = apiBaseUrl + "/lbs/update_pos";
    private String lsbDel_posUrl = apiBaseUrl + "/lbs/del_pos";
    private String lbsGet_poiUrl = apiBaseUrl + "/lbs/get_poi";
    private String lbsGet_around_newUrl = apiBaseUrl + "/lbs/get_around_new";
    private String lbsGet_around_peopleUrl = apiBaseUrl + "/lbs/get_around_people";
    private String lbsRgeocUrl = "http://ugc.map.soso.com/rgeoc/";
    private String lbsGeocUrl = "http://ugc.map.soso.com/geoc/";

    /**
     * 使用完毕后，请调用 shutdownConnection() 关闭自动生成的连接管理器
     * 
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     */
    public LBSAPI(String OAuthVersion) {
        super(OAuthVersion);
    }

    /**
     * @param OAuthVersion 根据OAuthVersion，配置通用请求参数
     * @param qHttpClient 使用已有的连接管理器
     */
    public LBSAPI(String OAuthVersion, QHttpClient qHttpClient) {
        super(OAuthVersion, qHttpClient);
    }

    /**
     * 更新地理位置
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param longitude 经度，例如：22.541321
     * @param latitude 纬度，例如：13.935558
     * @return
     * @throws Exception
     */
    public String update_pos(OAuth oAuth, String format, String longitude, String latitude)
            throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("longitude", longitude));
        paramsList.add(new BasicNameValuePair("latitude", latitude));

        return requestAPI.postContent(lbsUpdate_posUrl, paramsList, oAuth);
    }

    /**
     * 删除最后更新位置
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @return
     * @throws Exception
     */
    public String del_pos(OAuth oAuth, String format) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));

        return requestAPI.postContent(lsbDel_posUrl, paramsList, oAuth);
    }

    /**
     * 获取POI(Point of Interest)
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param longitude 经度，例如：22.541321
     * @param latitude 纬度，例如：13.935558
     * @param reqnum 每次请求记录的条数（1-25条）
     * @param radius POI的半径（单位为米），取值范围为100-1000，为达到比较好的搜索结果，建议设置为200
     * @param position 上次查询返回的位置，用于翻页（第一次请求时填0）
     * @return
     * @throws Exception
     */
    public String get_poi(OAuth oAuth, String format, String longitude, String latitude,
            String reqnum, String radius, String position) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("longitude", longitude));
        paramsList.add(new BasicNameValuePair("latitude", latitude));
        paramsList.add(new BasicNameValuePair("reqnum", reqnum));
        paramsList.add(new BasicNameValuePair("radius", radius));
        paramsList.add(new BasicNameValuePair("position", position));

        return requestAPI.postContent(lbsGet_poiUrl, paramsList, oAuth);
    }

    /**
     * 获取身边最新的微博
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param longitude 经度，例如：22.541321
     * @param latitude 纬度，例如：13.935558
     * @param pageinfo 翻页参数，由上次请求返回（第一次请求时请填空）
     * @param pagesize 请求的每页个数（1-50），建议25
     * @return
     * @throws Exception
     */
    public String get_around_new(OAuth oAuth, String format, String longitude, String latitude,
            String pageinfo, String pagesize) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("longitude", longitude));
        paramsList.add(new BasicNameValuePair("latitude", latitude));
        paramsList.add(new BasicNameValuePair("pageinfo", pageinfo));
        paramsList.add(new BasicNameValuePair("position", pagesize));

        return requestAPI.postContent(lbsGet_around_newUrl, paramsList, oAuth);
    }

    /**
     * 获取身边的人
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param longitude 经度，例如：22.541321
     * @param latitude 纬度，例如：13.935558
     * @param pageinfo 翻页参数，由上次请求返回（第一次请求时请填空）
     * @param pagesize 请求的每页个数（1-25个）
     * @param gender 性别，0-全部，1-男，2-女
     * @return
     * @throws Exception
     */
    public String get_around_people(OAuth oAuth, String format, String longitude, String latitude,
            String pageinfo, String pagesize, String gender) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("longitude", longitude));
        paramsList.add(new BasicNameValuePair("latitude", latitude));
        paramsList.add(new BasicNameValuePair("pageinfo", pageinfo));
        paramsList.add(new BasicNameValuePair("pagesize", pagesize));
        paramsList.add(new BasicNameValuePair("gender", gender));

        return requestAPI.postContent(lbsGet_around_peopleUrl, paramsList, oAuth);
    }

    /**
     * 通过经纬度获取地理位置
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param lnglat 经纬度值，采用经度在前，纬度在后，经纬度值之间用“,”隔开的方式
     *            如：113.94830703735352,22.54046422124227
     * @param reqsrc 请求来源，请填写"wb”
     * @return
     * @throws Exception
     */
    public String rgeoc(OAuth oAuth, String format, String lnglat, String reqsrc) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("lnglat", lnglat));
        paramsList.add(new BasicNameValuePair("reqsrc", reqsrc));

        return requestAPI.getResource(lbsRgeocUrl, paramsList, oAuth);
    }

    /**
     * 通过地理位置获取经纬度
     * 
     * @param oAuth
     * @param format 返回数据的格式（json或xml）
     * @param addr 地理位置
     * @param reqsrc 请求来源，请填写"wb”
     * @return
     * @throws Exception
     */
    public String geoc(OAuth oAuth, String format, String addr, String reqsrc) throws Exception {
        QArrayList paramsList = new QArrayList();
        paramsList.add(new BasicNameValuePair("format", format));
        paramsList.add(new BasicNameValuePair("addr", addr));
        paramsList.add(new BasicNameValuePair("reqsrc", reqsrc));

        return requestAPI.getResource(lbsGeocUrl, paramsList, oAuth);
    }

    @Override
    public void setAPIBaseUrl(String apiBaseUrl) {
        lbsUpdate_posUrl = apiBaseUrl + "/lbs/update_pos";
        lsbDel_posUrl = apiBaseUrl + "/lbs/del_pos";
        lbsGet_poiUrl = apiBaseUrl + "/lbs/get_poi";
        lbsGet_around_newUrl = apiBaseUrl + "/lbs/get_around_new";
        lbsGet_around_peopleUrl = apiBaseUrl + "/lbs/get_around_people";
    }
}
