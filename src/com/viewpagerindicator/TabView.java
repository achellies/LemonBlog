
package com.viewpagerindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TabView extends TextView {
    private int mIndex;

    public TabView(Context context) {
        super(context, null, 0/* R.attr.vpiTabPageIndicatorStyle */);
    }

    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }
}
