
package com.limemobile.app.blog.weibo.tencent.api;

import com.limemobile.app.blog.weibo.tencent.entity.Entity;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 加载结果监听器
 */
public abstract class IResultListener {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ArrayList<TAsyncTask<?>> asyncTaskList;;

    public IResultListener() {
    }

    public void addTask(TAsyncTask<?> task) {
        try {
            lock.writeLock().lock();
            if (asyncTaskList == null)
                asyncTaskList = new ArrayList<TAsyncTask<?>>();
            asyncTaskList.add(task);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeTask(TAsyncTask<?> task) {
        try {
            lock.writeLock().lock();
            if (asyncTaskList != null)
                asyncTaskList.remove(task);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clearPendingTask() {
        try {
            lock.writeLock().lock();
            if (asyncTaskList != null && !asyncTaskList.isEmpty()) {
                for (TAsyncTask<?> object : asyncTaskList)
                    object.cancel(true);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isTaskEmtpy() {
        boolean taskEmtpy = true;
        try {
            lock.writeLock().lock();
            if (asyncTaskList != null)
                taskEmtpy = asyncTaskList.isEmpty();
        } finally {
            lock.writeLock().unlock();
        }
        return taskEmtpy;
    }

    public abstract void onSuccess(Entity<TObject> data);

    public abstract void onFail(Entity<TObject> data);
}
