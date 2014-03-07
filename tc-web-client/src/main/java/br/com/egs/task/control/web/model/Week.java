package br.com.egs.task.control.web.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Week implements Collection<OneWeekTask>{

	private List<OneWeekTask> tasks;

    public Week(){
        this.tasks = new LinkedList<>();
    }

	public Week(List<OneWeekTask> tasks){
		this.tasks = tasks;
	}

    @Override
    public Iterator<OneWeekTask> iterator() {
        return tasks.iterator();
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
		return tasks.size();
	}

    @Override
	public boolean isEmpty() {
		return tasks.isEmpty();
	}

    @Override
	public boolean contains(Object o) {
		return tasks.contains(o);
	}

    @Override
	public boolean add(OneWeekTask e) {
        if(e != null){
            return tasks.add(e);
        }
		return false;
	}

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends OneWeekTask> c) {
        throw new UnsupportedOperationException();
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
}
