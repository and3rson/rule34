package com.dunai.rule34.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dunai.rule34.R;
import com.dunai.rule34.activities.PostActivity;
import com.dunai.rule34.helpers.DownloadAndCacheImageTask;
import com.dunai.rule34.interfaces.OnDownloadImageListener;
import com.dunai.rule34.models.impl.Post;

/**
 * Created by anderson on 12.10.15.
 */
public class PostItemView extends RelativeLayout {
    private static final String TAG = "rule34/PostItemView";
    private Post model;

    private class Holder {
        ImageView postPreview;
        TextView postTitle;
        FrameLayout postOverlay;
        DownloadAndCacheImageTask task;
    }

//    public long getModelId() {
//        return this.model.id;
//    }

    public static PostItemView createView(Context context, Post newModel, @Nullable View convertView) {
        PostItemView view;

        if (convertView != null) {
            view = (PostItemView) convertView;

//            if (view.getModelId() == newModel.id) {
//                // Do not recreate if Android attempts to reuse a convertView for the same unchanged model.
//                return view;
//            }
        } else {
            view = new PostItemView(context);
        }

        Log.i(TAG, "Updating with" + String.valueOf(newModel.id));

        view.update(newModel);

        return view;
    }

    private PostItemView(Context context) {
        super(context);

        inflate(context, R.layout.post_item, this);

        Holder holder = new Holder();
        holder.postPreview = (ImageView) this.findViewById(R.id.postPreview);
        holder.postTitle = (TextView) this.findViewById(R.id.postTitle);
        holder.postOverlay = (FrameLayout) this.findViewById(R.id.postOverlay);
        this.setTag(holder);
    }

    public void update(Post newModel) {
        this.model = newModel;
        Holder holder = (Holder) this.getTag();

        String currentURL = (String) holder.postPreview.getTag(R.id.TAG_URL);
        if (currentURL == null || ! currentURL.equals(this.model.previewUrl)) {
            if (holder.task != null && !holder.task.isCancelled()) {
                holder.task.cancel(false);
                Log.i(TAG, "Cancelled for " + String.valueOf(this.model.id));
            }

            holder.task = new DownloadAndCacheImageTask(this.getContext(), holder.postPreview, null);
            Log.i(TAG, "Started for " + String.valueOf(this.model.id));
            holder.task.downscaleTo(new Point(400, 400));
            holder.task.run(this.model.previewUrl);
        }

        holder.postTitle.setText(this.model.createdAt);
        holder.postOverlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showPost = new Intent(getContext(), PostActivity.class);
                Bundle bundle = new Bundle();
                model.serialize(bundle);
                showPost.putExtra("bundle", bundle);
                getContext().startActivity(showPost);
            }
        });
    }
}
