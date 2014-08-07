package com.limemobile.app.blog.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tweet;
import com.limemobile.app.blog.widget.TweetView;
import com.limemobile.app.utils.StringUtils;

import edu.mit.mobile.android.imagecache.ImageCache.OnImageLoadListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

public class TweetActivity extends ThemeActivity implements OnClickListener {
    private View mainContainer;
    private View subContainer;
    private View back;
    private View home;
    private TextView title;
    private ViewGroup titlebar;
    
    public ImageView headImage;
    public ImageView vipImage;
    
    public TextView userName;
    public ImageView gpsImage;
    public ImageView picImage;
    public TextView date;
    
    public TweetView tweetContent;
    public ImageView tweetPic1;
    
    public ViewGroup subTweet;
    public TweetView subTweetContent;
    public ImageView subTweetPic1;
    
    public TextView from;
    public TextView tweetRedirect;
    public TextView tweetComment;
    public TextView tweetRedirect_rt;
    public TextView tweetComment_rt;
    
    private TextView refresh;
    private TextView comment;
    private TextView forward;
    private TextView fav;
    private TextView more;
    
    private View imageContainer;
    private ImageView detailImage;
    private ProgressBar loadProgress;
    private Button backBtn;
    private Button saveBtn;
    
    private Tweet tweet;
    private ConcurrentHashMap<Long, WeakReference<ImageView>> imageIDs;
    private OnImageLoadListener imageLoadListener;
    private OnImageLoadListener detailImageLoadListener;
    private long imageID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        if (intent != null) {
            tweet = (Tweet) intent.getSerializableExtra(ITransKey.KEY);
        }
        
