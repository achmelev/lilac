package org.jasm.test.playground;

public class ArrayExecutable implements IExecutable {

	@Override
	public void execute() {
		String [] array = new String[2];
		Object o = new Object();
		array[1] = (String)o;
		
	}

}
