
package com.limemobile.app.blog.weibo;

import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;

import java.util.HashMap;
import java.util.Map;

public class TencentWeibo {
    public final static String app_key = "801229670";
    public final static String app_secret = "5044a9893c2eacf26b98aad6a5b888a6";
    public final static String redirectUri = "http://code.google.com/p/lemonweibo";

    public final static String JSON = "json";
    public final static String XML = "xml";
    
    public static OAuthV2 getInstance() {
        if (oAuth == null) {
            oAuth = new OAuthV2(redirectUri);
            oAuth.setClientId(app_key);
            oAuth.setClientSecret(app_secret);

            // 关闭OAuthV2Client中的默认开启的QHttpClient。
            OAuthV2Client.getQHttpClient().shutdownConnection();
        }
        return oAuth;
    }
    
    public static void setInstance(OAuthV2 object) {
        oAuth = object;
    }

    private static OAuthV2 oAuth;

    public static String openid;
    
    public static Map<String,String> faceMap = new HashMap<String,String>();
    // static {
    // oAuth = new OAuthV2(redirectUri);
    // oAuth.setClientId(app_key);
    // oAuth.setClientSecret(app_secret);
    //
    // //关闭OAuthV2Client中的默认开启的QHttpClient。
    // OAuthV2Client.getQHttpClient().shutdownConnection();
    // }
    public final static String HEAD_LARGE_SIZE = "/100";
    public final static String HEAD_MEDIUM_SIZE = "/50";
    public final static String HEAD_SMALL_SIZE = "/30";

    public final static String PICTURE_HUGE_SIZE = "/2000";
    public final static String PICTURE_LARGE_SIZE = "/400";
    public final static String PICTURE_MEDIUM_SIZE = "/160";
    public final static String PICTURE_SMALL_SIZE = "/120";
}
