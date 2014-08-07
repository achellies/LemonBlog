
package com.limemobile.app.blog.activity;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.imagecrop.ImageCropActivity;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.api.ILoadListener;
import com.limemobile.app.blog.weibo.tencent.api.IResultListener;
import com.limemobile.app.blog.weibo.tencent.api.user.UpdateHeadTask;
import com.limemobile.app.blog.weibo.tencent.api.user.UpdateTask;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.user.Info;
import com.limemobile.app.utils.MessageDigestUtil;
import com.limemobile.app.utils.NetUtils;
import com.limemobile.app.utils.ToastUtils;
import com.limemobile.app.utils.UserTask;

public class EditUserInfoActivity extends ThemeActivity implements OnClickListener {
    private static final int INTENT_EDIT_NICKNAME = 1;
    private static final int INTENT_EDIT_INTRODUCTION = 2;

    private ViewGroup mainContainer;
    private View back;
    private View save;
    private View headContainer;
    private View sexContainer;
    private RadioButton male;
    private RadioButton female;
    private View nickContainer;
    private TextView nick;
    private View descriptionContainer;
    private TextView introduction;
    private ImageView head;
    private View birthdayContainer;
    private TextView birthday;

    private Info userInfo;
    private Info editInfo;
    private long imageID = 0;
    
