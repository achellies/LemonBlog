
package com.limemobile.app.blog.activity;

import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.adapter.MayKnowPersonAdapter;
import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.other.KnowPersonTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.other.KnowPerson;
import com.limemobile.app.utils.NetUtils;
import com.limemobile.app.utils.ToastUtils;
import com.limemobile.app.utils.UserTask;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import edu.mit.mobile.android.imagecache.ImageLoaderAdapter;

import java.util.ArrayList;

public class MayKnowPersonActivity extends ThemeActivity implements OnClickListener,
        OnItemClickListener {
    private static final int REQNUM = 30;

    private View back;
    private View home;
    private TextView title;
    private ViewGroup titlebar;
    private ListView listView;
    private ViewGroup listFooter;
    private View listFooterContainer;
    private ProgressBar listFooterProgressBar;
    private TextView listFooterText;
    private ArrayList<KnowPerson.Data> array;
    private MayKnowPersonAdapter adapter;
    private int pageIndex = 0;
    private KnowPerson knowPerson;
    private UserTask<Object, Void, Boolean> refreshTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        array = new ArrayList<KnowPerson.Data>();
        adapter = new MayKnowPersonAdapter(this, array);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        titlebar.post(new Runnable() {

            @Override
            public void run() {
                ((PullToRefreshListView) listView).clickRefresh();
            }

        });
        super.onPostCreate(savedInstanceState);
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
    protected void onResume() {
    	listView.setFastScrollEnabled(Setting.fastScrollEnabled);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            case R.id.main_list_foot:
                refresh(true);
                break;
            case R.id.item_action:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.getTag() != null && view.getTag() instanceof MayKnowPersonAdapter.ViewHolder) {
            final MayKnowPersonAdapter.ViewHolder holder = (MayKnowPersonAdapter.ViewHolder) view.getTag();
            final KnowPerson.Data info = (KnowPerson.Data) holder.object;
            Intent intent = new Intent(this, UserInfoActivity.class);
            intent.putExtra(ITransKey.KEY, info.name);
            startActivity(intent);
        }
    }

    @Override
    public void initViews() {
        home = findViewById(R.id.home);
        home.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.square_guess_you_like);

        listView = (ListView) findViewById(R.id.listview);
        listFooter = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.list_footer, null);
        listFooterProgressBar = (ProgressBar) listFooter.findViewById(R.id.loading_progress);
        listFooterText = (TextView) listFooter.findViewById(R.id.progress_loading_text);
        listView.addFooterView(listFooter);
        listFooterContainer = (View) listFooter.findViewById(R.id.main_list_foot);
        listFooterContainer.setOnClickListener(this);
        Drawable drawable = getResources().getDrawable(R.drawable.portrait_small);
        int iconWidth = drawable.getIntrinsicWidth();
        int iconHeight = drawable.getIntrinsicHeight();
        listView.setAdapter(new ImageLoaderAdapter(this, adapter, MyApplication.imageCache,
                new int[] {
                    R.id.item_image
                }, iconWidth, iconHeight, ImageLoaderAdapter.UNIT_PX));

        // Set a listener to be invoked when the list should be refreshed.
        ((PullToRefreshListView) listView).setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                if (knowPerson == null || knowPerson.hasnext > 0)
                    refresh(false);
                else
                    ((PullToRefreshListView) listView).onRefreshComplete();
            }
        });
        listView.setOnItemClickListener(this);
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        back.setBackgroundDrawable(res.getDrawable(R.drawable.title_back));
        home.setBackgroundDrawable(res.getDrawable(R.drawable.title_home));
        titlebar.setBackgroundDrawable(res.getDrawable(R.drawable.title_bar_bg));
    }

    public void refresh(boolean invokedByListFooter) {
    	if (NetUtils.getType(this) == NetUtils.NO_NET) {
    		ToastUtils.show(this, R.string.msg_nonetwork, Toast.LENGTH_SHORT);
    		return;
    	}
        final UserTask<Object, Void, Boolean> task = refreshTask;
        if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
            return;
        }
        refreshTask = new KnowPersonTask(invokedByListFooter ? listFooterLoadListener : loadListener,
                resultListener).execute(REQNUM, REQNUM * (pageIndex++ - 1));
    }

    private IResultListener resultListener = new IResultListener() {

        @Override
        public void onSuccess(final Entity<TObject> data) {
            titlebar.post(new Runnable() {

                @Override
                public void run() {
                    // Call onRefreshComplete when the list has been refreshed.
                    if (listView != null)
                        ((PullToRefreshListView) listView).onRefreshComplete();
                    boolean hasItems = false;
                    if (data != null && data.data != null && data.data instanceof KnowPerson) {
                        knowPerson = (KnowPerson) data.data;
                        if (knowPerson.data != null)
                            for (KnowPerson.Data info : knowPerson.data) {
                                hasItems = true;
                                array.add(info);
                            }
                    }
                    if (knowPerson != null && !hasItems)
                        knowPerson.hasnext = 0;
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                }

            });
        }

        @Override
        public void onFail(Entity<TObject> data) {
            titlebar.post(new Runnable() {

                @Override
                public void run() {
                    // Call onRefreshComplete when the list has been refreshed.
                    if (listView != null)
                        ((PullToRefreshListView) listView).onRefreshComplete();
                    ToastUtils.show(titlebar.getContext(), R.string.communicating_failed,
                            Toast.LENGTH_SHORT);
                }

            });
        }

    };

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
            if (knowPerson != null && knowPerson.hasnext <= 0)
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
            if (knowPerson != null && knowPerson.hasnext <= 0)
                listFooterContainer.setVisibility(View.GONE);
            else
                listFooterContainer.setVisibility(View.VISIBLE);
        }

    };
}
