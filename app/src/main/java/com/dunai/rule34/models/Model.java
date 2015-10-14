package com.dunai.rule34.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by anderson on 12.10.15.
 */
public abstract class Model {
    private static final String TAG = "rule34/Model";

//    protected HashMap<String, String> values = new HashMap<>();

    public Model() {
//    public Model(@Nullable Node node) {
//        if (node != null) {
//            this.initialize(node);
//        }
    }

    public abstract void serialize(Bundle bundle);
    public abstract void unserialize(Bundle bundle);

//    void initialize(Node node) {
//        NamedNodeMap map = node.getAttributes();
//        for (int i = 0; i < map.getLength(); i++) {
//            Node item = map.item(i);
//            this.values.put(item.getNodeName(), item.getNodeValue());
//        }
//    }
//
//    public String getString(String key) {
//        return this.values.get(key);
//    }
//
//    public Long getLong(String key) {
//        return Long.decode(this.values.get(key));
//    }
//
//    public Boolean getBoolean(String key) {
//        return this.values.get(key).toLowerCase().equals("true");
//    }
}
