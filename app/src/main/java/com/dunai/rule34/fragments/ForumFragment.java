package com.dunai.rule34.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.dunai.rule34.R;
import com.dunai.rule34.activities.TopicActivity;
import com.dunai.rule34.exceptions.RESTException;
import com.dunai.rule34.helpers.FetchTask;
import com.dunai.rule34.interfaces.NamedFragment;
import com.dunai.rule34.interfaces.OnFetchReceiver;
import com.dunai.rule34.models.Query;
import com.dunai.rule34.models.Remote;
import com.dunai.rule34.models.impl.Forum;
import com.dunai.rule34.models.impl.ForumTopic;
import com.dunai.rule34.views.EndlessListView;
import com.dunai.rule34.views.TopicItemView;

import java.util.List;

/**
 * Created by anderson on 14.10.15.
 */
public class ForumFragment extends Fragment implements NamedFragment {
    private static final String TAG = "rule34/ForumFragment";

    private View view;
    private SwipeRefreshLayout refresher;
    private EndlessListView forumListView;

    private boolean noMoreResults = false;
    private long currentPage;
    private Forum forumIndex;
    private Forum newForumIndex;
    private ForumAdapter forumAdapter;

    class ForumAdapter extends ArrayAdapter<ForumTopic> {
        public ForumAdapter(Context context, int resource, List<ForumTopic> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return TopicItemView.createView(this.getContext(), this.getItem(position), convertView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.forum, container, false);

        this.refresher = (SwipeRefreshLayout) this.view.findViewById(R.id.refresher);
        this.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                noMoreResults = false;
                currentPage = 0;
                forumIndex.clear();
                refresh();
            }
        });

        this.forumIndex = new Forum();
        this.newForumIndex = new Forum();

        this.forumListView = (EndlessListView) this.view.findViewById(R.id.forumListView);
        this.forumListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public void loadMore(EndlessListView view) {
                if (noMoreResults) {
                    return;
                }
                currentPage++;

                refresh();
            }
        });
        this.forumAdapter = new ForumAdapter(this.getActivity(), R.layout.topic_item, this.forumIndex);
        this.forumListView.setAdapter(this.forumAdapter);

        this.refresh();

        return this.view;
    }

    private void refresh() {
        this.newForumIndex.clear();
        this.refresher.setRefreshing(true);

        new FetchTask(new OnFetchReceiver() {
            @Override
            public void onFetchRemote(Remote object, String tag) {
                forumListView.setProgress(false);
                refresher.setRefreshing(false);

//                for (ForumTopic thread : forumIndex) {
//                    Log.i(TAG, "Thread: " + thread.title);
//                }

                if (newForumIndex.size() == 0) {
                    noMoreResults = true;
                    Log.i(TAG, "No more results");
                }

                forumIndex.addAll(newForumIndex);

                forumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFetchRemoteError(RESTException e, String tag) {
                refresher.setRefreshing(false);
                Log.e(TAG, e.getMessage(), e);
            }
        }, "forum").setQuery(new Query().put("page", "forum").put("s", "list").put("pid", this.currentPage * 50)).execute(this.newForumIndex);

        this.forumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumTopic model = forumAdapter.getItem(position);
                Intent viewTopic = new Intent(getActivity(), TopicActivity.class);
                viewTopic.putExtra("id", model.id);
                viewTopic.putExtra("title", model.title);
                getActivity().startActivity(viewTopic);
            }
        });
    }

    @Override
    public String getName() {
        return "Forum";
    }
}
