package org.jasm.test.testclass;

public class DummyArrayCreator implements IArrayCreator {

	@Override
	public Object[] createArray() {
		Object[] result = new Object[100];
		int i=0;
		result[0] = null;
		
		return result;
	}
	
	

}
