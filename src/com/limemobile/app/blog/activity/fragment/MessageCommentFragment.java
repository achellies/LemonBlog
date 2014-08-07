
package com.limemobile.app.blog.activity.fragment;

import com.limemobile.app.blog.R;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageCommentFragment extends ThemeFragment {

    @Override
	public void onCreate(Bundle savedInstanceState) {
    	setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        TextView text = new TextView(getActivity());
        text.setGravity(Gravity.CENTER);
        text.setText(getResources().getString(R.string.message_comment));
        text.setTextSize(20 * getResources().getDisplayMetrics().density);
        text.setPadding(20, 20, 20, 20);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(text);

        return layout;
    }

    @Override
    public void themeChanged() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initViews(ViewGroup container) {
        // TODO Auto-generated method stub

    }

}
