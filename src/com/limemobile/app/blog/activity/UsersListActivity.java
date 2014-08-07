
package com.limemobile.app.blog.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.limemobile.app.blog.activity.adapter.UsersListAdapter;
import com.limemobile.app.blog.activity.adapter.UsersListAdapter.ViewHolder;
import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.friends.AddTask;
import com.limemobile.app.blog.weibo.tencent.api.friends.BlackListTask;
import com.limemobile.app.blog.weibo.tencent.api.friends.DelBlackListTask;
import com.limemobile.app.blog.weibo.tencent.api.friends.DelTask;
import com.limemobile.app.blog.weibo.tencent.api.friends.FansListTask;
import com.limemobile.app.blog.weibo.tencent.api.friends.IdolListTask;
import com.limemobile.app.blog.weibo.tencent.api.friends.UserFansListTask;
import com.limemobile.app.blog.weibo.tencent.api.friends.UserIdolListTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.friends.Friend;
import com.limemobile.app.blog.weibo.tencent.entity.friends.Friend.Info;
import com.limemobile.app.utils.NetUtils;
import com.limemobile.app.utils.ToastUtils;
import com.limemobile.app.utils.UserTask;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import edu.mit.mobile.android.imagecache.ImageLoaderAdapter;

import java.util.ArrayList;

