
package com.limemobile.app.blog.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.activity.theme.Theme;

public abstract class ThemeActivity extends FragmentActivity {
    private boolean themeChanged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setRequestedOrientation(Setting.screenOrientation);
    	Theme.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {    	
        if (themeChanged) {
            themeChanged = false;
            themeChanged();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Theme.getInstance().removeActivity(this);
    }

    public abstract void initViews();

    public abstract void themeChanged();
    
	public void screenOrientationChanged(int screenOrientation) {
		switch (screenOrientation) {
		case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
		case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
		case ActivityInfo.SCREEN_ORIENTATION_SENSOR:
			setRequestedOrientation(screenOrientation);
			break;
		}
    }
}
