
package com.limemobile.app.blog.weibo.tencent.entity.user;

import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.common.Music;
import com.limemobile.app.blog.weibo.tencent.entity.common.Video;

import java.util.ArrayList;

// 帐户相关/获取心情微博 https://open.t.qq.com/api/user/emotion
public class Emotion extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 9091558468596013169L;
    public String timestamp; // 服务器时间戳，不能用于翻页
    public int hasnext; // 0-表示还有微博可拉取，1-已拉取完毕
    public ArrayList<Info> info;
    public User user;

    public class Info extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -7220889920004931910L;
        public String text; // 微博内容
        public String origtext; // 原始内容
        public long count; // 微博被转次数
        public long mcount; // 点评次数
        public String from; // 来源
        public String fromurl; // 来源url
        public String id; // 微博唯一id
        public ArrayList<String> image; // 图片url列表
        public Video video; // 视频信息
        // public String picurl; // 缩略图
        // public String player; // 播放器地址
        // public String realurl; // 视频原地址
        // public String shorturl; // 视频的短url
        // public String title; // 视频标题
        public Music music; // 音频信息
        // public String author; // 演唱者
        // public String url; // 音频地址
        // public String title; // 音频名字，歌名
        public String name; // 发表人帐户名
        public String openid; // 用户唯一id，与name相对应
        public String nick; // 发表人昵称
        public int self; // 是否自已发的的微博，0-不是，1-是
        public String timestamp; // 发表时间
        public int type; // 微博类型，1-原创发表，2-转载，3-私信，4-回复，5-空回，6-提及，7-评论
        public String head; // 发表者头像url
        public String location; // 发表者所在地
        public String country_code; // 国家码（与地区发表时间线一样）
        public String province_code; // 省份码（与地区发表时间线一样）
        public String city_code; // 城市码（与地区发表时间线一样）
        public int isvip; // 是否微博认证用户，0-不是，1-是
        public int isrealname; // 是否实名认证，0-老用户，1-已实名认证，2-未实名认证
        public String geo; // 发表者地理信息
        public int status; // 微博状态，0-正常，1-系统删除，2-审核中，3-用户删除，4-根删除
        public String emotionurl; // 心情图片url
        public int emotiontype; // 心情类型,
        public String source; // 当type=2时，source即为源tweet
    }

    public class User extends TObject {
        /**
         * 
         */
        private static final long serialVersionUID = -6039458255572525229L;
        public String name; // nick 当页数据涉及到的用户的帐号与昵称映射
    }
}
