package com.limemobile.app.blog.activity.sns;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.ThemeActivity;
import com.limemobile.app.blog.activity.sns.BlowListener.OnBlowListener;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.widget.DandelionView;

import java.util.Date;

public class BlowActivity extends ThemeActivity implements OnClickListener {
    private static final String AUDIO_ENABLE = "audio_enable";
    
    private View mainContainer;
    private View back;
    private TextView title;
    private ViewGroup titlebar;
    
    private ImageView audio;
    private ImageView blowGuide;
    private ImageView dandelion;
    private DandelionView dandelionEffect;
    private boolean audioEnable;
    
    private Handler handler;
    private Animation alphaFadeOut;
    private Animation translateUpAndDown;
    
    private BlowListener blowListener;
    
    private OnBlowListener onBlowListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        onBlowListener = new OnBlowListener() {

            @Override
            public void onBlowStart() {
                blowTrigged();
            }

            @Override
            public void onBlowOver() {
                blowOver();
            }
        };
        
        blowListener = new BlowListener();
        blowListener.setOnBlowListener(onBlowListener);
        
        handler = new Handler();
        alphaFadeOut = new AlphaAnimation(1.0f, 0.0f);
        alphaFadeOut.setDuration(3000);
        alphaFadeOut.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                titlebar.setVisibility(View.GONE);
                audio.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            
        });
        
        translateUpAndDown = AnimationUtils.loadAnimation(this, R.anim.blow_guide);
//        translateUpAndDown = new TranslateAnimation(
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -0.3f
//                );
        translateUpAndDown.setRepeatCount(Animation.INFINITE);
        translateUpAndDown.setDuration(500);
        
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        audioEnable = sp.getBoolean(AUDIO_ENABLE, true);
        
        setContentView(R.layout.activity_blow);
        initViews();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            if (titlebar.getVisibility() != View.VISIBLE) {
                showTitlebar(false);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        blowGuide.startAnimation(translateUpAndDown);
        blowListener.startMonitor();
        Date date = new Date();
        if (date.getHours() >= 6 && date.getHours() <= 18)
            mainContainer.setBackgroundResource(R.drawable.wb_wlog_blow_bg_daytime);
        else
            mainContainer.setBackgroundResource(R.drawable.wb_wlog_blow_bg_night);
       
        showTitlebar(true);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isFinishing()) {
            blowListener.stopMonitor();
        }
    }
    
    private void blowOver() {
        blowGuide.startAnimation(translateUpAndDown);
        blowGuide.setVisibility(View.VISIBLE);
        blowListener.startMonitor();
        dandelion.setImageResource(R.drawable.wb_wlog_blow_parent_dandelion);
    }
    
    private void blowTrigged() {
        blowGuide.clearAnimation();
        blowGuide.setVisibility(View.GONE);
        blowListener.stopMonitor();
        
        Rect outRect = new Rect();
        dandelion.getHitRect(outRect);
        dandelionEffect.setCenterPoint((int)(outRect.left + outRect.width() * 0.46f), (int)(outRect.top + outRect.height() * 0.38f));
        
        dandelionEffect.blowTrigged();
        dandelion.setImageResource(R.drawable.wb_wlog_blow_parent_dandelion06);
    }
    
    private void showTitlebar(boolean onResume) {
        titlebar.setVisibility(View.VISIBLE);
        audio.setVisibility(View.VISIBLE);
        handler.postAtTime(new Runnable() {

            @Override
            public void run() {
                audio.startAnimation(alphaFadeOut);
                titlebar.startAnimation(alphaFadeOut);
            }
            
        }, 1000);
        if (onResume) {
            blowGuide.setVisibility(View.VISIBLE);
            handler.postAtTime(new Runnable() {

                @Override
                public void run() {
                    blowGuide.startAnimation(translateUpAndDown);
                }
                
            }, 1000);
        }
    }

    @Override
    protected void onPause() {
        blowListener.stopMonitor();
        super.onPause();
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_audio:
                audioEnable = !audioEnable;
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit().putBoolean(AUDIO_ENABLE, audioEnable).commit();
                audio.setImageResource(audioEnable ? R.drawable.wb_wlog_blow_sound_nor : R.drawable.wb_wlog_blow_sound_press);
                break;
        }
    }

    @Override
    public void initViews() {
        mainContainer = findViewById(R.id.main_container);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.square_blowing);
        
        dandelionEffect = (DandelionView) findViewById(R.id.dandelion_effect);
        dandelionEffect.setZOrderOnTop(true);
        dandelionEffect.getHolder().setFormat(PixelFormat.TRANSPARENT);
        blowGuide = (ImageView) findViewById(R.id.blow_guide);
        dandelion = (ImageView) findViewById(R.id.dandelion);
        
        dandelionEffect.setOnBlowListener(onBlowListener);
        
        audio = (ImageView) findViewById(R.id.btn_audio);
        audio.setOnClickListener(this);
        audio.setImageResource(audioEnable ? R.drawable.wb_wlog_blow_sound_nor : R.drawable.wb_wlog_blow_sound_press);
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        
        titlebar.setBackgroundDrawable(res.getDrawable(R.drawable.title_bar_bg));
    }
}
