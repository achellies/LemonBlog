
package com.limemobile.app.blog.activity.fragment;

import java.lang.reflect.Field;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.MyApplication;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.widget.FixedSpeedScroller;
import com.viewpagerindicator.TabPageIndicator;

public class TabMessageFragment extends ThemeFragment {
    private TabPageIndicator tabPageIndicator;
    private ViewPager viewPager;
    private LinearLayout tab;

    private ImageView tabSelectedIndicator;
    private FragmentStatePagerAdapter pageAdapter;

    private String[] tabTitles = new String[3];
    private ThemeFragment[] fragments = new ThemeFragment[3];
    
    private View titleBar;
    private TextView atMe;
    private TextView comment;
    private TextView privateLetter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Resources res = getResources();
        tabTitles[0] = res.getString(R.string.message_at_me);
        tabTitles[1] = res.getString(R.string.message_comment);
        tabTitles[2] = res.getString(R.string.message_private_letter);

        fragments[0] = new TweetListFragment();
        fragments[1] = new TweetListFragment();
        fragments[2] = new MessagePrivateLetterFragment();
        
        Bundle extras = new Bundle();
        extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.MentionsTweet.getNumericType());
        fragments[0].setArguments(extras);
        
        extras = new Bundle();
        extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.CommentTweet.getNumericType());
        fragments[1].setArguments(extras);
        
        pageAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {

            @Override
            public Fragment getItem(int arg0) {
                return fragments[arg0];
            }

            @Override
            public int getCount() {
                return tabTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }
        };
        
        setRetainInstance(true);
    }
    
	@Override
	public void onResume() {
		super.onResume();
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_tab_message, null);
        initViews((ViewGroup) layout);
        themeChanged();
        new setAdapterTask().executeOnExecutor(MyApplication.uiExecutor);
        return layout;
    }

    @Override
    public void themeChanged() {
        if (isAdded()) {
        	Resources res = Theme.getInstance().getContext(getActivity()).getResources();
        	
            titleBar.setBackgroundResource(R.drawable.title_bar_bg);
            
            int tabTextColor = res.getColor(R.color.tab_text_color);
            float tabTextSize = res.getDimension(R.dimen.title_text_size)
                    / res.getDisplayMetrics().density;

            atMe.setTextColor(tabTextColor);
            atMe.setText(res.getText(R.string.message_at_me));
            atMe.setBackgroundDrawable(res.getDrawable(R.drawable.sub_tab_item_selector));
            atMe.setTextSize(tabTextSize);

            comment.setTextColor(tabTextColor);
            comment.setText(res.getText(R.string.message_comment));
            comment.setBackgroundDrawable(res.getDrawable(R.drawable.sub_tab_item_selector));
            comment.setTextSize(tabTextSize);

            privateLetter.setTextColor(tabTextColor);
            privateLetter.setText(res.getText(R.string.message_private_letter));
            privateLetter.setBackgroundDrawable(res.getDrawable(R.drawable.sub_tab_item_selector));
            privateLetter.setTextSize(tabTextSize);
            
            tabSelectedIndicator.setBackgroundDrawable(res.getDrawable(R.drawable.dark_orange_triangle_up));
        }
    }

    @Override
    public void initViews(ViewGroup container) {
        tab = (LinearLayout) container.findViewById(R.id.sub_tab_bar);
        tabSelectedIndicator = (ImageView) container.findViewById(R.id.sub_tab_selectedIndicator);

        viewPager = (ViewPager) container.findViewById(R.id.sub_tab_viewpager);
        tabPageIndicator = new TabPageIndicator(getActivity(), tab, tabSelectedIndicator);
        
        titleBar = container.findViewById(R.id.main_titlebar);
        atMe = (TextView) container.findViewById(R.id.message_at_me);
        comment = (TextView) container.findViewById(R.id.message_comment);
        privateLetter = (TextView) container.findViewById(R.id.message_private_letter);
        
        viewPager.setOffscreenPageLimit(tabTitles.length);
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(),
                    new AccelerateInterpolator());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private class setAdapterTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            viewPager.setAdapter(pageAdapter);
            tabPageIndicator.setViewPager(viewPager);
        }
    }
}
