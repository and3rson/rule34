package com.dunai.rule34.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dunai.rule34.R;
import com.dunai.rule34.models.impl.TopicReply;

/**
 * Created by anderson on 12.10.15.
 */
public class ReplyItemView extends RelativeLayout {
    private static final String TAG = "rule34/ReplyItemView";
    private TopicReply model;

    private class Holder {
        TextView replyAuthor;
        TextView replyDate;
        TextView replyComment;
    }

//    public long getModelId() {
//        return this.model.id;
//    }

    public static ReplyItemView createView(Context context, TopicReply newModel, @Nullable View convertView) {
        ReplyItemView view;

        if (convertView != null) {
            view = (ReplyItemView) convertView;

//            if (view.getModelId() == newModel.id) {
//                // Do not recreate if Android attempts to reuse a convertView for the same unchanged model.
//                return view;
//            }
        } else {
            view = new ReplyItemView(context);
        }

        view.update(newModel);

        return view;
    }

    private ReplyItemView(Context context) {
        super(context);

        inflate(context, R.layout.reply_item, this);

        Holder holder = new Holder();
        holder.replyAuthor = (TextView) this.findViewById(R.id.replyAuthor);
        holder.replyDate = (TextView) this.findViewById(R.id.replyDate);
        holder.replyComment = (TextView) this.findViewById(R.id.replyComment);
        this.setTag(holder);
    }

    public void update(TopicReply newModel) {
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

        holder.replyAuthor.setText(this.model.author);
        holder.replyDate.setText(this.model.date);
        holder.replyComment.setText(Html.fromHtml(this.model.comment));
        holder.replyComment.setMovementMethod(LinkMovementMethod.getInstance());
        this.setClickable(false);
    }
}
