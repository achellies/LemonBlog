package com.limemobile.app.blog.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.EditUserInfoActivity;
import com.limemobile.app.blog.activity.MutualListActivity;
import com.limemobile.app.blog.activity.MyApplication;
import com.limemobile.app.blog.activity.MyTopicActivity;
import com.limemobile.app.blog.activity.TweetListActivity;
import com.limemobile.app.blog.activity.UsersListActivity;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.user.InfoTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.user.Info;
import com.limemobile.app.utils.MessageDigestUtil;
import com.limemobile.app.utils.StringUtils;
import com.limemobile.app.utils.ToastUtils;
import com.limemobile.app.utils.UserTask;

import edu.mit.mobile.android.imagecache.ImageCache.OnImageLoadListener;

import java.io.File;
import java.io.IOException;

public class TabSelfInfoFragment extends ThemeFragment implements OnClickListener {
    private static final int INTENT_EDIT = 1;

    private ViewGroup mainContainer;
    private View titleBar;
    private TextView title;
    private ImageView newTweet;
    private ImageView reload;
    private View headContainer;
    private ImageView head;
    private View vipContainer;
    private TextView vip;
    private View nickContainer;
    private TextView nick;
    private ImageView sex;
    private ImageView constellation;
    private View addressContainer;
    private TextView address;
    private View followContainer;
    private TextView follow;
    private View fansContainer;
    private TextView fans;
    private View favoriteContainer;
    private TextView favorite;
    private View tweetContainer;
    private TextView tweet;
    private View mutualContainer;
    private TextView mutual;
    private View blacklistContainer;
    private TextView blacklist;
    private TextView level;
    private View topicContainer;

    private Info selfInfo;
    private long imageID = 0;

    private OnImageLoadListener headLoadListener;
    
