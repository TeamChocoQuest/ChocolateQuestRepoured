package team.cqr.cqrepoured.util;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ObjectIdentityMap<T> implements Iterable<T> {
	
	protected final IdentityHashMap<T, Integer> idMap;
	protected final List<T> objects;
	
	public ObjectIdentityMap() {
		this(512);
	}
	
	public ObjectIdentityMap(int capacity) {
		this.objects = new ObjectArrayList<T>(capacity);
		this.idMap = new IdentityHashMap<T, Integer>(capacity);
	}

	public void put(T object, int id) {
		this.idMap.put(object, id);
		
		while (this.objects.size() <= id) {
			this.objects.add(null);
		}
		this.objects.set(id, object);
	}
	
	public int get(T object) {
		Integer intTmp = this.idMap.get(object);
		return intTmp != null ? intTmp.intValue() : -1;
	}

	@Nullable
	public T getById(int id) {
		return (T)(id >= 0 && id < this.objects.size() ? this.objects.get(id) : null);
	}

	public int size() {
		return this.idMap.size();
	}
	
	@Override
	public Iterator<T> iterator() {
		return Iterators.filter(this.objects.iterator(), Predicates.notNull());
	}
	

}
