package com.dunai.rule34.models;

import com.dunai.rule34.exceptions.RESTException;
import com.dunai.rule34.models.impl.Post;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by anderson on 12.10.15.
 */
public class Document<T extends Model> extends Remote {
    protected Class<T> reference;
    public Object root;

    public Document(Class<T> reference, TYPE type) {
        super(type);

        this.reference = reference;
    }

    @Override
    public void load(Query query) throws RESTException {
        this.root = super._get(query);
    }

    public Element getXMLRoot() {
        return (Element) this.root;
    }

    public org.jsoup.nodes.Element getHTMLRoot() {
        return (org.jsoup.nodes.Element) this.root;
    }
}
