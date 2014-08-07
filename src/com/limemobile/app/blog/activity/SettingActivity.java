package com.limemobile.app.blog.activity;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.utils.FileUtils;
import com.limemobile.app.utils.ToastUtils;
import com.limemobile.app.utils.UserTask;

public class SettingActivity extends PreferenceActivity implements OnClickListener {
    private View back;
    private TextView title;
    private ViewGroup titlebar;
    
    private ProgressDialog progressDialog;
    private UserTask<Object, Void, Boolean> clearCacheTask;
    
    private int autoRemindType;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setRequestedOrientation(Setting.screenOrientation);
	    super.onCreate(savedInstanceState);
	    
		addPreferencesFromResource(R.xml.setting);
		setContentView(R.layout.activity_setting);
		
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titlebar = (ViewGroup) findViewById(R.id.main_titlebar);
        title = (TextView) findViewById(R.id.title);
        
        Resources res = Theme.getInstance().getContext(this).getResources();
        back.setBackgroundDrawable(res.getDrawable(R.drawable.title_back));
        titlebar.setBackgroundDrawable(res.getDrawable(R.drawable.title_bar_bg));
        title.setText(res.getString(R.string.more_setting));
        
        getPreferenceScreen().findPreference("screenorientation").setOnPreferenceChangeListener(preferenceChangeListener);
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isFinishing()) {
            final UserTask<Object, Void, Boolean> task = clearCacheTask;
            if (task != null && task.getStatus() != UserTask.Status.FINISHED) {
                task.cancel(true);
                clearCacheTask = null;
            }
        }
    }
    
    @Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals("clear_cache")) {
    		new AlertDialog.Builder(this)
    		.setTitle(R.string.preferences_clear_cache_title)
    		.setMessage(R.string.setting_need_clear_cache)
    		.setIcon(R.drawable.moreitems_about_icon)
    		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    				clearCacheTask = new ClearCacheTask().execute();
    			}
    		})
    		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    			}
    		})
    		.show();
		} else if (preference.getKey().equals("remind_category")) {
		    autoRemindType = Setting.autoRemindType;
		    
			String[] notifyCategory = new String[] {
				getString(R.string.pref_setting_weibo),
				getString(R.string.pref_setting_at),
				getString(R.string.pref_setting_comment),
				getString(R.string.pref_setting_message),
				getString(R.string.pref_setting_fans)
			};
			boolean[] notifyCategoryValue = new boolean[] {
					(Setting.autoRemindType & Setting.weiboRemind) > 0,
					(Setting.autoRemindType & Setting.atRemind) > 0,
					(Setting.autoRemindType & Setting.commentRemind) > 0,
					(Setting.autoRemindType & Setting.messageRemind) > 0,
					(Setting.autoRemindType & Setting.fansRemind) > 0
					
			};
			Dialog dialog = new AlertDialog.Builder(this)
					.setIcon(R.drawable.moreitems_about_icon)
					.setTitle(R.string.pref_setting_notify)
					.setMultiChoiceItems(
							notifyCategory,
							notifyCategoryValue,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {
								    int value = 0;
								    switch (whichButton) {
								        case 0:
								            value = Setting.weiboRemind;
								            break;
								        case 1:
								            value = Setting.atRemind;
								            break;
								        case 2:
								            value = Setting.commentRemind;
								            break;
								        case 3:
								            value = Setting.messageRemind;
								            break;
								        case 4:
								            value = Setting.fansRemind;
								            break;
								    }
                                    if (isChecked)
                                        autoRemindType |= value;
                                    else
                                        autoRemindType ^= value;
								}
							})
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
									
									Setting.autoRemindType = autoRemindType;
                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("remind_category", Setting.autoRemindType);
                                    editor.commit();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
								}
							}).create();
			dialog.show();
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

	@Override
	protected void onPause() {
		Setting.settingChanged(this);
		super.onPause();
	}
	
	private OnPreferenceChangeListener preferenceChangeListener = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference,
				Object newValue) {
			if (preference.getKey().equals("screenorientation")) {
				Boolean autoOrientation = (Boolean) newValue;
				
				Theme.getInstance().changeScreenOrientation(autoOrientation ? ActivityInfo.SCREEN_ORIENTATION_SENSOR : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
			return true;
		}
    	
    };
	
	private class ClearCacheTask extends UserTask<Object, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SettingActivity.this);
	        progressDialog.setCancelable(false);
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setMessage(getText(R.string.setting_doing));
	        progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Object... params) {
		    {
		        File cacheBase = null;
		        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
		                    + Constant.cacheDir + Constant.imageFolder);
		        else
		            cacheBase = new File(SettingActivity.this.getCacheDir() + File.separator + Constant.imageFolder);
		        if (cacheBase.exists() && cacheBase.canRead())
		        	FileUtils.deleteDirectory(cacheBase.getAbsolutePath());
		    }
		    {
		        File cacheBase = null;
		        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
		                    + Constant.cacheDir + Constant.dataFolder);
		        else
		            cacheBase = new File(SettingActivity.this.getCacheDir() + File.separator + Constant.dataFolder);
		        if (cacheBase.exists() && cacheBase.canRead())
		        	FileUtils.deleteDirectory(cacheBase.getAbsolutePath());
		    }
		    {
		        File cacheBase = null;
		        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
		                    + Constant.cacheDir + Constant.tempFolder);
		        else
		            cacheBase = new File(SettingActivity.this.getCacheDir() + File.separator + Constant.tempFolder);
		        if (cacheBase.exists() && cacheBase.canRead())
		        	FileUtils.deleteDirectory(cacheBase.getAbsolutePath());
		    }
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			ToastUtils.show(SettingActivity.this, R.string.setting_success_clear, Toast.LENGTH_SHORT);
			super.onPostExecute(result);
		}
	}
}
