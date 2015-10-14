package com.dunai.rule34.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dunai.rule34.R;
import com.dunai.rule34.activities.PostActivity;
import com.dunai.rule34.activities.TopicActivity;
import com.dunai.rule34.helpers.DownloadAndCacheImageTask;
import com.dunai.rule34.models.impl.ForumTopic;
import com.dunai.rule34.models.impl.Post;

/**
 * Created by anderson on 12.10.15.
 */
public class TopicItemView extends RelativeLayout {
    private static final String TAG = "rule34/TopicItemView";
    private ForumTopic model;

    private class Holder {
        TextView topicIsSticky;
        TextView topicTitle;
        TextView topicReplyCount;
    }

//    public long getModelId() {
//        return this.model.id;
//    }

    public static TopicItemView createView(Context context, ForumTopic newModel, @Nullable View convertView) {
        TopicItemView view;

        if (convertView != null) {
            view = (TopicItemView) convertView;

//            if (view.getModelId() == newModel.id) {
//                // Do not recreate if Android attempts to reuse a convertView for the same unchanged model.
//                return view;
//            }
        } else {
            view = new TopicItemView(context);
        }

        view.update(newModel);

        return view;
    }

    private TopicItemView(Context context) {
        super(context);

        inflate(context, R.layout.topic_item, this);

        Holder holder = new Holder();
        holder.topicIsSticky = (TextView) this.findViewById(R.id.topicIsSticky);
        holder.topicTitle = (TextView) this.findViewById(R.id.topicTitle);
        holder.topicReplyCount = (TextView) this.findViewById(R.id.topicReplyCount);
        this.setTag(holder);
    }

    public void update(ForumTopic newModel) {
        this.model = newModel;

        Holder holder = (Holder) this.getTag();

//        String currentURL = (String) holder.postPreview.getTag(R.id.TAG_URL);
//        if (currentURL == null || ! currentURL.equals(this.model.previewUrl)) {
//            if (holder.task != null && !holder.task.isCancelled()) {
//                holder.task.cancel(false);
//                Log.i(TAG, "Cancelled for " + String.valueOf(this.model.id));
//            }
//
//            holder.task = new DownloadAndCacheImageTask(this.getContext(), holder.postPreview, null);
//            Log.i(TAG, "Started for " + String.valueOf(this.model.id));
//            holder.task.downscaleTo(new Point(400, 400));
//            holder.task.run(this.model.previewUrl);
//        }
//
//        holder.postTitle.setText(this.model.createdAt);
//        holder.postOverlay.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent showPost = new Intent(getContext(), PostActivity.class);
//                Bundle bundle = new Bundle();
//                model.serialize(bundle);
//                showPost.putExtra("bundle", bundle);
//                getContext().startActivity(showPost);
//            }
//        });

        holder.topicIsSticky.setVisibility(this.model.isSticky ? VISIBLE : GONE);
        holder.topicTitle.setText(this.model.title);
        holder.topicReplyCount.setText(String.valueOf(this.model.replies));

//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent viewTopic = new Intent(getContext(), TopicActivity.class);
//                viewTopic.putExtra("id", model.id);
//                viewTopic.putExtra("title", model.title);
//                getContext().startActivity(viewTopic);
//            }
//        });

//        this.setClickable(true);
    }
}
