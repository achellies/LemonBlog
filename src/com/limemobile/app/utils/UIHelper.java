package com.limemobile.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class UIHelper {
    /**
     * 打开浏览器
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url){
        try {
            Uri uri = Uri.parse(url);  
            Intent it = new Intent(Intent.ACTION_VIEW, uri);  
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
