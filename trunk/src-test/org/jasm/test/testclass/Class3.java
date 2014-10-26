package org.jasm.test.testclass;

public class Class3 implements Interface1 {
	
	Interface1 delegate;

	@Override
	public void testMethod(int value) {
		delegate.testMethod(value);
	}

	@Override
	public void testMethodWithoutArgs() {
		delegate.testMethodWithoutArgs();
		
	}
	

}
