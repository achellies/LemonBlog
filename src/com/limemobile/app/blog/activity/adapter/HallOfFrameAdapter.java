
package com.limemobile.app.blog.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.entity.trends.RecommendCelebrity;

import java.util.ArrayList;

public class HallOfFrameAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<RecommendCelebrity.Info> array;

    public HallOfFrameAdapter(Context context, ArrayList<RecommendCelebrity.Info> hallOfFrame) {
        this.context = context;
        this.array = hallOfFrame;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.users_list_item, null);
            holder.image = (ImageView) convertView.findViewById(R.id.item_image);
            holder.subImage = (ImageView) convertView.findViewById(R.id.item_subimage);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.subTitle = (TextView) convertView.findViewById(R.id.item_subtitle);
            holder.operation = (TextView) convertView.findViewById(R.id.item_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecommendCelebrity.Info hallOfFrame = array.get(position);
        holder.object = hallOfFrame;
        holder.title.setText(hallOfFrame.nick);
        holder.subTitle.setText(hallOfFrame.brief);

        holder.image.setImageDrawable(null);
        holder.image.setTag(null);
        holder.image.setImageResource(R.drawable.portrait_small);
        if (!TextUtils.isEmpty(hallOfFrame.head)) {
            holder.image.setTag(Uri.parse(hallOfFrame.head + TencentWeibo.HEAD_MEDIUM_SIZE));
        }

        holder.subImage.setVisibility(View.VISIBLE);

        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public ImageView subImage;
        public TextView title;
        public TextView subTitle;
        public TextView operation;
        public Object object;
    }
}
