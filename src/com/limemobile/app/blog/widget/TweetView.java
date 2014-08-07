
package com.limemobile.app.blog.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.limemobile.app.blog.weibo.TencentWeibo;

import java.util.Set;

public class TweetView extends TextView {
    public enum ClickableType {
        Link, Huati, User, Face
    }
    
    private OnWidgetClick mOnWidgetClickListener;
    private String mOriginalText;
    private boolean mLinkClickable = true;
    
    public TweetView(Context context) {
        super(context, (AttributeSet) null, android.R.attr.textViewStyle);
    }

    public TweetView(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.textViewStyle);
    }

    public TweetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public CharSequence getText() {
        return mOriginalText;
    }
    
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null) {
            mOriginalText = text.toString();
            Spanned span = Html.fromHtml(mOriginalText);
            parserClickableItem(span, type);
        }
    }
    
    private void parserClickableItem(Spanned span, BufferType type) {
        SpannableStringBuilder style = new SpannableStringBuilder(span);
        
        String content = span.toString();
        int length = content.length();
        URLSpan[] urls = span.getSpans(0, span.length(), URLSpan.class);
        
        int start = 0;
        while (true) {
            if (start >= length)
                break;
            boolean oneStep = true;
            char character = content.charAt(start);
            
            if (character == 'h') {
                // http
                String temp = content.substring(start);
                temp = temp.trim();
                for (int i = 0; i < urls.length; ++i) {
                    if (temp.equalsIgnoreCase(urls[i].getURL()) || temp.startsWith(urls[i].getURL())) {
                        oneStep = false;
                        
                        style.removeSpan(urls[i]);
                        
                        MyClickableSpan mySpan = new MyClickableSpan(ClickableType.User, urls[i].getURL());

                        style.setSpan(mySpan, start, start + urls[i].getURL().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        start += urls[i].getURL().length() + 1;
                        break;
                    }
                }
            } else if (character == '/') {
                // face
                Set<String> keys = TencentWeibo.faceMap.keySet();
                String temp = content.substring(start + 1);
                temp = temp.toLowerCase();
                for (String key : keys) {
                    if (temp.startsWith(key)) {
                        oneStep = false;
                        
                        ImageSpan mySpan = new ImageSpan(mImageGetter.getDrawable(TencentWeibo.faceMap.get(key)), DynamicDrawableSpan.ALIGN_BOTTOM);
                        
                        style.setSpan(mySpan, start, start + key.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        start += key.length() + 1;
                        break;
                    }
                }
            } else if (character == '@') {
                // at
                int subIndex = start + 1;
                while (true) {
                    if (subIndex >= length)
                        return;
                    
                    char subCharacter = content.charAt(subIndex);
                    if (subCharacter == ' ' 
                            || (subCharacter == ',') 
                            || (subCharacter == ';')
                            || (subCharacter == '.') 
                            || (subCharacter == ':') 
                            || (subCharacter == '@') 
                            || (subCharacter == '#') 
                            || (subCharacter == '\n')
                            || (subCharacter == '，')
                            || (subCharacter == '：')) {
                        oneStep = false;
                        
                        String temp = content.substring(start, subIndex);
                        MyClickableSpan mySpan = new MyClickableSpan(ClickableType.User, temp);
                        
                        style.setSpan(mySpan, start, subIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        start = subIndex;
                        break;
                    }
                    ++subIndex;
                }
            } else if (character == '#') {
                int index = content.indexOf("#", start + 1);
                if (index > 0) {
                    oneStep = false;
                    
                    String huati = content.substring(start, index + 1);
                    MyClickableSpan mySpan = new MyClickableSpan(ClickableType.Huati, huati);
                    
                    style.setSpan(mySpan, start, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = index + 2;
                }
            }
            
            if (oneStep)
                ++start;
        }
        super.setText(style, type);
        if (mLinkClickable)
            setMovementMethod(LinkMovementMethod.getInstance());
    }

    public OnWidgetClick getOnWidgetClickListener() {
        return mOnWidgetClickListener;
    }

    public void setOnWidgetClickListener(OnWidgetClick listener) {
        this.mOnWidgetClickListener = listener;
    }
    
    public boolean isLinkClickable() {
        return mLinkClickable;
    }

    public void setLinkClickable(boolean mClickable) {
        this.mLinkClickable = mClickable;
    }

    private Html.ImageGetter mImageGetter = new Html.ImageGetter() {
        
        @Override
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            String sourceName = getContext().getPackageName() + ":drawable/" + source;
            int id = getResources().getIdentifier(sourceName, null, null);
            if (id != 0) {
                drawable = getResources().getDrawable(id);
                if (drawable != null) {
                    drawable.setBounds(0, 0,
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight());
                }
            }
            return drawable;
        }
    };

    private class MyClickableSpan extends ClickableSpan {

        private String mClickableText;
        private ClickableType mType;

        MyClickableSpan(ClickableType type, String text) {
            mType = type;
            mClickableText = text;
        }

        @Override
        public void onClick(View widget) {
            if (mOnWidgetClickListener != null)
                mOnWidgetClickListener.onClick(widget, mType, mClickableText);
        }
    }
    
    public interface OnWidgetClick {
        void onClick(View widget, ClickableType type, String text);
    }
}
