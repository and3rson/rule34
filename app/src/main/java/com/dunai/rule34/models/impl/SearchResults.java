package com.dunai.rule34.models.impl;

import com.dunai.rule34.models.IterableDocument;

/**
 * Created by anderson on 13.10.15.
 */
public class SearchResults extends IterableDocument<Post> {
    public SearchResults() {
        super(Post.class);
    }
}
