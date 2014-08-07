
package com.limemobile.app.blog.activity.fragment;

import java.lang.reflect.Field;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.MyApplication;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.widget.FixedSpeedScroller;
import com.viewpagerindicator.CirclePageIndicator;

public class TabSquareFragment extends ThemeFragment implements OnClickListener, OnEditorActionListener, TextWatcher {
    private CirclePageIndicator viewPagerIndicator;
    private ViewPager viewPager;
    private FragmentStatePagerAdapter pageAdapter;
    private ThemeFragment[] fragments = new ThemeFragment[3];
    
    private ImageView newTweet;
    private ImageView camera;
    private View titleBar;
    private TextView title;
    private AutoCompleteTextView searchInput;
    private Button searchClear;
    private Button search;
    private Button searchVoice;
    private RadioButton searchByTweet;
    private RadioButton searchByUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments[0] = new SquareSubFragment();
        Bundle extras = new Bundle();
        extras.putInt(ITransKey.KEY, 0);
        fragments[0].setArguments(extras);
        
        fragments[1] = new SquareSubFragment();
        extras = new Bundle();
        extras.putInt(ITransKey.KEY, 1);
        fragments[1].setArguments(extras);
        
        fragments[2] = new SquareSubFragment();
        extras = new Bundle();
        extras.putInt(ITransKey.KEY, 2);
        fragments[2].setArguments(extras);
        
        pageAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {

            @Override
            public Fragment getItem(int arg0) {
                return fragments[arg0];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "";
            }
        };
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View layout = inflater.inflate(R.layout.fragment_tab_square, null);
        initViews((ViewGroup) layout);
        themeChanged();
        new setAdapterTask().executeOnExecutor(MyApplication.uiExecutor);
        return layout;
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
	        
            title.setText(res.getString(R.string.tab_square));
            title.setTextColor(res.getColor(R.color.tab_text_color));
            float titleTextSize = res.getDimension(R.dimen.title_text_size)
                    / res.getDisplayMetrics().density;
            title.setTextSize(titleTextSize);
            
	        searchInput.setBackgroundDrawable(res.getDrawable(R.drawable.search_edittext_bg));
	        searchInput.setPadding((int)res.getDimension(R.dimen.searchbar_paddingleft), 0, (int)res.getDimension(R.dimen.searchbar_paddingright), 0);
	        
	        searchClear.setBackgroundDrawable(res.getDrawable(R.drawable.search_button_clear));
	        search.setBackgroundDrawable(res.getDrawable(R.drawable.search_button_bg));
	        searchVoice.setBackgroundDrawable(res.getDrawable(R.drawable.search_button_voice_bg));        
	        
	        searchByTweet.setBackgroundDrawable(res.getDrawable(R.drawable.search_radio_weibo));
	        searchByTweet.setPadding((int)res.getDimension(R.dimen.searchbar_radiobutton_padding), 0, 0, 0);
	        searchByTweet.setCompoundDrawablesWithIntrinsicBounds(res.getDrawable(R.drawable.search_radio_1), null, null, null);
	        searchByUser.setBackgroundDrawable(res.getDrawable(R.drawable.search_radio_user));
	        searchByUser.setPadding((int)res.getDimension(R.dimen.searchbar_radiobutton_padding_user), 0, 0, 0);
	        searchByUser.setCompoundDrawablesWithIntrinsicBounds(res.getDrawable(R.drawable.search_radio_2), null, null, null);
        }
    }

	@Override
    public void initViews(ViewGroup container) {		
        viewPager = (ViewPager) container.findViewById(R.id.viewpager);
        viewPagerIndicator = (CirclePageIndicator) container.findViewById(R.id.viewpager_indicator);
        viewPager.setSaveEnabled(true);
        
        Drawable defaultDrawable = container.getContext().getResources().getDrawable(R.drawable.page_gray);
        Drawable selectedDrawable = container.getContext().getResources().getDrawable(R.drawable.page_black);
        viewPagerIndicator.setDrawable(defaultDrawable, selectedDrawable);
        
        newTweet = (ImageView) container.findViewById(R.id.new_tweet);
        newTweet.setOnClickListener(this);
        camera = (ImageView) container.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        titleBar = container.findViewById(R.id.main_titlebar);
        title = (TextView) container.findViewById(R.id.title);
        searchInput = (AutoCompleteTextView) container.findViewById(R.id.search_edittext);
        searchInput.setOnEditorActionListener(this);
        searchInput.addTextChangedListener(this);
        searchClear = (Button) container.findViewById(R.id.search_button_clear);
        searchClear.setOnClickListener(this);
        search = (Button) container.findViewById(R.id.search_button);
        search.setOnClickListener(this);
        searchVoice = (Button) container.findViewById(R.id.search_voice_button);
        searchVoice.setOnClickListener(this);
        searchByTweet = (RadioButton) container.findViewById(R.id.search_radiobutton_weibo);
        searchByUser = (RadioButton) container.findViewById(R.id.search_radiobutton_user);        
        
        searchClear.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        searchByTweet.setChecked(true);
        
        viewPager.setOffscreenPageLimit(fragments.length);
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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_tweet:
			break;
		case R.id.camera:
			break;
		case R.id.search_button_clear:
			break;
		case R.id.search_button:
			break;
		case R.id.search_voice_button:
			break;
		}
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (v.equals(searchInput) && EditorInfo.IME_ACTION_DONE	== actionId) {
			if (!TextUtils.isEmpty(searchInput.getText().toString())) {
				searchVoice.setVisibility(View.GONE);
				search.setVisibility(View.VISIBLE);
				searchClear.setVisibility(View.VISIBLE);
			} else {
				searchVoice.setVisibility(View.VISIBLE);
				search.setVisibility(View.GONE);
				searchClear.setVisibility(View.GONE);
			}
		}
		return false;
	}
    
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before,
			int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (TextUtils.isEmpty(s.toString())) {
			searchVoice.setVisibility(View.VISIBLE);
			search.setVisibility(View.GONE);
			searchClear.setVisibility(View.GONE);
		}
	}
	
    private class setAdapterTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            viewPager.setAdapter(pageAdapter);
            viewPagerIndicator.setViewPager(viewPager);
        }
    }
}
