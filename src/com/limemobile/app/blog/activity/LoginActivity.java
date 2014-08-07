
package com.limemobile.app.blog.activity;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.bean.Account;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.utils.ToastUtils;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class LoginActivity extends ThemeActivity {
	private static final int REQUEST_CODE_OATHV2AUTHORIZE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        boolean startupLogin = true;
        if (intent != null)
            startupLogin = intent.getBooleanExtra(ITransKey.KEY, true);
        
        intent = new Intent(this, OAuthV2AuthorizeWebView.class);
        intent.putExtra("oauth", TencentWeibo.getInstance());
        intent.putExtra(ITransKey.KEY, startupLogin);
        startActivityForResult(intent, REQUEST_CODE_OATHV2AUTHORIZE);
    }

    @Override
    public void initViews() {
    }

    @Override
    public void themeChanged() {
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_OATHV2AUTHORIZE == requestCode) {
            if (OAuthV2AuthorizeWebView.RESULT_CODE == resultCode) {
                TencentWeibo.setInstance((OAuthV2) data.getExtras().getSerializable("oauth"));
                if (TencentWeibo.getInstance().getStatus() == 0) {
                    String msg = TencentWeibo.getInstance().getMsg();
                    String name = msg.substring(msg.indexOf("&name=") + 6);
                    String nick = name.substring(name.indexOf("&nick=") + 6);
                    name = name.substring(0, name.length() - nick.length() - 6);
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(LunchActivity.ACCESS_TOKEN, TencentWeibo.getInstance().getAccessToken());
                    editor.putString(LunchActivity.EXPIRES_IN, TencentWeibo.getInstance().getExpiresIn());
                    editor.putString(LunchActivity.OPEN_ID, TencentWeibo.getInstance().getOpenid());
                    editor.putString(LunchActivity.OPEN_KEY, TencentWeibo.getInstance().getOpenkey());
                    editor.putString(LunchActivity.MSG, TencentWeibo.getInstance().getMsg());
                    editor.putString(LunchActivity.NAME, name);
                    editor.putString(LunchActivity.NICK, nick);
                    editor.commit();
                    
                    Account account = new Account();
                    account.active = true;
                    account.name = name;
                    account.nick = nick;
                    account.openid = TencentWeibo.getInstance().getOpenid();
                    account.openkey = TencentWeibo.getInstance().getOpenkey();
                    account.accessToken = TencentWeibo.getInstance().getAccessToken();
                    account.expiresIn = TencentWeibo.getInstance().getExpiresIn();
                    account.msg = TencentWeibo.getInstance().getMsg();
                    account.addAccount(this);

                    finish();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else {
                ToastUtils.show(this, R.string.communicating_failed,
                        Toast.LENGTH_SHORT);
                finish();
            }
        }
    }
}
