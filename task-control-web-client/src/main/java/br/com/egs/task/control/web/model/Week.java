package br.com.egs.task.control.web.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class Week implements List<Task>{
	
	private Integer id;
	private List<Task> tasks;
	
	public Week(Integer id, List<Task> tasks){
		this.id = id;
		this.tasks = tasks;
	}
	
	public Integer getId(){
		return id;
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
	public Iterator<Task> iterator() {
		return tasks.iterator();
	}

	@Override
	public Object[] toArray() {
		return tasks.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return tasks.toArray(a);
	}

	@Override
	public boolean add(Task e) {
		return tasks.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return tasks.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return tasks.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Task> c) {
		return tasks.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Task> c) {
		return tasks.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return tasks.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return tasks.retainAll(c);
	}

	@Override
	public void clear() {
		tasks.clear();
	}

	@Override
	public Task get(int index) {
		return tasks.get(index);
	}

	@Override
	public Task set(int index, Task element) {
		return tasks.set(index, element);
	}

	@Override
	public void add(int index, Task element) {
		tasks.add(index, element);
	}

	@Override
	public Task remove(int index) { 
		return tasks.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return tasks.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return tasks.lastIndexOf(o);
	}

	@Override
	public ListIterator<Task> listIterator() {
		return tasks.listIterator();
	}

	@Override
	public ListIterator<Task> listIterator(int index) {
		return tasks.listIterator(index);
	}

	@Override
	public List<Task> subList(int fromIndex, int toIndex) {
		return tasks.subList(fromIndex, toIndex);
	}

}
