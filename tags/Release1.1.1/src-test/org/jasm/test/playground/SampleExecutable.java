package org.jasm.test.playground;

public class SampleExecutable implements IExecutable {

	@Override
	public void execute() {
		try {
			System.out.println("Hello World!");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}

}
