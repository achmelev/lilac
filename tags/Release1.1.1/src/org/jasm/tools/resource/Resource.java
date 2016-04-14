package org.jasm.tools.resource;

import java.io.InputStream;

public interface Resource {
	
	public String getName();
	public InputStream createInputStream();

}
