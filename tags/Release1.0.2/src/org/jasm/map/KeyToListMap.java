package org.jasm.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeyToListMap<U,T> extends HashMap<U, List<T>> {
	
	public List<T> get(Object key) {
		List<T> result = super.get(key);
		if (result == null) {
			result = new ArrayList<>();
			super.put((U)key, result);
		}
		return result;
	}
	
	public void addToList(U key, T value) {
		List<T> list = get(key);
		if (list.indexOf(value)<0) {
			list.add(value);
		}
	}
	
	public void removeFromList(U key, T value) {
		List<T> list = get(key);
		list.remove(value);
	}
	
	
	

}
