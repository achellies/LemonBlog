package com.limemobile.app.blog.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.fragment.ThemeFragment;
import com.limemobile.app.blog.activity.fragment.TweetListFragment;
import com.limemobile.app.blog.activity.fragment.TweetListFragment.TweetType;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;

public class TweetListActivity extends ThemeActivity implements OnClickListener {
    private View back;
    private View home;
    private TextView title;
    private ViewGroup titlebar;
    private int tweetType = TweetType.HomeTweet.getNumericType();
    private Bundle extras;
    private ThemeFragment fragment;
    private String titleText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        if (intent != null) {
        	extras = intent.getExtras();
        	if (extras == null) {
        		finish();
        		return;
        	}
        	tweetType = extras.getInt(ITransKey.KEY);
        	
        	titleText = intent.getStringExtra(ITransKey.KEY5);
        }
        
        setContentView(R.layout.activity_tweet_list);
        initViews();
        if (savedInstanceState == null) {
            // First-time init; create fragment to embed in activity.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = new TweetListFragment();
            fragment.setArguments(extras);
            ft.add(R.id.main_container, fragment);
            ft.commit();
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (fragment != null && fragment.onKeyDown(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(ITransKey.KEY, 0);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void initViews() {
        home = findViewById(R.id.home);
        home.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        
        if (!TextUtils.isEmpty(titleText)) {
            title.setText(titleText);
        } else {
            if (TweetType.HomeTweet.getNumericType() == tweetType) {
            } else if (TweetType.MyTweet.getNumericType() == tweetType) {
                title.setText(R.string.my_tweet_label);
            } else if (TweetType.PublicTweet.getNumericType() == tweetType) {
            } else if (TweetType.UserTweet.getNumericType() == tweetType) {
            } else if (TweetType.HuatiTweet.getNumericType() == tweetType) {
            } else if (TweetType.MentionsTweet.getNumericType() == tweetType) {
            } else if (TweetType.SpecialTweet.getNumericType() == tweetType) {
            } else if (TweetType.HotTweet.getNumericType() == tweetType) {
                title.setText(R.string.square_hot_tweet);
            } else if (TweetType.AroundTweet.getNumericType() == tweetType) {
                title.setText(R.string.square_around_tweet);
            } else if (TweetType.SearchTweet.getNumericType() == tweetType) {
            } else if (TweetType.AreaTweet.getNumericType() == tweetType) {
            } else if (TweetType.MyFavoriteTweet.getNumericType() == tweetType) {
                title.setText(R.string.my_favorites_label);
            }
        }
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        back.setBackgroundDrawable(res.getDrawable(R.drawable.title_back));
        home.setBackgroundDrawable(res.getDrawable(R.drawable.title_home));
        titlebar.setBackgroundDrawable(res.getDrawable(R.drawable.title_bar_bg));
    }
}
