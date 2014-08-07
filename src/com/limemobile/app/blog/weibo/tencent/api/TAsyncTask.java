
package com.limemobile.app.blog.weibo.tencent.api;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.utils.UserTask;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 异步加载基类s
 * 
 * @param <T>
 */
public abstract class TAsyncTask<T> extends UserTask<Object, Void, Boolean> {
    protected final ILoadListener loadListener;
    protected final IResultListener resultListener;
    protected final ViewGroup baseContainer;
    protected RelativeLayout maskContainer;
    protected ViewGroup progressBar;
    protected TextView progressText;

    public TAsyncTask(ILoadListener loadListener, IResultListener successListener) {
        this(null, loadListener, successListener);
    }

    public TAsyncTask(ViewGroup container, ILoadListener loadListener,
            IResultListener successListener) {
        this.loadListener = loadListener;
        this.resultListener = successListener;
        this.baseContainer = container;

        if (container != null) {
            maskContainer = new RelativeLayout(container.getContext());
            maskContainer.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }

            });
            ColorDrawable mask = new ColorDrawable(Color.BLACK);
            mask.setAlpha(100);
            maskContainer.setBackgroundDrawable(mask);
            progressBar = (ViewGroup) LayoutInflater.from(container.getContext()).inflate(
                    R.layout.loadingbar, null);
            progressText = (TextView) progressBar.findViewById(R.id.progress_loading_text);
        }

        if (this.resultListener != null)
            this.resultListener.addTask(this);
    }

    public abstract String callAPI(Object... params);

    public boolean parse(Entity<TObject> data, JSONObjectProxy object) {
        return true;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        Boolean isOk = Boolean.FALSE;
        String result = callAPI(params);
        Entity<TObject> data = new Entity<TObject>();
        if (!TextUtils.isEmpty(result)) {
            try {
                JSONObjectProxy object = new JSONObjectProxy(new JSONObject(result));

                data.response = result;
                data.ret = object.getIntOrNull(Result.RETURN_CODE_KEY);
                data.errcode = object.getIntOrNull(Result.ERROR_CODE_KEY);
                data.msg = object.getStringOrNull(Result.ERROR_MESSAGE_KEY);
                data.seqid = object.getStringOrNull(Result.SEQID_KEY);
                if (data.ret == Result.RETURN_CODE_SUCCESS) {
                    isOk = Boolean.TRUE;
                    parse(data, object);
                }
            } catch (JSONException e) {
//                e.printStackTrace();
            }
        }
        if (resultListener != null) {
            resultListener.removeTask(this);
            if (isOk) {
                if (resultListener.isTaskEmtpy())
                    resultListener.onSuccess(data);
            }
            else {
                resultListener.clearPendingTask();
                resultListener.onFail(data);
            }
        }
        return isOk;
    }

    @Override
    protected void onPreExecute() {
        if (baseContainer != null && maskContainer != null) {
            baseContainer.post(new Runnable() {

                @Override
                public void run() {
                    final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    maskContainer.addView(progressBar, params);
                    baseContainer.addView(maskContainer, new LayoutParams(LayoutParams.FILL_PARENT,
                            LayoutParams.FILL_PARENT));
                    baseContainer.invalidate();
                }

            });
        }
        if (loadListener != null)
            loadListener.onStart(progressText);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean isOk) {
        if (baseContainer != null && maskContainer != null) {
            baseContainer.post(new Runnable() {

                @Override
                public void run() {
                    baseContainer.removeView(maskContainer);
                }

            });
        }
        if (loadListener != null)
            loadListener.onEnd();
        super.onPostExecute(isOk);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

}
