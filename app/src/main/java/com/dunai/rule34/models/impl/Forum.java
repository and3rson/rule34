package com.dunai.rule34.models.impl;

import com.dunai.rule34.models.IterableHTMLDocument;

/**
 * Created by anderson on 14.10.15.
 */
public class Forum extends IterableHTMLDocument<ForumTopic> {
    public Forum() {
        super(ForumTopic.class, "#forum table.highlightable tbody tr");
    }
}
