package com.dunai.rule34.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.dunai.rule34.R;
import com.dunai.rule34.exceptions.RESTException;
import com.dunai.rule34.helpers.FetchTask;
import com.dunai.rule34.interfaces.OnFetchReceiver;
import com.dunai.rule34.models.Query;
import com.dunai.rule34.models.Remote;
import com.dunai.rule34.models.impl.Topic;
import com.dunai.rule34.models.impl.TopicReply;
import com.dunai.rule34.views.EndlessListView;
import com.dunai.rule34.views.ReplyItemView;

import java.util.List;

/**
 * Created by anderson on 14.10.15.
 */
public class TopicActivity extends ActionBarActivity {
    private static final String TAG = "rule34/TopicActivity";

    private SwipeRefreshLayout refresher;
    private EndlessListView topicReplyListView;

    private long currentPage = 0;

    private long id;
    private Topic topicReplies;
    private Topic newTopicReplies;
    private TopicReplyAdapter topicReplyAdapter;
    private boolean noMoreResults = false;

    class TopicReplyAdapter extends ArrayAdapter<TopicReply> {
        public TopicReplyAdapter(Context context, int resource, List<TopicReply> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return ReplyItemView.createView(this.getContext(), this.getItem(position), convertView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.topic);

        this.id = this.getIntent().getLongExtra("id", 0);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(this.getIntent().getStringExtra("title"));

        this.refresher = (SwipeRefreshLayout) this.findViewById(R.id.refresher);
        this.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                noMoreResults = false;
                currentPage = 0;
                topicReplies.clear();
                refresh();
            }
        });

        this.topicReplies = new Topic();
        this.newTopicReplies = new Topic();

        this.topicReplyListView = (EndlessListView) this.findViewById(R.id.topicReplyListView);
        this.topicReplyListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public void loadMore(EndlessListView view) {
                Log.i(TAG, "Load more");

                if (noMoreResults) {
                    Log.i(TAG, "cancelled load more");
                    return;
                }

                currentPage++;
                refresh();
            }
        });

        this.topicReplyAdapter = new TopicReplyAdapter(this, R.layout.topic_item, this.topicReplies);
        this.topicReplyListView.setAdapter(this.topicReplyAdapter);

        this.refresh();
    }

    private void refresh() {
        this.newTopicReplies.clear();
        this.refresher.setRefreshing(true);

        new FetchTask(new OnFetchReceiver() {
            @Override
            public void onFetchRemote(Remote object, String tag) {
                topicReplyListView.setProgress(false);
                refresher.setRefreshing(false);

//                for (ForumTopic thread : forumIndex) {
//                    Log.i(TAG, "Thread: " + thread.title);
//                }

                if (newTopicReplies.size() == 0) {
                    noMoreResults = true;
                    Log.i(TAG, "No more results");
                }

                topicReplies.addAll(newTopicReplies);
                topicReplyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFetchRemoteError(RESTException e, String tag) {
                topicReplyListView.setProgress(false);
                refresher.setRefreshing(false);
                Log.e(TAG, e.getMessage(), e);
            }
        }, "topic").setQuery(new Query().put("page", "forum").put("s", "view").put("id", this.id).put("pid", this.currentPage * 15)).execute(this.newTopicReplies);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
