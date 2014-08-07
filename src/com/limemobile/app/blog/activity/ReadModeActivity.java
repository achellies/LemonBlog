package com.limemobile.app.blog.activity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.activity.theme.Theme;

public class ReadModeActivity extends ThemeActivity implements OnClickListener, OnSeekBarChangeListener {
    private View back;
    private TextView title;
    private ViewGroup titlebar;
    
    private View imageMode;
    private ImageView imageModeRadio;
    private View onlyTextMode;
    private ImageView onlyTextModeRadio;
    private TextView textSizeLabel;
    private SeekBar textSizeSeekBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_readmode);
        initViews();
    }
    
    @Override
	protected void onPause() {
		Setting.settingChanged(this);
		super.onPause();
	}

	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.readmode_image:
            	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            	SharedPreferences.Editor editor = sp.edit();
            	editor.putInt(Setting.PREF_READMODE, 0);
            	editor.commit();
            	imageModeRadio.setImageResource(R.drawable.btn_check_on);
            	onlyTextModeRadio.setImageResource(R.drawable.btn_check_off);
            	break;
            case R.id.readmode_only_text:
            	sp = PreferenceManager.getDefaultSharedPreferences(this);
            	editor = sp.edit();
            	editor.putInt(Setting.PREF_READMODE, 1);
            	editor.commit();
            	imageModeRadio.setImageResource(R.drawable.btn_check_off);
            	onlyTextModeRadio.setImageResource(R.drawable.btn_check_on);
            	break;
        }
    }

    @Override
    public void initViews() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        
        imageMode = findViewById(R.id.readmode_image);
        imageMode.setOnClickListener(this);
        imageModeRadio = (ImageView) findViewById(R.id.readmode_image_radio);
        onlyTextMode = findViewById(R.id.readmode_only_text);
        onlyTextMode.setOnClickListener(this);
        onlyTextModeRadio = (ImageView) findViewById(R.id.readmode_only_text_radio);
        textSizeLabel = (TextView) findViewById(R.id.readmode_text_size_label);
        textSizeSeekBar = (SeekBar) findViewById(R.id.readmode_text_size);
        
        int readMode = PreferenceManager.getDefaultSharedPreferences(this).getInt(Setting.PREF_READMODE, 0);
        imageModeRadio.setImageResource(readMode == 0? R.drawable.btn_check_on : R.drawable.btn_check_off);
        onlyTextModeRadio.setImageResource(readMode == 1? R.drawable.btn_check_on : R.drawable.btn_check_off);
        textSizeSeekBar.setOnSeekBarChangeListener(this);
        
        float currentContentSize = PreferenceManager.getDefaultSharedPreferences(this).getFloat(Setting.PREF_CONTENT_TEXT_SIZE, 17.0f);
        textSizeSeekBar.setMax((Setting.MAX_CONTENT_TEXT_SIZE - Setting.MIN_CONTENT_TEXT_SIZE) * 10);
        textSizeSeekBar.setProgress((int)(currentContentSize - Setting.MIN_CONTENT_TEXT_SIZE) * 10);
        
        textSizeLabel.setTextSize(currentContentSize);
        textSizeLabel.setText(getString(R.string.readmode_text_size_label) + Float.toString(currentContentSize));
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        back.setBackgroundDrawable(res.getDrawable(R.drawable.title_back));
        titlebar.setBackgroundDrawable(res.getDrawable(R.drawable.title_bar_bg));
        title.setText(res.getString(R.string.more_readmode));
        
        imageMode.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
        onlyTextMode.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_bottom));
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		float currentContentSize = (progress / 10.0f + Setting.MIN_CONTENT_TEXT_SIZE);
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putFloat(Setting.PREF_CONTENT_TEXT_SIZE, currentContentSize);
    	editor.commit();
		textSizeChanged();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {	
	}
	
	private void textSizeChanged() {
        float currentContentSize = PreferenceManager.getDefaultSharedPreferences(this).getFloat(Setting.PREF_CONTENT_TEXT_SIZE, 17.0f);        
        textSizeLabel.setText(getString(R.string.readmode_text_size_label) + Float.toString(currentContentSize));
        textSizeLabel.setTextSize(currentContentSize);
	}
}
