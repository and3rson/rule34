package com.dunai.rule34.views;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by anderson on 13.10.15.
 */
public class PackView extends LinearLayout {
    private static final String TAG = "rule34/PackView";
    private ArrayList<View> children = new ArrayList<>();
//    private ArrayList<LinearLayout> rows = new ArrayList<>();
//    private int maxHeight = 0;

    public PackView(Context context) {
        super(context);
        this.initialize();
    }

    public PackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public PackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    public PackView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    public void initialize() {
//        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                repack();
//            }
//        });
        this.setOrientation(VERTICAL);
    }

    @Override
    public void addView(@NonNull View child) {
//        super.addView(child);
        this.children.add(child);
    }

    public void repack() {
        Log.i(TAG, "repack");

        this.removeAllViews();

//        Point windowSize = new Point();
//        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
//        wm.getDefaultDisplay().getSize(windowSize);

//        this.measure(windowSize.x, windowSize.y);

//        Log.i(TAG, "Measured: " + this.getMeasuredWidth());

//        for (LinearLayout row : this.rows) {
////            row.removeAllViews();
//            while (row.getChildCount() > 0) {
//                row.removeView(row.getChildAt(0));
//            }
//            ((LinearLayout) row.getParent()).removeView(row);
//        }
//        this.removeAllViews();

        for (View child : this.children) {
            //            if (child.getParent() != null) {
//                ((ViewGroup) child.getParent()).removeView(child);
//            }
            child.measure(this.getWidth(), this.getHeight());

            if (this.getLastRow() != null) {
                this.getLastRow().measure(this.getWidth(), this.getHeight());
            }

            if (this.getLastRow() == null || this.getLastRow().getMeasuredWidth() + child.getMeasuredWidth() > this.getWidth()) {
                LinearLayout row = new LinearLayout(this.getContext());
                row.setOrientation(HORIZONTAL);
                LayoutParams lp = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                super.addView(row);
            }

            Log.i(TAG, "" + this.getLastRow().getMeasuredWidth() + " + " + child.getMeasuredWidth() + " ~ " + this.getWidth());

            this.getLastRow().addView(child);

//            if (start.x + child.getWidth() > this.getWidth()) {
//                // Overflow
//                start.x = 0;
//                start.y += this.maxHeight;
//                this.maxHeight = 0;
//                Log.i(TAG, "Overflow!");
//            }
//            Log.i(TAG, "Packing view at " + start.x + "/" + start.y + ", width = " + child.getWidth());
//            child.setLeft(start.x);
//            child.setTop(start.y);
//            start.x += child.getWidth();
//            this.maxHeight = Math.max(this.maxHeight, child.getHeight());
//            child.setVisibility(VISIBLE);
        }
    }

    private LinearLayout getLastRow() {
        if (this.getChildCount() == 0) {
            return null;
        }
        return (LinearLayout) this.getChildAt(this.getChildCount() - 1);
    }
}
