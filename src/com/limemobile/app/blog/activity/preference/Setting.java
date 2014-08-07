package com.limemobile.app.blog.activity.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;

public class Setting {
	public static final int MAX_CONTENT_TEXT_SIZE = 20;
	public static final int MIN_CONTENT_TEXT_SIZE = 15;
	
	public static final String PREF_CONTENT_TEXT_SIZE = "content_text_size";
	public static final String PREF_READMODE = "readmode";
	
	public enum ImageSize {
		Large, Medium, Small
	}
	
	public enum ReadMode {
		Image, OnlyText
	}
	
	// 微博提醒
    public static int weiboRemind = 0x01;
    // @我提醒
    public static int atRemind = 0x10;
    // 评论提醒
    public static int commentRemind = 0x100;
    // 私信提醒
    public static int messageRemind = 0x1000;
    // 新粉丝提醒
    public static int fansRemind = 0x10000;

    public static boolean autoLoadMore = false;
	public static boolean fastScrollEnabled = true;
	public static int screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	public static boolean autoOptimized = true;
	public static ImageSize uploadImageSize = ImageSize.Medium;
	public static ImageSize downloadImageSize = ImageSize.Medium;
	public static boolean autoRemind = true;
	public static boolean audioRemind = true;
	public static boolean vibratorRemind = true;	
	public static long remindInterval = 120000;
	
	public static ReadMode readMode = ReadMode.Image;
	public static float contentTextSize = 17.0f;
	
    public static int autoRemindType = atRemind | messageRemind | commentRemind | fansRemind;
    
    public static void settingChanged(Context context) {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	
    	autoLoadMore = sp.getBoolean("autoload_more", false);
    	fastScrollEnabled = sp.getBoolean("screenorientation", true);
    	screenOrientation = sp.getBoolean("screenorientation", true) ? ActivityInfo.SCREEN_ORIENTATION_SENSOR : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    	autoOptimized = sp.getBoolean("auto_opt", true);
    	
    	autoRemind = sp.getBoolean("auto_remind", true);
    	audioRemind = sp.getBoolean("audio", true);
    	vibratorRemind = sp.getBoolean("vibrator", true);
    	
    	remindInterval = Long.parseLong(sp.getString("interval", "120000"));
    	autoRemindType = sp.getInt("remind_category", atRemind | messageRemind | commentRemind | fansRemind);
    	
    	int mode = sp.getInt(PREF_READMODE, 0);   	
    	switch (mode) {
    	case 0:
    		readMode = ReadMode.Image;
    		break;
    	case 1:
    		readMode = ReadMode.OnlyText;
    		break;
    	}
    	
    	contentTextSize = sp.getFloat(PREF_CONTENT_TEXT_SIZE, 17.0f);
    	
    	String imageSize = sp.getString("upload_image_size", "TwoMP");
    	if (imageSize.equals("TwoMP")) {
    		uploadImageSize = ImageSize.Medium;
    	} else if (imageSize.equals("ThreeMP")) {
    		uploadImageSize = ImageSize.Large;
    	} else if (imageSize.endsWith("OneMP")) {
    		uploadImageSize = ImageSize.Small;
    	}
    	
    	imageSize = sp.getString("download_image_size", "wap690");
    	if (imageSize.equals("wap690")) {
    		downloadImageSize = ImageSize.Medium;
    	} else if (imageSize.equals("woriginal")) {
    		downloadImageSize = ImageSize.Large;
    	}
    }
}
