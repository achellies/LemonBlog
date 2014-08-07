
package com.limemobile.app.blog.weibo.tencent.entity.common;

import com.limemobile.app.blog.weibo.tencent.entity.TObject;

// 视频信息
public class Video extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -8692308126559759309L;
    public String picurl; // 缩略图
    public String player; // 播放器地址
    public String realurl; // 视频原地址
    public String shorturl; // 视频的短url
    public String title; // 视频标题
}
