package com.dunai.rule34.models.impl;

import android.os.Bundle;
import com.dunai.rule34.models.Model;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by anderson on 14.10.15.
 */
public class TopicReply extends Model {
    public String author;
    public String date;
    public String comment;

    public TopicReply(Element element) {
        this.author = element.select(".author h6.author > a[href]").text();
        this.date = element.select(".author .date").text();
        this.comment = element.select(".content .body").html();
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
