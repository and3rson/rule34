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
    public Element root;

    public Document(Class<T> reference) {
        super();

        this.reference = reference;
    }

    @Override
    public void load(Query query) throws RESTException {
        this.root = super._get(query);
    }
}
