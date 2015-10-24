package org.jasm.tools.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class ListResourceCollection implements ResourceCollection {
	
	private List<Resource> resources = new ArrayList<Resource>();

	@Override
	public Enumeration<Resource> elements() {
		return Collections.enumeration(resources);
	}
	
	public void add(Resource res) {
		resources.add(res);
	}

}