    private UserTask<Object, Void, Boolean> updateHeadTask;
    private UserTask<Object, Void, Boolean> saveTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_user_info);
        Intent intent = getIntent();
        if (intent != null) {
            userInfo = (Info) intent.getSerializableExtra(ITransKey.KEY);
        }
        if (userInfo == null) {
            finish();
            return;
        }

        initViews();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isFinishing()) {
            final UserTask<Object, Void, Boolean> task = saveTask;
            if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                task.cancel(true);
                saveTask = null;
            }
            final UserTask<Object, Void, Boolean> task2 = updateHeadTask;
            if (task2 != null && task2.getStatus() != UserTask.Status.FINISHED) {
                task2.cancel(true);
                updateHeadTask = null;
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        updateViews();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initViews() {
        mainContainer = (ViewGroup) findViewById(R.id.main_container);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        save = findViewById(R.id.save);
        save.setOnClickListener(this);
        headContainer = findViewById(R.id.user_info_head_container);
        head = (ImageView) findViewById(R.id.user_info_head);
        headContainer.setOnClickListener(this);
        sexContainer = findViewById(R.id.user_info_sex_container);
        male = (RadioButton) findViewById(R.id.user_info_sex_male);
        female = (RadioButton) findViewById(R.id.user_info_sex_female);
        nickContainer = findViewById(R.id.user_info_nick_container);
        nickContainer.setOnClickListener(this);
        nick = (TextView) findViewById(R.id.user_info_nick);
        descriptionContainer = findViewById(R.id.user_info_description_container);
        descriptionContainer.setOnClickListener(this);
        introduction = (TextView) findViewById(R.id.user_info_description);
        birthdayContainer = findViewById(R.id.user_info_birthday_container);
        birthdayContainer.setOnClickListener(this);
        birthday = (TextView) findViewById(R.id.user_info_birthday);
    }

    private void updateViews() {
        if (userInfo != null) {
            nick.setText(userInfo.nick);
            introduction.setText(userInfo.introduction);

            // 用户性别，1-男，2-女，0-未填写
            if (userInfo.sex == 2)
                female.setChecked(true);
            else
                male.setChecked(true);

            if (userInfo.birth_year > 0) {
                birthday.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(userInfo.birth_year).append("年")
                        .append(userInfo.birth_month).append("月")
                        .append(userInfo.birth_day).append("日"));
            }

            if (!TextUtils.isEmpty(userInfo.head)) {
                imageID = MyApplication.imageCache.getNewID();
                // attempt to bypass all the loading machinery to get the image
                // loaded as quickly
                // as possible
                Drawable d = null;
                try {
                    Uri uri = null;
                    if (userInfo.head.startsWith("http")) {
                        uri = Uri.parse(userInfo.head + TencentWeibo.HEAD_LARGE_SIZE);
                    }
                    else {
                        File file = new File(userInfo.head);
                        uri = Uri.fromFile(file);
                    }
                    d = MyApplication.imageCache.loadImage(imageID, uri, 0, 0);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                if (d != null)
                    head.setImageDrawable(d);
                else
                    head.setImageResource(R.drawable.portrait_small);
                head.invalidate();
            }
        }
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        headContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
        sexContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
        nickContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
        descriptionContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_middle));
        birthdayContainer.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_bottom));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            String capturePath = intent.getStringExtra(ITransKey.KEY);
            if (userInfo != null && !TextUtils.isEmpty(capturePath)) {
                File file = new File(capturePath);
                if (file.exists()) {
                    userInfo.head = capturePath;
                    updateViews();
                }
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case INTENT_EDIT_NICKNAME:
                    userInfo.nick = data.getStringExtra(ITransKey.KEY);
                    updateViews();
                    break;
                case INTENT_EDIT_INTRODUCTION:
                    userInfo.introduction = data.getStringExtra(ITransKey.KEY);
                    updateViews();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ITransKey.KEY, editInfo);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra(ITransKey.KEY, editInfo);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.save:
            	if (NetUtils.getType(this) == NetUtils.NO_NET) {
            		ToastUtils.show(this, R.string.msg_nonetwork, Toast.LENGTH_SHORT);
            		return;
            	}
                userInfo.sex = male.isChecked() ? 1 : 2;
                if (!userInfo.head.startsWith("http")) {
                    final UserTask<Object, Void, Boolean> task = saveTask;
                    if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                        return;
                    }
                    final UserTask<Object, Void, Boolean> task2 = updateHeadTask;
                    if (task2 != null && task2.getStatus() != UserTask.Status.FINISHED) {
                        return;
                    }
                    File file = new File(userInfo.head);
                    String picPath = file.getPath();
                    UserTask<Object, Void, Boolean> updateHeadTask = new UpdateHeadTask(mainContainer, loadListener,
                            resultListener);
                    UserTask<Object, Void, Boolean> updateTask = new UpdateTask(mainContainer, loadListener,
                            resultListener);
                    updateHeadTask = updateHeadTask.execute(picPath);
                    saveTask = updateTask.execute(userInfo.nick, userInfo.sex, userInfo.birth_year,
                            userInfo.birth_month,
                            userInfo.birth_day, userInfo.country_code, userInfo.province_code,
                            userInfo.city_code, userInfo.introduction);
                } else {
                    final UserTask<Object, Void, Boolean> task = saveTask;
                    if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                        return;
                    }
                    saveTask = new UpdateTask(mainContainer, loadListener, resultListener).execute(
                            userInfo.nick, userInfo.sex, userInfo.birth_year, userInfo.birth_month,
                            userInfo.birth_day, userInfo.country_code, userInfo.province_code,
                            userInfo.city_code, userInfo.introduction);
                }
                break;
            case R.id.user_info_head_container:
                File cacheBase = null;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                    cacheBase = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + Constant.cacheDir + Constant.imageFolder);
                else
                    cacheBase = new File(getCacheDir() + File.separator + Constant.imageFolder);
                String cacheName = MessageDigestUtil.getInstance().hash(
                        "tmp_avatar" /*
                                      * +
                                      * String.valueOf(System.currentTimeMillis
                                      * ())
                                      */);
                File cacheFile = new File(cacheBase.getAbsolutePath() + File.separator + cacheName
                        + ".png");
                intent = new Intent(this, ImageCropActivity.class);
                intent.putExtra(ITransKey.KEY, cacheFile.getPath());
                startActivity(intent);
                break;
            case R.id.user_info_nick_container:
                intent = new Intent(this, EditUserInfoInputActivity.class);
                intent.putExtra(ITransKey.KEY, userInfo.nick);
                startActivityForResult(intent, INTENT_EDIT_NICKNAME);
                break;
            case R.id.user_info_description_container:
                intent = new Intent(this, EditUserInfoInputActivity.class);
                intent.putExtra(ITransKey.KEY, userInfo.introduction);
                startActivityForResult(intent, INTENT_EDIT_INTRODUCTION);
                break;
            case R.id.user_info_birthday_container:
                if (userInfo != null) {
                    final Calendar c = Calendar.getInstance();
                    new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                        // when dialog box is closed, below method will be
                        // called.
                        public void onDateSet(DatePicker view, int selectedYear,
                                int selectedMonth, int selectedDay) {
                            userInfo.birth_year = selectedYear;
                            userInfo.birth_month = selectedMonth + 1;
                            userInfo.birth_day = selectedDay;
                            updateViews();
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
                break;
        }
    }

    private ILoadListener loadListener = new ILoadListener() {

        @Override
        public void onStart(TextView v) {
            v.setText("");
        }

        @Override
        public void onEnd() {
        }

    };

    private IResultListener resultListener = new IResultListener() {

        @Override
        public void onSuccess(Entity<TObject> data) {
            if (data != null) {
                if (data.data instanceof Info) {
                    if (!TextUtils.isEmpty(data.response)) {
                        File cacheBase = null;
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.cacheDir + Constant.dataFolder);
                        else
                            cacheBase = new File(getCacheDir() + File.separator + Constant.dataFolder);
                        String cacheName = MessageDigestUtil.getInstance().hash(Constant.SELFINFO_CACHE_FILE);
                        File cacheFile = new File(cacheBase.getAbsolutePath()
                                + File.separator +
                                MessageDigestUtil.getInstance().hash(TencentWeibo.getInstance().getOpenid()) + File.separator
                                + cacheName);
                        Entity.cache2file(cacheFile, data.response);
                    }
                    editInfo = userInfo = (Info) data.data;
                    updateViews();

                    save.post(new Runnable() {

                        @Override
                        public void run() {
                            ToastUtils.show(head.getContext(), R.string.modify_userinfo_success,
                                    Toast.LENGTH_SHORT);
                        }

                    });
                }
            }
        }

        @Override
        public void onFail(Entity<TObject> data) {
            save.post(new Runnable() {

                @Override
                public void run() {
                    ToastUtils.show(head.getContext(), R.string.modify_userinfo_fail,
                            Toast.LENGTH_SHORT);
                }

            });
        }

    };
}
