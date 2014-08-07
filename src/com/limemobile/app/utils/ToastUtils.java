
package com.limemobile.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

public class ToastUtils {
    public static void show(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence s, int duration) {
        Toast toast = Toast.makeText(context, s, duration);
        // Toast toast = new Toast(context);
        // toast.setDuration(duration);
        // LayoutInflater inflater = (LayoutInflater)
        // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View view = inflater.inflate(R.layout.toast, null);
        // TextView text = (TextView)
        // view.findViewById(R.id.widget_content_text);
        // text.setText(s);
        // view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
        // LayoutParams.WRAP_CONTENT));
        // toast.setView(view);
        toast.show();
    }
}
