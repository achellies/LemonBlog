
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
import com.limemobile.app.blog.activity.UsersListActivity;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.entity.friends.Friend;

public class UsersListAdapter extends BaseAdapter {
    private final Context context;
    private final int listType;
    private final ArrayList<Friend.Info> array;
    private final OnClickListener clickListener;

    public UsersListAdapter(Context context, int listType, ArrayList<Friend.Info> friends,
            OnClickListener clickListener) {
        this.context = context;
        this.listType = listType;
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
        Friend.Info friend = array.get(position);
        holder.object = friend;
        holder.title.setText(friend.nick);
        if (friend.tweet != null && friend.tweet.size() > 0)
            holder.subTitle.setText(friend.tweet.get(0).text);

        holder.image.setImageDrawable(null);
        holder.image.setTag(null);
        holder.image.setImageResource(R.drawable.portrait_small);
        if (!TextUtils.isEmpty(friend.head)) {
            holder.image.setTag(Uri.parse(friend.head + TencentWeibo.HEAD_MEDIUM_SIZE));
        }

        holder.subImage.setVisibility(friend.isvip > 0 ? View.VISIBLE : View.INVISIBLE);

        holder.operation.setOnClickListener(clickListener);
        holder.operation.setTag(friend);
        Resources res = Theme.getInstance().getContext(context).getResources();
        if (listType == UsersListActivity.UserListType.Blacklist.getNumericType()) {
            holder.operation.setBackgroundDrawable(res.getDrawable(R.drawable.btn_orange));
            holder.operation.setText(res.getString(R.string.delete_from_blacklist));
        } else {
            if (friend.isidol) {
                holder.operation.setBackgroundDrawable(res.getDrawable(R.drawable.btn_orange));
                holder.operation.setText(res.getString(R.string.user_delattention));
            } else {
                holder.operation.setBackgroundDrawable(res.getDrawable(R.drawable.btn_green));
                holder.operation.setText(res.getString(R.string.attention));
            }
        }

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
