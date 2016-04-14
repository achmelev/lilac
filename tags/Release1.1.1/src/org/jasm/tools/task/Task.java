package org.jasm.tools.task;

import org.jasm.tools.resource.Resource;

public interface Task extends Runnable {
	
	public Resource getResource();

}
