package com.limemobile.app.blog.activity.sns;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BlowListener {    
    private static final int MSG_WIND_BLOWING = 1;
    
    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 2;
    private static final int KEEP_ALIVE = 1;
    
    private static int SAMPLE_RATE_IN_HZ = 8000;

    private AudioRecord ar;
    private int bs;
    
    private final Executor executor;
    private RecordTask recordTask;
    private OnBlowListener blowListener;
    private Handler handler;

    public BlowListener() {
        super();
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        
        bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bs);
        
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_WIND_BLOWING:
                        Bundle b = msg.getData();
                        int value = b.getInt("value");
                        int number=b.getInt("number");
                        if (value / number > 3200) {
                            if (blowListener != null)
                                blowListener.onBlowStart();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    
    public void startMonitor() {
        if (recordTask == null) {
            ar.startRecording();
            recordTask = new RecordTask(true);
            recordTask.executeOnExecutor(executor);
        }
    }
    
    public void stopMonitor () {
        if (recordTask != null && recordTask.getStatus() != AsyncTask.Status.FINISHED) {
            recordTask.cancel();
            recordTask = null;
            
            ar.stop();
        }
    }
    
    public void setOnBlowListener(OnBlowListener blowListener) {
        this.blowListener = blowListener;        
    }

    private class RecordTask extends AsyncTask<Void, Void, Void> {
        private boolean continuallyMonitor;
        
        private Message msg;
        private int number = 0;
        private int tal = 0;
        private long currenttime;
        private long endtime;
        private long time;
        
        public RecordTask(boolean continually) {
            continuallyMonitor = continually;
        }
        
        public void cancel() {
            continuallyMonitor = false;
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
            // 用于读取的 buffer
            byte[] buffer = new byte[bs];
            while (continuallyMonitor) {
                number++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currenttime = System.currentTimeMillis();
                int r = ar.read(buffer, 0, bs);
                if (r == 0)
                    continue;
                int v = 0;
                for (int i = 0; i < buffer.length; i++) {
                    v += buffer[i] * buffer[i];
                }
                // 平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。
                // 如果想利用这个数值进行操作，建议用 sendMessage 将其抛出，在 Handler 里进行处理。
                int value = Integer.valueOf(v / (int) r);
                tal = tal + value;
                endtime = System.currentTimeMillis();
                time = time + (endtime - currenttime);
                if (time >= 1000 || number > 5) {
                    msg = handler.obtainMessage(MSG_WIND_BLOWING);
                    Bundle b = new Bundle();
                    b.putInt("value", tal);
                    b.putInt("number", number);
                    msg.setData(b);
                    msg.sendToTarget();
                    number = 0;
                    tal = 0;
                    time = 0;
                }
            }
            return null;
        }
    }
    
    public interface OnBlowListener {
        public void onBlowStart();
        
        public void onBlowOver();
    }
}
