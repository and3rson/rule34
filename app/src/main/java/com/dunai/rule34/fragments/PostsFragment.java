package com.dunai.rule34.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.dunai.rule34.R;
import com.dunai.rule34.exceptions.RESTException;
import com.dunai.rule34.helpers.FetchTask;
import com.dunai.rule34.helpers.InstallTracker;
import com.dunai.rule34.interfaces.NamedFragment;
import com.dunai.rule34.interfaces.OnFetchReceiver;
import com.dunai.rule34.models.Query;
import com.dunai.rule34.models.Remote;
import com.dunai.rule34.models.impl.*;
import com.dunai.rule34.views.EndlessGridView;
import com.dunai.rule34.views.PostItemView;

import java.util.List;

/**
 * Created by anderson on 14.10.15.
 */
public class PostsFragment extends Fragment implements NamedFragment {
    private static final String TAG = "rule34/PostsFragment";

    private View view;

    private EndlessGridView gridView;
    private ListingAdapter listingAdapter;
    private EditText searchQuery;

    private SearchResults posts;
    private SearchResults newPosts;
    private long currentPage = 0;

    private String lastQuery = null;

    private TextView footerText;

    private boolean noMoreResults = false;

    @Override
    public String getName() {
        return "Posts";
    }

    enum State {
        IDLE,
        LOADING_MORE,
        NOTHING_FOUND
    }

    private class ListingAdapter extends ArrayAdapter<Post> {
        public ListingAdapter(Context context, int resource, List<Post> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Post model = this.getItem(position);
            Log.i(TAG, "Taking [" + String.valueOf(position) + "] / " + String.valueOf(model.id));
            return PostItemView.createView(this.getContext(), model, convertView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.posts, container, false);

        new InstallTracker(this.getActivity()).execute();

        this.gridView = (EndlessGridView) this.view.findViewById(R.id.gridView);
        this.posts = new SearchResults();
        this.newPosts = new SearchResults();
        this.listingAdapter = new ListingAdapter(this.getActivity(), R.layout.post_item, this.posts);
        this.gridView.setAdapter(this.listingAdapter);

        this.footerText = (TextView) this.view.findViewById(R.id.footerText);
//        ((RelativeLayout) this.footerText.getParent()).removeView(this.footerText);

        this.gridView.setOnLoadMoreListener(new EndlessGridView.OnLoadMoreListener() {
            @Override
            public void loadMore(EndlessGridView view) {
                if (noMoreResults) {
                    gridView.setProgress(false);
                    return;
                }

                currentPage++;
                search(lastQuery);

                setState(State.LOADING_MORE);
            }
        });

        this.searchQuery = (EditText) this.view.findViewById(R.id.searchQuery);
        this.searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = searchQuery.getText().toString().trim();
                    if (query.length() > 0) {
                        posts.clear();
//                        listingAdapter.notifyDataSetChanged();
                        currentPage = 0;
                        search(query);

                        View currentFocus = getActivity().getCurrentFocus();
                        if (currentFocus != null) {
                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        String tag = getActivity().getIntent().getStringExtra("tag");
        if (tag != null) {
            this.searchQuery.setText(tag);
            this.searchQuery.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
        }

//        final Topic threadIndex = new Topic();
//        new FetchTask(new OnFetchReceiver() {
//            @Override
//            public void onFetchRemote(Remote object, String tag) {
//                for (TopicReply reply : threadIndex) {
//                    Log.i(TAG, "Reply: " + reply.author);
//                }
//            }
//
//            @Override
//            public void onFetchRemoteError(RESTException e, String tag) {
//                Log.e(TAG, e.getMessage(), e);
//            }
//        }, "thread").setQuery(new Query().put("page", "forum").put("s", "view").put("id", 4640)).execute(threadIndex);

        return this.view;
    }

    private void search(String query) {
        this.newPosts.clear();
        new FetchTask(new OnFetchReceiver() {
            @Override
            public void onFetchRemote(Remote object, String tag) {
                posts.addAll(newPosts);
                listingAdapter.notifyDataSetChanged();
                gridView.setProgress(false);

                noMoreResults = newPosts.size() == 0;

                if (posts.size() == 0) {
                    setState(State.NOTHING_FOUND);
                } else {
                    setState(State.IDLE);
                }
            }

            @Override
            public void onFetchRemoteError(RESTException e, String tag) {
                setState(State.IDLE);
                Toast.makeText(getActivity(), "Network error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, "get_posts").setQuery(new Query().put("s", "post").put("tags", query).put("limit", 20).put("pid", this.currentPage).put("page", "dapi").put("q", "index")).run(newPosts);

        this.lastQuery = query;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void setState(State state) {
        switch (state) {
            case IDLE:
                this.footerText.setVisibility(View.GONE);
                break;
            case LOADING_MORE:
                this.footerText.setVisibility(View.VISIBLE);
                this.footerText.setText("Loading more...");
                break;
            case NOTHING_FOUND:
                this.footerText.setVisibility(View.VISIBLE);
                this.footerText.setText("Nothing found.");
                break;
        }
    }
}
