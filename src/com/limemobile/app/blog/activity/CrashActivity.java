
package com.limemobile.app.blog.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.utils.ToastUtils;

public class CrashActivity extends ThemeActivity implements OnClickListener {

    private TextView title;
    private ViewGroup titlebar;
    private Button exit;
    private Button send;
    private String crashString;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            crashString = intent.getStringExtra(ITransKey.KEY);
        }
        setContentView(R.layout.activity_crash);
        initViews();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode)
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.send:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[] {
                    "achellies@163.com"
                });
                i.putExtra(Intent.EXTRA_TEXT, crashString);
                i.putExtra(Intent.EXTRA_SUBJECT, "LimeMobile Crash Log");
                try {
                    startActivity(Intent.createChooser(i, getResources().getText(R.string.choose_email_client)));
                } catch (android.content.ActivityNotFoundException ex) {
                    ToastUtils.show(this, "There are no email clients installed.",
                            Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    @Override
    public void initViews() {
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(this);
        error = (TextView) findViewById(R.id.edit_input);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);
        
        if (!TextUtils.isEmpty(crashString)) {
            error.setText(crashString);
        }
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        titlebar.setBackgroundDrawable(res.getDrawable(R.drawable.title_bar_bg));
        title.setText(res.getString(R.string.app_name));
    }

}
