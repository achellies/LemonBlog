
package com.limemobile.app.blog.weibo.tencent.entity.common;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.common.Music;
import com.limemobile.app.blog.weibo.tencent.entity.common.Video;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

// 微博信息
public class Tweet extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 4693572381710824029L;
    public String text; // 微博内容
    public String origtext; // 原始内容
    public long count; // 微博被转次数
    public long mcount; // 点评次数
    public String from; // 来源
    public String fromurl; // 来源url
    public String id; // 微博唯一id
    public ArrayList<String> image; // 图片url列表
    public Video video; // 视频信息
    public Music music; // 音频信息
    public String name; // 发表人帐户名
    public String openid; // 用户唯一id，与name相对应
    public String nick; // 发表人昵称
    public int self; // 是否自已发的的微博，0-不是，1-是
    public String timestamp; // 发表时间
    public int type; // 微博类型，1-原创发表，2-转载，3-私信，4-回复，5-空回，6-提及，7-评论
    public String head; // 发表者头像url
    public String location; // 发表者所在地
    public long latitude; // 纬度
    public long longitude; // 经度
    public int country_code; // 国家码（与地区发表时间线一样）
    public int province_code; // 省份码（与地区发表时间线一样）
    public int city_code; // 城市码（与地区发表时间线一样）
    public int isvip; // 是否微博认证用户，0-不是，1-是
    public String geo; // 发表者地理信息
    public int status; // 微博状态，0-正常，1-系统删除，2-审核中，3-用户删除，4-根删除（根节点被系统审核删除）
    public String emotionurl; // 心情图片url
    public long emotiontype; // 心情类型
    public Tweet source; // 当type=2时，source即为源tweet

    @Override
    public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
            throws IllegalArgumentException, IllegalAccessException {
        if (typeString.equals(Constant.CLASS_TYPE_ARRAY_LIST)) {
            JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
            if (jsonArray != null) {
                ArrayList<String> array = new ArrayList<String>();
                for (int index = 0; index < jsonArray.length(); ++index) {
                    try {
                        String image = jsonArray.getString(index);
                        if (image != null)
                            array.add(image);
                    } catch (JSONException e) {
                    }
                }
                field.set(this, array);
            }
        } else if (typeString
                .equals("class com.limemobile.app.blog.weibo.tencent.entity.common.Music")) {
            try {
                JSONObjectProxy musicObject = data.getJSONObject(fieldName);
                if (musicObject != null) {
                    Music music = new Music();
                    music.parse(musicObject);
                    field.set(this, music);
                }
            } catch (JSONException e) {
            }
        } else if (typeString
                .equals("class com.limemobile.app.blog.weibo.tencent.entity.common.Video")) {
            try {
                JSONObjectProxy videoObject = data.getJSONObject(fieldName);
                if (videoObject != null) {
                    Video video = new Video();
                    video.parse(videoObject);
                    field.set(this, video);
                }
            } catch (JSONException e) {
            }
        } else if (typeString
                .equals("class com.limemobile.app.blog.weibo.tencent.entity.common.Tweet")) {
            try {
                JSONObjectProxy tweetObject = data.getJSONObject(fieldName);
                if (tweetObject != null) {
                    Tweet tweet = new Tweet();
                    tweet.parse(tweetObject);
                    field.set(this, tweet);
                }
            } catch (JSONException e) {
            }
        }
    }
}
