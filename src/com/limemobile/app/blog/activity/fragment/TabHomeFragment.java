
package com.limemobile.app.blog.activity.fragment;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;
import net.londatiga.android.QuickAction.OnDismissListener;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.constant.ITransKey;

public class TabHomeFragment extends ThemeFragment implements OnClickListener {
    private static final String SELECTED_TWEET_TYPE = "selected_tweet_type";
    
    private ThemeFragment[] fragments = new ThemeFragment[6];
    private String[] fragmentTitles = new String[] {
    	"全部", "我的微博", "周边", "热门微博", "广场", "特别关注"
    };
	
    private ImageView newTweet;
    private ImageView camera;
    private View titleBar;
    private TextView title;
    private int tweetType;
    
    private QuickAction tweetAction;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        tweetType = sp.getInt(SELECTED_TWEET_TYPE, 0);
        
        fragments[0] = new TweetListFragment();
        Bundle extras = new Bundle();
        extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.HomeTweet.getNumericType());
        fragments[0].setArguments(extras);
        
        fragments[1] = new TweetListFragment();
        extras = new Bundle();
        extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.MyTweet.getNumericType());
        fragments[1].setArguments(extras);
        
        fragments[2] = new TweetListFragment();
        extras = new Bundle();
        extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.AroundTweet.getNumericType());
        extras.putString(ITransKey.KEY1, Float.toString(Constant.latitute));
        extras.putString(ITransKey.KEY2, Float.toString(Constant.lontitue));
        fragments[2].setArguments(extras);
        
        fragments[3] = new TweetListFragment();
        extras = new Bundle();
        extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.HotTweet.getNumericType());
        fragments[3].setArguments(extras);
        
        fragments[4] = new TweetListFragment();
        extras = new Bundle();
        extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.PublicTweet.getNumericType());
        fragments[4].setArguments(extras);
        
        fragments[5] = new TweetListFragment();
        extras = new Bundle();
        extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.SpecialTweet.getNumericType());
        fragments[5].setArguments(extras);
        
        if (savedInstanceState == null) {
            // First-time init; create fragment to embed in activity.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            for (Fragment fragment : fragments) {
            	ft.add(R.id.main_container, fragment);
            	ft.hide(fragment);
            }
            ft.commit();
        }
        
        tweetAction = new QuickAction(getActivity());
        tweetAction.addActionItem(new ActionItem(0, fragmentTitles[0]));
        tweetAction.addActionItem(new ActionItem(1, fragmentTitles[1]));
        tweetAction.addActionItem(new ActionItem(2, fragmentTitles[2]));
        tweetAction.addActionItem(new ActionItem(3, fragmentTitles[3]));
        tweetAction.addActionItem(new ActionItem(4, fragmentTitles[4]));
        tweetAction.addActionItem(new ActionItem(5, fragmentTitles[5]));
        
        tweetAction.setOnActionItemClickListener(new OnActionItemClickListener() {

			@Override
			public void onItemClick(QuickAction source, final int pos, int actionId) {
			    new Thread() {

                    @Override
                    public void run() {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        sp.edit().putInt(SELECTED_TWEET_TYPE, pos).commit();
                        super.run();
                    }
			        
			    }.start();
			    changeFragment(pos);
			}
        	
        });
        tweetAction.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				Resources res = Theme.getInstance().getContext(getActivity()).getResources();
				title.setCompoundDrawablesWithIntrinsicBounds(null, null, res.getDrawable(R.drawable.arrow_down), null);
			}
        	
        });
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_tab_home, null);
        initViews((ViewGroup) layout);
        themeChanged();
        return layout;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            
            boolean responseBackKey = true;
            for (ThemeFragment fragment : fragments) {
                if (fragment.onKeyDown(keyCode, event))
                    responseBackKey = false;
            }
            if (!responseBackKey)
                return true;
        }
        return super.onKeyDown(keyCode, event);
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
    public void themeChanged() {
        if (isAdded()) {
        	Resources res = Theme.getInstance().getContext(getActivity()).getResources();
        	
            titleBar.setBackgroundResource(R.drawable.title_bar_bg);
	        newTweet.setImageDrawable(res.getDrawable(R.drawable.title_new));
	        camera.setImageDrawable(res.getDrawable(R.drawable.title_camera));

            title.setTextColor(res.getColor(R.color.tab_text_color));
            float titleTextSize = res.getDimension(R.dimen.title_text_size)
                    / res.getDisplayMetrics().density;
            title.setTextSize(titleTextSize);  
        }
    }

    @Override
    public void initViews(ViewGroup container) {
        newTweet = (ImageView) container.findViewById(R.id.new_tweet);
        newTweet.setOnClickListener(this);
        camera = (ImageView) container.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        titleBar = container.findViewById(R.id.main_titlebar);
        title = (TextView) container.findViewById(R.id.title);
        title.setOnClickListener(this);
        title.setText(fragmentTitles[tweetType]);
        tweetAction.setItemSelected(tweetType);
        
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(fragments[tweetType]);
        ft.commit();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title:
			Resources res = Theme.getInstance().getContext(getActivity()).getResources();
			title.setCompoundDrawablesWithIntrinsicBounds(null, null, res.getDrawable(R.drawable.arrow_up), null);
			tweetAction.show(v);
			break;
		case R.id.new_tweet:
			break;
		case R.id.camera:
			break;
		}
	}
	
	private void changeFragment(int type) {
		if (tweetType != type) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.alpha_fade_in, R.anim.alpha_fade_out);
            ft.hide(fragments[tweetType]);
            ft.show(fragments[type]);
            ft.commit();
			tweetType = type;
			title.setText(fragmentTitles[tweetType]);
		}
	}
}
