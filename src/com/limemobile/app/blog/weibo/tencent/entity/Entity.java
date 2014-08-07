
package com.limemobile.app.blog.weibo.tencent.entity;

import android.text.TextUtils;

import com.limemobile.app.blog.weibo.tencent.api.Result;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Entity<T extends TObject> {
    public String response;
    public int ret; // 返回值，0-成功，非0-失败
    public int errcode; // 返回错误码
    public String msg; // 错误信息
    public T data; // 实际数据
    public String seqid; // 序列号

    // 下面主要用于 http://open.t.qq.com/api/other/get_emotions
    public int num; // 表示本类表情的个数
    public int type; // 表示类型（附类型说明：）
                     // 0：默认QQ表情
                     // 1：魂儿喵喵
                     // 2：阿囧
                     // 3：哎呀猩猩
                     // 4：爱心龟
                     // 5：大眼鼓
                     // 6：唛哩唛哩轰
                     // 7：细哥细妹
                     // 8：想念熊
                     // 9：小幺鸡
                     // 10：哎哟熊
                     // 11：吕查德
                     // 12：炮炮兵
                     // 13：张小盒
                     // 14：阿狸
                     // 15：几何猫
                     // 16：boto
                     // 17：emoji

    public static void cache2file(File cacheFile, String response) {
        if (!TextUtils.isEmpty(response)) {
            if (!cacheFile.getParentFile().exists())
                cacheFile.getParentFile().mkdirs();
            if (cacheFile.exists())
                cacheFile.delete();

            OutputStream os = null;
            try {
                os = new FileOutputStream(cacheFile);
                os.write(response.getBytes());
                os.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean parsefile(File cacheFile, TObject data) {
        boolean parsed = false;
        if (cacheFile.exists() && cacheFile.isFile() && cacheFile.canRead()) {
            String cacheData;
            InputStream is = null;
            try {
                is = new FileInputStream(cacheFile);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                cacheData = new String(buffer);
                JSONObjectProxy object = new JSONObjectProxy(new JSONObject(cacheData));

                JSONObjectProxy dataObject = object.getJSONObjectOrNull(Result.DATA_KEY);
                if (dataObject != null) {
                    data.parse(dataObject);
                    parsed = true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return parsed;
    }
}
