package com.dunai.rule34.models;

import android.support.annotation.NonNull;
import android.util.Log;
import com.dunai.rule34.exceptions.RESTException;
import com.dunai.rule34.models.Document;
import com.dunai.rule34.models.Model;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Created by anderson on 13.10.15.
 */
public class IterableXMLDocument<T extends Model> extends Document<T> implements List<T> {
    private static final String TAG = "rule34/IterableDocument";

    ArrayList<T> models = new ArrayList<>();

    public IterableXMLDocument(Class<T> reference) {
        super(reference, TYPE.XML);
    }

    @Override
    public void load(Query query) throws RESTException {
        super.load(query);

        NodeList nodes = this.getXMLRoot().getElementsByTagName("post");
        for (int i = 0; i < nodes.getLength(); i++) {
            T model;
            Node node = nodes.item(i);

            try {
                Constructor<?> constructor = this.reference.getConstructor(Node.class);
                model = (T) constructor.newInstance(node);
            } catch (Exception e) {
                Log.e(TAG, "Failed to create model!", e);
                continue;
            }

            this.models.add(model);
        }
    }

    @Override
    public void add(int location, T object) {
        this.models.add(location, object);
    }

    @Override
    public boolean add(T object) {
        return this.models.add(object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends T> collection) {
        return this.models.addAll(location, collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return this.models.addAll(collection);
    }

    @Override
    public void clear() {
        this.models.clear();
    }

    @Override
    public boolean contains(Object object) {
        return this.models.contains(object);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return this.models.containsAll(collection);
    }

    @Override
    public T get(int location) {
        return this.models.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return this.models.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return this.models.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return this.models.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return this.models.lastIndexOf(object);
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator() {
        return this.models.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int location) {
        return this.models.listIterator(location);
    }

    @Override
    public T remove(int location) {
        return this.models.remove(location);
    }

    @Override
    public boolean remove(Object object) {
        return this.models.remove(object);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return this.models.removeAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return this.models.retainAll(collection);
    }

    @Override
    public T set(int location, T object) {
        return this.models.set(location, object);
    }

    @Override
    public int size() {
        return this.models.size();
    }

    @NonNull
    @Override
    public List<T> subList(int start, int end) {
        return this.models.subList(start, end);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return this.models.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] array) {
        return this.models.toArray(array);
    }
}
