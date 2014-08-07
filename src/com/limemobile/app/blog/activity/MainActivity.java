
package com.limemobile.app.blog.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.fragment.TabHomeFragment;
import com.limemobile.app.blog.activity.fragment.TabMessageFragment;
import com.limemobile.app.blog.activity.fragment.TabMoreFragment;
import com.limemobile.app.blog.activity.fragment.TabSelfInfoFragment;
import com.limemobile.app.blog.activity.fragment.TabSquareFragment;
import com.limemobile.app.blog.activity.fragment.ThemeFragment;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.widget.FixedSpeedScroller;
import com.limemobile.app.lbs.LocateTask;
import com.limemobile.app.lbs.LocateTask.OnLocationListener;
import com.limemobile.app.utils.NetUtils;
import com.limemobile.app.utils.ToastUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.lang.reflect.Field;

public class MainActivity extends ThemeActivity implements OnClickListener {

    /**
     * 底部tab工具栏
     */
    private TabPageIndicator tabPageIndicator;
    private ViewPager viewPager;
    private View tabContainer;
    private LinearLayout tab;
    private TextView tabHome;
    private TextView tabMessage;
    private TextView tabSelfInfo;
    private TextView tabSquare;
    private TextView tabMore;
    private ImageView tabSelectedIndicator;
    private FragmentStatePagerAdapter pageAdapter;

    private String[] tabTitles = new String[5];
    private ThemeFragment[] fragments = new ThemeFragment[5];
    
    private LocateTask locate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        
        locate = new LocateTask(this);
        locate.setLocationListener(new OnLocationListener() {

            @Override
            public void onError() {
            }

            @Override
            public void onSuccess(double latitude, double longitude, String location) {
                if (!TextUtils.isEmpty(location)) {
                    Constant.latitute = (float) latitude;
                    Constant.lontitue = (float) longitude;
                }
            }
            
        });
        locate.getLocation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isFinishing() && locate != null) {
            locate.uninit();
            locate = null;
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            int tabIndex = intent.getIntExtra(ITransKey.KEY, 0);
            tabPageIndicator.setCurrentItem(tabIndex);
        }
        super.onNewIntent(intent);
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
    	    
    		new AlertDialog.Builder(this)
    		.setTitle(R.string.sure_to_exit)
    		.setIcon(R.drawable.moreitems_about_icon)
    		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    				finish();
    			}
    		})
    		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    			}
    		})
    		.show();
    		return true;
    	}
		return super.onKeyDown(keyCode, event);
	}

	@Override
    public void initViews() {
        tabContainer = (View) findViewById(R.id.main_toolbar);
        tab = (LinearLayout) findViewById(R.id.tab_bar);
        tabHome = (TextView) findViewById(R.id.tab_home);
        tabMessage = (TextView) findViewById(R.id.tab_message);
        tabSelfInfo = (TextView) findViewById(R.id.tab_selfinfo);
        tabSquare = (TextView) findViewById(R.id.tab_square);
        tabMore = (TextView) findViewById(R.id.tab_more);
        tabSelectedIndicator = (ImageView) findViewById(R.id.tab_selectedIndicator);

        Resources res = getResources();
        tabTitles[0] = res.getString(R.string.tab_home);
        tabTitles[1] = res.getString(R.string.tab_message);
        tabTitles[2] = res.getString(R.string.tab_selfinfo);
        tabTitles[3] = res.getString(R.string.tab_square);
        tabTitles[4] = res.getString(R.string.tab_more);

        fragments[0] = new TabHomeFragment();
        fragments[1] = new TabMessageFragment();
        fragments[2] = new TabSelfInfoFragment();
        fragments[3] = new TabSquareFragment();
        fragments[4] = new TabMoreFragment();
        pageAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

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

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        viewPager.setAdapter(pageAdapter);
        tabPageIndicator = new TabPageIndicator(this, tab, tabSelectedIndicator);
        tabPageIndicator.setViewPager(viewPager);
        
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

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        int tabTextColor = res.getColor(R.color.tab_text_color);
        float tabTextSize = res.getDimension(R.dimen.tab_button_text_size)
                / res.getDisplayMetrics().density;

        tabContainer.setBackgroundDrawable(res.getDrawable(R.drawable.maintab_toolbar_bg));

        tabHome.setTextColor(tabTextColor);
        tabHome.setText(res.getText(R.string.tab_home));
        tabHome.setBackgroundDrawable(res.getDrawable(R.drawable.tab_item_selector));
        tabHome.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.icon_home), null, null);
        tabHome.setTextSize(tabTextSize);

        tabMessage.setTextColor(tabTextColor);
        tabMessage.setText(res.getText(R.string.tab_message));
        tabMessage.setBackgroundDrawable(res.getDrawable(R.drawable.tab_item_selector));
        tabMessage.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.icon_message), null, null);
        tabMessage.setTextSize(tabTextSize);

        tabSelfInfo.setTextColor(tabTextColor);
        tabSelfInfo.setText(res.getText(R.string.tab_selfinfo));
        tabSelfInfo.setBackgroundDrawable(res.getDrawable(R.drawable.tab_item_selector));
        tabSelfInfo.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.icon_selfinfo), null, null);
        tabSelfInfo.setTextSize(tabTextSize);

        tabSquare.setTextColor(tabTextColor);
        tabSquare.setText(res.getText(R.string.tab_square));
        tabSquare.setBackgroundDrawable(res.getDrawable(R.drawable.tab_item_selector));
        tabSquare.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.icon_square), null, null);
        tabSquare.setTextSize(tabTextSize);

        tabMore.setTextColor(tabTextColor);
        tabMore.setText(res.getText(R.string.tab_more));
        tabMore.setBackgroundDrawable(res.getDrawable(R.drawable.tab_item_selector));
        tabMore.setCompoundDrawablesWithIntrinsicBounds(null,
                res.getDrawable(R.drawable.icon_more), null, null);
        tabMore.setTextSize(tabTextSize);

        tabSelectedIndicator.setBackgroundDrawable(res
                .getDrawable(R.drawable.tab_selected_indicator));

        for (ThemeFragment object : fragments)
            object.themeChanged();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onResume() {
    	if (NetUtils.getType(this) == NetUtils.NO_NET) {
    		ToastUtils.show(this, R.string.msg_nonetwork, Toast.LENGTH_SHORT);
    	}
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