public class UsersListActivity extends ThemeActivity implements OnClickListener,
        OnItemClickListener {
    private static final int REQNUM = 30;

    public enum UserListType {
        FansList(0), IdolList(1), Blacklist(2);

        UserListType(int i)
        {
            this.type = i;
        }

        private int type;

        public int getNumericType()
        {
            return type;
        }
    }

    private ViewGroup mainContainer;
    private View back;
    private View home;
    private TextView title;
    private ViewGroup titlebar;
    private ListView listView;
    private ViewGroup listFooter;
    private View listFooterContainer;
    private ProgressBar listFooterProgressBar;
    private TextView listFooterText;
    private ArrayList<Friend.Info> array;
    private Friend friend;
    private UsersListAdapter adapter;
    private String openid;
    private String userName;
    private int pageIndex = 1;
    private int listType;
    private int sex;
    
    private UserTask<Object, Void, Boolean> refreshTask;
    private UserTask<Object, Void, Boolean> addTask;
    private UserTask<Object, Void, Boolean> delTask;
    private UserTask<Object, Void, Boolean> delBlacklistTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra(ITransKey.KEY);
            openid = intent.getStringExtra(ITransKey.KEY1);
            listType = intent.getIntExtra(ITransKey.KEY2, 0);
            sex = intent.getIntExtra(ITransKey.KEY3, 0);
        }
        if (TextUtils.isEmpty(openid) && TextUtils.isEmpty(userName)) {
            finish();
            return;
        }
        if (TextUtils.isEmpty(userName))
            userName = "";
        if (TextUtils.isEmpty(openid))
            openid = "";
        setContentView(R.layout.activity_users_list);

        array = new ArrayList<Friend.Info>();
        adapter = new UsersListAdapter(this, listType, array, this);
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
	        
            final UserTask<Object, Void, Boolean> task2 = addTask;
            if (task2 != null && task2.getStatus() != UserTask.Status.FINISHED) {
                task2.cancel(true);
                addTask = null;
            }
            
            final UserTask<Object, Void, Boolean> task3 = delTask;
            if (task3 != null && task3.getStatus() != UserTask.Status.FINISHED) {
                task3.cancel(true);
                delTask = null;
            }
            
            final UserTask<Object, Void, Boolean> task4 = delBlacklistTask;
            if (task4 != null && task4.getStatus() != UserTask.Status.FINISHED) {
                task4.cancel(true);
                delBlacklistTask = null;
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
            	if (NetUtils.getType(this) == NetUtils.NO_NET) {
            		ToastUtils.show(this, R.string.msg_nonetwork, Toast.LENGTH_SHORT);
            		return;
            	}
                if (v.getTag() != null && v.getTag() instanceof Friend.Info) {
                    final Friend.Info info = (Info) v.getTag();
                    if (listType == UserListType.Blacklist.getNumericType()) {
                        new AlertDialog.Builder(this)
                                .setMessage(R.string.delete_person_from_blacklist_or_not)
                                .setPositiveButton(R.string.ok,
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                final UserTask<Object, Void, Boolean> task = delBlacklistTask;
                                                if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                                                    return;
                                                }
                                                delBlacklistTask = new DelBlackListTask(mainContainer, loadListener,
                                                        new IResultListener() {

                                                            @Override
                                                            public void onSuccess(
                                                                    Entity<TObject> data) {
                                                                mainContainer.post(new Runnable() {

                                                                    @Override
                                                                    public void run() {
                                                                        array.remove(info);
                                                                        if (adapter != null)
                                                                            adapter.notifyDataSetChanged();
                                                                        ToastUtils.show(
                                                                                mainContainer
                                                                                        .getContext(),
                                                                                R.string.ok_to_delete_from_blacklist,
                                                                                Toast.LENGTH_SHORT);
                                                                    }

                                                                });
                                                            }

                                                            @Override
                                                            public void onFail(Entity<TObject> data) {
                                                                mainContainer.post(new Runnable() {

                                                                    @Override
                                                                    public void run() {
                                                                        ToastUtils.show(
                                                                                mainContainer
                                                                                        .getContext(),
                                                                                R.string.fail_to_delete_from_blacklist,
                                                                                Toast.LENGTH_SHORT);
                                                                    }

                                                                });
                                                            }

                                                        }).execute(info.name, "");
                                            }

                                        })
                                .setNegativeButton(R.string.cancel,
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }

                                        });
                    } else {
                        if (info.isidol) {
                            final UserTask<Object, Void, Boolean> task1 = delTask;
                            if (task1 != null && task1.getStatus() != UserTask.Status.FINISHED) {
                                return;
                            }
                            delTask = new DelTask(mainContainer, loadListener, new IResultListener() {

                                @Override
                                public void onSuccess(Entity<TObject> data) {
                                    mainContainer.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            if (listType == UserListType.IdolList.getNumericType()) {
                                                array.remove(info);
                                            } else
                                                info.isidol = false;
                                            if (adapter != null)
                                                adapter.notifyDataSetChanged();
                                            ToastUtils.show(mainContainer.getContext(),
                                                    R.string.del_attention_ok, Toast.LENGTH_SHORT);
                                        }

                                    });
                                }

                                @Override
                                public void onFail(Entity<TObject> data) {
                                    mainContainer.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            ToastUtils.show(mainContainer.getContext(),
                                                    R.string.del_attention_failed,
                                                    Toast.LENGTH_SHORT);
                                        }

                                    });
                                }

                            }).execute(info.name, "");
                        } else {
                            final UserTask<Object, Void, Boolean> task1 = addTask;
                            if (task1 != null && task1.getStatus() != UserTask.Status.FINISHED) {
                                return;
                            }
                            addTask = new AddTask(mainContainer, loadListener, new IResultListener() {

                                @Override
                                public void onSuccess(Entity<TObject> data) {
                                    mainContainer.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            info.isidol = true;
                                            if (adapter != null)
                                                adapter.notifyDataSetChanged();
                                            ToastUtils.show(mainContainer.getContext(),
                                                    R.string.add_attention_ok, Toast.LENGTH_SHORT);
                                        }

                                    });
                                }

                                @Override
                                public void onFail(Entity<TObject> data) {
                                    mainContainer.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            ToastUtils.show(mainContainer.getContext(),
                                                    R.string.add_attention_failed,
                                                    Toast.LENGTH_SHORT);
                                        }

                                    });
                                }

                            }).execute(info.name, "");
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.getTag() != null && view.getTag() instanceof UsersListAdapter.ViewHolder) {
            final UsersListAdapter.ViewHolder holder = (ViewHolder) view.getTag();
            final Friend.Info info = (Info) holder.object;
            Intent intent = new Intent(this, UserInfoActivity.class);
            intent.putExtra(ITransKey.KEY, info.name);
            intent.putExtra(ITransKey.KEY1, info.openid);
            startActivity(intent);
        }
    }

    @Override
    public void initViews() {
        mainContainer = (ViewGroup) findViewById(R.id.main_container);
        home = findViewById(R.id.home);
        home.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);

        if (listType == UserListType.FansList.getNumericType()) {
            if (TencentWeibo.getInstance().getOpenid() != null && TencentWeibo.getInstance().getOpenid().equals(openid))
                title.setText(R.string.my_fans_label);
            else {
                if (sex == 2)
                    title.setText(R.string.she_fans_label);
                else
                    title.setText(R.string.he_fans_label);
            }
        } else if (listType == UserListType.IdolList.getNumericType()) {
            if (TencentWeibo.getInstance().getOpenid() != null && TencentWeibo.getInstance().getOpenid().equals(openid))
                title.setText(R.string.my_follower_label);
            else {
                if (sex == 2)
                    title.setText(R.string.she_follower_label);
                else
                    title.setText(R.string.he_follower_label);
            }
        } else {
            title.setText(R.string.blacklist);
        }

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
                refresh(false);
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
        if (listType == UserListType.FansList.getNumericType()) {
            if ((friend != null && friend.hasnext == 0) || friend == null) {
                if (TencentWeibo.getInstance().getOpenid() != null && TencentWeibo.getInstance().getOpenid().equals(openid))
                	refreshTask = new FansListTask(invokedByListFooter ? listFooterLoadListener : loadListener,
                            resultListener).execute(REQNUM, REQNUM * (pageIndex++ - 1), 1, 0);
                else
                	refreshTask = new UserFansListTask(invokedByListFooter ? listFooterLoadListener
                            : loadListener, resultListener).execute(REQNUM, REQNUM
                            * (pageIndex++ - 1), userName, openid, 1, 0);
            }
        } else if (listType == UserListType.IdolList.getNumericType()) {
            if ((friend != null && friend.hasnext == 0) || friend == null)
                if (TencentWeibo.getInstance().getOpenid() != null && TencentWeibo.getInstance().getOpenid().equals(openid))
                	refreshTask = new IdolListTask(invokedByListFooter ? listFooterLoadListener : loadListener,
                            resultListener).execute(REQNUM, REQNUM * (pageIndex++ - 1), 0);
                else
                	refreshTask = new UserIdolListTask(invokedByListFooter ? listFooterLoadListener
                            : loadListener, resultListener).execute(REQNUM, REQNUM
                            * (pageIndex++ - 1), userName, openid, 0);
        } else {
            if ((friend != null && friend.hasnext == 0) || friend == null)
            	refreshTask = new BlackListTask(invokedByListFooter ? listFooterLoadListener : loadListener,
                        resultListener).execute(REQNUM, REQNUM * (pageIndex++ - 1));
        }
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
                    if (data != null && data.data != null && data.data instanceof Friend) {
                        friend = (Friend) data.data;
                        if (friend.info != null)
                            for (Friend.Info info : friend.info)
                                array.add(info);
                    }
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
            if (friend != null && friend.hasnext == 1)
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
            if (friend != null && friend.hasnext == 1)
                listFooterContainer.setVisibility(View.GONE);
            else
                listFooterContainer.setVisibility(View.VISIBLE);
        }

    };
}
