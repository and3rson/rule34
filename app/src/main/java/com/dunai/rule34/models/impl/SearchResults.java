package com.dunai.rule34.models.impl;

import com.dunai.rule34.models.IterableXMLDocument;

/**
 * Created by anderson on 13.10.15.
 */
public class SearchResults extends IterableXMLDocument<Post> {
    public SearchResults() {
        super(Post.class);
    }
}