    private UserTask<Object, Void, Boolean> refreshTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        File cacheBase = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + Constant.cacheDir + Constant.dataFolder);
        else
            cacheBase = new File(getActivity().getCacheDir() + File.separator + Constant.dataFolder);
        String cacheName = MessageDigestUtil.getInstance().hash(Constant.SELFINFO_CACHE_FILE);
        File cacheFile = new File(cacheBase.getAbsolutePath() + File.separator +
                MessageDigestUtil.getInstance().hash(TencentWeibo.getInstance().getOpenid())
                + File.separator + cacheName);

        if (cacheFile.exists() && cacheFile.isFile() && cacheFile.canRead()) {
//            selfInfo = new Info();
//            if (!Entity.parsefile(input, selfInfo))
//                selfInfo = null;
            refreshTask = new loadCacheTask().execute(cacheFile);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (getActivity() != null && getActivity().isFinishing()) {
            final UserTask<Object, Void, Boolean> task = refreshTask;
            if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                task.cancel(true);
                refreshTask = null;
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_tab_selfinfo, null);
        initViews(layout);
        themeChanged();
        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        if (headLoadListener != null) {
            MyApplication.imageCache.unregisterOnImageLoadListener(headLoadListener);
            headLoadListener = null;
        }
        super.onDetach();
    }

    @Override
    public void onResume() {
        updateViews();
        super.onResume();
    }

    @Override
    public void themeChanged() {
        if (isAdded()) {
            titleBar.setBackgroundResource(R.drawable.title_bar_bg);

            Resources res = Theme.getInstance().getContext(getActivity()).getResources();

            title.setText(res.getString(R.string.tab_selfinfo));
            title.setTextColor(res.getColor(R.color.tab_text_color));
            float titleTextSize = res.getDimension(R.dimen.title_text_size)
                    / res.getDisplayMetrics().density;
            title.setTextSize(titleTextSize);

            newTweet.setImageDrawable(res.getDrawable(R.drawable.title_new));
            reload.setImageDrawable(res.getDrawable(R.drawable.title_reload));

            headContainer.setBackgroundDrawable(res.getDrawable(R.drawable.portrait_bg));

            vipContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
            nickContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
            addressContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_bottom));

            followContainer.setBackgroundDrawable(res.getDrawable(R.drawable.bg_panel_above_left));
            fansContainer.setBackgroundDrawable(res.getDrawable(R.drawable.bg_panel_above_right));
            favoriteContainer.setBackgroundDrawable(res.getDrawable(R.drawable.bg_panel_below_left));
            tweetContainer.setBackgroundDrawable(res.getDrawable(R.drawable.bg_panel_below_right));

            mutualContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
            topicContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
            blacklistContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_bottom));
        }
    }

    @Override
    public void initViews(ViewGroup container) {
        mainContainer = (ViewGroup) container.findViewById(R.id.sub_container);
        titleBar = container.findViewById(R.id.main_titlebar);
        title = (TextView) container.findViewById(R.id.title);
        newTweet = (ImageView) container.findViewById(R.id.edit);
        reload = (ImageView) container.findViewById(R.id.refresh);
        newTweet.setOnClickListener(this);
        reload.setOnClickListener(this);

        headContainer = container.findViewById(R.id.user_info_head_container);
        head = (ImageView) container.findViewById(R.id.user_info_head);
        vipContainer = container.findViewById(R.id.user_info_vip_container);
        vip = (TextView) container.findViewById(R.id.user_info_vip);
        nickContainer = container.findViewById(R.id.user_info_nick_container);
        nick = (TextView) container.findViewById(R.id.user_info_nick);
        sex = (ImageView) container.findViewById(R.id.user_info_sex);
        constellation = (ImageView) container.findViewById(R.id.user_info_constellation);
        addressContainer = container.findViewById(R.id.user_info_address_container);
        address = (TextView) container.findViewById(R.id.user_info_address);
        followContainer = container.findViewById(R.id.user_info_follow_container);
        followContainer.setOnClickListener(this);
        follow = (TextView) container.findViewById(R.id.user_info_follow);
        fansContainer = container.findViewById(R.id.user_info_fans_container);
        fansContainer.setOnClickListener(this);
        fans = (TextView) container.findViewById(R.id.user_info_fans);
        favoriteContainer = container.findViewById(R.id.user_info_favorite_container);
        favoriteContainer.setOnClickListener(this);
        favorite = (TextView) container.findViewById(R.id.user_info_favorite);
        tweetContainer = container.findViewById(R.id.user_info_tweet_container);
        tweetContainer.setOnClickListener(this);
        tweet = (TextView) container.findViewById(R.id.user_info_tweet);
        mutualContainer = container.findViewById(R.id.user_info_mutual_container);
        mutualContainer.setOnClickListener(this);
        mutual = (TextView) container.findViewById(R.id.user_info_mutual);
        blacklistContainer = container.findViewById(R.id.user_info_blacklist_container);
        blacklistContainer.setOnClickListener(this);
        blacklist = (TextView) container.findViewById(R.id.user_info_blacklist);
        topicContainer = container.findViewById(R.id.user_info_topic_container);
        topicContainer.setOnClickListener(this);

        level = (TextView) container.findViewById(R.id.user_info_level);
        
        vip.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case INTENT_EDIT:
                    Info temp = (Info) data.getSerializableExtra(ITransKey.KEY);
                    if (temp != null) {
                        selfInfo = temp;
                        updateViews();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    public void refresh() {
        final UserTask<Object, Void, Boolean> task = refreshTask;
        if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
            return;
        }
        refreshTask = new InfoTask(mainContainer, loadListener, resultListener).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                if (selfInfo != null) {
                    Intent intent = new Intent(getActivity(), EditUserInfoActivity.class);
                    intent.putExtra(ITransKey.KEY, selfInfo);
                    startActivityForResult(intent, INTENT_EDIT);
                }
                break;
            case R.id.refresh:
                refresh();
                break;
            case R.id.user_info_follow_container:
                if (selfInfo != null) {
                    Intent intent = new Intent(getActivity(), UsersListActivity.class);
                    intent.putExtra(ITransKey.KEY, selfInfo.name);
                    intent.putExtra(ITransKey.KEY1, selfInfo.openid);
                    intent.putExtra(ITransKey.KEY2, UsersListActivity.UserListType.IdolList.getNumericType());
                    intent.putExtra(ITransKey.KEY3, selfInfo.sex);
                    startActivity(intent);
                }
                break;
            case R.id.user_info_fans_container:
                if (selfInfo != null) {
                    Intent intent = new Intent(getActivity(), UsersListActivity.class);
                    intent.putExtra(ITransKey.KEY, selfInfo.name);
                    intent.putExtra(ITransKey.KEY1, selfInfo.openid);
                    intent.putExtra(ITransKey.KEY2, UsersListActivity.UserListType.FansList.getNumericType());
                    intent.putExtra(ITransKey.KEY3, selfInfo.sex);
                    startActivity(intent);
                }
                break;
            case R.id.user_info_favorite_container:
            	if (selfInfo != null) {
                    Intent intent = new Intent(getActivity(), TweetListActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.MyFavoriteTweet.getNumericType());
                    intent.putExtras(extras);
                    String titleText = getString(R.string.my_favorites_label);
                    intent.putExtra(ITransKey.KEY5, titleText);
                    startActivity(intent);
            	}
                break;
            case R.id.user_info_tweet_container:
                if (selfInfo != null) {
                    Intent intent = new Intent(getActivity(), TweetListActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.MyTweet.getNumericType());
                    intent.putExtras(extras);
                    String titleText = getString(R.string.my_tweet_label);
                    intent.putExtra(ITransKey.KEY5, titleText);
                    startActivity(intent);
                }
                break;
            case R.id.user_info_topic_container:
                if (selfInfo != null) {
                    Intent intent = new Intent(getActivity(), MyTopicActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.user_info_mutual_container:
                if (selfInfo != null) {
                    Intent intent = new Intent(getActivity(), MutualListActivity.class);
                    intent.putExtra(ITransKey.KEY, selfInfo.openid);
                    startActivity(intent);
                }
                break;
            case R.id.user_info_blacklist_container:
                if (selfInfo != null) {
                    Intent intent = new Intent(getActivity(), UsersListActivity.class);
                    intent.putExtra(ITransKey.KEY, selfInfo.name);
                    intent.putExtra(ITransKey.KEY1, selfInfo.openid);
                    intent.putExtra(ITransKey.KEY2, UsersListActivity.UserListType.Blacklist.getNumericType());
                    startActivity(intent);
                }
                break;
        }
    }

    private void updateViews() {
        if (selfInfo != null) {
            Resources res = Theme.getInstance().getContext(getActivity()).getResources();
            
            if (!TextUtils.isEmpty(selfInfo.nick))
                nick.setText(selfInfo.nick);

            if (!TextUtils.isEmpty(selfInfo.location))
                address.setText(selfInfo.location);

            follow.setText(StringUtils.friendly_long(getActivity(), selfInfo.idolnum));
            fans.setText(StringUtils.friendly_long(getActivity(), selfInfo.fansnum));
            favorite.setText(StringUtils.friendly_long(getActivity(), selfInfo.favnum));
            tweet.setText(StringUtils.friendly_long(getActivity(), selfInfo.tweetnum));
            level.setText(res.getString(R.string.user_info_level) + Long.toString(selfInfo.level));
            
            mutual.setText("");
            blacklist.setText("");

            // if (selfInfo.isvip == 1) {
            // vipContainer.setVisibility(View.VISIBLE);
            // vip.setText(selfInfo.verifyinfo);
            // nameContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
            // } else {
            vipContainer.setVisibility(View.GONE);
            nickContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
            // }

            if (selfInfo.birth_year > 0)
                constellation.setImageResource(correspondConstellation(selfInfo));
            else
                constellation.setVisibility(View.GONE);

            // 用户性别，1-男，2-女，0-未填写,
            if (selfInfo.sex == 0)
                sex.setVisibility(View.INVISIBLE);
            else
                sex.setImageResource(selfInfo.sex == 1 ? R.drawable.icon_male
                        : R.drawable.icon_female);

            if (!TextUtils.isEmpty(selfInfo.head)) {
                imageID = MyApplication.imageCache.getNewID();
                if (headLoadListener == null) {
                    headLoadListener = new OnImageLoadListener() {

                        @Override
                        public void onImageLoaded(long id, Uri imageUri, Drawable image) {
                            Uri uri = null;
                            if (selfInfo.head.startsWith("http")) {
                                uri = Uri.parse(selfInfo.head + TencentWeibo.HEAD_LARGE_SIZE);
                            }
                            else {
                                File file = new File(selfInfo.head);
                                uri = Uri.fromFile(file);
                            }
                            if (selfInfo != null && id == imageID
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
                    if (selfInfo.head.startsWith("http")) {
                        uri = Uri.parse(selfInfo.head + TencentWeibo.HEAD_LARGE_SIZE);
                    }
                    else {
                        File file = new File(selfInfo.head);
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

    public static int correspondConstellation(Info selfInfo) {
        int constellationResId = 0;

        if (selfInfo != null) {
            if ((selfInfo.birth_month == 1 && selfInfo.birth_day >= 20)
                    || (selfInfo.birth_month == 2 && selfInfo.birth_day <= 18)) {
                // 水瓶座：1月20日～2月18日
                constellationResId = R.drawable.aquarius;
            } else if ((selfInfo.birth_month == 2 && selfInfo.birth_day >= 19)
                    || (selfInfo.birth_month == 3 && selfInfo.birth_day <= 20)) {
                // 双鱼座：2月19日～3月20日
                constellationResId = R.drawable.pisces;
            } else if ((selfInfo.birth_month == 3 && selfInfo.birth_day >= 21)
                    || (selfInfo.birth_month == 4 && selfInfo.birth_day <= 20)) {
                // 白羊座：3月21日～4月20日
                constellationResId = R.drawable.aries;
            } else if ((selfInfo.birth_month == 4 && selfInfo.birth_day >= 21)
                    || (selfInfo.birth_month == 5 && selfInfo.birth_day <= 20)) {
                // 金牛座：4月21日～5月20日
                constellationResId = R.drawable.taurus;
            } else if ((selfInfo.birth_month == 5 && selfInfo.birth_day >= 21)
                    || (selfInfo.birth_month == 6 && selfInfo.birth_day <= 21)) {
                // 双子座：5月21日～6月21日
                constellationResId = R.drawable.gemini;
            } else if ((selfInfo.birth_month == 6 && selfInfo.birth_day >= 22)
                    || (selfInfo.birth_month == 7 && selfInfo.birth_day <= 22)) {
                // 巨蟹座：6月22日～7月22日
                constellationResId = R.drawable.cancer;
            } else if ((selfInfo.birth_month == 7 && selfInfo.birth_day >= 23)
                    || (selfInfo.birth_month == 8 && selfInfo.birth_day <= 22)) {
                // 狮子座：7月23日～8月22日
                constellationResId = R.drawable.leo;
            } else if ((selfInfo.birth_month == 8 && selfInfo.birth_day >= 23)
                    || (selfInfo.birth_month == 9 && selfInfo.birth_day <= 22)) {
                // 处女座：8月23日～9月22日
                constellationResId = R.drawable.virgo;
            } else if ((selfInfo.birth_month == 9 && selfInfo.birth_day >= 23)
                    || (selfInfo.birth_month == 10 && selfInfo.birth_day <= 22)) {
                // 天秤座：9月23日～10月22日
                constellationResId = R.drawable.libra;
            } else if ((selfInfo.birth_month == 10 && selfInfo.birth_day >= 23)
                    || (selfInfo.birth_month == 11 && selfInfo.birth_day <= 21)) {
                // 天蝎座：10月23日～11月21日
                constellationResId = R.drawable.scorpio;
            } else if ((selfInfo.birth_month == 11 && selfInfo.birth_day >= 22)
                    || (selfInfo.birth_month == 12 && selfInfo.birth_day <= 21)) {
                // 射手座：11月22日～12月21日
                constellationResId = R.drawable.sagittarius;
            } else if ((selfInfo.birth_month == 12 && selfInfo.birth_day >= 22)
                    || (selfInfo.birth_month == 1 && selfInfo.birth_day <= 19)) {
                // 魔羯座：12月22日～1月19日
                constellationResId = R.drawable.capricorn;
            }
        }

        return constellationResId;
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
                    if (!TextUtils.isEmpty(data.response)) {
                        File cacheBase = null;
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.cacheDir + Constant.dataFolder);
                        else
                            cacheBase = new File(getActivity().getCacheDir() + File.separator + Constant.dataFolder);
                        String cacheName = MessageDigestUtil.getInstance().hash(Constant.SELFINFO_CACHE_FILE);
                        File cacheFile = new File(cacheBase.getAbsolutePath()
                                + File.separator +
                                MessageDigestUtil.getInstance().hash(TencentWeibo.getInstance().getOpenid()) + File.separator
                                + cacheName);
                        Entity.cache2file(cacheFile, data.response);
                    }

                    selfInfo = (Info) data.data;

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
    
    private class loadCacheTask extends UserTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            File input = (File) params[0];
            selfInfo = new Info();
            if (!Entity.parsefile(input, selfInfo))
                selfInfo = null;
            return selfInfo == null ? false : true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                updateViews();
            super.onPostExecute(result);
        }
        
    }
}