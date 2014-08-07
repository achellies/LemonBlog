
package com.limemobile.app.blog.activity.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.AroundPeopleActivity;
import com.limemobile.app.blog.activity.HallOfFrameActivity;
import com.limemobile.app.blog.activity.HotTopicActivity;
import com.limemobile.app.blog.activity.MayKnowPersonActivity;
import com.limemobile.app.blog.activity.TweetListActivity;
import com.limemobile.app.blog.activity.sns.BlowActivity;
import com.limemobile.app.blog.activity.sns.ShakeActivity;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.utils.UIHelper;

public class SquareSubFragment extends ThemeFragment implements OnClickListener {
	private TextView hotTweet;
	private TextView hotHuati;
	private TextView aroundPeople;
	private TextView aroundTweet;
	private TextView vip;
	private TextView casualLookAt;
	
    private TextView knowPerson;
    private TextView weather;
    private TextView wangyiWeibo;
    private TextView sinaWeibo;
    private TextView sohuWeibo;
    private TextView tencentWeibo;
    
    private TextView shake;
    private TextView blowing;
    
    private int subSquareType = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle args = getArguments();
        if (args != null) {
        	subSquareType = args.getInt(ITransKey.KEY, 0);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = R.layout.fragment_tab_square_sub_fragment1;
        switch (subSquareType) {
        case 1:
        	layoutId = R.layout.fragment_tab_square_sub_fragment2;
        	break;
        case 2:
        	layoutId = R.layout.fragment_tab_square_sub_fragment3;
        	break;
        default:
        	break;
        }
        View layout = inflater.inflate(layoutId, null);
        initViews((ViewGroup) layout);
        themeChanged();
        return layout;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.square_hot_tweet:
			Intent intent = new Intent(getActivity(), TweetListActivity.class);
            Bundle extras = new Bundle();
            extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.HotTweet.getNumericType());
            intent.putExtras(extras);
            intent.putExtra(ITransKey.KEY5, getActivity().getString(R.string.square_hot_tweet));
            startActivity(intent);
			break;
		case R.id.square_hot_huati:
		    intent = new Intent(getActivity(), HotTopicActivity.class);
		    startActivity(intent);
			break;
		case R.id.square_around_people:
            intent = new Intent(getActivity(), AroundPeopleActivity.class);
            startActivity(intent);
			break;
		case R.id.square_around_tweet:
			intent = new Intent(getActivity(), TweetListActivity.class);
            extras = new Bundle();
            extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.AroundTweet.getNumericType());
            extras.putString(ITransKey.KEY1, Float.toString(Constant.latitute));
            extras.putString(ITransKey.KEY2, Float.toString(Constant.lontitue));
            intent.putExtras(extras);
            intent.putExtra(ITransKey.KEY5, getActivity().getString(R.string.square_around_tweet));
            startActivity(intent);
			break;
		case R.id.square_vip:
            intent = new Intent(getActivity(), HallOfFrameActivity.class);
            startActivity(intent);
			break;
		case R.id.square_casual_look_at:
			intent = new Intent(getActivity(), TweetListActivity.class);
            extras = new Bundle();
            extras.putInt(ITransKey.KEY, TweetListFragment.TweetType.PublicTweet.getNumericType());
            intent.putExtra(ITransKey.KEY5, getActivity().getString(R.string.square_casual_look_at));
            intent.putExtras(extras);
            startActivity(intent);
			break;
        case R.id.square_guess_you_like:
            intent = new Intent(getActivity(), MayKnowPersonActivity.class);
            startActivity(intent);
            break;
        case R.id.square_weather:
            break;
        case R.id.square_163_weibo:
            UIHelper.openBrowser(getActivity(), "http://t.163.com/");
            break;
        case R.id.square_sina_weibo:
            UIHelper.openBrowser(getActivity(), "http://weibo.com/");
            break;
        case R.id.square_sohu_weibo:
            UIHelper.openBrowser(getActivity(), "http://t.sohu.com/");
            break;
        case R.id.square_tencent_weibo:
            UIHelper.openBrowser(getActivity(), "http://t.qq.com/");
            break;
        case R.id.square_shake:
            intent = new Intent(getActivity(), ShakeActivity.class);
            startActivity(intent);
            break;
        case R.id.square_blowing:
            intent = new Intent(getActivity(), BlowActivity.class);
            startActivity(intent);
            break;
		}
	}

	@Override
    public void themeChanged() {
		if (isAdded()) {
			Resources res = Theme.getInstance().getContext(getActivity()).getResources();
			int textColor = res.getColor(R.color.black);
			
			if (subSquareType == 0) {
				hotTweet.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.square_hot_tweet), null, null);
				hotTweet.setTextColor(textColor);
				
				hotHuati.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.square_hot_huati), null, null);
				hotHuati.setTextColor(textColor);
	            
				aroundPeople.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.square_around_people), null, null);
				aroundPeople.setTextColor(textColor);
	            
				aroundTweet.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.square_hot_tweet), null, null);
				aroundTweet.setTextColor(textColor);
	            
				vip.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.square_vip), null, null);
				vip.setTextColor(textColor);
	            
				casualLookAt.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.square_casual_look_at), null, null);
				casualLookAt.setTextColor(textColor);
			}
            
			if (subSquareType == 1) {
	            knowPerson.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.square_guess_you_like), null, null);
	            knowPerson.setTextColor(textColor);
	            
	            weather.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.square_weather), null, null);
	            weather.setTextColor(textColor);
	            
	            wangyiWeibo.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.icon_163_weibo), null, null);
	            wangyiWeibo.setTextColor(textColor);
	            
	            sinaWeibo.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.icon_sina_weibo), null, null);
	            sinaWeibo.setTextColor(textColor);
	            
	            sohuWeibo.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.icon_sohu_weibo), null, null);
	            sohuWeibo.setTextColor(textColor);
	            
	            tencentWeibo.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.icon_qq_weibo), null, null);
	            tencentWeibo.setTextColor(textColor);
			}
		}
    }

    @Override
    public void initViews(ViewGroup container) {
    	if (subSquareType == 0) {
	    	hotTweet = (TextView) container.findViewById(R.id.square_hot_tweet);
	    	hotTweet.setOnClickListener(this);
	    	hotHuati = (TextView) container.findViewById(R.id.square_hot_huati);
	    	hotHuati.setOnClickListener(this);
	    	aroundPeople = (TextView) container.findViewById(R.id.square_around_people);
	    	aroundPeople.setOnClickListener(this);
	    	aroundTweet = (TextView) container.findViewById(R.id.square_around_tweet);
	    	aroundTweet.setOnClickListener(this);
	    	vip = (TextView) container.findViewById(R.id.square_vip);
	    	vip.setOnClickListener(this);
	    	casualLookAt = (TextView) container.findViewById(R.id.square_casual_look_at);
	    	casualLookAt.setOnClickListener(this);
    	}
    	
    	if (subSquareType == 1) {
	        knowPerson = (TextView) container.findViewById(R.id.square_guess_you_like);
	        knowPerson.setOnClickListener(this);
	        weather = (TextView) container.findViewById(R.id.square_weather);
	        weather.setOnClickListener(this);
	        wangyiWeibo = (TextView) container.findViewById(R.id.square_163_weibo);
	        wangyiWeibo.setOnClickListener(this);
	        sinaWeibo = (TextView) container.findViewById(R.id.square_sina_weibo);
	        sinaWeibo.setOnClickListener(this);
	        sohuWeibo = (TextView) container.findViewById(R.id.square_sohu_weibo);
	        sohuWeibo.setOnClickListener(this);
	        tencentWeibo = (TextView) container.findViewById(R.id.square_tencent_weibo);
	        tencentWeibo.setOnClickListener(this);
    	}
    	
    	if (subSquareType == 2) {
    	    shake = (TextView) container.findViewById(R.id.square_shake);
    	    shake.setOnClickListener(this);
    	    blowing = (TextView) container.findViewById(R.id.square_blowing);
    	    blowing.setOnClickListener(this);
    	}
    }
}
