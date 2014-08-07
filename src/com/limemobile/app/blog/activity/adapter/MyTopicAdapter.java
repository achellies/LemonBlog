
package com.limemobile.app.blog.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.weibo.tencent.entity.ht.HuaTi;
import com.limemobile.app.utils.StringUtils;

import java.util.ArrayList;

public class MyTopicAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<HuaTi.Info> array;

    public MyTopicAdapter(Context context, ArrayList<HuaTi.Info> topics) {
        this.context = context;
        this.array = topics;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.hot_topic_list_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.subTitle = (TextView) convertView.findViewById(R.id.item_subtitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HuaTi.Info topic = array.get(position);
        holder.object = topic;
        holder.title.setText("#" + topic.text + "#");
        holder.title.setTextColor(context.getResources().getColor(R.color.huati_color));
        String subText = context.getString(R.string.user_info_favorite) + ": " + StringUtils.friendly_long(context, topic.favnum) + " " +
                context.getString(R.string.user_info_tweet) + ": " + StringUtils.friendly_long(context, topic.tweetnum);
        holder.subTitle.setText(subText);

        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public TextView subTitle;
        public Object object;
    }
}
