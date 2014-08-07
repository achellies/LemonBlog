package com.limemobile.app.blog.activity;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.adapter.SkinListAdapter;
import com.limemobile.app.blog.activity.adapter.SkinListAdapter.SkinItem;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.TAsyncTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.utils.NetUtils;
import com.limemobile.app.utils.ToastUtils;
import com.limemobile.app.utils.UserTask;

public class SkinActivity extends ThemeActivity implements OnClickListener, OnItemClickListener {
    private View back;
    private View refresh;
    private TextView title;
    private ViewGroup titlebar;
    
    private ArrayList<SkinItem> array;
    private SkinListAdapter adapter;
    private ListView listView;
    private ViewGroup listViewContainer;
    private UserTask<Object, Void, Boolean> refreshTask;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_theme);
        
        array = new ArrayList<SkinItem>();
        adapter = new SkinListAdapter(this, array, this);
        initViews();
        
        SkinItem skinItem = new SkinItem();
        skinItem.name = "经典主题";
        skinItem.packageName = "com.limemobile.app.blog";
        array.add(skinItem);
        
        skinItem = new SkinItem();
        skinItem.name = "天际蓝";
        skinItem.packageName = "com.limemobile.app.blog.bluesky";
        array.add(skinItem);
        
        skinItem = new SkinItem();
        skinItem.name = "青草绿";
        skinItem.packageName = "com.limemobile.app.blog.greenvitality";
        array.add(skinItem);
        
        skinItem = new SkinItem();
        skinItem.name = "蜜桃粉";
        skinItem.packageName = "com.limemobile.app.blog.pinkallure";
        array.add(skinItem);
        
        skinItem = new SkinItem();
        skinItem.name = "子夜灰";
        skinItem.packageName = "com.limemobile.app.blog.grey";
        array.add(skinItem);
        
        skinItem = new SkinItem();
        skinItem.name = "摩卡棕";
        skinItem.packageName = "com.limemobile.app.blog.brown";
        array.add(skinItem);
    }
    
    @Override
	protected void onSaveInstanceState(Bundle outState) {
        if (isFinishing()) {
            final UserTask<Object, Void, Boolean> task = refreshTask;
            if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                task.cancel(true);
                refreshTask = null;
            }
        }
		super.onSaveInstanceState(outState);
	}

	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.refresh:
            	refresh();
                break;
            case R.id.item_action:
            	break;
        }
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
        refreshTask = new RefreshTask(listViewContainer, loadListener, resultListener).execute();
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

    @Override
    public void initViews() {
    	refresh = findViewById(R.id.refresh);
    	refresh.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        
        listViewContainer = (ViewGroup) findViewById(R.id.main_container);
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        back.setBackgroundDrawable(res.getDrawable(R.drawable.title_back));
        refresh.setBackgroundDrawable(res.getDrawable(R.drawable.title_reload));
        titlebar.setBackgroundDrawable(res.getDrawable(R.drawable.title_bar_bg));
        title.setText(res.getString(R.string.more_theme));
    }
    
    private IResultListener resultListener = new IResultListener() {

        @Override
        public void onSuccess(Entity<TObject> data) {
        }

        @Override
        public void onFail(Entity<TObject> data) {
        }

    };

    private ILoadListener loadListener = new ILoadListener() {

        @Override
        public void onStart(TextView v) {
        }

        @Override
        public void onEnd() {
        }

    };
    
    private class RefreshTask extends TAsyncTask<String> {

		public RefreshTask(ViewGroup container, ILoadListener loadListener,
				IResultListener successListener) {
			super(container, loadListener, successListener);
		}

		public RefreshTask(ILoadListener loadListener,
				IResultListener successListener) {
			super(loadListener, successListener);
		}

		@Override
		public String callAPI(Object... params) {
			return null;
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			return super.doInBackground(params);
		}
    }
}
