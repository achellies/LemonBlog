
package com.limemobile.app.blog.activity.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

import com.limemobile.app.blog.activity.ThemeActivity;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Theme {
	public static final String PREF_THEME_PACKAGE_NAME = "theme_package";
	
    private static Theme theme = null;
    private Context context = null;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ArrayList<ThemeActivity> activityList = new ArrayList<ThemeActivity>();
    private String themePackageName;

    public static synchronized Theme getInstance() {
        if (theme == null)
            theme = new Theme();
        return theme;
    }
    
    private Theme() {
    }
    
    public String currentThemePackage() {
    	return themePackageName;
    }
    
    public void changeScreenOrientation(int screenOrientation) {
        try {
            lock.writeLock().lock();
            for (ThemeActivity object : activityList)
                object.screenOrientationChanged(screenOrientation);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void changeTheme(Context context, String packageName) {
        try {
            Context themeContext = context.createPackageContext(packageName,
                    Context.CONTEXT_IGNORE_SECURITY);
            this.context = themeContext;
            themePackageName = packageName;
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(PREF_THEME_PACKAGE_NAME, themePackageName);
            editor.commit();
        } catch (NameNotFoundException e) {
        } finally {
            if (this.context == null)
                this.context = context.getApplicationContext();
        }
        setContext(this.context);
    }

    public Context getContext(Context context) {
        if (this.context == null) {
            //String blueSkyPackageName = "com.limemobile.app.blog.bluesky";
        	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        	themePackageName = sp.getString(PREF_THEME_PACKAGE_NAME, "com.limemobile.app.blog");
            try {
                this.context = context.createPackageContext(themePackageName,
                        Context.CONTEXT_IGNORE_SECURITY);
            } catch (NameNotFoundException e) {
            	themePackageName = "com.limemobile.app.blog";
            	changeTheme(context, themePackageName);
            }
        }
        return this.context;
    }

    public void addActivity(ThemeActivity object) {
        try {
            lock.writeLock().lock();
            activityList.add(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeActivity(ThemeActivity object) {
        try {
            lock.writeLock().lock();
            activityList.remove(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setContext(Context context) {
        this.context = context;

        try {
            lock.writeLock().lock();
            for (ThemeActivity object : activityList)
                object.themeChanged();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
