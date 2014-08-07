
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
import com.limemobile.app.blog.activity.adapter.HallOfFrameAdapter;
import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.trends.RecommendCelebrityTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.trends.RecommendCelebrity;
import com.limemobile.app.utils.NetUtils;
import com.limemobile.app.utils.ToastUtils;
import com.limemobile.app.utils.UserTask;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import edu.mit.mobile.android.imagecache.ImageLoaderAdapter;

import java.util.ArrayList;
import java.util.Random;

public class HallOfFrameActivity extends ThemeActivity implements OnClickListener,
        OnItemClickListener {
    
    public static class ClassType {
        public ClassType(int classid, int subclassid) {
            this.classid = classid;
            this.subclassid = subclassid;
            this.loaded = false;
        }

        public int classid;
        public int subclassid;
        public boolean loaded;
    }

    // 类别id 类别名称及子类别id
    static ClassType classType[] = new ClassType[] {
            // 娱乐明星
            new ClassType(101, 0),

            // 体育明星
            new ClassType(102, 0),

            // 生活时尚
            new ClassType(103, 0),

            // 财经
            new ClassType(104, 0),
            
            // 科技网络
            new ClassType(105, 0),
            
            // 文化出版
            new ClassType(106, 0),
            
            // 汽车
            new ClassType(108, 0),
            
            // 动漫
            new ClassType(109, 0),
            
            // 游戏
            new ClassType(110, 0),
            
            // 星座命理
            new ClassType(111, 0),
            
            // 教育
            new ClassType(112, 0),
            
            // 企业品牌
            new ClassType(114, 0),
            
            // 酷站汇
            new ClassType(115, 0),
            
            // 腾讯产品
            new ClassType(116, 0),
            
            // 营销广告
            new ClassType(267, 0),

            // 媒体机构
            new ClassType(268, 959), // 广播
            
            new ClassType(268, 960),// 电视
            
            new ClassType(268, 961), // 报纸
            
            new ClassType(268, 962), // 杂志
            
            new ClassType(268, 963), // 网络媒体
            
            new ClassType(268, 964), // 通讯社

            // 有趣用户
            new ClassType(288, 0),

            // 传媒人士
            new ClassType(294, 953),// 传媒领袖
            
            new ClassType(294, 955),// 名编名记
            
            new ClassType(294, 956),// 主持人
            
            new ClassType(294, 957),// 传媒学者
            
            new ClassType(294, 958),// 专栏评论

            // 政府机构
            new ClassType(304, 0),
            
            // 公益慈善
            new ClassType(363, 0),
            
            // 公务人员
            new ClassType(945, 0),
            
//            // 快乐女声
//            new ClassType(949, 0),
            
            // 公共名人
            new ClassType(950, 0),
            
//            // 花儿朵朵
//            new ClassType(951, 0)
    };

    private View back;
    private View home;
    private TextView title;
    private ViewGroup titlebar;
    private ListView listView;
    private ViewGroup listFooter;
    private View listFooterContainer;
    private ProgressBar listFooterProgressBar;
    private TextView listFooterText;
    private ArrayList<RecommendCelebrity.Info> array;
    private HallOfFrameAdapter adapter;
    
    private UserTask<Object, Void, Boolean> refreshTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        array = new ArrayList<RecommendCelebrity.Info>();
        adapter = new HallOfFrameAdapter(this, array);
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
        if (view.getTag() != null && view.getTag() instanceof HallOfFrameAdapter.ViewHolder) {
            final HallOfFrameAdapter.ViewHolder holder = (HallOfFrameAdapter.ViewHolder) view.getTag();
            final RecommendCelebrity.Info info = (RecommendCelebrity.Info) holder.object;
            Intent intent = new Intent(this, UserInfoActivity.class);
            intent.putExtra(ITransKey.KEY, info.account);
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
        title.setText(R.string.square_vip);

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
                if (!hasAllLoaded())
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
    
    private boolean hasAllLoaded() {
        boolean allLoaded = true;
        for (int index = 0; index < classType.length; ++index) {
            if (classType[index].loaded) {
                allLoaded = false;
                break;
            }
        }
        return allLoaded;
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
        int pageIndex = 0;
        int unLoadedCount = 0;
        for (int index = 0; index < classType.length; ++index) {
            if (!classType[index].loaded)
                ++unLoadedCount;
        }
        Random r = new Random();
        int randomIndex = r.nextInt(unLoadedCount);
        int unLoadedIndex = 0;
        for (int index = 0; index < classType.length; ++index) {
            if (!classType[index].loaded) {
                if (randomIndex == unLoadedIndex) {
                    pageIndex = index;
                    break;
                } else
                    ++unLoadedIndex;
            }
        }
        classType[pageIndex].loaded = true;
        refreshTask = new RecommendCelebrityTask(invokedByListFooter ? listFooterLoadListener : loadListener,
                resultListener).execute(classType[pageIndex].classid, classType[pageIndex].subclassid);
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
                    
                    if (data != null && data.data != null && data.data instanceof RecommendCelebrity) {
                        RecommendCelebrity hallOfFrame = (RecommendCelebrity) data.data;
                        if (hallOfFrame.info != null)
                            for (RecommendCelebrity.Info info : hallOfFrame.info)
                                array.add(info);
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    }
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
            if (hasAllLoaded())
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
            if (hasAllLoaded())
                listFooterContainer.setVisibility(View.GONE);
            else
                listFooterContainer.setVisibility(View.VISIBLE);
        }

    };
}
