package com.limemobile.app.blog.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.sns.BlowListener.OnBlowListener;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DandelionView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int CORE_POOL_SIZE = 1;
    private static final int KEEP_ALIVE = 1;

    private static final int VALUE_DELTA_MAX = 30;
    private static final long TIME_INTERVAL = 50;
    
    private SurfaceHolder holder;
    
    private Drawable dandelion;
    private Executor executor;
    private DrawTask drawTask;
    
    private OnBlowListener blowListener;
    private Handler handler;
    
    //private Random random;
    private int radius;
    private int centerX;
    private int centerY;
    private int screenWidth;
    private int screenHeight;
    private int[][] positions = new int[36 * 3][2];
    
    public DandelionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DandelionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DandelionView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        holder = getHolder();
        holder.addCallback(this);
        
        //random = new Random();
        
        dandelion = context.getResources().getDrawable(R.drawable.wb_wlog_blow_dandelion);
        
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE,
                KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (blowListener != null)
                    blowListener.onBlowOver();
                drawTask = null;
                super.handleMessage(msg);
            }
        };
        
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }
    
    public void setOnBlowListener(OnBlowListener blowListener) {
        this.blowListener = blowListener;        
    }
    
    public void setCenterPoint(int x, int y) {
        centerX = x;
        centerY = y;
        radius = VALUE_DELTA_MAX;
        for (int index = 0; index < positions.length; ++index) {
            positions[index][0] = centerX;
            positions[index][1] = centerY;
        }
    }
    
    public void blowTrigged() {
        if (drawTask == null) {
            radius = VALUE_DELTA_MAX;
            drawTask = new DrawTask();
            drawTask.executeOnExecutor(executor);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (drawTask != null && drawTask.getStatus() != AsyncTask.Status.FINISHED) {
            drawTask.cancel();
            drawTask = null;
        }
        drawTask = null;
        
        ViewGroup parent = (ViewGroup) getParent();
        ImageView dandelion = (ImageView) parent.findViewById(R.id.dandelion);
        Rect outRect = new Rect();
        dandelion.getHitRect(outRect);
        
        setCenterPoint((int)(outRect.left + outRect.width() * 0.46f), (int)(outRect.top + outRect.height() * 0.38f));
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (drawTask != null && drawTask.getStatus() != AsyncTask.Status.FINISHED) {
            drawTask.cancel();
            drawTask = null;
        }
        drawTask = null;
    }
    
    private class DrawTask extends AsyncTask<Void, Void, Void> {
        private boolean infinite;
        
        public DrawTask() {
            infinite = true;
        }
        
        public void cancel() {
            infinite = false;
            cancel(true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            int width = dandelion.getIntrinsicWidth();
            int height = dandelion.getIntrinsicHeight();
            
            while (infinite) {
                try {
                    radius += VALUE_DELTA_MAX;
                    
                    Canvas canvas = holder.lockCanvas();
                    try {
                        if (canvas != null) {
                            canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
                            for (int index = 0; index < positions.length; ++index) {
                                canvas.save();
                                float degree = (index % 36) * 360.0f / 36;
                                
                                int radius2 = radius - ((int)(index / 36)) * width;
                                if (radius2 > 0) {
	                                positions[index][0] = (int)(centerX + Math.cos(degree * Math.PI * 2/360) * radius2);
	                                positions[index][1] = (int)(centerY + Math.sin(degree * Math.PI * 2/360) * radius2);
	                        		
	                                dandelion.setBounds(positions[index][0] - width / 2, positions[index][1] - height / 2, positions[index][0] + width / 2, positions[index][1] + height / 2);
	                                dandelion.draw(canvas);
                                }
                                canvas.restore();
                            }
                        }
                    } finally {
                        if (canvas != null)
                            holder.unlockCanvasAndPost(canvas);
                    }
                    boolean outBoundary = true;
                    for (int index = 0; index < positions.length; ++index) {
                        if ((positions[index][0] < 0 || positions[index][1] < 0) || (positions[index][0] > screenWidth || positions[index][1] > screenHeight)) {
                            continue;
                        }
                        else {
                            outBoundary = false;
                            break;
                        }
                    }
                    
                    if (outBoundary) {
                        infinite = false;
                    }
  
                    try {
                        Thread.sleep(TIME_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                }
            }
            
            Canvas canvas = holder.lockCanvas();
            try {
                if (canvas != null) {
                    canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            
            Message msg = handler.obtainMessage();
            msg.sendToTarget();
            return null;
        }
        
    }
    
    public interface OnBlowOver {
        public void blowOver();
    }
}
