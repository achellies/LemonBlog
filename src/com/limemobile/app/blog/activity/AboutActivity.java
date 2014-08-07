
package com.limemobile.app.blog.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.utils.ToastUtils;

public class AboutActivity extends ThemeActivity implements OnClickListener {
    private View back;
    private View versionContainer;
    private TextView version;
    private View authorContainer;
    private TextView author;
    private View emailContainer;
    private TextView email;
    private View qqContainer;
    private TextView qq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.about_email_container:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[] {
                    "achellies@163.com"
                });
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
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        versionContainer = findViewById(R.id.about_version_container);
        version = (TextView) findViewById(R.id.about_version);
        authorContainer = findViewById(R.id.about_author_container);
        author = (TextView) findViewById(R.id.about_author);
        emailContainer = findViewById(R.id.about_email_container);
        emailContainer.setOnClickListener(this);
        email = (TextView) findViewById(R.id.about_email);
        qqContainer = findViewById(R.id.about_qq_container);
        qq = (TextView) findViewById(R.id.about_qq);

        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            version.setText(pi.versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        author.setText("achellies");
        email.setText("achellies@163.com");
        qq.setText("86455009");
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        versionContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
        authorContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
        emailContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
        qqContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_bottom));
    }

}
