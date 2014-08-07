
package com.limemobile.app.blog.activity.fragment;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.ViewGroup;

public abstract class ThemeFragment extends Fragment {
    public abstract void themeChanged();

    public abstract void initViews(ViewGroup container);
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
