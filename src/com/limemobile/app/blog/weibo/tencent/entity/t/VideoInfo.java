
package com.limemobile.app.blog.weibo.tencent.entity.t;

import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

public class VideoInfo extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 8181389051037578083L;
    public String minipic; // 视频小图片地址
    public String player; // 播放器地址
    public String real; // 视频实际地址
    public String short_; // 视频短url
    public String title; // 视频标题

    @Override
    public void parse(JSONObjectProxy data) {
        super.parse(data);
        try {
            short_ = data.getString("short");
        } catch (JSONException e) {
        }
    }
}
