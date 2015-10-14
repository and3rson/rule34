package com.dunai.rule34.models.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dunai.rule34.models.Document;
import com.dunai.rule34.models.Model;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Created by anderson on 12.10.15.
 */
public class Post extends Model {
    private static final String TAG = "rule34/Post";
    public long id;
    public long change;
    public long creatorId;
    public String createdAt;
    public String md5;
    public long score;
    public String tags;

    public String fileUrl;
    public int width;
    public int height;

    public String previewUrl;
    public int previewWidth;
    public int previewHeight;

    public String sampleUrl;
    public int sampleWidth;
    public int sampleHeight;

    public Post(@Nullable Node node) {
        super();

        if (node != null) {
            try {
                NamedNodeMap map = node.getAttributes();

                this.id = Long.parseLong(map.getNamedItem("id").getNodeValue());
                this.change = Long.parseLong(map.getNamedItem("change").getNodeValue());
                this.creatorId = Long.parseLong(map.getNamedItem("creator_id").getNodeValue());
                this.createdAt = map.getNamedItem("created_at").getNodeValue();
                this.md5 = map.getNamedItem("md5").getNodeValue();
                this.score = Long.parseLong(map.getNamedItem("score").getNodeValue());
                this.tags = map.getNamedItem("tags").getNodeValue();

                this.fileUrl = map.getNamedItem("file_url").getNodeValue();
                this.width = Integer.parseInt(map.getNamedItem("width").getNodeValue());
                this.height = Integer.parseInt(map.getNamedItem("height").getNodeValue());

                this.previewUrl = map.getNamedItem("preview_url").getNodeValue();
                this.previewWidth = Integer.parseInt(map.getNamedItem("preview_width").getNodeValue());
                this.previewHeight = Integer.parseInt(map.getNamedItem("preview_height").getNodeValue());

                this.sampleUrl = map.getNamedItem("sample_url").getNodeValue();
                this.sampleWidth = Integer.parseInt(map.getNamedItem("sample_width").getNodeValue());
                this.sampleHeight = Integer.parseInt(map.getNamedItem("sample_height").getNodeValue());
            } catch (Exception e) {
                Log.e(TAG, "Failed to initialize model", e);
            }
        }
    }

    @Override
    public void serialize(Bundle bundle) {
        bundle.putLong("id", this.id);
        bundle.putLong("change", this.change);
        bundle.putLong("creator_id", this.creatorId);
        bundle.putString("created_at", this.createdAt);
        bundle.putString("md5", this.md5);
        bundle.putLong("score", this.score);
        bundle.putString("tags", this.tags);

        bundle.putString("file_url", this.fileUrl);
        bundle.putInt("width", this.width);
        bundle.putInt("height", this.height);

        bundle.putString("preview_url", this.previewUrl);
        bundle.putInt("preview_width", this.previewWidth);
        bundle.putInt("preview_height", this.previewHeight);

        bundle.putString("sample_url", this.sampleUrl);
        bundle.putInt("sample_width", this.sampleWidth);
        bundle.putInt("sample_height", this.sampleHeight);
    }

    @Override
    public void unserialize(Bundle bundle) {
        this.id = bundle.getLong("id");
        this.change = bundle.getLong("change");
        this.creatorId = bundle.getLong("creator_id");
        this.createdAt = bundle.getString("created_at");
        this.md5 = bundle.getString("md5");
        this.score = bundle.getLong("score");
        this.tags = bundle.getString("tags");

        this.fileUrl = bundle.getString("file_url");
        this.width = bundle.getInt("width");
        this.height = bundle.getInt("height");

        this.previewUrl = bundle.getString("preview_url");
        this.previewWidth = bundle.getInt("preview_width");
        this.previewHeight = bundle.getInt("preview_height");

        this.sampleUrl = bundle.getString("sample_url");
        this.sampleWidth = bundle.getInt("sample_width");
        this.sampleHeight = bundle.getInt("sample_height");
    }
}
