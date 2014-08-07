
package com.limemobile.app.blog.activity.fragment;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.MyApplication;
import com.limemobile.app.blog.activity.adapter.TweetListAdapter;
import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tweet;
import com.limemobile.app.utils.NetUtils;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import edu.mit.mobile.android.imagecache.ImageLoaderAdapter;

public class MessagePrivateLetterFragment extends ThemeFragment implements OnClickListener, OnItemClickListener {
	
    private ListView listView;
    private ViewGroup listFooter;
    private View writeLetter;
    private View listFooterContainer;
    private ProgressBar listFooterProgressBar;
    private TextView listFooterText;
    
    private ArrayList<Tweet> array;
    private TweetListAdapter adapter;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        array = new ArrayList<Tweet>();
        adapter = new TweetListAdapter(getActivity(), array, this);
        
        setRetainInstance(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		listView.setFastScrollEnabled(Setting.fastScrollEnabled);
        if (isVisible() && NetUtils.getType(getActivity()) != NetUtils.NO_NET) {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    
    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.item_add:
    		break;
    	}
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.pull2refresh_list_view, null);
        initViews((ViewGroup) layout);
        themeChanged();
        return layout;
    }

    @Override
    public void themeChanged() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initViews(ViewGroup container) {
        listView = (ListView) container.findViewById(R.id.listview);
        listFooter = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer,
                null);
        listFooterProgressBar = (ProgressBar) listFooter.findViewById(R.id.loading_progress);
        listFooterText = (TextView) listFooter.findViewById(R.id.progress_loading_text);
        listView.addFooterView(listFooter);
        listFooterContainer = (View) listFooter.findViewById(R.id.main_list_foot);
        listFooterContainer.setOnClickListener(this);

        View listHeader = LayoutInflater.from(getActivity()).inflate(
                R.layout.private_letter_list_header, null);
        listView.addHeaderView(listHeader);
        writeLetter = (View) listHeader.findViewById(R.id.item_add);
        writeLetter.setOnClickListener(this);

        Drawable drawable = getResources().getDrawable(R.drawable.portrait_small);
        int iconWidth = drawable.getIntrinsicWidth();
        int iconHeight = drawable.getIntrinsicHeight();
        listView.setAdapter(new ImageLoaderAdapter(getActivity(), adapter,
                MyApplication.imageCache,
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
    }
    
    public void refresh(boolean invokedByListFooter) {
    	
    }
}
