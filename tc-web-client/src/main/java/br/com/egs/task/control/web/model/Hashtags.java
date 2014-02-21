package br.com.egs.task.control.web.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Hashtags implements Collection<Hashtag> {

    private Set<Hashtag> hashtags;

    private Hashtags() {
        this.hashtags = new HashSet<>();
    }

    public Hashtags(Hashtag hashtag) {
        this();
        add(hashtag);
    }

    public Hashtags(Set<Hashtag> hashtags) {
        this();
        addAll(hashtags);
    }

    @Override
    public Iterator<Hashtag> iterator() {
        return hashtags.iterator();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return hashtags.size();
    }

    @Override
    public boolean isEmpty() {
        return hashtags.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return hashtags.contains(o);
    }

    @Override
    public boolean add(Hashtag hashtag) {
        return hashtags.add(hashtag);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return hashtags.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Hashtag> c) {
        return hashtags.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return hashtags.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Hashtags h = (Hashtags) obj;
        return hashtags.equals(h.hashtags);
    }

    @Override
    public int hashCode() {
        return hashtags.hashCode();
    }
}
