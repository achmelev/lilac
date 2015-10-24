package org.jasm.tools.resource;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class FilterResourceCollection implements ResourceCollection {
	
	private ResourceCollection delegate = null;
	private ResourceFilter filter = null;
	
	public FilterResourceCollection(ResourceCollection delegate, ResourceFilter filter) {
		this.delegate = delegate;
		this.filter = filter;
	}

	@Override
	public Enumeration<Resource> elements() {
		return new FilterResourceEnumeration(delegate.elements(), filter);
	}

}

class FilterResourceEnumeration implements Enumeration<Resource> {
	
	private Enumeration<Resource> delegate;
	private ResourceFilter filter = null;
	
	private Resource currentResource = null;
	
	FilterResourceEnumeration(Enumeration<Resource> delegate, ResourceFilter filter) {
		this.delegate = delegate;
		this.filter = filter;
	}

	@Override
	public boolean hasMoreElements() {
		if (currentResource != null) {
			return true;
		} else {
			while (delegate.hasMoreElements() && currentResource == null) {
				currentResource = delegate.nextElement();
				if (!filter.accept(currentResource)) {
					currentResource = null;
				}
			}
			return currentResource != null;
		}
		
	}

	@Override
	public Resource nextElement() {
		if (!hasMoreElements()) {
			throw new NoSuchElementException();
		}
		Resource result = currentResource;
		currentResource = null;
		return result;
	}
	
}
