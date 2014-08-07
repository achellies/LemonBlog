package com.limemobile.app.blog.activity;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.fragment.TabSelfInfoFragment;
import com.limemobile.app.blog.activity.fragment.TweetListFragment;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.friends.AddBlackListTask;
import com.limemobile.app.blog.weibo.tencent.api.friends.AddTask;
import com.limemobile.app.blog.weibo.tencent.api.friends.DelTask;
import com.limemobile.app.blog.weibo.tencent.api.user.OtherInfoTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.user.Info;
import com.limemobile.app.blog.widget.LinkView;
import com.limemobile.app.utils.NetUtils;
import com.limemobile.app.utils.StringUtils;
import com.limemobile.app.utils.ToastUtils;
import com.limemobile.app.utils.UserTask;

import edu.mit.mobile.android.imagecache.ImageCache.OnImageLoadListener;

public class UserInfoActivity extends ThemeActivity implements OnClickListener {

    private ViewGroup mainContainer;
    private View titleBar;
    private TextView title;
    private ImageView back;
    private ImageView home;
    private View headContainer;
    private ImageView head;
    private View vipContainer;
    private TextView vip;
    private TextView nick;
    private TextView name;
    private ImageView sex;
    private View descriptionContainer;
    private TextView description;
    private View addressContainer;
    private TextView address;
    private View followContainer;
    private TextView follow;
    private View fansContainer;
    private TextView fans;
    private View tweetContainer;
    private TextView tweet;
    private View favoriteContainer;
    private TextView favorite;
    private TextView level;
    private TextView action;
    private ImageView constellation;
    private TextView refresh;
    private TextView atHe;
    private TextView privateLetter;
    private TextView add2BlackList;

    private Info userInfo;
    private String openId;
    private String userName;
    private long imageID = 0;

    private OnImageLoadListener headLoadListener;
    
