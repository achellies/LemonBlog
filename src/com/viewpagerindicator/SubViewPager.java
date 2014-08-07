package com.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SubViewPager extends ViewPager {
	private GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (!allowInterceptTouchEvent(e1, e2))
				return true;
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (!allowInterceptTouchEvent(e1, e2))
				return true;
			return super.onFling(e1, e2, velocityX, velocityY);
		}
		
	});

	public SubViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SubViewPager(Context context) {
		super(context);
	}
	
	private boolean allowInterceptTouchEvent(MotionEvent e1, MotionEvent e2) {
		if (getAdapter() == null || getAdapter().getCount() == 0)
			return true;
		
		boolean move2Left = e1.getX() > e2.getX();
		boolean move2Right = e1.getX() < e2.getX();
		
		if (move2Left && getCurrentItem() == getAdapter().getCount() - 1)
			return true;
		
		if (move2Right && getCurrentItem() == 0)
			return true;
		
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (getParent() != null && gestureDetector.onTouchEvent(ev)) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}
}