        if (tweet == null) {
            finish();
            return;
        }
        imageIDs = new ConcurrentHashMap<Long, WeakReference<ImageView>>();
        setContentView(R.layout.activity_tweet);
        initViews();
    }
    
    @Override
    protected void onDestroy() {
        if (imageLoadListener != null) {
            MyApplication.imageCache.unregisterOnImageLoadListener(imageLoadListener);
            imageLoadListener = null;
        }
        if (detailImageLoadListener != null) {
            MyApplication.imageCache.unregisterOnImageLoadListener(detailImageLoadListener);
            detailImageLoadListener = null;
        }
        super.onDestroy();
    }
    
    @Override
    protected void onResume() {
        updateViews();
        tweetContent.setTextSize(Setting.contentTextSize);
        subTweetContent.setTextSize(Setting.contentTextSize);
        super.onResume();
    }

    private void downloadImage(ImageView view, String imageUrl) {
        long imageID = MyApplication.imageCache.getNewID();
        if (imageLoadListener == null) {
            imageLoadListener = new OnImageLoadListener() {

                @Override
                public void onImageLoaded(long id, Uri imageUri, Drawable image) {
                    if (imageIDs.containsKey(id)) {
                        if (imageIDs.get(id) != null && imageIDs.get(id).get() != null) {
                            imageIDs.get(id).get().setImageDrawable(image);
                            imageIDs.get(id).get().invalidate();
                        }
                    }
                }

            };
            MyApplication.imageCache.registerOnImageLoadListener(imageLoadListener);
        }
        // attempt to bypass all the loading machinery to get the image
        // loaded as quickly
        // as possible
        Drawable d = null;
        try {
            Uri uri = Uri.parse(imageUrl);
            d = MyApplication.imageCache.loadImage(imageID, uri, 0, 0);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        if (d != null) {
            view.setImageDrawable(d);
            view.invalidate();
        } else {
            imageIDs.put(imageID, new WeakReference<ImageView>(view));
        }
    }
    
    private void updateViews() {
        headImage.setImageDrawable(null);
        headImage.setTag(null);
        headImage.setImageResource(R.drawable.portrait_small);
        if (!TextUtils.isEmpty(tweet.head) && Setting.readMode == Setting.readMode.Image) {
            downloadImage(headImage, tweet.head + TencentWeibo.HEAD_MEDIUM_SIZE);
        }
        
        vipImage.setVisibility(tweet.isvip > 0 ? View.VISIBLE : View.INVISIBLE);
        
        userName.setText(tweet.nick);
        date.setText(StringUtils.friendly_time(this, tweet.timestamp));
        
        if (gpsImage != null) {
            if (!TextUtils.isEmpty(tweet.geo))
                gpsImage.setVisibility(View.VISIBLE);
            else
                gpsImage.setVisibility(View.GONE);
        }
        
        tweetContent.setText(tweet.text);
        
        tweetPic1.setImageDrawable(null);
        tweetPic1.setTag(null);
        subTweetPic1.setImageDrawable(null);
        subTweetPic1.setTag(null);
        
        if (tweet.image != null && !tweet.image.isEmpty() && !TextUtils.isEmpty(tweet.image.get(0)) && Setting.readMode == Setting.readMode.Image) {
            if (picImage != null)
                picImage.setVisibility(View.VISIBLE);
            tweetPic1.setVisibility(View.VISIBLE);
            tweetPic1.setOnClickListener(this);
            tweetPic1.setTag(tweet);
            downloadImage(tweetPic1, tweet.image.get(0) + TencentWeibo.PICTURE_MEDIUM_SIZE);
        } else {
            tweetPic1.setVisibility(View.GONE);
            if (picImage != null)
                picImage.setVisibility(View.GONE);
        }
        
        // 微博类型，1-原创发表，2-转载，3-私信，4-回复，5-空回，6-提及，7-评论
        if (tweet.type != 1)
            subTweet.setVisibility(View.VISIBLE);
        else
            subTweet.setVisibility(View.GONE);
        
        if (tweet.source != null) {
            subTweetContent.setText("@" + tweet.nick + "：" + tweet.source.text);
            
            if (tweet.source.image != null && !tweet.source.image.isEmpty() && !TextUtils.isEmpty(tweet.source.image.get(0)) && Setting.readMode == Setting.readMode.Image) {
                subTweetPic1.setVisibility(View.VISIBLE);
                downloadImage(subTweetPic1, tweet.source.image.get(0) + TencentWeibo.PICTURE_MEDIUM_SIZE);
                subTweetPic1.setOnClickListener(this);
                subTweetPic1.setTag(tweet.source);
                if (picImage != null)
                    picImage.setVisibility(View.VISIBLE);
            } else {
                subTweetPic1.setVisibility(View.GONE);
                if (picImage != null)
                    picImage.setVisibility(View.GONE);
            }
            
            if (tweet.source.count > 0) {
                tweetRedirect_rt.setText(StringUtils.friendly_long(this, tweet.source.count));
                tweetRedirect_rt.setVisibility(View.VISIBLE);
            } else
                tweetRedirect_rt.setVisibility(View.GONE);
            
            if (tweet.source.mcount > 0) {
                tweetComment_rt.setText(StringUtils.friendly_long(this, tweet.source.mcount));
                tweetComment_rt.setVisibility(View.VISIBLE);
            } else
                tweetComment_rt.setVisibility(View.GONE);
        } else 
            subTweet.setVisibility(View.GONE);
        
        if (!TextUtils.isEmpty(tweet.from)) {
            String fromString = String.format(this.getString(R.string.from), tweet.from);
            from.setText(fromString);
        } else
            from.setVisibility(View.GONE);
        
        tweetRedirect.setText(StringUtils.friendly_long(this, tweet.count));
        tweetRedirect.setVisibility(View.VISIBLE);
        
        tweetComment.setText(StringUtils.friendly_long(this, tweet.mcount));
        tweetComment.setVisibility(View.VISIBLE);
    }

    @Override
    public void initViews() {
        home = findViewById(R.id.home);
        home.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        
        headImage = (ImageView) findViewById(R.id.item_image);
        vipImage = (ImageView) findViewById(R.id.item_subimage);
        userName = (TextView) findViewById(R.id.item_title);
        gpsImage = (ImageView) findViewById(R.id.item_gps);
        picImage = (ImageView) findViewById(R.id.item_pic);
        date = (TextView) findViewById(R.id.item_date);
        tweetContent = (TweetView) findViewById(R.id.item_content);
        tweetPic1 = (ImageView) findViewById(R.id.tweet_upload_pic1);
        tweetPic1.setOnClickListener(this);
        subTweet = (ViewGroup) findViewById(R.id.subLayout);
        subTweetContent = (TweetView) findViewById(R.id.sub_item_content);
        subTweetPic1 = (ImageView) findViewById(R.id.tweet_upload_pic2);
        subTweetPic1.setOnClickListener(this);
        from = (TextView) findViewById(R.id.tweet_form);
        tweetRedirect = (TextView) findViewById(R.id.tweet_redirect);
        tweetRedirect.setOnClickListener(this);
        tweetComment = (TextView) findViewById(R.id.tweet_comment);
        tweetComment.setOnClickListener(this);
        tweetRedirect_rt = (TextView) findViewById(R.id.tweet_redirect_rt);
        tweetComment_rt = (TextView) findViewById(R.id.tweet_comment_rt);
        
        mainContainer = findViewById(R.id.main_container);
        
        subContainer = findViewById(R.id.sub_container);
        subContainer.setOnClickListener(this);
        
        refresh = (TextView) findViewById(R.id.toolbar_refresh);
        refresh.setOnClickListener(this);
        comment = (TextView) findViewById(R.id.toolbar_comment);
        comment.setOnClickListener(this);
        forward = (TextView) findViewById(R.id.toolbar_forward);
        forward.setOnClickListener(this);
        fav = (TextView) findViewById(R.id.toolbar_fav);
        fav.setOnClickListener(this);
        more = (TextView) findViewById(R.id.toolbar_more);
        more.setOnClickListener(this);
        
        imageContainer = findViewById(R.id.mask_container);
//        imageContainer.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				return true;
//			}
//        	
//        });
        backBtn = (Button) findViewById(R.id.detail_image_back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				imageContainer.setVisibility(View.GONE);
			}
        
        });
        saveBtn = (Button) findViewById(R.id.detail_image_save);
        saveBtn.setVisibility(View.VISIBLE);
        saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
        	
        });
        detailImage = (ImageView) findViewById(R.id.tweet_upload_pic);
        loadProgress = (ProgressBar) findViewById(R.id.loading_progress);
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
            case R.id.sub_container:
                intent = new Intent(this, UserInfoActivity.class);
                intent.putExtra(ITransKey.KEY, tweet.name);
                startActivity(intent);
                break;
            case R.id.tweet_redirect:
            case R.id.toolbar_forward:
                break;
            case R.id.tweet_comment:
            case R.id.toolbar_comment:
                break;
            case R.id.toolbar_refresh:
                break;
            case R.id.toolbar_fav:
                break;
            case R.id.toolbar_more:
                break;
            case R.id.tweet_upload_pic1:
            case R.id.tweet_upload_pic2:
            	if (v.getTag() != null && v.getTag() instanceof Tweet) {
            		Tweet tweet = (Tweet) v.getTag();
            		if (tweet != null && tweet.image != null && !tweet.image.isEmpty() && !TextUtils.isEmpty(tweet.image.get(0)) && Setting.readMode == Setting.readMode.Image) {
            			imageContainer.setVisibility(View.VISIBLE);
            			
                        imageID = MyApplication.imageCache.getNewID();
                        if (detailImageLoadListener == null) {
                        	detailImageLoadListener = new OnImageLoadListener() {

                                @Override
                                public void onImageLoaded(long id, Uri imageUri, Drawable image) {
                                	if (id == imageID && image != null) {
                                		loadProgress.setVisibility(View.GONE);
                                		detailImage.setImageDrawable(image);
                                		detailImage.setVisibility(View.VISIBLE);
                                	}
                                }

                            };
                            MyApplication.imageCache.registerOnImageLoadListener(detailImageLoadListener);
                        }
                        // attempt to bypass all the loading machinery to get the image
                        // loaded as quickly
                        // as possible
                        Drawable d = null;
                        try {
                            Uri uri = Uri.parse(tweet.image.get(0) + TencentWeibo.PICTURE_LARGE_SIZE);
                            d = MyApplication.imageCache.loadImage(imageID, uri, 0, 0);
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                        if (d != null) {
                        	loadProgress.setVisibility(View.GONE);
                        	detailImage.setImageDrawable(d);
                        	detailImage.setVisibility(View.VISIBLE);
                        } else {
                        	detailImage.setVisibility(View.GONE);
                        	loadProgress.setVisibility(View.VISIBLE);
                        }
            		}
            	}
            	break;
        }
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        back.setBackgroundDrawable(res.getDrawable(R.drawable.title_back));
        home.setBackgroundDrawable(res.getDrawable(R.drawable.title_home));
        titlebar.setBackgroundDrawable(res.getDrawable(R.drawable.title_bar_bg));
        
        title.setText(R.string.weibo_detail);
        
        refresh.setBackgroundDrawable(res.getDrawable(R.drawable.toolbar_bg_left));
        refresh.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.toolbar_refresh_icon), null, null);
        
        comment.setBackgroundDrawable(res.getDrawable(R.drawable.toolbar_bg_middle));
        comment.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.toolbar_comment_icon), null, null);
        
        forward.setBackgroundDrawable(res.getDrawable(R.drawable.toolbar_bg_middle));
        forward.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.toolbar_forward_icon), null, null);
        
        fav.setBackgroundDrawable(res.getDrawable(R.drawable.toolbar_bg_middle));
        fav.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.toolbar_fav_icon), null, null);
        
        more.setBackgroundDrawable(res.getDrawable(R.drawable.toolbar_bg_right));
        more.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.toolbar_more_icon), null, null);
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (imageContainer.getVisibility() == View.VISIBLE) {
				imageContainer.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
    
}
