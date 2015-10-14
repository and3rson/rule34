package com.dunai.rule34.models;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by anderson on 14.10.15.
 */
public class Query {
    private HashMap<String, Object> values = new HashMap<>();

    public Query put(String key, String value) {
        this.values.put(key, value);
        return this;
    }

    public Query put(String key, int value) {
        this.values.put(key, value);
        return this;
    }

    public Query put(String key, long value) {
        this.values.put(key, value);
        return this;
    }

    public String toString() {
        String query = "";
        for(HashMap.Entry<String, Object> entry : this.values.entrySet()) {
            try {
                query += entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8") + "&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return query;
    }
}
