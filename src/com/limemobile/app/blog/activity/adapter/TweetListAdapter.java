package com.limemobile.app.blog.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.preference.Setting;
import com.limemobile.app.blog.activity.preference.Setting.ReadMode;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tweet;
import com.limemobile.app.blog.widget.TweetView;
import com.limemobile.app.utils.StringUtils;

import java.util.ArrayList;

public class TweetListAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Tweet> array;
    private final OnClickListener clickListener;
    public TweetListAdapter(Context context, ArrayList<Tweet> array, OnClickListener clickListener) {
        this.context = context;
        this.array = array;
        this.clickListener = clickListener;
    }
    
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.tweet_item_view, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            
            holder.headImage = (ImageView) convertView.findViewById(R.id.item_image);
            holder.vipImage = (ImageView) convertView.findViewById(R.id.item_subimage);
            holder.userName = (TextView) convertView.findViewById(R.id.item_title);
            holder.gpsImage = (ImageView) convertView.findViewById(R.id.item_gps);
            holder.picImage = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.date = (TextView) convertView.findViewById(R.id.item_date);
            holder.tweetContent = (TweetView) convertView.findViewById(R.id.item_content);
            holder.tweetPic1 = (ImageView) convertView.findViewById(R.id.tweet_upload_pic1);
            holder.subTweet = (ViewGroup) convertView.findViewById(R.id.subLayout);
            holder.subTweetContent = (TweetView) convertView.findViewById(R.id.sub_item_content);
            holder.subTweetPic1 = (ImageView) convertView.findViewById(R.id.tweet_upload_pic2);
            holder.from = (TextView) convertView.findViewById(R.id.tweet_form);
            holder.tweetRedirect = (TextView) convertView.findViewById(R.id.tweet_redirect);
            holder.tweetComment = (TextView) convertView.findViewById(R.id.tweet_comment);
            
            holder.tweetContent.setLinkClickable(false);
            holder.subTweetContent.setLinkClickable(false);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Tweet tweet = array.get(position);
        holder.object = tweet;
        
        holder.tweetContent.setTextSize(Setting.contentTextSize);
        holder.subTweetContent.setTextSize(Setting.contentTextSize);
        
        holder.headImage.setImageDrawable(null);
        holder.headImage.setTag(null);
        holder.headImage.setImageResource(R.drawable.portrait_small);
        if (!TextUtils.isEmpty(tweet.head) && Setting.readMode == ReadMode.Image)
            holder.headImage.setTag(Uri.parse(tweet.head + TencentWeibo.HEAD_MEDIUM_SIZE));
        
        holder.vipImage.setVisibility(tweet.isvip > 0 ? View.VISIBLE : View.INVISIBLE);
        
        holder.userName.setText(tweet.nick);
        holder.date.setText(StringUtils.friendly_time(context, tweet.timestamp));
        
        if (!TextUtils.isEmpty(tweet.geo))
            holder.gpsImage.setVisibility(View.VISIBLE);
        else
            holder.gpsImage.setVisibility(View.GONE);
        
        holder.tweetContent.setText(tweet.text);
        
        holder.tweetPic1.setImageDrawable(null);
        holder.tweetPic1.setTag(null);
        holder.subTweetPic1.setImageDrawable(null);
        holder.subTweetPic1.setTag(null);
        
        if (tweet.image != null && !tweet.image.isEmpty() && !TextUtils.isEmpty(tweet.image.get(0)) && Setting.readMode == ReadMode.Image) {
            holder.picImage.setVisibility(View.VISIBLE);
            holder.tweetPic1.setTag(Uri.parse(tweet.image.get(0) + TencentWeibo.PICTURE_MEDIUM_SIZE));
            holder.tweetPic1.setVisibility(View.VISIBLE);
            ((View)holder.tweetPic1.getParent()).setTag(tweet);
            holder.tweetPic1.setOnClickListener(clickListener);
        } else {
        	((View)holder.tweetPic1.getParent()).setTag(null);
        	holder.tweetPic1.setOnClickListener(null);
        	holder.tweetPic1.setVisibility(View.GONE);
        	holder.picImage.setVisibility(View.GONE);
        }
        
        // 微博类型，1-原创发表，2-转载，3-私信，4-回复，5-空回，6-提及，7-评论
        if (tweet.type != 1)
            holder.subTweet.setVisibility(View.VISIBLE);
        else
            holder.subTweet.setVisibility(View.GONE);
        
        if (tweet.source != null) {
            holder.subTweetContent.setText("@" + tweet.nick + "：" + tweet.source.text);
            
            if (tweet.source.image != null && !tweet.source.image.isEmpty() && !TextUtils.isEmpty(tweet.source.image.get(0)) && Setting.readMode == ReadMode.Image) {
                holder.subTweetPic1.setTag(Uri.parse(tweet.source.image.get(0) + TencentWeibo.PICTURE_MEDIUM_SIZE));
                holder.subTweetPic1.setVisibility(View.VISIBLE);
                holder.picImage.setVisibility(View.VISIBLE);
                ((View)holder.subTweetPic1.getParent()).setTag(tweet.source);
                holder.subTweetPic1.setOnClickListener(clickListener);
            } else {
            	holder.subTweetPic1.setVisibility(View.GONE);
            	holder.picImage.setVisibility(View.GONE);
            	((View)holder.subTweetPic1.getParent()).setTag(null);
            	holder.subTweetPic1.setOnClickListener(null);
            }
        } else
            holder.subTweet.setVisibility(View.GONE);
        
        if (!TextUtils.isEmpty(tweet.from)) {
            String from = String.format(context.getString(R.string.from), tweet.from);
            holder.from.setText(from);
        } else
            holder.from.setVisibility(View.GONE);
        
        if (tweet.count > 0) {
            holder.tweetRedirect.setText(StringUtils.friendly_long(context, tweet.count));
            holder.tweetRedirect.setVisibility(View.VISIBLE);
        } else
            holder.tweetRedirect.setVisibility(View.GONE);
        
        if (tweet.mcount > 0) {
            holder.tweetComment.setText(StringUtils.friendly_long(context, tweet.mcount));
            holder.tweetComment.setVisibility(View.VISIBLE);
        } else
            holder.tweetComment.setVisibility(View.GONE);
        
        return convertView;
    }

    public class ViewHolder {
        public Object object;
        public ImageView headImage;
        public ImageView vipImage;
        
        public TextView userName;
        public ImageView gpsImage;
        public ImageView picImage;
        public TextView date;
        
        public TweetView tweetContent;
        public ImageView tweetPic1;
        
        public ViewGroup subTweet;
        public TweetView subTweetContent;
        public ImageView subTweetPic1;
        
        public TextView from;
        public TextView tweetRedirect;
        public TextView tweetComment;
    }
}
