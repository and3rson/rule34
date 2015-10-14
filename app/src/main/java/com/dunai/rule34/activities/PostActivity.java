package com.dunai.rule34.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.*;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.dunai.rule34.R;
import com.dunai.rule34.helpers.DownloadAndCacheImageTask;
import com.dunai.rule34.interfaces.OnDownloadImageListener;
import com.dunai.rule34.models.impl.Post;
import com.dunai.rule34.views.PackView;
import com.dunai.rule34.views.TagChip;

/**
 * Created by anderson on 13.10.15.
 */
public class PostActivity extends ActionBarActivity {
    Post post;

    private ImageView imageView;
    private TextView scoreTextView;
//    private TextView tagsTextView;
    private PackView tagsPackView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.post);

        this.post = new Post(null);
        this.post.unserialize(getIntent().getBundleExtra("bundle"));

        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        this.scoreTextView = (TextView) this.findViewById(R.id.scoreTextView);
//        this.tagsTextView = (TextView) this.findViewById(R.id.tagsTextView);
        this.tagsPackView = (PackView) this.findViewById(R.id.tagsPackView);

        final Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);

        ViewGroup.LayoutParams lp = this.imageView.getLayoutParams();
        lp.height = screenSize.x;
        this.imageView.setLayoutParams(lp);
        new DownloadAndCacheImageTask(this, this.imageView, new OnDownloadImageListener() {
            @Override
            public void onDownload(Bitmap bm) {
                new DownloadAndCacheImageTask(PostActivity.this, imageView, null).downscaleTo(new Point(screenSize.x, screenSize.y)).run(post.sampleUrl);
            }
        }).downscaleTo(new Point(screenSize.x, screenSize.y)).run(this.post.previewUrl);

        this.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showImage = new Intent(PostActivity.this, ImageActivity.class);
                showImage.putExtra("url", post.fileUrl);
                startActivity(showImage);
            }
        });

        this.scoreTextView.setText(String.valueOf(this.post.score));

        String[] links = this.post.tags.split("\\s+");
//        String out = "";
//        for (String link : links) {
//            if (!out.isEmpty()) {
//                out += " ";
//            }
//            out += "<a href=\"" + link + "\">" + link + "</a>";
//        }
//
//        CharSequence sequence = Html.fromHtml(out);
//        SpannableStringBuilder builder = new SpannableStringBuilder(sequence);
//
//        URLSpan[] urls = builder.getSpans(0, sequence.length(), URLSpan.class);
//        for (final URLSpan span : urls) {
//            int start = builder.getSpanStart(span);
//            int end = builder.getSpanEnd(span);
//            int flags = builder.getSpanFlags(span);
//            ClickableSpan clickable = new ClickableSpan() {
//                @Override
//                public void onClick(View widget) {
//                    Intent showTag = new Intent(PostActivity.this, MainActivity.class);
//                    showTag.putExtra("tag", span.getURL());
//                    startActivity(showTag);
////                    Toast.makeText(PostActivity.this, "Clicked span " + span.getURL(), Toast.LENGTH_SHORT).show();
//                }
//            };
//            builder.setSpan(clickable, start, end, flags);
//            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), start, end, flags);
//            builder.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.dark)), start, end, flags);
//            builder.removeSpan(span);
//        }
//
//        this.tagsTextView.setText(builder);
//        this.tagsTextView.setMovementMethod(LinkMovementMethod.getInstance());

        for (String link : links) {
            if (link.trim().length() > 0) {
                this.tagsPackView.addView(new TagChip(this, link));
            }
        }
        this.tagsPackView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tagsPackView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                tagsPackView.repack();
            }
        });

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