    private UserTask<Object, Void, Boolean> refreshTask;
    private UserTask<Object, Void, Boolean> addTask;
    private UserTask<Object, Void, Boolean> delTask;
    private UserTask<Object, Void, Boolean> blacklistTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra(ITransKey.KEY);
            openId = intent.getStringExtra(ITransKey.KEY1);
        }
        if (TextUtils.isEmpty(openId) && TextUtils.isEmpty(userName)) {
            finish();
            return;
        }
        if (TextUtils.isEmpty(userName))
            userName = "";
        if (TextUtils.isEmpty(openId))
            openId = "";
        setContentView(R.layout.activity_userinfo);
        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (headLoadListener != null) {
            MyApplication.imageCache.unregisterOnImageLoadListener(headLoadListener);
            headLoadListener = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isFinishing()) {
            final UserTask<Object, Void, Boolean> task = refreshTask;
            if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                task.cancel(true);
                refreshTask = null;
            }
            
            final UserTask<Object, Void, Boolean> task2 = addTask;
            if (task2 != null && task2.getStatus() != UserTask.Status.FINISHED) {
                task2.cancel(true);
                addTask = null;
            }
            
            final UserTask<Object, Void, Boolean> task3 = delTask;
            if (task3 != null && task3.getStatus() != UserTask.Status.FINISHED) {
                task3.cancel(true);
                delTask = null;
            }
            
            final UserTask<Object, Void, Boolean> task4 = blacklistTask;
            if (task4 != null && task4.getStatus() != UserTask.Status.FINISHED) {
                task4.cancel(true);
                blacklistTask = null;
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        updateViews();
        super.onResume();
    }
    
    private void refresh() {
    	if (NetUtils.getType(this) == NetUtils.NO_NET) {
    		ToastUtils.show(this, R.string.msg_nonetwork, Toast.LENGTH_SHORT);
    		return;
    	}
        final UserTask<Object, Void, Boolean> task = refreshTask;
        if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
            return;
        }
        refreshTask = new OtherInfoTask(mainContainer, loadListener, resultListener).execute(userName, openId);
    }

    @Override
    public void themeChanged() {
        titleBar.setBackgroundResource(R.drawable.title_bar_bg);

        Resources res = Theme.getInstance().getContext(this).getResources();

        title.setText(res.getString(R.string.title_other_user_info));
        title.setTextColor(res.getColor(R.color.tab_text_color));
        float titleTextSize = res.getDimension(R.dimen.title_text_size)
                / res.getDisplayMetrics().density;
        title.setTextSize(titleTextSize);

        back.setImageDrawable(res.getDrawable(R.drawable.title_back));
        home.setImageDrawable(res.getDrawable(R.drawable.title_home));

        headContainer.setBackgroundDrawable(res.getDrawable(R.drawable.portrait_bg));

        vipContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
        descriptionContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_bottom));
        addressContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));

        followContainer.setBackgroundDrawable(res.getDrawable(R.drawable.bg_panel_above_left));
        fansContainer.setBackgroundDrawable(res.getDrawable(R.drawable.bg_panel_above_right));
        favoriteContainer.setBackgroundDrawable(res.getDrawable(R.drawable.bg_panel_below_left));
        tweetContainer.setBackgroundDrawable(res.getDrawable(R.drawable.bg_panel_below_right));

        refresh.setBackgroundDrawable(res.getDrawable(R.drawable.toolbar_bg_left));
        refresh.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.toolbar_refresh_icon), null, null);
        atHe.setBackgroundDrawable(res.getDrawable(R.drawable.toolbar_bg_middle));
        atHe.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.toolbar_at_icon), null, null);
        privateLetter.setBackgroundDrawable(res.getDrawable(R.drawable.toolbar_bg_middle));
        privateLetter.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.toolbar_letter_icon), null, null);
        add2BlackList.setBackgroundDrawable(res.getDrawable(R.drawable.toolbar_bg_right));
        add2BlackList.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.toolbar_blacklist_icon), null, null);
    }

    @Override
    public void initViews() {
        mainContainer = (ViewGroup) findViewById(R.id.sub_container);
        titleBar = findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        back.setOnClickListener(this);
        home.setOnClickListener(this);

        headContainer = findViewById(R.id.user_info_head_container);
        head = (ImageView) findViewById(R.id.user_info_head);
        vipContainer = findViewById(R.id.user_info_vip_container);
        vip = (TextView) findViewById(R.id.user_info_vip);
        descriptionContainer = findViewById(R.id.user_info_description_container);
        description = (TextView) findViewById(R.id.user_info_description);
        nick = (TextView) findViewById(R.id.user_info_nick);
        name = (TextView) findViewById(R.id.user_info_name);
        sex = (ImageView) findViewById(R.id.user_info_sex);
        addressContainer = findViewById(R.id.user_info_address_container);
        address = (TextView) findViewById(R.id.user_info_address);
        followContainer = findViewById(R.id.user_info_follow_container);
        followContainer.setOnClickListener(this);
        follow = (TextView) findViewById(R.id.user_info_follow);
        fansContainer = findViewById(R.id.user_info_fans_container);
        fansContainer.setOnClickListener(this);
        fans = (TextView) findViewById(R.id.user_info_fans);
        tweetContainer = findViewById(R.id.user_info_tweet_container);
        tweetContainer.setOnClickListener(this);
        tweet = (TextView) findViewById(R.id.user_info_tweet);
        favoriteContainer = findViewById(R.id.user_info_favorite_container);
        favoriteContainer.setOnClickListener(this);
        favorite = (TextView) findViewById(R.id.user_info_favorite);
        level = (TextView) findViewById(R.id.user_info_level);
        action = (TextView) findViewById(R.id.item_action);
        action.setOnClickListener(this);
        constellation = (ImageView) findViewById(R.id.user_info_constellation);

        refresh = (TextView) findViewById(R.id.toolbar_refresh);
        refresh.setOnClickListener(this);
        atHe = (TextView) findViewById(R.id.toolbar_at);
        atHe.setOnClickListener(this);
        privateLetter = (TextView) findViewById(R.id.toolbar_privateletter);
        privateLetter.setOnClickListener(this);
        add2BlackList = (TextView) findViewById(R.id.toolbar_blacklist);
        add2BlackList.setOnClickListener(this);
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
            case R.id.toolbar_at:
                break;
            case R.id.toolbar_privateletter:
                break;
            case R.id.toolbar_blacklist:
            	if (NetUtils.getType(this) == NetUtils.NO_NET) {
            		ToastUtils.show(this, R.string.msg_nonetwork, Toast.LENGTH_SHORT);
            		return;
            	}
                final UserTask<Object, Void, Boolean> task = blacklistTask;
                if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                    return;
                }
            	blacklistTask = new AddBlackListTask(mainContainer, loadListener, new IResultListener() {

                    @Override
                    public void onSuccess(Entity<TObject> data) {
                        mainContainer.post(new Runnable() {

                            @Override
                            public void run() {
                                ToastUtils.show(mainContainer.getContext(),
                                        R.string.ok_to_add_to_blacklist, Toast.LENGTH_SHORT);
                            }

                        });
                    }

                    @Override
                    public void onFail(Entity<TObject> data) {
                        mainContainer.post(new Runnable() {

                            @Override
                            public void run() {
                                ToastUtils.show(mainContainer.getContext(),
                                        R.string.fail_to_add_to_blacklist, Toast.LENGTH_SHORT);
                            }

                        });
                    }

                }).execute(userName, openId);
                break;
            case R.id.toolbar_refresh:
            case R.id.refresh:
                refresh();
                break;
            case R.id.item_action:
            	if (NetUtils.getType(this) == NetUtils.NO_NET) {
            		ToastUtils.show(this, R.string.msg_nonetwork, Toast.LENGTH_SHORT);
            		return;
            	}
                if (userInfo != null) {
                    if (userInfo.ismyidol > 0) {
                        final UserTask<Object, Void, Boolean> task1 = delTask;
                        if (task1 != null && task1.getStatus() != UserTask.Status.FINISHED) {
                            return;
                        }
                    	delTask = new DelTask(mainContainer, loadListener, new IResultListener() {

                            @Override
                            public void onSuccess(Entity<TObject> data) {
                                mainContainer.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        userInfo.ismyidol = 0;
                                        updateViews();
                                        ToastUtils.show(mainContainer.getContext(),
                                                R.string.del_attention_ok, Toast.LENGTH_SHORT);
                                    }

                                });
                            }

                            @Override
                            public void onFail(Entity<TObject> data) {
                                mainContainer.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        ToastUtils.show(mainContainer.getContext(),
                                                R.string.del_attention_failed, Toast.LENGTH_SHORT);
                                    }

                                });
                            }

                        }).execute(userInfo.name, "");
                    } else {
                        final UserTask<Object, Void, Boolean> task1 = addTask;
                        if (task1 != null && task1.getStatus() != UserTask.Status.FINISHED) {
                            return;
                        }
                    	addTask = new AddTask(mainContainer, loadListener, new IResultListener() {

                            @Override
                            public void onSuccess(Entity<TObject> data) {
                                mainContainer.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        userInfo.ismyidol = 1;
                                        updateViews();
                                        ToastUtils.show(mainContainer.getContext(),
                                                R.string.add_attention_ok, Toast.LENGTH_SHORT);
                                    }

                                });
                            }

                            @Override
                            public void onFail(Entity<TObject> data) {
                                mainContainer.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        ToastUtils.show(mainContainer.getContext(),
                                                R.string.add_attention_failed, Toast.LENGTH_SHORT);
                                    }

                                });
                            }

                        }).execute(userInfo.name, "");
                    }
                }
                break;
            case R.id.user_info_follow_container:
                if (userInfo != null) {
                    intent = new Intent(this, UsersListActivity.class);
                    intent.putExtra(ITransKey.KEY, userInfo.name);
                    intent.putExtra(ITransKey.KEY1, userInfo.openid);
                    intent.putExtra(ITransKey.KEY2,
                            UsersListActivity.UserListType.IdolList.getNumericType());
                    intent.putExtra(ITransKey.KEY3, userInfo.sex);
                    startActivity(intent);
                }
                break;
            case R.id.user_info_fans_container:
                if (userInfo != null) {
                    intent = new Intent(this, UsersListActivity.class);
                    intent.putExtra(ITransKey.KEY, userInfo.name);
                    intent.putExtra(ITransKey.KEY1, userInfo.openid);
                    intent.putExtra(ITransKey.KEY2,
                            UsersListActivity.UserListType.FansList.getNumericType());
                    intent.putExtra(ITransKey.KEY3, userInfo.sex);
                    startActivity(intent);
                }
                break;
            case R.id.user_info_tweet_container:
                if (userInfo != null) {
                    String titleText = getString(R.string.he_tweet_label);
                    if (userInfo.sex == 2)
                        titleText = getString(R.string.she_tweet_label);
                    intent = new Intent(this, TweetListActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.UserTweet.getNumericType());
                    extras.putString(ITransKey.KEY1, userInfo.name);
                    extras.putString(ITransKey.KEY2, userInfo.openid);
                    intent.putExtras(extras);
                    intent.putExtra(ITransKey.KEY5, titleText);
                    startActivity(intent);
                }
                break;
            case R.id.user_info_favorite_container:
                if (userInfo != null) {
                    String titleText = getString(R.string.he_favorites_label);
                    if (userInfo.sex == 2)
                        titleText = getString(R.string.she_favorites_label);
                    intent = new Intent(this, TweetListActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.UserFavoriteTweet.getNumericType());
                    extras.putString(ITransKey.KEY1, userInfo.name);
                    extras.putString(ITransKey.KEY2, userInfo.openid);
                    intent.putExtras(extras);
                    intent.putExtra(ITransKey.KEY5, titleText);
                    startActivity(intent);
                }
                break;
        }
    }

    private void updateViews() {
        if (userInfo != null) {
            Resources res = Theme.getInstance().getContext(this).getResources();
            if (!TextUtils.isEmpty(userInfo.name))
                ((LinkView)name).setLinkText("@" + userInfo.name);
            if (!TextUtils.isEmpty(userInfo.nick))
                nick.setText(userInfo.nick);

            if (!TextUtils.isEmpty(userInfo.location))
                address.setText(userInfo.location);

            follow.setText(StringUtils.friendly_long(this, userInfo.idolnum));
            fans.setText(StringUtils.friendly_long(this, userInfo.fansnum));
            tweet.setText(StringUtils.friendly_long(this, userInfo.tweetnum));
            level.setText(res.getString(R.string.user_info_level) + Long.toString(userInfo.level));
            favorite.setText(StringUtils.friendly_long(this, userInfo.favnum));

            if (userInfo.birth_year > 0)
                constellation.setImageResource(TabSelfInfoFragment
                        .correspondConstellation(userInfo));
            else
                constellation.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(userInfo.introduction))
                description.setText(userInfo.introduction);
            else
                description.setText(R.string.nothing_left);

            if (userInfo.isvip == 1) {
                vipContainer.setVisibility(View.VISIBLE);
                vip.setText(userInfo.verifyinfo);
                addressContainer.setBackgroundDrawable(res
                        .getDrawable(R.drawable.circle_list_middle));
            } else {
                vipContainer.setVisibility(View.GONE);
                addressContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
            }

            if (userInfo.ismyidol > 0) {
                action.setBackgroundDrawable(res.getDrawable(R.drawable.btn_orange));
                action.setText(res.getString(R.string.user_delattention));
            } else {
                action.setBackgroundDrawable(res.getDrawable(R.drawable.btn_green));
                if (userInfo.sex == 0)
                    action.setText(res.getString(R.string.attention));
                else if (userInfo.sex == 2)
                    action.setText(res.getString(R.string.attend_to_her));
                else
                    action.setText(res.getString(R.string.attend_to_him));
            }

            // 用户性别，1-男，2-女，0-未填写,
            if (userInfo.sex == 0)
                sex.setVisibility(View.INVISIBLE);
            else
                sex.setImageResource(userInfo.sex == 1 ? R.drawable.icon_male
                        : R.drawable.icon_female);

            if (!TextUtils.isEmpty(userInfo.head)) {
                imageID = MyApplication.imageCache.getNewID();
                if (headLoadListener == null) {
                    headLoadListener = new OnImageLoadListener() {

                        @Override
                        public void onImageLoaded(long id, Uri imageUri, Drawable image) {
                            Uri uri = null;
                            if (userInfo.head.startsWith("http")) {
                                uri = Uri.parse(userInfo.head + TencentWeibo.HEAD_LARGE_SIZE);
                            }
                            else {
                                File file = new File(userInfo.head);
                                uri = Uri.fromFile(file);
                            }
                            if (userInfo != null && id == imageID
                                    && imageUri.toString().equals(uri.toString()) && image != null) {
                                head.setImageDrawable(image);
                            }
                        }

                    };
                    MyApplication.imageCache.registerOnImageLoadListener(headLoadListener);
                }
                // attempt to bypass all the loading machinery to get the image
                // loaded as quickly
                // as possible
                Drawable d = null;
                try {
                    Uri uri = null;
                    if (userInfo.head.startsWith("http")) {
                        uri = Uri.parse(userInfo.head + TencentWeibo.HEAD_LARGE_SIZE);
                    }
                    else {
                        File file = new File(userInfo.head);
                        uri = Uri.fromFile(file);
                    }
                    d = MyApplication.imageCache.loadImage(imageID, uri, 0, 0);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                if (d != null) {
                    head.setImageDrawable(d);
                } else
                    head.setImageResource(R.drawable.portrait_default);
            }
        } else {
            refresh();
        }
    }

    private ILoadListener loadListener = new ILoadListener() {

        @Override
        public void onStart(TextView v) {
            if (v != null)
                v.setText(R.string.progress_loading_text);
        }

        @Override
        public void onEnd() {
        }

    };

    private IResultListener resultListener = new IResultListener() {

        @Override
        public void onSuccess(Entity<TObject> data) {
            if (data != null) {
                if (data.data instanceof Info) {
                    userInfo = (Info) data.data;

                    mainContainer.post(new Runnable() {

                        @Override
                        public void run() {
                            updateViews();
                            ToastUtils.show(head.getContext(), R.string.load_userinfo_success,
                                    Toast.LENGTH_SHORT);
                        }

                    });
                }
            }
        }

        @Override
        public void onFail(Entity<TObject> data) {
            mainContainer.post(new Runnable() {

                @Override
                public void run() {
                    ToastUtils.show(head.getContext(), R.string.load_userinfo_failed,
                            Toast.LENGTH_SHORT);
                }

            });
        }

    };
}