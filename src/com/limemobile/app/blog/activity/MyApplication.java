
package com.limemobile.app.blog.activity;

import android.app.Application;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.StrictMode;

import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.utils.CrashHandler;

import edu.mit.mobile.android.imagecache.ImageCache;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {
    public static ImageCache imageCache;
    
    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;
    
    private static final boolean DEVELOPER_MODE = true;

    public static final Executor uiExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    @Override
    public void onCreate() {
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectLeakedSqlLiteObjects()
//                    .detectLeakedClosableObjects()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build());
        }
        
        super.onCreate();
        
        Setting.settingChanged(this);
        
        File cacheBase = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + Constant.cacheDir + Constant.imageFolder);
        } else
            cacheBase = new File(this.getCacheDir() + File.separator + Constant.imageFolder);
        imageCache = ImageCache.getInstance(this, cacheBase, CompressFormat.PNG);
        
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance(this));
        
        TencentWeibo.faceMap.put("闭嘴", "f000");
        TencentWeibo.faceMap.put("便便", "f001");
        TencentWeibo.faceMap.put("擦汗", "f002");
        TencentWeibo.faceMap.put("菜刀", "f003");
        TencentWeibo.faceMap.put("差劲", "f004");
        TencentWeibo.faceMap.put("大兵", "f005");
        TencentWeibo.faceMap.put("大哭", "f006");
        TencentWeibo.faceMap.put("蛋糕", "f007");
        TencentWeibo.faceMap.put("刀", "f008");
        TencentWeibo.faceMap.put("得意", "f009");
        TencentWeibo.faceMap.put("凋谢", "f010");
        TencentWeibo.faceMap.put("发呆", "f011");
        TencentWeibo.faceMap.put("发抖", "f012");
        TencentWeibo.faceMap.put("发怒", "f013");
        TencentWeibo.faceMap.put("饭", "f014");
        TencentWeibo.faceMap.put("飞吻", "f015");
        TencentWeibo.faceMap.put("奋斗", "f016");
        TencentWeibo.faceMap.put("尴尬", "f017");
        TencentWeibo.faceMap.put("勾引", "f018");
        TencentWeibo.faceMap.put("鼓掌", "f019");
        TencentWeibo.faceMap.put("哈欠", "f020");
        TencentWeibo.faceMap.put("害羞", "f021");
        TencentWeibo.faceMap.put("憨笑", "f022");
        TencentWeibo.faceMap.put("坏笑", "f023");
        TencentWeibo.faceMap.put("挥手", "f024");
        TencentWeibo.faceMap.put("回头", "f025");
        TencentWeibo.faceMap.put("饥饿", "f026");
        TencentWeibo.faceMap.put("激动", "f027");
        TencentWeibo.faceMap.put("街舞", "f028");
        TencentWeibo.faceMap.put("惊恐", "f029");
        TencentWeibo.faceMap.put("惊讶", "f030");
        TencentWeibo.faceMap.put("咖啡", "f031");
        TencentWeibo.faceMap.put("磕头", "f032");
        TencentWeibo.faceMap.put("可爱", "f033");
        TencentWeibo.faceMap.put("可怜", "f034");
        TencentWeibo.faceMap.put("抠鼻", "f035");
        TencentWeibo.faceMap.put("骷髅", "f036");
        TencentWeibo.faceMap.put("酷", "f037");
        TencentWeibo.faceMap.put("快哭了", "f038");
        TencentWeibo.faceMap.put("困", "f039");
        TencentWeibo.faceMap.put("篮球", "f040");
        TencentWeibo.faceMap.put("冷汗", "f041");
        TencentWeibo.faceMap.put("礼物", "f042");
        TencentWeibo.faceMap.put("流汗", "f043");
        TencentWeibo.faceMap.put("流泪", "f044");
        TencentWeibo.faceMap.put("玫瑰", "f045");
        TencentWeibo.faceMap.put("难过", "f046");
        TencentWeibo.faceMap.put("怄火", "f047");
        TencentWeibo.faceMap.put("啤酒", "f048");
        TencentWeibo.faceMap.put("瓢虫", "f049");
        TencentWeibo.faceMap.put("撇嘴", "f050");
        TencentWeibo.faceMap.put("乒乓", "f051");
        TencentWeibo.faceMap.put("强", "f052");
        TencentWeibo.faceMap.put("敲打", "f053");
        TencentWeibo.faceMap.put("亲亲", "f054");
        TencentWeibo.faceMap.put("糗大了", "f055");
        TencentWeibo.faceMap.put("拳头", "f056");
        TencentWeibo.faceMap.put("弱", "f057");
        TencentWeibo.faceMap.put("色", "f058");
        TencentWeibo.faceMap.put("闪电", "f059");
        TencentWeibo.faceMap.put("胜利", "f060");
        TencentWeibo.faceMap.put("示爱", "f061");
        TencentWeibo.faceMap.put("衰", "f062");
        TencentWeibo.faceMap.put("睡", "f063");
        TencentWeibo.faceMap.put("太阳", "f064");
        TencentWeibo.faceMap.put("调皮", "f065");
        TencentWeibo.faceMap.put("跳绳", "f066");
        TencentWeibo.faceMap.put("跳跳", "f067");
        TencentWeibo.faceMap.put("偷笑", "f068");
        TencentWeibo.faceMap.put("吐", "f069");
        TencentWeibo.faceMap.put("微笑", "f070");
        TencentWeibo.faceMap.put("委屈", "f071");
        TencentWeibo.faceMap.put("握手", "f072");
        TencentWeibo.faceMap.put("西瓜", "f073");
        TencentWeibo.faceMap.put("吓", "f074");
        TencentWeibo.faceMap.put("献吻", "f075");
        TencentWeibo.faceMap.put("心碎", "f076");
        TencentWeibo.faceMap.put("嘘", "f077");
        TencentWeibo.faceMap.put("疑问", "f078");
        TencentWeibo.faceMap.put("阴险", "f079");
        TencentWeibo.faceMap.put("拥抱", "f080");
        TencentWeibo.faceMap.put("右哼哼", "f081");
        TencentWeibo.faceMap.put("右太极", "f082");
        TencentWeibo.faceMap.put("月亮", "f083");
        TencentWeibo.faceMap.put("晕", "f084");
        TencentWeibo.faceMap.put("再见", "f085");
        TencentWeibo.faceMap.put("炸弹", "f086");
        TencentWeibo.faceMap.put("折磨", "f087");
        TencentWeibo.faceMap.put("咒骂", "f088");
        TencentWeibo.faceMap.put("猪头", "f089");
        TencentWeibo.faceMap.put("抓狂", "f090");
        TencentWeibo.faceMap.put("转圈", "f091");
        TencentWeibo.faceMap.put("龇牙", "f092");
        TencentWeibo.faceMap.put("足球", "f093");
        TencentWeibo.faceMap.put("左哼哼", "f094");
        TencentWeibo.faceMap.put("左太极", "f095");
        TencentWeibo.faceMap.put("no", "f096");
        TencentWeibo.faceMap.put("ok", "f097");
        TencentWeibo.faceMap.put("爱你", "f098");
        TencentWeibo.faceMap.put("爱情", "f099");
        TencentWeibo.faceMap.put("爱心", "f100");
        TencentWeibo.faceMap.put("傲慢", "f101");
        TencentWeibo.faceMap.put("白眼", "f102");
        TencentWeibo.faceMap.put("抱拳", "f103");
        TencentWeibo.faceMap.put("鄙视", "f104");
//        
//        File file = new File("/mnt/sdcard/emo");
//        File[] files = file.listFiles();
//        
//        StringBuilder sb = new StringBuilder();
//        for (int index = 0; index < files.length; ++index) {
//            File object = files[index];
//            
//            Bitmap bitmap = null;
//            try {
//                bitmap = BitmapFactory.decodeFile(object.getCanonicalPath());
//                String resourceName = String.format("f%03d", index);
//                OutputStream stream = new FileOutputStream("/mnt/sdcard/emo/" + resourceName + ".png");
//                /* Write bitmap to file using JPEG or PNG and 80% quality hint for JPEG. */
//                bitmap.compress(CompressFormat.PNG, 100, stream);
//                stream.close();
//                
//                sb.append("TencentWeibo.faceMap.put(\"" + object.getName().substring(0, object.getName().lastIndexOf(".")) + "\", \"" + resourceName + "\");\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (bitmap != null)
//                    bitmap.recycle();
//            }
//        }
//        sb.toString();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
