package com.dunai.rule34.models.impl;

import android.os.Bundle;
import com.dunai.rule34.models.Model;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by anderson on 14.10.15.
 */
public class ForumTopic extends Model {
    public long id;
    public boolean isSticky;
    public String title;
    public String author;
    public String updated;
    public String updatedBy;
    public long replies;

    public ForumTopic(Element element) {
        Elements rows = element.select("td");
        Element forumTopic = rows.get(0).select(".forum-topic").first();
        if (forumTopic != null) {
            this.isSticky = forumTopic.text().contains("Sticky:");
            this.title = forumTopic.select("a").text();
            String href = forumTopic.select("a").attr("href");
            String[] parts = href.split("=");
            this.id = Long.parseLong(parts[parts.length - 1]);
        }
        if (rows.size() >= 5) {
            this.author = rows.get(1).text();
            this.updated = rows.get(2).text();
            this.updatedBy = rows.get(3).text();
            this.replies = Long.parseLong(rows.get(4).text());
        }
    }

    @Override
    public void serialize(Bundle bundle) {
        // TODO: Implement this
    }

    @Override
    public void unserialize(Bundle bundle) {
        // TODO: Implement this
    }
}
