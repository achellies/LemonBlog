
package com.limemobile.app.blog.activity.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.bean.Account;

public class AccountListAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Account> array;
    private final OnClickListener clickListener;
    private boolean beginEditing = false;

    public AccountListAdapter(Context context, ArrayList<Account> accounts,
            OnClickListener clickListener) {
        this.context = context;
        this.array = accounts;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.account_list_item, null);
            holder.image = (ImageView) convertView.findViewById(R.id.item_image);
            holder.subImage = (ImageView) convertView.findViewById(R.id.item_subimage);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.operation = (TextView) convertView.findViewById(R.id.item_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Account account = array.get(position);
        holder.object = account;
        holder.title.setText(account.name);

        holder.image.setImageResource(account.active ? R.drawable.icon_green : R.drawable.icon_grey);

        if (beginEditing) {
        	holder.operation.setTag(account);
        	holder.operation.setText(R.string.delete);
        	holder.operation.setOnClickListener(clickListener);
        	holder.operation.setVisibility(View.VISIBLE);
        	holder.subImage.setVisibility(View.GONE);
        } else {
        	holder.operation.setTag(null);
        	holder.operation.setText("");
        	holder.operation.setOnClickListener(null);
        	holder.operation.setVisibility(View.GONE);
        	holder.subImage.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public boolean isBeginEditing() {
		return beginEditing;
	}

	public void setBeginEditing(boolean beginEditing) {
		this.beginEditing = beginEditing;
	}

	public class ViewHolder {
        public ImageView image;
        public ImageView subImage;
        public TextView title;
        public TextView operation;
        public Object object;
    }
}
