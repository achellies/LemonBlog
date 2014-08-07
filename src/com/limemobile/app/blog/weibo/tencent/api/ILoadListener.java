
package com.limemobile.app.blog.weibo.tencent.api;

import android.widget.TextView;

/**
 * 加载进度监听器
 */
public interface ILoadListener {
    void onStart(TextView v);

    void onEnd();
}
