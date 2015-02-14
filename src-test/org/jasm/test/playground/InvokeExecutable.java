package org.jasm.test.playground;

public class InvokeExecutable implements IExecutable {

	@Override
	public void execute() {
		
		
		add(1,2);
		new InvokeExecutable().add2(1, 2);
		new InvokeExecutable().add3(2, 3);
		System.out.println("Done!");
		
		
	}
	
	public static int add(int a, int b) {
		return a+b;
	}
	
	public int add2(int a, int b) {
		return a+b;
	}
	
	public long add3(int a, int b) {
		return a+b;
	}

}
