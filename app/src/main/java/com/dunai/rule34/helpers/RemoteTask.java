package com.dunai.rule34.helpers;

import android.os.AsyncTask;
import android.util.Log;
import com.dunai.rule34.models.Query;
import com.dunai.rule34.models.Remote;

import java.util.HashMap;

/**
 * Created by anderson on 24.08.15.
 */
public abstract class RemoteTask extends AsyncTask<Remote, Void, Remote> {
    private static final String TAG = "RemoteTask";
    private volatile boolean _lock;
    private String tag;
    private Query query;
    private static volatile HashMap<String, RemoteTask> registry = new HashMap<>();

    public RemoteTask(String tag) {
        this.tag = tag;
        this._lock = false;
    }

    public RemoteTask setQuery(Query query) {
        this.query = query;
        return this;
    }

    public final Query getQuery() {
        return this.query;
    }

    public RemoteTask setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public final String getTag() {
        return this.tag;
    }

    public final void run(Remote... params) {
        if (RemoteTask.isRunning(this.getTag())) {
            Log.e(TAG, "Warning: task with tag '" + this.getTag() + "' already running.");
            return;
        }
        RemoteTask.registry.put(this.getTag(), this);
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    public static boolean isRunning(String tag) {
        return RemoteTask.registry.get(tag) != null;
    }

    @Override
    protected void onPostExecute(Remote remote) {
        RemoteTask.registry.remove(this.getTag());
        this._lock = false;
    }

    @Override
    protected void onCancelled() {
        RemoteTask.registry.remove(this.getTag());
        this._lock = false;
    }

    public void cancelIfRunning() {
        RemoteTask.registry.remove(this.getTag());

        if (!this.isCancelled()) {
            this.cancel(false);
        }
    }
}
