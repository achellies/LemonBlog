
package com.limemobile.app.blog.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.limemobile.app.blog.R;

public class CornerListView extends ListView {

    public CornerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CornerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CornerListView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int itemnum = pointToPosition(x, y);

                if (itemnum == AdapterView.INVALID_POSITION) {
                    break;
                } else {
                    if (itemnum == 0) {
                        if (itemnum == (getAdapter().getCount() - 1)) {
                            // there is only one item
                            setSelector(R.drawable.circle_list_middle);
                        } else {
                            // first item
                            setSelector(R.drawable.circle_list_top);
                        }
                    } else if (itemnum == (getAdapter().getCount() - 1)) {
                        // last item
                        setSelector(R.drawable.circle_list_bottom);
                    } else {
                        // mid item
                        setSelector(R.drawable.circle_list_middle);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
