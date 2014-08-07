
package com.limemobile.app.blog.weibo.tencent.entity.other;

import com.limemobile.app.blog.weibo.tencent.entity.TObject;

public class VideoKey extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = -1610289073463800607L;
    public String videokey; // 视频上传的key (24小时内有效，期间会返回同一个key)
    public String uid; // 用户id
}
