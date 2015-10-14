package com.dunai.rule34.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dunai.rule34.R;
import com.dunai.rule34.activities.MainActivity;
import com.dunai.rule34.activities.PostActivity;

/**
 * Created by anderson on 13.10.15.
 */
public class TagChip extends FrameLayout {
    private TextView textView;

    public TagChip(Context context, String text) {
        super(context);
        this.initialize();

        this.setText(text);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void initialize() {
        this.textView = new TextView(this.getContext());
        FrameLayout.LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int padding = this.dpToPx(4);
        lp.rightMargin = padding;
        lp.bottomMargin = padding;
        this.textView.setLayoutParams(lp);
        this.textView.setPadding(padding, padding, padding, padding);
        this.textView.setBackgroundColor(getContext().getResources().getColor(R.color.dark));
        this.textView.setTextColor(getContext().getResources().getColor(R.color.white));
        this.textView.setTextSize(this.dpToPx(6));

        this.addView(this.textView);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showTag = new Intent(getContext(), MainActivity.class);
                showTag.putExtra("tag", textView.getText().toString());
                getContext().startActivity(showTag);
            }
        });
    }
}
