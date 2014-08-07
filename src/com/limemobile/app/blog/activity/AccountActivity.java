package com.limemobile.app.blog.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.adapter.AccountListAdapter;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.bean.Account;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.widget.DragListView;
import com.limemobile.app.blog.widget.DragListView.DetermineDragable;
import com.limemobile.app.blog.widget.DragListView.DropListener;

public class AccountActivity extends ThemeActivity implements OnClickListener, OnItemClickListener {
    private View back;
    private Button edit;
    private TextView title;
    private ViewGroup titlebar;
    private ArrayList<Account> array;
    private AccountListAdapter adapter;
    private ListView listView;
    private boolean hasDraged = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        
        array = new ArrayList<Account>();
        adapter = new AccountListAdapter(this, array, this);
        initViews();
    }
    
    @Override
	protected void onResume() {
        updateList();
		super.onResume();
	}

	@Override
    protected void onPause() {
	    if (hasDraged) {
	        hasDraged = false;
	        for (Account object : array)
	            object.addAccount(this);
	    }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit:
            	if (adapter.isBeginEditing()) {
            	    edit.setText(R.string.account_edit);
            		adapter.setBeginEditing(false);
            	} else {
            	    edit.setText(R.string.account_add_done);
            		adapter.setBeginEditing(true);
            	}
            	adapter.notifyDataSetChanged();
            	break;
            case R.id.item_action:
            	if (v.getTag() != null && v.getTag() instanceof Account) {
            		Account account = (Account) v.getTag();
            		Account object = account.deleteAccount(this);
            		if (object != null)
            		    changeAccount(this, object, false);
            		updateList();
            	}
            	break;
            case R.id.item_add:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra(ITransKey.KEY, false);
                startActivity(intent);
            	break;
        }
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	    if (view.getTag() != null && view.getTag() instanceof AccountListAdapter.ViewHolder) {
	        AccountListAdapter.ViewHolder holder = (AccountListAdapter.ViewHolder) view.getTag();
	        Account account = (Account) holder.object;
	        if (!account.active) {
	            for (Account object : array)
	                object.active = false;
	            account.active = true;
	            account.addAccount(this);
	            changeAccount(this, account, false);
	            adapter.notifyDataSetChanged();
	        }
	    }
	}
	
	public static void changeAccount(Context context, Account account, boolean implict) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LunchActivity.ACCESS_TOKEN, account.accessToken);
        editor.putString(LunchActivity.EXPIRES_IN, account.expiresIn);
        editor.putString(LunchActivity.OPEN_ID, account.openid);
        editor.putString(LunchActivity.OPEN_KEY, account.openkey);
        editor.putString(LunchActivity.MSG, account.msg);
        editor.putString(LunchActivity.NAME, account.name);
        editor.putString(LunchActivity.NICK, account.nick);
        editor.commit();

        TencentWeibo.getInstance().setMsg(account.msg);
        TencentWeibo.getInstance().setAccessToken(account.accessToken);
        TencentWeibo.getInstance().setExpiresIn(account.expiresIn);
        TencentWeibo.getInstance().setOpenid(account.openid);
        TencentWeibo.getInstance().setOpenkey(account.openkey);
        
        if (!implict) {
            // TODO
        }
	}
	
	private void updateList() {
	    Account.getAccountList(this, array);
	    adapter.notifyDataSetChanged();
	}

    @Override
    public void initViews() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(this);
        
        listView = (ListView) findViewById(R.id.listview);
        
        View viewFooter = LayoutInflater.from(this).inflate(R.layout.account_list_footer, null);
        listView.addFooterView(viewFooter);
        View newAccount = viewFooter.findViewById(R.id.item_add);
        newAccount.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        
        edit.setText(R.string.account_edit);
        adapter.setBeginEditing(false);
        ((DragListView)listView).setDropListener(new DropListener() {

            @Override
            public void drop(int from, int to) {
                if (from >= 0 && from < array.size() && to >= 0 && to < array.size()) {
                    hasDraged = true;
                    Account fromAccount = array.get(from);
                    array.remove(fromAccount);
                    array.add(to, fromAccount);
                    adapter.notifyDataSetChanged();
                }
            }
            
        });
        
        ((DragListView)listView).setDetermineDragable(new DetermineDragable() {

            @Override
            public boolean dragable(ViewGroup view, int x, int y) {
                if (view.getTag() != null && view.getTag() instanceof AccountListAdapter.ViewHolder) {
                    AccountListAdapter.ViewHolder holder = (AccountListAdapter.ViewHolder) view.getTag();
                    Rect outRect = new Rect();
                    Rect frameRect = new Rect();
                    view.getHitRect(frameRect);
                    holder.subImage.getHitRect(outRect);
                    outRect.left += frameRect.left;
                    outRect.top += frameRect.top;
                    outRect.right += frameRect.right;
                    outRect.bottom += frameRect.bottom;
                    return outRect.contains(x, y) ? true : false;
                }
                return false;
            }
            
        });
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        back.setBackgroundDrawable(res.getDrawable(R.drawable.title_back));
        titlebar.setBackgroundDrawable(res.getDrawable(R.drawable.title_bar_bg));
        title.setText(res.getString(R.string.more_account));
    }
}
