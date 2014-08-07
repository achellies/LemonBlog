
package com.limemobile.app.blog.activity.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.utils.APKUtil;
import com.limemobile.app.utils.MessageDigestUtil;

public class SkinListAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<SkinItem> array;
    private final OnClickListener clickListener;

    public SkinListAdapter(Context context, ArrayList<SkinItem> skins,
            OnClickListener clickListener) {
        this.context = context;
        this.array = skins;
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.skin_list_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.subTitle = (TextView) convertView.findViewById(R.id.item_subtitle);
            holder.operation = (TextView) convertView.findViewById(R.id.item_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SkinItem skinItem = array.get(position);
        holder.object = skinItem;
        holder.title.setText(skinItem.name);
        holder.subTitle.setVisibility(View.GONE);

        holder.operation.setOnClickListener(clickListener);
        holder.operation.setTag(skinItem);
        Resources res = Theme.getInstance().getContext(context).getResources();
        
        if (Theme.getInstance().currentThemePackage().equals(skinItem.packageName)) {
        	holder.operation.setBackgroundDrawable(res.getDrawable(R.drawable.btn_green));
        	if (skinItem.canUpdate) {
        		if (skinItem.downloading)
        			holder.operation.setText(res.getString(R.string.theme_skin_download_update));
        		else
        			holder.operation.setText(res.getString(R.string.theme_skin_download_using));
        	} else {
        		holder.operation.setText(res.getString(R.string.theme_skin_download_update));
        	}
        } else if (APKUtil.isPackageInstalled(context, skinItem.packageName)) {
            holder.operation.setBackgroundDrawable(res.getDrawable(R.drawable.btn_green));
            holder.operation.setText(res.getString(R.string.theme_skin_download_use));
        } else {
        	holder.operation.setBackgroundDrawable(res.getDrawable(R.drawable.btn_silver));
        	if (skinItem.downloading) {
        		holder.operation.setBackgroundDrawable(res.getDrawable(R.drawable.btn_green));
        		holder.operation.setText(res.getString(R.string.theme_skin_download_cancel));
        	}
        	else {
    	        File cacheBase = null;
    	        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    	            cacheBase = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
    	                    + Constant.cacheDir + Constant.downloadFolder);
    	        else
    	            cacheBase = new File(context.getCacheDir() + File.separator + Constant.downloadFolder);
    	        File themeFile = new File(cacheBase.getAbsolutePath() + File.separator +
    	                MessageDigestUtil.getInstance().hash(TencentWeibo.getInstance().getOpenid())
    	                + File.separator + skinItem.packageName);
    	        if (themeFile.exists()) {
    	        	String packageName = APKUtil.isPackageArchive(context, themeFile.getAbsolutePath());
    	        	if (!TextUtils.isEmpty(packageName) && skinItem.packageName.equals(packageName)) {
    	        		holder.operation.setBackgroundDrawable(res.getDrawable(R.drawable.btn_green));
    	        		holder.operation.setText(res.getString(R.string.theme_skin_download_use));
    	        	} else
    	        		holder.operation.setText(res.getString(R.string.theme_skin_download_download));
    	        } else
    	        	holder.operation.setText(res.getString(R.string.theme_skin_download_download));
        	}
        }
        
        if (position == 0) {
            if (position == (getCount() - 1)) {
                // there is only one item
            	convertView.setBackgroundResource(R.drawable.circle_list_middle);
            } else {
                // first item
            	convertView.setBackgroundResource(R.drawable.circle_list_top);
            }
        } else if (position == (getCount() - 1)) {
            // last item
        	convertView.setBackgroundResource(R.drawable.circle_list_bottom);
        } else {
            // mid item
        	convertView.setBackgroundResource(R.drawable.circle_list_middle);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public TextView subTitle;
        public TextView operation;
        public Object object;
    }
    
    public static class SkinItem {
    	public String name;
    	public long totalSize;
    	public long size;
    	public boolean canUpdate;
    	public boolean downloading;
    	public String version;
    	public String donwloadUrl;
    	public String packageName;
    }
}
