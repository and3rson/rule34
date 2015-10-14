package com.dunai.rule34.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

import java.util.Formatter;

/**
 * Created by anderson on 21.07.15.
 */

public class EndlessListView extends ListView {
    private OnLoadMoreListener listener = null;
    private boolean progress = false;

    public interface OnLoadMoreListener {
        /**
         * Called when more data should be loaded.
         *
         * @param view The EventScrollView that triggered this event.
         *             Don't forget to call {@link #setProgress(boolean) setProgress(true)}
         *             when you're finished adding custom contents.
         */
        public void loadMore(EndlessListView view);
    }

    public static final String TAG = "resonance/EndlessScrollView";

    public EndlessListView(Context context) {
        super(context);
    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (this.getCount() < 10) {
            // Do not load more if there are < 10 items.
            return;
        }

//        int pageSize = this.getHeight();
//        int current = t + pageSize;
//        int max = 0;
//        for (int i = 0; i < this.getChildCount(); i++) {
//            // TODO: Improve this
//            max += this.getChildAt(i).getMeasuredHeight();
//            max += this.getDividerHeight();
//        }
//        int max = this.getChildAt(0).getHeight();


//        Log.i(TAG, new Formatter().format("onScrollChanged: current=%s, max=%s, visible=%s, count=%s", current, max, this.getFirstVisiblePosition(), this.getCount()).toString());
//
//
        if (this.getProgress()) {
            return;
        }

        if (this.listener == null) {
            Log.e(TAG, "OnLoadMoreListener is not defined for current EndlessScrollView," +
                    "please set it using 'setOnLoadMoreListener(...)'");
            return;
        }
//
        if (this.getLastVisiblePosition() > this.getCount() - 3) {
            this.setProgress(true);
            this.listener.loadMore(this);
        }

        // TODO: Get few next items and trigger preload event
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public boolean getProgress() {
        return this.progress;
    }

    public void setProgress(boolean progress) {
        this.progress = progress;
    }
}
