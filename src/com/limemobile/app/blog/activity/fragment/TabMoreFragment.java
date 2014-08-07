
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
import com.limemobile.app.blog.activity.AboutActivity;
import com.limemobile.app.blog.activity.AccountActivity;
import com.limemobile.app.blog.activity.ReadModeActivity;
import com.limemobile.app.blog.activity.SettingActivity;
import com.limemobile.app.blog.activity.SkinActivity;
import com.limemobile.app.blog.activity.UserInfoActivity;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.entity.UpgradeInfo;

public class TabMoreFragment extends ThemeFragment implements OnClickListener {
    private View titleBar;
    private TextView title;
    private View setting;
    private View account;
    private View readMode;
    private View officeTweet;
    private View about;
    private View theme;
    private View update;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_tab_more, null);
        initViews((ViewGroup) layout);
        themeChanged();
        return layout;
    }

    @Override
    public void themeChanged() {
        if (isAdded()) {
            Resources res = Theme.getInstance().getContext(getActivity()).getResources();
            
            titleBar.setBackgroundResource(R.drawable.title_bar_bg);
	        
            title.setText(res.getString(R.string.tab_more));
            title.setTextColor(res.getColor(R.color.tab_text_color));
            float titleTextSize = res.getDimension(R.dimen.title_text_size)
                    / res.getDisplayMetrics().density;
            title.setTextSize(titleTextSize);
            
            setting.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
            theme.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
            account.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
            readMode.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
            officeTweet.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
            update.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
            about.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_bottom));
        }
    }

    @Override
    public void initViews(ViewGroup container) {
        titleBar = container.findViewById(R.id.main_titlebar);
        title = (TextView) container.findViewById(R.id.title);
        setting = container.findViewById(R.id.more_setting);
        setting.setOnClickListener(this);
        about = container.findViewById(R.id.more_about);
        about.setOnClickListener(this);
        theme = container.findViewById(R.id.more_theme);
        theme.setOnClickListener(this);
        update = container.findViewById(R.id.more_update);
        update.setOnClickListener(this);
        account = container.findViewById(R.id.more_account);
        account.setOnClickListener(this);
        readMode = container.findViewById(R.id.more_readmode);
        readMode.setOnClickListener(this);
        officeTweet = container.findViewById(R.id.more_office_tweet);
        officeTweet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.more_setting:
            	startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.more_theme:
            	startActivity(new Intent(getActivity(), SkinActivity.class));
                break;
            case R.id.more_update:
                UpgradeInfo.checkUpdate(getActivity(), false);
                break;
            case R.id.more_account:
            	startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
            case R.id.more_readmode:
            	startActivity(new Intent(getActivity(), ReadModeActivity.class));
            	break;
            case R.id.more_office_tweet:
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra(ITransKey.KEY, "achellies");
                startActivity(intent);
            	break;
        }
    }

}
