
package com.limemobile.app.blog.activity.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.entity.friends.Mutual;
import com.limemobile.app.utils.StringUtils;

public class MutualListAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Mutual.Info> array;
    private final OnClickListener clickListener;

    public MutualListAdapter(Context context, ArrayList<Mutual.Info> friends,
            OnClickListener clickListener) {
        this.context = context;
        this.array = friends;
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
        Mutual.Info friend = array.get(position);
        holder.object = friend;
        holder.title.setText(friend.nick);

        String subTilte = context.getString(R.string.user_info_fans) + ": "
                + StringUtils.friendly_long(context, friend.fansnum) + " "
                + context.getString(R.string.user_info_follow) + ": "
                + StringUtils.friendly_long(context, friend.idolnum);
        holder.subTitle.setText(subTilte);

        holder.image.setImageDrawable(null);
        holder.image.setTag(null);
        holder.image.setImageResource(R.drawable.portrait_small);
        if (!TextUtils.isEmpty(friend.headurl))
            holder.image.setTag(Uri.parse(friend.headurl + TencentWeibo.HEAD_MEDIUM_SIZE));

        holder.subImage.setVisibility(friend.isvip > 0 ? View.VISIBLE : View.INVISIBLE);

        holder.operation.setOnClickListener(clickListener);
        holder.operation.setTag(friend);
        Resources res = Theme.getInstance().getContext(context).getResources();

        holder.operation.setBackgroundDrawable(res.getDrawable(R.drawable.btn_orange));
        holder.operation.setText(res.getString(R.string.user_delattention));

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
