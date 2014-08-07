package com.limemobile.app.blog.activity.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.MainActivity;
import com.limemobile.app.blog.activity.MyApplication;
import com.limemobile.app.blog.activity.TweetActivity;
import com.limemobile.app.blog.activity.adapter.TweetListAdapter;
import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.activity.preference.Setting.ReadMode;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.fav.ListTTask;
import com.limemobile.app.blog.weibo.tencent.api.lbs.GetAroundNewTask;
import com.limemobile.app.blog.weibo.tencent.api.search.SearchTask;
import com.limemobile.app.blog.weibo.tencent.api.statuses.AreaTimelineTask;
import com.limemobile.app.blog.weibo.tencent.api.statuses.BroadcastTimelineTask;
import com.limemobile.app.blog.weibo.tencent.api.statuses.HTTimelineTask;
import com.limemobile.app.blog.weibo.tencent.api.statuses.HomeTimelineTask;
import com.limemobile.app.blog.weibo.tencent.api.statuses.MentionsTimelineTask;
import com.limemobile.app.blog.weibo.tencent.api.statuses.PublicTimelineTask;
import com.limemobile.app.blog.weibo.tencent.api.statuses.SpecialTimelineTask;
import com.limemobile.app.blog.weibo.tencent.api.statuses.UserTimelineTask;
import com.limemobile.app.blog.weibo.tencent.api.trends.HotBroadcastTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tweet;
import com.limemobile.app.blog.weibo.tencent.entity.trends.HotBroadcast;
import com.limemobile.app.utils.MessageDigestUtil;
import com.limemobile.app.utils.NetUtils;
import com.limemobile.app.utils.ToastUtils;
import com.limemobile.app.utils.UserTask;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import edu.mit.mobile.android.imagecache.ImageLoaderAdapter;
import edu.mit.mobile.android.imagecache.ImageCache.OnImageLoadListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TweetListFragment extends ThemeFragment implements OnClickListener, OnItemClickListener {
	public enum TweetType {
		// 主页微博
		HomeTweet(0), // HomeTimelineTask
		
		// 广播大厅微博
		PublicTweet(1), // PublicTimelineTask
		
		// 我的微博
		MyTweet(2), // BroadcastTimelineTask
		
		// 其他用户微博
		UserTweet(3), // UserTimelineTask
		
		// 话题微博
		HuatiTweet(4), // HTTimelineTask
		
		// 提到我的微博
		MentionsTweet(5), // MentionsTimelineTask
		
		// 特别收听的微博
		SpecialTweet(6), // SpecialTimelineTask
		
		// 热门微博
		HotTweet(7), // HotBroadcastTask
		
		// 周围微博
		AroundTweet(8), // GetAroundNewTask
		
		// 搜索微博
		SearchTweet(9), // SearchTask
		
		// 地区发表的微博
		AreaTweet(10), // AreaTimelineTask
		
		// 我的收藏的微博
		MyFavoriteTweet(11), // ListTTask
		
		// 他的收藏
		UserFavoriteTweet(12),
		
		// 评论我的
		CommentTweet(13);
		
		TweetType(int type) {
			this.type = type;
		}

		private int type;

		public int getNumericType() {
			return type;
		}
	}
    
    private static final int REQNUM = 50;
    
    private boolean dataInitialized = false;
    private boolean autoLoad = false;
    private int pageflag = 0;
    private ArrayList<Tweet> array;
    private TweetListAdapter adapter;
    private ListView listView;
    private ViewGroup listFooter;
    private View listFooterContainer;
    private ProgressBar listFooterProgressBar;
    private TextView listFooterText;
    private HotBroadcast tweet;
    private boolean invokedByListFooter;
    private int tweetType = TweetType.HomeTweet.getNumericType();
    private String name;
    private String fopenid;
    private String httext;
    private String htid;
    private String longitude;
    private String latitude;
    private int country;
    private int province;
    private int city;
    
    private View imageContainer;
    private ImageView detailImage;
    private ProgressBar loadProgress;
    private long imageID = 0;
    private OnImageLoadListener imageLoadListener;
    
    private UserTask<Object, Void, Boolean> refreshTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle args = getArguments();
        if (args != null) {
        	tweetType = args.getInt(ITransKey.KEY, TweetType.HomeTweet.getNumericType());
        	
        	name = args.getString(ITransKey.KEY1);
        	fopenid = args.getString(ITransKey.KEY2);
        	
        	htid = args.getString(ITransKey.KEY1);
        	httext = args.getString(ITransKey.KEY2);
        	
        	if (TweetType.AroundTweet.getNumericType() == tweetType) {
            	longitude = args.getString(ITransKey.KEY1);
            	latitude = args.getString(ITransKey.KEY2);
        	}
        	
        	if (TweetType.AreaTweet.getNumericType() == tweetType) {
            	country = args.getInt(ITransKey.KEY1, 0);
            	province = args.getInt(ITransKey.KEY2, 0);
            	city = args.getInt(ITransKey.KEY3, 0);
        	}
        }
        
        if (TextUtils.isEmpty(name))
        	name = "";
        if (TextUtils.isEmpty(fopenid))
        	fopenid = "";
        
        if (TextUtils.isEmpty(htid))
        	htid = "";
        if (TextUtils.isEmpty(httext))
        	httext = "";
        
        if (TextUtils.isEmpty(longitude))
        	longitude = "";
        if (TextUtils.isEmpty(latitude))
        	latitude = "";
        
        array = new ArrayList<Tweet>();
        adapter = new TweetListAdapter(getActivity(), array, this);
        
        setRetainInstance(true);
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
	public void onDestroy() {
        if (imageLoadListener != null) {
            MyApplication.imageCache.unregisterOnImageLoadListener(imageLoadListener);
            imageLoadListener = null;
        }
		super.onDestroy();
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	String cacheName = "";
    	if (TweetType.MentionsTweet.getNumericType() == tweetType) {
    		cacheName = MessageDigestUtil.getInstance().hash(Constant.SELFINFO_CACHE_FILE);
    	} else if (TweetType.HomeTweet.getNumericType() == tweetType) {
    		cacheName = MessageDigestUtil.getInstance().hash(Constant.HOMETWEET_CACHE_FILE);
    	} else if (TweetType.PublicTweet.getNumericType() == tweetType) {
    		cacheName = MessageDigestUtil.getInstance().hash(Constant.PUBLICTWEET_CACHE_FILE);
    	} else if (TweetType.SpecialTweet.getNumericType() == tweetType) {
    		cacheName = MessageDigestUtil.getInstance().hash(Constant.SPECIALTWEET_CACHE_FILE);
    	} else if (TweetType.HotTweet.getNumericType() == tweetType) {
    		cacheName = MessageDigestUtil.getInstance().hash(Constant.HOTTWEET_CACHE_FILE);
    	} else if (TweetType.AroundTweet.getNumericType() == tweetType) {
    		cacheName = MessageDigestUtil.getInstance().hash(Constant.AROUNDTWEET_CACHE_FILE);
    	} else if (TweetType.MyTweet.getNumericType() == tweetType) {
    		cacheName = MessageDigestUtil.getInstance().hash(Constant.MYTWEET_CACHE_FILE);
    	} else if (TweetType.CommentTweet.getNumericType() == tweetType) {
    		cacheName = MessageDigestUtil.getInstance().hash(Constant.COMMENT_CACHE_FILE);
    	}
    	if (!TextUtils.isEmpty(cacheName)) {
	        File cacheBase = null;
	        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
	            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
	                    + Constant.cacheDir + Constant.dataFolder);
	        else
	            cacheBase = new File(getActivity().getCacheDir() + File.separator + Constant.dataFolder);
	        File cacheFile = new File(cacheBase.getAbsolutePath() + File.separator +
	                MessageDigestUtil.getInstance().hash(TencentWeibo.getInstance().getOpenid())
	                + File.separator + cacheName);
	
	        if (cacheFile.exists() && cacheFile.isFile() && cacheFile.canRead()) {
	        	dataInitialized = true;
	            refreshTask = new loadCacheTask().execute(cacheFile);
	        }
    	} else
    		dataInitialized = false;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
    	listView.setFastScrollEnabled(Setting.fastScrollEnabled);
    	adapter.notifyDataSetChanged();
        if (!dataInitialized && !autoLoad && NetUtils.getType(getActivity()) != NetUtils.NO_NET) {
            autoLoad = true;
            listView.post(new Runnable() {
    
                @Override
                public void run() {
                    ((PullToRefreshListView) listView).clickRefresh();
                }
    
            });
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.pull2refresh_list_view, null);
        initViews((ViewGroup) layout);
        themeChanged();
        return layout;
    }
    

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.getTag() != null && view.getTag() instanceof TweetListAdapter.ViewHolder) {
            TweetListAdapter.ViewHolder holder = (TweetListAdapter.ViewHolder) view.getTag();
            Tweet tweet = (Tweet) holder.object;
            
            Intent intent = new Intent(getActivity(), TweetActivity.class);
            intent.putExtra(ITransKey.KEY, tweet);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.home:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(ITransKey.KEY, 0);
                startActivity(intent);
                break;
            case R.id.main_list_foot:
                refresh(true);
                break;
            case R.id.tweet_upload_pic1:
            case R.id.tweet_upload_pic2:
            	if (v.getParent() != null && ((View)v.getParent()).getTag() != null && ((View)v.getParent()).getTag() instanceof Tweet && Setting.readMode == ReadMode.Image) {
            		Tweet tweet = (Tweet) ((View)v.getParent()).getTag();
            		if (tweet != null && tweet.image != null && !tweet.image.isEmpty() && !TextUtils.isEmpty(tweet.image.get(0))) {
            			imageContainer.setVisibility(View.VISIBLE);
            			
                        imageID = MyApplication.imageCache.getNewID();
                        if (imageLoadListener == null) {
                        	imageLoadListener = new OnImageLoadListener() {

                                @Override
                                public void onImageLoaded(long id, Uri imageUri, Drawable image) {
                                	if (id == imageID && image != null) {
                                		loadProgress.setVisibility(View.GONE);
                                		detailImage.setImageDrawable(image);
                                		detailImage.setVisibility(View.VISIBLE);
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
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && imageContainer.getVisibility() == View.VISIBLE) {
            imageContainer.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
    @Override
    public void initViews(ViewGroup container) {
        listView = (ListView) container.findViewById(R.id.listview);
        listFooter = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        listFooterProgressBar = (ProgressBar) listFooter.findViewById(R.id.loading_progress);
        listFooterText = (TextView) listFooter.findViewById(R.id.progress_loading_text);
        listView.addFooterView(listFooter);
        listFooterContainer = (View) listFooter.findViewById(R.id.main_list_foot);
        listFooterContainer.setOnClickListener(this);
        Drawable drawable = getResources().getDrawable(R.drawable.portrait_small);
        int iconWidth = drawable.getIntrinsicWidth();
        int iconHeight = drawable.getIntrinsicHeight();
        listView.setAdapter(new ImageLoaderAdapter(getActivity(), adapter, MyApplication.imageCache,
                new int[] {
                    R.id.item_image, R.id.tweet_upload_pic1, R.id.tweet_upload_pic2
                }, iconWidth, iconHeight, ImageLoaderAdapter.UNIT_PX));

        // Set a listener to be invoked when the list should be refreshed.
        ((PullToRefreshListView) listView).setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                refresh(false);
            }
        });
        listView.setOnItemClickListener(this);
        
        View container2 = LayoutInflater.from(getActivity()).inflate(R.layout.tweet_detail_image_view, null);
        ViewGroup decorView = (ViewGroup) getActivity().getWindow().peekDecorView();
        decorView.addView(container2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        imageContainer = container2.findViewById(R.id.mask_container);
        imageContainer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				imageContainer.setVisibility(View.GONE);
				return false;
			}
        	
        });
        detailImage = (ImageView) container2.findViewById(R.id.tweet_upload_pic);
        loadProgress = (ProgressBar) container2.findViewById(R.id.loading_progress);
    }
    
    public void refresh(boolean invokedByListFooter) {
        if (tweet == null || !invokedByListFooter || (invokedByListFooter && shouldLoadMore())) {
            final UserTask<Object, Void, Boolean> task = refreshTask;
            if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                return;
            }
            this.invokedByListFooter = invokedByListFooter;
            
            int type = 0;
            int contenttype = 0;
            int reqnum = REQNUM;
            pageflag = (tweet == null || array.isEmpty()) ? 0 : (invokedByListFooter ? 1 : 0);
            String pagetime = (tweet == null || array.isEmpty()) ? "0" : (invokedByListFooter ? array.get(array.size() - 1).timestamp : "0");
            String lastid = (tweet == null || array.isEmpty()) ? "0" : (invokedByListFooter ? array.get(array.size() - 1).id : "0");
            long pos = (tweet == null || array.isEmpty()) ? 0 : (tweet == null ? 0 : 0);
            String pageinfo = (tweet == null) ? "" : tweet.pageinfo;
            
        	if (TweetType.HomeTweet.getNumericType() == tweetType) {
        	    if (!invokedByListFooter)
        	        dataInitialized = false;
        	    refreshTask = new HomeTimelineTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pageflag, pagetime, reqnum, type, contenttype);
        	} else if (TweetType.MyTweet.getNumericType() == tweetType) {
                if (!invokedByListFooter)
                    dataInitialized = false;
        	    refreshTask = new BroadcastTimelineTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pageflag, pagetime, reqnum, lastid, type, contenttype);
        	} else if (TweetType.PublicTweet.getNumericType() == tweetType) {
        	    refreshTask = new PublicTimelineTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pos, reqnum);
        	} else if (TweetType.UserTweet.getNumericType() == tweetType) {
                if (!invokedByListFooter)
                    dataInitialized = false;
        	    refreshTask = new UserTimelineTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pageflag, pagetime, reqnum, lastid, name, fopenid, type, contenttype);
        	} else if (TweetType.HuatiTweet.getNumericType() == tweetType) {
                if (!invokedByListFooter)
                    dataInitialized = false;
        		int flag = 0;
        		refreshTask = new HTTimelineTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(reqnum, lastid, pagetime, pageflag, flag, httext, "0", type, contenttype);
        	} else if (TweetType.MentionsTweet.getNumericType() == tweetType) {
                if (!invokedByListFooter)
                    dataInitialized = false;
        	    refreshTask = new MentionsTimelineTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pageflag, pagetime, reqnum, lastid, type, contenttype);
        	} else if (TweetType.SpecialTweet.getNumericType() == tweetType) {
                if (!invokedByListFooter)
                    dataInitialized = false;
        	    refreshTask = new SpecialTimelineTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pageflag, pagetime, reqnum, lastid, type, contenttype);
        	} else if (TweetType.HotTweet.getNumericType() == tweetType) {
        	    refreshTask = new HotBroadcastTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(reqnum, pos, type);
        	} else if (TweetType.AroundTweet.getNumericType() == tweetType) {
        	    longitude = Float.toString(Constant.latitute);
        	    latitude = Float.toString(Constant.lontitue);
        	    refreshTask = new GetAroundNewTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(longitude, latitude, pageinfo, reqnum);
        	} else if (TweetType.SearchTweet.getNumericType() == tweetType) {
        		// TODO
        	    refreshTask = new SearchTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute();
        	} else if (TweetType.AreaTweet.getNumericType() == tweetType) {
        	    refreshTask = new AreaTimelineTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pos, reqnum, country, province, city);
        	} else if (TweetType.MyFavoriteTweet.getNumericType() == tweetType) {
                if (!invokedByListFooter)
                    dataInitialized = false;
        	    refreshTask = new ListTTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pageflag, pagetime, reqnum, lastid);
        	} else if (TweetType.UserFavoriteTweet.getNumericType() == tweetType) {
                if (!invokedByListFooter)
                    dataInitialized = false;
        	    refreshTask = new ListTTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pageflag, pagetime, reqnum, lastid);
        	} else if (TweetType.CommentTweet.getNumericType() == tweetType) {
                if (!invokedByListFooter)
                    dataInitialized = false;
        	    refreshTask = new ListTTask(invokedByListFooter ? listFooterLoadListener : loadListener,
	                    resultListener).execute(pageflag, pagetime, reqnum, lastid);
        	}
        } else {
            if (listView != null)
                ((PullToRefreshListView) listView).onRefreshComplete();
        }
    }

    private IResultListener resultListener = new IResultListener() {

        @Override
        public void onSuccess(final Entity<TObject> data) {
            if (data != null && data.data != null && data.data instanceof HotBroadcast) {
                if (pageflag == 0 || pageflag == 2) {
                    pageflag = 1;
                	String cacheName = "";
                	if (TweetType.MentionsTweet.getNumericType() == tweetType) {
                		cacheName = MessageDigestUtil.getInstance().hash(Constant.SELFINFO_CACHE_FILE);
                	} else if (TweetType.HomeTweet.getNumericType() == tweetType) {
                		cacheName = MessageDigestUtil.getInstance().hash(Constant.HOMETWEET_CACHE_FILE);
                	} else if (TweetType.PublicTweet.getNumericType() == tweetType) {
                		cacheName = MessageDigestUtil.getInstance().hash(Constant.PUBLICTWEET_CACHE_FILE);
                	} else if (TweetType.SpecialTweet.getNumericType() == tweetType) {
                		cacheName = MessageDigestUtil.getInstance().hash(Constant.SPECIALTWEET_CACHE_FILE);
                	} else if (TweetType.HotTweet.getNumericType() == tweetType) {
                		cacheName = MessageDigestUtil.getInstance().hash(Constant.HOTTWEET_CACHE_FILE);
                	} else if (TweetType.AroundTweet.getNumericType() == tweetType) {
                		cacheName = MessageDigestUtil.getInstance().hash(Constant.AROUNDTWEET_CACHE_FILE);
                	} else if (TweetType.MyTweet.getNumericType() == tweetType) {
                		cacheName = MessageDigestUtil.getInstance().hash(Constant.MYTWEET_CACHE_FILE);
                	} else if (TweetType.CommentTweet.getNumericType() == tweetType) {
                		cacheName = MessageDigestUtil.getInstance().hash(Constant.COMMENT_CACHE_FILE);
                	}
                	if (!TextUtils.isEmpty(cacheName)) {
    	                if (!TextUtils.isEmpty(data.response)) {
    	                    File cacheBase = null;
    	                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    	                        cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.cacheDir + Constant.dataFolder);
    	                    else
    	                        cacheBase = new File(getActivity().getCacheDir() + File.separator + Constant.dataFolder);
    	                    final File cacheFile = new File(cacheBase.getAbsolutePath()
    	                            + File.separator +
    	                            MessageDigestUtil.getInstance().hash(TencentWeibo.getInstance().getOpenid()) + File.separator
    	                            + cacheName);
    	                    new Thread() {

                                @Override
                                public void run() {
                                    Entity.cache2file(cacheFile, data.response);
                                    super.run();
                                }
    	                        
    	                    }.start();
    	                }
                	}
                }
                tweet = (HotBroadcast) data.data;
            }
            if (tweet != null && tweet.info != null) {
                if (!dataInitialized) {
                    dataInitialized = true;
                    array.clear();
                }
                if (invokedByListFooter) {
                    for (Tweet info : tweet.info)
                        array.add(info);
                } else {
                    for (int index = tweet.info.size() - 1; index >= 0; index --) {
                        Tweet info = tweet.info.get(index);
                        array.add(0, info);
                    }
                }
            }
            listView.post(new Runnable() {

                @Override
                public void run() {
                    // Call onRefreshComplete when the list has been refreshed.
                    if (listView != null)
                        ((PullToRefreshListView) listView).onRefreshComplete();
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                }

            });
        }

        @Override
        public void onFail(Entity<TObject> data) {
            listView.post(new Runnable() {

                @Override
                public void run() {
                    // Call onRefreshComplete when the list has been refreshed.
                    if (listView != null)
                        ((PullToRefreshListView) listView).onRefreshComplete();
                    ToastUtils.show(listView.getContext(), R.string.communicating_failed,
                            Toast.LENGTH_SHORT);
                }

            });
        }

    };
    
    private boolean shouldLoadMore() {
        if (tweet != null) {
            if (TweetType.AroundTweet.getNumericType() == tweetType) {
                if (tweet.hasnext == 0)
                    return false;
                return true;
            }
            
            return !(tweet.hasnext == 1);
        }
        return true;
    }

    private ILoadListener listFooterLoadListener = new ILoadListener() {

        @Override
        public void onStart(TextView v) {
            listFooterProgressBar.setVisibility(View.VISIBLE);
            listFooterText.setText(R.string.progress_loading_text);
        }

        @Override
        public void onEnd() {
            listFooterProgressBar.setVisibility(View.GONE);
            listFooterText.setText(R.string.more);
            if (!shouldLoadMore())
                listFooterContainer.setVisibility(View.GONE);
            else
                listFooterContainer.setVisibility(View.VISIBLE);
        }

    };

    private ILoadListener loadListener = new ILoadListener() {

        @Override
        public void onStart(TextView v) {
            listFooterContainer.setVisibility(View.GONE);
            if (v != null)
                v.setText(R.string.progress_loading_text);
        }

        @Override
        public void onEnd() {
            listFooterContainer.setVisibility(View.VISIBLE);
            if (!shouldLoadMore())
                listFooterContainer.setVisibility(View.GONE);
            else
                listFooterContainer.setVisibility(View.VISIBLE);
        }

    };
    
    private class loadCacheTask extends UserTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            File input = (File) params[0];
            tweet = new HotBroadcast();
            if (!Entity.parsefile(input, tweet)) {
                tweet = null;
            }
            return tweet == null ? false : true;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            if (getActivity() != null)
        	getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
		            if (result && adapter != null) {
		                if (tweet.info != null) {
		                    for (Tweet info : tweet.info)
		                        array.add(info);
		                }
		                tweet.hasnext = 0;
		                adapter.notifyDataSetChanged();
		            }
				}
        		
        	});
            super.onPostExecute(result);
        }
        
    }
}
