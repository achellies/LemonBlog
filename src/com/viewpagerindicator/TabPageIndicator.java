/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator implements PageIndicator {
    /** Title text used when no title is provided by the adapter. */
    private static final CharSequence EMPTY_TITLE = "";

    private static final int TAB_INDICATOR_ANIMATION_INTERVAL = 300;

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         * 
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView) view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    private final LinearLayout mTabLayout;
    private final ImageView mTabSelectedIndicator;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;

    private int mSelectedTabIndex = -1;

    private OnTabReselectedListener mTabReselectedListener;

    public TabPageIndicator(Context context, LinearLayout layout, ImageView indicator) {
        mTabLayout = layout;
        mTabSelectedIndicator = indicator;
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    private void animateToTab(final int previousPosition, final int currentPosition) {
        final View tabView = mTabLayout.getChildAt(currentPosition);
        final View preTabView = previousPosition >= 0 ? mTabLayout.getChildAt(previousPosition)
                : null;
        if (mTabSelector != null)
            mTabLayout.removeCallbacks(mTabSelector);
        mTabSelector = new Runnable() {
            public void run() {
                View parentView = (View) mTabLayout.getParent();
                Animation animation = null;
                if (parentView instanceof FrameLayout) {
                    LayoutParams params = new FrameLayout.LayoutParams(tabView.getWidth(),
                            tabView.getHeight(), Gravity.CENTER_VERTICAL);
                    mTabSelectedIndicator.setLayoutParams(params);
                    animation = new TranslateAnimation(preTabView != null ? preTabView.getLeft()
                            : tabView.getLeft(),
                            tabView.getLeft(),
                            tabView.getTop(), tabView.getTop());
                } else {
                    if (parentView instanceof LinearLayout) {
                    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(tabView.getWidth(),
                                LayoutParams.WRAP_CONTENT);
                        mTabSelectedIndicator.setLayoutParams(params);
                    } else if (parentView instanceof RelativeLayout) {
                        animation = new TranslateAnimation(preTabView != null ? (preTabView.getLeft() + (preTabView.getWidth() - mTabSelectedIndicator.getWidth()) / 2)
                                : (tabView.getLeft() + (tabView.getWidth() - mTabSelectedIndicator.getWidth()) / 2),
                        		tabView.getLeft() + (tabView.getWidth() - mTabSelectedIndicator.getWidth()) / 2,
                        		0, 0);
                    }
                }
                animation.setFillAfter(true);
                animation.setDuration(TAB_INDICATOR_ANIMATION_INTERVAL);
                mTabSelectedIndicator.startAnimation(animation);
                mTabSelector = null;
            }
        };
        mTabLayout.post(mTabSelector);
    }

    private void addTab(CharSequence text, int index) {
        final TabView tabView = (TabView) mTabLayout.getChildAt(index);
        if (tabView != null) {
            tabView.setIndex(index);
            tabView.setFocusable(true);
            tabView.setClickable(true);
            tabView.setOnClickListener(mTabClickListener);
            tabView.setText(text);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        PagerAdapter adapter = mViewPager.getAdapter();
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null)
                title = EMPTY_TITLE;
            addTab(title, i);
        }
        if (mSelectedTabIndex < 0)
            mSelectedTabIndex = 0;
        if (mSelectedTabIndex > count)
            mSelectedTabIndex = count - 1;
        setCurrentItem(mSelectedTabIndex);
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        int prevSelectedIndex = mSelectedTabIndex;
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected)
                animateToTab(prevSelectedIndex, item);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }
}
