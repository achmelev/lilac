package org.jasm.tools.resource;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

public class CompositeResourceCollection implements ResourceCollection {
	
	private List<ResourceCollection> entries = new ArrayList<ResourceCollection>();
	

	@Override
	public Enumeration<Resource> elements() {
		List<Enumeration<Resource>> enums = new ArrayList<Enumeration<Resource>>();
		for (ResourceCollection col: entries) {
			enums.add(col.elements());
		}
		return new CompositeResourceEnumeration(enums);
	}
	
	public void add(ResourceCollection col) {
		entries.add(col);
	}
	

}

class CompositeResourceEnumeration implements Enumeration<Resource> {
	private List<Enumeration<Resource>> entries = null;
	int currentIndex = 0;
	
	CompositeResourceEnumeration(List<Enumeration<Resource>> entries) {
		this.entries = entries;
	}

	@Override
	public boolean hasMoreElements() {
		if (currentIndex>=entries.size()) {
			return false;
		} else {
			while (currentIndex<entries.size() && !entries.get(currentIndex).hasMoreElements()) {
				currentIndex++;
			}
		}
		return currentIndex<entries.size();
	}

	@Override
	public Resource nextElement() {
		if (!hasMoreElements()) {
			throw new NoSuchElementException();
		}
		return entries.get(currentIndex).nextElement();
	}
}